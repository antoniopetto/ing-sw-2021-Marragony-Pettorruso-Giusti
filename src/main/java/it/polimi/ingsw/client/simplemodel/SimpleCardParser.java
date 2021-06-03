package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.exceptions.IllegalConfigXMLException;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * This class parses the content of an xml file in which are stored the cards
 */
public class SimpleCardParser {

    private final Node config;
    private final XPath xPath = XPathFactory.newInstance().newXPath();
    private static final String CONFIG_PATH = "src/main/resources/cards/config.xml";
    private static SimpleCardParser instance;

    private final List<SimpleDevCard> simpleDevCards;
    private final List<SimpleLeaderCard> simpleLeaderCards;

    public static SimpleCardParser getInstance(){
        if (instance == null)
            instance = new SimpleCardParser();
        return instance;
    }

    public SimpleDevCard getSimpleDevelopmentCard(int id){

        for (SimpleDevCard devCard : simpleDevCards){
            if (devCard.getId() == id)
                return devCard;
        }
        throw new IllegalArgumentException("DevelopmentCard" + id + "not found");
    }

    public SimpleLeaderCard getSimpleLeaderCard(int id){

        for (SimpleLeaderCard leaderCard : simpleLeaderCards){
            if (leaderCard.getId() == id){
                return leaderCard;
            }
        }
        throw new IllegalArgumentException("LeaderCard" + id + "not found");
    }

    private SimpleCardParser(){
        try {
            File configFile = new File(CONFIG_PATH);
            DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domBuilderFactory.newDocumentBuilder();
            Document dom = domBuilder.parse(configFile);
            dom.getDocumentElement().normalize();
            config = getChildNode("config", dom)
                    .orElseThrow(() -> new IllegalConfigXMLException("Missing root config node"));

            simpleDevCards = parseSimpleDevelopmentCards();
            simpleLeaderCards = parseSimpleLeaderCards();

        } catch (IOException | ParserConfigurationException | SAXException e){
            throw new UncheckedIOException(new FileNotFoundException("Cannot find/read config.xml"));
        }
    }

    public List<SimpleDevCard> parseSimpleDevelopmentCards(){

        NodeList devCardNodes = getChildrenByName("developmentCard", config);
        if (devCardNodes.getLength() != 48)
            throw new IllegalConfigXMLException("Wrong number of development cards");
        return IntStream.range(0, 48)
                .mapToObj(i -> parseDevCard(devCardNodes.item(i))).collect(toList());
    }

    public List<SimpleLeaderCard> parseSimpleLeaderCards() {

        NodeList leaderCardNodes = getChildrenByName("leaderCard", config);
        if (leaderCardNodes.getLength() != 16)
            throw new IllegalConfigXMLException("Wrong number of leader cards");
        return IntStream.range(0, 16)
                .mapToObj(i -> parseLeaderCard(leaderCardNodes.item(i))).collect(toList());
    }

    private SimpleDevCard parseDevCard(Node devCardNode){

        int id = getAttributeInteger("id", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing development card id"));
        int points = getAttributeInteger("points", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing points"));
        CardColor color = getAttributeEnum(CardColor.class, "color", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing color"));
        int level = getAttributeInteger("level", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing level"));
        Map<Resource, Integer> resReqs = getChildNode("resourceRequirements", devCardNode).map(this::parseResQtyMap)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing resource requirements"));
        Node powerNode = getChildNode("productionPower", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing production power"));
        Map<Resource, Integer> input = getChildNode("input", powerNode).map(this::parseResQtyMap).orElse(new HashMap<>());
        Map<Resource, Integer> output = getChildNode("output", powerNode).map(this::parseResQtyMap).orElse(new HashMap<>());
        return new SimpleDevCard(id, points, color, level, resReqs, input, output);
    }

    private SimpleLeaderCard parseLeaderCard(Node leaderCardNode){

        int id = getAttributeInteger("id", leaderCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing leader card id"));
        int points = getAttributeInteger("points", leaderCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Leader card" + id + ": missing points"));

        SimpleAbility ability = null;
        Optional<Resource> res;

        res = getChildEnum(Resource.class, "cardDiscountAbility", leaderCardNode);
        if(res.isPresent()) ability = new SimpleAbility(SimpleAbility.Type.CARDDISCOUNT, res.get());

        res = getChildEnum(Resource.class, "extraDepotAbility", leaderCardNode);
        if(res.isPresent()) ability = new SimpleAbility(SimpleAbility.Type.EXTRADEPOT, res.get());

        res = getChildEnum(Resource.class, "whiteMarbleAbility", leaderCardNode);
        if(res.isPresent()) ability = new SimpleAbility(SimpleAbility.Type.WHITEMARBLE, res.get());

        Optional<Node> powerNode = getChildNode("extraProductionAbility", leaderCardNode);
        if(powerNode.isPresent()) {
            res = getChildNode("input", powerNode.get())
                .flatMap(i -> getChildNode("resQty", i))
                .map(this::parseResQty).map(ResQty::getResource);
            if(res.isPresent()) ability = new SimpleAbility(SimpleAbility.Type.EXTRAPRODUCTION, res.get());
        }

        Map<Resource, Integer> resReqs = new HashMap<>();
        getChildNode("resourceRequirements", leaderCardNode)
                .ifPresent(i -> resReqs.putAll(parseResQtyMap(i)));

        List<SimpleCardRequirement> cardReqs = new ArrayList<>();
        getChildNode("cardRequirements", leaderCardNode)
                .ifPresent(i -> cardReqs.addAll(parseCardRequirements(i)));

        return new SimpleLeaderCard(id, points, cardReqs, resReqs, ability);
    }

    private List<SimpleCardRequirement> parseCardRequirements(Node cardReqsNode){

        NodeList cardReqQtyNodes = getChildrenByName("cardReq", cardReqsNode);
        List<SimpleCardRequirement> cardReqList = new ArrayList<>();
        IntStream.range(0, cardReqQtyNodes.getLength()).mapToObj(i -> parseCardReq(cardReqQtyNodes.item(i)))
                .forEach(cardReqList::add);
        return cardReqList;
    }

    private Map<Resource, Integer> parseResQtyMap(Node resQtyMapNode){

        NodeList resQtyNodes = getChildrenByName("resQty", resQtyMapNode);
        return IntStream.range(0, resQtyNodes.getLength())
                .mapToObj(i -> parseResQty(resQtyNodes.item(i)))
                .collect(toMap(ResQty::getResource, ResQty::getQuantity));
    }

    private ResQty parseResQty(Node resQtyNode){

        Resource resource = getAttributeEnum(Resource.class,"type", resQtyNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing type in resQty"));
        int quantity = getInteger(resQtyNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in resQty"));
        return new ResQty(resource, quantity);
    }

    private SimpleCardRequirement parseCardReq(Node cardReqNode){

        CardColor color = getAttributeEnum(CardColor.class, "color", cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing color in cardQty"));
        Integer level = getAttributeInteger("level", cardReqNode).orElse(null);
        int quantity = getInteger(cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in cardQty"));
        return new SimpleCardRequirement(color, level, quantity);
    }

    private NodeList getChildrenByName(String childName, Node context){
        try {
            return (NodeList) xPath.evaluate("./" + childName, context, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e){
            throw new IllegalConfigXMLException("Illegal XPath expression");
        }
    }
    @SuppressWarnings("SameParameterValue")
    private <T extends Enum<T>> Optional<T> getChildEnum(Class<T> enumType, String childName, Node context){
        try{
            return Optional.of(Enum.valueOf(enumType, getChildText(childName, context)
                    .orElseThrow(NullPointerException::new).toUpperCase()));
        }
        catch (IllegalArgumentException | NullPointerException e){
            return Optional.empty();
        }
    }

    private Optional<String> getChildText(String childName, Node context){
        return getChildNode(childName, context).map(Node::getTextContent);
    }

    private Optional<Node> getChildNode(String elementName, Node context){
        return getNode("./" + elementName, context);
    }

    private Optional<Integer> getAttributeInteger(String attributeName, Node context){
        return getAttributeText(attributeName, context).map(Integer::parseInt);
    }

    private <T extends Enum<T>> Optional<T> getAttributeEnum(Class<T> enumType, String attributeName, Node context){
        try{
            return Optional.of(Enum.valueOf(enumType, getAttributeText(attributeName, context)
                    .orElseThrow(NullPointerException::new).toUpperCase()));
        }
        catch (IllegalArgumentException | NullPointerException e){
            return Optional.empty();
        }
    }

    private Optional<String> getAttributeText(String attributeName, Node context){
        return getAttributeNode(attributeName, context).map(Node::getTextContent);
    }

    private Optional<Node> getAttributeNode(String attributeName, Node context){
        return getNode("./@" + attributeName, context);
    }

    private Optional<Integer> getInteger(Node node){
        return getText(node).map(Integer::parseInt);
    }

    private Optional<String> getText(Node node){
        return Optional.ofNullable(node.getTextContent());
    }

    private Optional<Node> getNode(String path, Node context){
        Optional<Node> node;
        try{
            node = Optional.ofNullable((Node) xPath.evaluate(path, context, XPathConstants.NODE));
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