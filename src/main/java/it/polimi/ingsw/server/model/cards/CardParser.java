package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.client.simplemodel.SimpleDevCard;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * This class parses the content of an xml file in which are stored the cards
 */
public class CardParser {

    private static final String CONFIG_PATH = "src/main/resources/cards/config.xml";
    private final Node config;
    private final XPath xPath = XPathFactory.newInstance().newXPath();
    private static CardParser instance;

    public static CardParser getInstance() throws ParserConfigurationException, IOException, SAXException{
        if (instance == null)
            instance = new CardParser();
        return instance;
    }

    /**
     * <code>CardParser</code> constructor.
     * Looks for the config.xml and converts it to a DOM object.
     *
     * @throws ParserConfigurationException     If the content of config.xml is illegal
     * @throws IOException                      If there's an I/O error when retrieving the file
     * @throws SAXException                     If the <code>DocumentBuilder</code> can't parse the file
     */
    private CardParser() throws ParserConfigurationException, IOException, SAXException{

        File configFile = new File(CONFIG_PATH);
        DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domBuilderFactory.newDocumentBuilder();
        Document dom = domBuilder.parse(configFile);
        dom.getDocumentElement().normalize();
        config = getChildNode("config", dom)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing root config node"));
    }

    public List<SimpleDevCard> parseSimpleDevelopmentCards(){
        return parseDevelopmentCards().stream().map(DevelopmentCard::getSimple).collect(Collectors.toList());
    }

    public List<SimpleLeaderCard> parseSimpleLeaderCards() {
        return parseLeaderCards().stream().map(LeaderCard::getSimple).collect(Collectors.toList());
    }

    /**
     * Parses in the config.xml all the <code>DevelopmentCard</code>s
     *
     * @return      A list with all the <code>DevelopmentCard</code>s
     */
    public List<DevelopmentCard> parseDevelopmentCards(){

        NodeList devCardNodes = getChildrenByName("developmentCard", config);
        if (devCardNodes.getLength() != 48)
            throw new IllegalConfigXMLException("Wrong number of development cards");
        List<DevelopmentCard> devCards = IntStream.range(0, 48)
                .mapToObj(i -> parseDevCard(devCardNodes.item(i))).collect(toList());
        if (Card.containsDuplicates(devCards))
            throw new IllegalConfigXMLException("Duplicate development card IDs");

        return devCards;
    }

    /**
     * Parses in the config.xml all the <code>LeaderCard</code>s
     *
     * @return      A list with all the <code>LeaderCard</code>s
     */
    public List<LeaderCard> parseLeaderCards() {

        NodeList leaderCardNodes = getChildrenByName("leaderCard", config);
        if (leaderCardNodes.getLength() != 16)
            throw new IllegalConfigXMLException("Wrong number of leader cards");
        List<LeaderCard> leaderCards = IntStream.range(0, 16)
                .mapToObj(i -> parseLeaderCard(leaderCardNodes.item(i))).collect(toList());
        if (Card.containsDuplicates(leaderCards))
            throw new IllegalConfigXMLException("Duplicate leader card IDs");

        return leaderCards;
    }

    /**
     * Parses in the config.xml the playerboard's base production power.
     *
     * @return      The base production power
     */
    public ProductionPower parseBaseProductionPower(){

        return getChildNode("baseProductionPower", config).map(this::parseProductionPower)
                .orElseThrow(() -> new IllegalConfigXMLException("Config.xml lacks base production power"));
    }

    private DevelopmentCard parseDevCard(Node devCardNode){

        int id = getAttributeInteger("id", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing development card id"));
        int points = getAttributeInteger("points", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing points"));
        int level = getAttributeInteger("level", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing level"));
        CardColor color = getAttributeEnum(CardColor.class, "color", devCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Development card" + id + ": missing color"));
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

        List<SpecialAbility> abilityList = new ArrayList<>();
        getChildEnum(Resource.class, "cardDiscountAbility", leaderCardNode)
                .ifPresent(i -> abilityList.add(new CardDiscountAbility(i)));
        getChildEnum(Resource.class, "extraDepotAbility", leaderCardNode)
                .ifPresent(i -> abilityList.add(new ExtraDepotAbility(i)));
        getChildEnum(Resource.class, "whiteMarbleAbility", leaderCardNode)
                .ifPresent(i -> abilityList.add(new WhiteMarbleAbility(i)));
        getChildNode("extraProductionAbility", leaderCardNode)
                .ifPresent(i -> abilityList.add(new ExtraProductionAbility(parseProductionPower(i))));
        if (abilityList.size() != 1)
            throw new IllegalConfigXMLException("Leader card" + id + "Wrong number of special abilities");


        List<Requirement> reqs = new ArrayList<>();
        getChildNode("resourceRequirements", leaderCardNode)
                .ifPresent(i -> reqs.addAll(parseResourceRequirements(i)));
        getChildNode("cardRequirements", leaderCardNode)
                .ifPresent(i -> reqs.addAll(parseCardRequirements(i)));

        return new LeaderCard(id, points, reqs, abilityList.get(0));
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

        Resource resource = getAttributeEnum(Resource.class,"type", resQtyNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing type in resQty"));
        int quantity = getInteger(resQtyNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in resQty"));
        return new ResQty(resource, quantity);
    }

    private CardRequirement parseCardRequirement(Node cardReqNode){

        CardColor color = getAttributeEnum(CardColor.class, "color", cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing color in cardQty"));
        Integer level = getAttributeInteger("level", cardReqNode).orElse(null);
        int quantity = getInteger(cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in cardQty"));
        return new CardRequirement(color, level, quantity);
    }


    private NodeList getChildrenByName(String childName, Node context){
        try {
            return (NodeList) xPath.evaluate("./" + childName, context, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e){
            throw new IllegalConfigXMLException("Illegal XPath expression");
        }
    }

    private Optional<Integer> getChildInteger(String childName, Node context){
        return getChildText(childName, context).map(Integer::parseInt);
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