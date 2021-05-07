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

    private final List<SimpleDevelopmentCard> simpleDevelopmentCards;
    private final List<SimpleLeaderCard> simpleLeaderCards;

    public static SimpleCardParser getInstance(){
        if (instance == null)
            instance = new SimpleCardParser();
        return instance;
    }

    public SimpleDevelopmentCard getSimpleDevelopmentCard(int id){

        for (SimpleDevelopmentCard devCard : simpleDevelopmentCards){
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

            simpleDevelopmentCards = parseSimpleDevelopmentCards();
            simpleLeaderCards = parseSimpleLeaderCards();

        } catch (IOException | ParserConfigurationException | SAXException e){
            throw new UncheckedIOException(new FileNotFoundException("Cannot find/read config.xml"));
        }
    }

    public List<SimpleDevelopmentCard> parseSimpleDevelopmentCards(){

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

    private SimpleDevelopmentCard parseDevCard(Node devCardNode){

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
        return new SimpleDevelopmentCard(id, points, color, level, resReqs, input, output);
    }

    private SimpleLeaderCard parseLeaderCard(Node leaderCardNode){

        int id = getAttributeInteger("id", leaderCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing leader card id"));
        int points = getAttributeInteger("points", leaderCardNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Leader card" + id + ": missing points"));

        SimpleLeaderCard.Ability ability = null;
        Resource abilityResource = null;
        Optional<Resource> res;

        res = getChildEnum(Resource.class, "cardDiscountAbility", leaderCardNode);
        if(res.isPresent()) {
            abilityResource = res.get();
            ability = SimpleLeaderCard.Ability.CARDDISCOUNT;
        }
        res = getChildEnum(Resource.class, "extraDepotAbility", leaderCardNode);
        if(res.isPresent()) {
            abilityResource = res.get();
            ability = SimpleLeaderCard.Ability.EXTRADEPOT;
        }
        res = getChildEnum(Resource.class, "whiteMarbleAbility", leaderCardNode);
        if(res.isPresent()) {
            abilityResource = res.get();
            ability = SimpleLeaderCard.Ability.WHITEMARBLE;
        }

        Optional<Node> powerNode = getChildNode("extraProductionAbility", leaderCardNode);
        if(powerNode.isPresent()) {
            abilityResource = getChildNode("input", powerNode.get())
                .flatMap(i -> getChildNode("resQty", i))
                .map(this::parseResQty).map(ResQty::getResource)
                .orElseThrow(() -> new IllegalConfigXMLException("Illegal special production power"));
            ability = SimpleLeaderCard.Ability.EXTRAPRODUCTION;
        }

        Map<Resource, Integer> resReqs = new HashMap<>();
        getChildNode("resourceRequirements", leaderCardNode)
                .ifPresent(i -> resReqs.putAll(parseResQtyMap(i)));

        Map<CardColor, Map<Integer, Integer>> cardReqs = new HashMap<>();
        getChildNode("cardRequirements", leaderCardNode)
                .ifPresent(i -> cardReqs.putAll(parseCardRequirements(i)));

        return new SimpleLeaderCard(id, points, cardReqs, resReqs, ability, abilityResource);
    }

    private Map<CardColor, Map<Integer, Integer>> parseCardRequirements(Node cardReqsNode){

        NodeList cardReqQtyNodes = getChildrenByName("cardReq", cardReqsNode);
        Map<CardColor, Map<Integer, Integer>> resultMap = new HashMap<>();
        IntStream.range(0, cardReqQtyNodes.getLength()).mapToObj(i -> parseCardReq(cardReqQtyNodes.item(i)))
            .forEach(i -> {
                if(resultMap.containsKey(i.getColor()))
                    resultMap.get(i.getColor()).put(i.getLevel(), i.getQuantity());
                else {
                    Map<Integer, Integer> newValue = new HashMap<>();
                    newValue.put(i.getLevel(), i.getQuantity());
                    resultMap.put(i.getColor(), newValue);
                }
            });
        return resultMap;
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

    private CardReq parseCardReq(Node cardReqNode){

        CardColor color = getAttributeEnum(CardColor.class, "color", cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing color in cardQty"));
        Integer level = getAttributeInteger("level", cardReqNode).orElse(null);
        int quantity = getInteger(cardReqNode)
                .orElseThrow(() -> new IllegalConfigXMLException("Missing quantity in cardQty"));
        return new CardReq(color, level, quantity);
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

    static class CardReq {
        private final CardColor color;
        private final Integer level;
        private final int quantity;

        public CardReq(CardColor color, Integer level, int quantity){
            if (color == null || quantity < 0)
                throw new IllegalArgumentException();
            this.color = color;
            this.level = level;
            this.quantity = quantity;
        }

        public CardColor getColor() {
            return color;
        }

        public Integer getLevel(){
            return level;
        }

        public int getQuantity() {
            return quantity;
        }
    }

}