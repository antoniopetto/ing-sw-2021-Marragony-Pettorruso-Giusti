package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.exceptions.IllegalConfigXMLException;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CardParser {

    private final Document dom;
    private final XPath xPath = XPathFactory.newInstance().newXPath();

    public CardParser(String configPath) throws ParserConfigurationException, IOException, SAXException{

        File configFile = new File(configPath);
        DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domBuilderFactory.newDocumentBuilder();
        dom = domBuilder.parse(configFile);
        dom.getDocumentElement().normalize();
    }

    public List<DevelopmentCard> parseDevelopmentCards(){

        NodeList devCardNodes = getChildrenByName("developmentCard", dom);

        if (devCardNodes.getLength() != 48)
            throw new IllegalConfigXMLException("Wrong number of development cards");

        List<DevelopmentCard> devCards = IntStream.range(0, 48)
                .mapToObj(i -> parseDevCard(devCardNodes.item(i))).collect(toList());

        if (Card.containsDuplicates(devCards))
            throw new IllegalConfigXMLException("Duplicate development card IDs");

        return devCards;
    }

    public List<LeaderCard> parseLeaderCards() {

        NodeList leaderCardNodes = getChildrenByName("leaderCard", dom);

        if (leaderCardNodes.getLength() != 16)
            throw new IllegalConfigXMLException("Wrong number of leader cards");

        List<LeaderCard> leaderCards = IntStream.range(0, 16)
                .mapToObj(i -> parseLeaderCard(leaderCardNodes.item(i))).collect(toList());

        if (Card.containsDuplicates(leaderCards))
            throw new IllegalConfigXMLException("Duplicate leader card IDs");

        return leaderCards;
    }

    public ProductionPower parseBaseProductionPower(){

        return getChildNode("baseProductionPower", dom).map(this::parseProductionPower)
                .orElseThrow(() -> new IllegalConfigXMLException("Config.xml lacks base production power"));
    }

    private DevelopmentCard parseDevCard(Node devCardNode){

        int id = getAttributeInteger("id", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing development card id"));
        int points = getAttributeInteger("points", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing points"));
        int level = getAttributeInteger("level", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing level"));
        CardColor color = CardColor.valueOf(getAttributeText("color", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing color")).toUpperCase());
        List<ResourceRequirement> resReqs = getChildNode("resourceRequirements", devCardNode).map(this::parseResourceRequirements)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing resource requirements"));
        ProductionPower power = getChildNode("productionPower", devCardNode).map(this::parseProductionPower)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing production power"));

        return new DevelopmentCard(id, points, level, color, resReqs, power);
    }

    private LeaderCard parseLeaderCard(Node leaderCardNode){

        int id = getAttributeInteger("id", leaderCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing leader card id"));
        int points = getAttributeInteger("points", leaderCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Leader card" + id + ": missing points"));
        //SpecialAbility cardAbility = getChildText("cardDiscountAbility", leaderCardNode).map(this::parseSpecialAbility)
        //        .orElseThrow(() -> new IllegalConfigXMLException("Leader card" + id + ": missing special ability"));

        List<Requirement> reqs = new ArrayList<>();
        getChildNode("resourceRequirements", leaderCardNode)
                .ifPresent(i -> reqs.addAll(parseResourceRequirements(i)));
        getChildNode("cardRequirements", leaderCardNode)
                .ifPresent(i -> reqs.addAll(parseCardRequirements(i)));

        //TODO fix ability
        return new LeaderCard(id, points, reqs, null);
    }

    private ProductionPower parseProductionPower(Node powerNode){

        Map<Resource, Integer> input = getChildNode("input", powerNode).map(this::parseResQtyMap).orElse(null);
        Map<Resource, Integer> output = getChildNode("output", powerNode).map(this::parseResQtyMap).orElse(null);
        Integer agnosticInput = getChildInteger("agnosticInput", powerNode).orElse(null);
        Integer agnosticOutput = getChildInteger("agnosticOutput", powerNode).orElse(null);

        return new ProductionPower(input, output, agnosticInput, agnosticOutput);
    }

    private List<ResourceRequirement> parseResourceRequirements(Node resReqsNode){

        NodeList resQtyNodes = getChildrenByName("resQty", resReqsNode);
        return IntStream.range(0, resQtyNodes.getLength())
                .mapToObj(i -> parseResourceRequirement(resQtyNodes.item(i))).collect(toList());
    }

    private List<CardRequirement> parseCardRequirements(Node cardReqsNode){

        NodeList cardReqQtyNodes = getChildrenByName("cardReq", cardReqsNode);
        return IntStream.range(0, cardReqQtyNodes.getLength())
                .mapToObj(i -> parseCardRequirement(cardReqQtyNodes.item(i))).collect(toList());
    }

    private Map<Resource, Integer> parseResQtyMap(Node resQtyMapNode){

        NodeList resQtyNodes = getChildrenByName("resQty", resQtyMapNode);
        return IntStream.range(0, resQtyNodes.getLength())
                .mapToObj(i -> parseResQty(resQtyNodes.item(i)))
                .collect(toMap(ResQty::getResource, ResQty::getQuantity));
    }

    private ResourceRequirement parseResourceRequirement(Node resQtyNode){

        ResQty resQty = parseResQty(resQtyNode);
        return new ResourceRequirement(resQty.getResource(), resQty.getQuantity());
    }

    private ResQty parseResQty(Node resQtyNode){

        Resource resource = Resource.valueOf(getAttributeText("type", resQtyNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing type in resQty")).toUpperCase());
        int quantity = getInteger(resQtyNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in resQty"));
        return new ResQty(resource, quantity);
    }

    private CardRequirement parseCardRequirement(Node cardReqNode){

        CardColor color = CardColor.valueOf(getAttributeText("color", cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing color in cardQty")).toUpperCase());
        Integer level = getAttributeInteger("level", cardReqNode).orElse(null);
        int quantity = getInteger(cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in cardQty"));
        return new CardRequirement(color, level, quantity);
    }


    private NodeList getChildrenByName(String childName, Node context){
        try {
            return (NodeList) xPath.evaluate("/*/" + childName, context, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e){
            throw new IllegalConfigXMLException("Illegal XPath expression");
        }
    }

    private Optional<Integer> getChildInteger(String childName, Node context){
        return getChildText(childName, context).map(Integer::parseInt);
    }

    //private Optional<Enum> getChildEnum()

    private Optional<String> getChildText(String childName, Node context){
        return getChildNode(childName, context).map(Node::getTextContent);
    }

    private Optional<Node> getChildNode(String elementName, Node context){
        return getNode("/*/" + elementName, context);
    }

    private Optional<Integer> getAttributeInteger(String attributeName, Node context){
        return getAttributeText(attributeName, context).map(Integer::parseInt);
    }

    private Optional<String> getAttributeText(String attributeName, Node context){
        return getAttributeNode(attributeName, context).map(Node::getTextContent);
    }

    private Optional<Node> getAttributeNode(String attributeName, Node context){
        return getNode("/*/@" + attributeName, context);
    }

    private Optional<Integer> getInteger(Node node){
        return getText(node).map(Integer::parseInt);
    }

    private Optional<String> getText(Node node){
        return getNode("/*", node).map(Node::getTextContent);
    }

    private Optional<Node> getNode(String path, Node context){
        Optional<Node> node;
        try{
            node = Optional.of((Node) xPath.evaluate(path, context, XPathConstants.NODE));
        }
        catch (XPathExpressionException e){
            throw new IllegalConfigXMLException("Illegal XPath expression");
        }
        return node;
    }

    static class ResQty {
        private final Resource resource;
        private final int quantity;

        public ResQty(Resource resource, int quantity){
            if (resource == null)
                throw new IllegalArgumentException();
            this.resource = resource;
            this.quantity = quantity;
        }

        public Resource getResource() {
            return resource;
        }

        public int getQuantity() {
            return quantity;
        }
    }

}

