package game;

import game.Domain;
import game.DungeonLoaderXML;
import game.character.Creature;
import game.character.Wizard;
import game.conditions.*;
import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.dungeon.*;
import game.object.*;
import game.objectContainer.*;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.*;
import game.spellContainer.Knowledge;
import game.spellContainer.Library;
import game.util.ValueOverMaxException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;


/**
 * Cosas a cargar:
 * <p>
 * 1 - HOME
 * <p>
 * 2 - DUNGEON
 * <p>
 * 3 - ROOMS
 * <p>
 * 4 - DOORS
 * <p>
 * 5 - WIZARD
 * <p>
 * 6 - END CONDITIONS
 */
public class DungeonLoaderManualXML implements DungeonLoaderXML {

    //Home
    static String DESCRIPTION_HOME;
    static int INITIAL_COMFORT_HOME;
    static int INITIAL_SINGA_HOME;
    static int INITIAL_SINGA_CAPACITY_HOME;
    static int INITIAL_CHEST_CAPACITY_HOME;

    static List<Item> CHEST_ITEMS_HOME = new ArrayList<>();

    static Chest CHEST_HOME;


    static Knowledge LIBRARY_HOME = new Library();

    static List<Spell> LIBRARY_SPELLS_HOME = new ArrayList<>();

    static Home HOME;

    // Dungeon
    static Dungeon DUNGEON = new Dungeon();

    //Rooms
    static List<Room> ROOMS = new ArrayList<>();

    //Wizard
    static String NAME_WIZARD;
    static int INITIAL_LIFE_WIZARD = 10;
    static int INITIAL_LIFE_MAX_WIZARD = 10;
    static int INITIAL_ENERGY_WIZARD = 10;
    static int INITIAL_ENERGY_MAX_WIZARD = 10;
    static int INITIAL_CRYSTAL_CARRIER_CAPACITY = 3;
    static List<SingaCrystal> CRYSTALS_CARRIED_WIZARD = new ArrayList<>();
    static CrystalCarrier CRYSTAL_CARRIER_WIZARD;
    static Wearables WEARABLES_WIZARD;

    static JewelryBag JEWELRY_BAG_WIZARD;

    static int MAX_WEAPONS = 1;
    static int MAX_NECKLACES = 1;
    static int MAX_RINGS = 2;

    @Override
    public void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File xmlFile) throws Exception
            , ValueOverMaxException
            , ContainerFullException
            , ContainerUnacceptedItemException
            , ItemCreationErrorException
            , SpellUnknowableException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document baseXML = dBuilder.parse(xmlFile);
        baseXML.getDocumentElement().normalize();
        System.out.println(baseXML.getFirstChild());


        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        // Si no se encuentra la expresión, devuelve "" pero NO NULL

        createHome(demiurge, baseXML, xpath);
        createRooms(demiurge, baseXML, xpath);
        createWizard(demiurge, baseXML, xpath);
        createEndConditions(demiurge, baseXML, xpath);
        setDay(demiurge, baseXML, xpath);
    }

    @Override
    public void save(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File file) throws Exception {
        Path filePath = file.toPath();
        StringBuilder demiurgeBuilder = new StringBuilder();
        String home = saveHome(demiurge);
        String rooms = saveRooms(demiurge);
        String wizard = saveWizard(demiurge);

        String conditions = saveConditions(demiurge);
        String day = saveDay(demiurge);
        String endOfFine = "</demiurge>";
        demiurgeBuilder.append(home)
                .append(rooms)
                .append(wizard)
                .append(conditions)
                .append(day)
                .append(endOfFine);
        String demiurgeStr = demiurgeBuilder.toString();
        Files.newBufferedWriter(filePath).flush();
        Files.write(filePath, demiurgeStr.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }


    private static void setDay(Demiurge demiurge, Document baseXML, XPath xpath) {
        try {
            NodeList day = (NodeList) xpath.compile("/demiurge/day").evaluate(baseXML, XPathConstants.NODESET);
            int numberDay = (Integer.parseInt(getAtributo(day.item(0), "value")));
            for (int i = 0; i < numberDay; i++) {
                demiurge.nextDay();
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private static void createEndConditions(Demiurge demiurge, Document baseXML, XPath xpath) throws XPathExpressionException {
        XPathExpression expr = xpath.compile("/demiurge/conditions/*");
        NodeList conditions = (NodeList) expr.evaluate(baseXML, XPathConstants.NODESET);
        for (int i = 0; i < conditions.getLength(); i++) {
            Node condition = conditions.item(i);
            if (elNodoEsElemento(condition)) {
                switch (getAtributo(condition, "type").toUpperCase()) {
                    //TODO: ADD MORE END CONDITIONS
                    case "VISITALLROOMS":
                        demiurge.addCondition(new VisitAllRoomsCondition(DUNGEON));
                        break;
                    case "KILLALLCREATURES":
                        demiurge.addCondition(new KillAllCreaturesCondition(DUNGEON));
                        break;
                    default:
                        break;
                }
            }

        }
    }

    private static void createRooms(Demiurge demiurge, Document baseXML, XPath xpath) throws XPathExpressionException, SpellUnknowableException, ContainerUnacceptedItemException, ValueOverMaxException, ContainerFullException, ItemCreationErrorException {
        XPathExpression expr = xpath.compile("/demiurge/dungeon/rooms/*");
        NodeList rooms = (NodeList) expr.evaluate(baseXML, XPathConstants.NODESET);
        for (int i = 0; i < rooms.getLength(); i++) {
            switch (rooms.item(i).getNodeName()) {
                case "room":
                    createRoom(rooms.item(i));
                    break;
                case "doors":
                    createDoors(rooms.item(i));
                    break;
                default:
                    throw new RuntimeException("El nodo no es valido:  " + rooms.item(i).getNodeName());
            }
        }
        for (int i = 0; i < ROOMS.size(); i++) {
            DUNGEON.addRoom(ROOMS.get(i));
        }
        demiurge.setDungeon(DUNGEON);
    }

    private static void createDoors(Node doors) {
        for (int i = 0; i < doors.getChildNodes().getLength(); i++) {
            Node door = doors.getChildNodes().item(i);
            if (elNodoEsElemento(door)) {
                switch (door.getNodeName()) {
                    case "door":
                        createDoor(door);
                        break;
                }
            }

        }

    }

    private static void createDoor(Node door) {
        Site site1 = null;
        Site site2 = null;
        for (int i = 0; i < door.getChildNodes().getLength(); i++) {
            Node doorChild = door.getChildNodes().item(i);
            if (elNodoEsElemento(doorChild)) {
                switch (doorChild.getNodeName()) {
                    case "idRoom1":
                        site1 = getSite(Integer.parseInt(doorChild.getTextContent()));
                        break;
                    case "idRoom2":
                        site2 = getSite(Integer.parseInt(doorChild.getTextContent()));
                        break;
                }
            }
        }
        //CREATE DOOR
        if (site1 != null && site2 != null) {
            new Door(site1, site2);
        } else {
            throw new RuntimeException("Error al crear la puerta");
        }
    }

    private static Site getSite(int roomID) {
        Site r = ROOMS.stream().filter(room -> room.getID() == roomID).findFirst().orElse(null);
        if (r == null) {
            if (roomID == -1) {
                r = HOME;
            } else {
                throw new RuntimeException("No se ha encontrado la sala con ID " + roomID);
            }
        }
        return r;

    }

    private static void createRoom(Node roomNode) throws ItemCreationErrorException, SpellUnknowableException, ValueOverMaxException, ContainerUnacceptedItemException, ContainerFullException {
//            <room>
//                <id>1</id>
//                <exit value="false"/>
//                <description>Room 1 with creature and necklace</description>
//				  <visited>false</visited>
//                <items...>
//                <creatures...>
//            </room>
        NodeList roomChildren = roomNode.getChildNodes();
        int id = -1;
        boolean exit = false;
        String description = "";
        boolean visited = false;
        List<Item> items = new ArrayList<>();
        Creature creature = null;
        for (int i = 0; i < roomChildren.getLength(); i++) {
            Node child = roomChildren.item(i);
            if (elNodoEsElemento(child)) {
                switch (getNombre(child)) {
                    case "id":
                        id = Integer.parseInt(child.getTextContent());
                        break;
                    case "exit":
                        exit = Boolean.parseBoolean(getAtributo(child, "value"));
                        break;
                    case "description":
                        description = child.getTextContent();
                        break;
                    case "visited":
                        visited = Boolean.parseBoolean(child.getTextContent());
                        break;
                    case "items":
                        items = getItemsNode(child, "//room/items/*");
                        break;
                    case "creatures":
                        creature = getCreatureNode(child);
                        break;
                }
            }

        }
        Room newR = new Room(id, description, new RoomSet(items.size()), exit);
        for (int j = 0; j < items.size(); j++) {
            newR.addItem(items.get(j));
        }
        if (creature != null) {
            newR.setCreature(creature);
        }
        if (visited) {
            newR.visit();
        }
        ROOMS.add(newR);
    }

    private static Creature getCreatureNode(Node creaturesNode) throws SpellUnknowableException, ValueOverMaxException {
//				<creatures>
//					<creature type="electricity">
//						<name>Big Monster</name>
//						<life>5</life>
//						<punch>1</punch>
//						<spells>
//							<spell>
//								<domain element="electricity"/>
//								<level value="1"/>
//							</spell>
//						</spells>
//					</creature>
//				</creatures>
        Creature c = null;
        for (int i = 0; i < creaturesNode.getChildNodes().getLength(); i++) {
            Node child = creaturesNode.getChildNodes().item(i);
            if (elNodoEsElemento(child)) {
                switch (getNombre(child)) {
                    case "creature":
                        c = getCreature(child);
                }
            }
        }
        return c;
    }

    private static Creature getCreature(Node creatureNode) throws ValueOverMaxException, SpellUnknowableException {
        Domain d = Domain.valueOf(getAtributo(creatureNode, "type").toUpperCase());
        String name = null;
        int life = -1;
        int punch = -1;
        List<Spell> spells = new ArrayList<>();
        for (int i = 0; i < creatureNode.getChildNodes().getLength(); i++) {
            Node child = creatureNode.getChildNodes().item(i);
            if (elNodoEsElemento(child)) {
                switch (getNombre(child)) {
                    case "name":
                        name = child.getTextContent();
                        break;
                    case "life":
                        life = Integer.parseInt(child.getTextContent());
                        break;
                    case "punch":
                        punch = Integer.parseInt(child.getTextContent());
                        break;
                    case "spells":
                        spells = getSpellsNode(child, "//creature/spells/*");
                        break;
                }
            }
        }
        Creature c = new Creature(name, life, punch, d);
        for (int i = 0; i < spells.size(); i++) {
            c.addSpell(spells.get(i));
        }
        return c;
    }

    private static void createWizard(Demiurge demiurge, Document baseXML, XPath xpath) throws XPathExpressionException, ItemCreationErrorException, ContainerUnacceptedItemException, ContainerFullException, ValueOverMaxException {
        // GETTING HOME CHILDREN --> [description, comfort, singa, chest, library]
        XPathExpression expr = xpath.compile("/demiurge/wizard/*");
        NodeList list = (NodeList) expr.evaluate(baseXML, XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            switch (getNombre(node)) {
                case "name":
                    NAME_WIZARD = node.getTextContent();
                    break;
                case "life":
                    getLifeValues(node);
                    break;
                case "energy":
                    getEnergyValues(node);
                    break;
                case "crystalCarrier":
                    generateCrystalCarrier(node);
                    break;
                case "weareables":
                    createWereables(node);
                    break;
                case "jewelryBag":
                    createJewelryBag(node);
                    break;
                default:
                    throw new IllegalArgumentException("NODO NO VÁLIDO EN //home/* : " + getNombre(node));
            }

        }
        Wizard wizard = new Wizard(NAME_WIZARD, INITIAL_LIFE_WIZARD, INITIAL_LIFE_MAX_WIZARD, INITIAL_ENERGY_WIZARD, INITIAL_ENERGY_MAX_WIZARD, WEARABLES_WIZARD, CRYSTAL_CARRIER_WIZARD, JEWELRY_BAG_WIZARD);
        demiurge.setWizard(wizard);
        System.out.println(wizard);
    }

    private static void createJewelryBag(Node node) throws ItemCreationErrorException, ContainerUnacceptedItemException, ContainerFullException {
//        <capacity>2</capacity>
//			<items>
//				<item type="necklace">
//					<domain element="energy"/>
//					<level value="1"/>
//				</item>
//			</items>
        int capacity = 0;
        List<Item> items = new ArrayList<>();
        NodeList jewelryChilds = node.getChildNodes();
        for (int j = 0; j < jewelryChilds.getLength(); j++) {
            Node chestChild = jewelryChilds.item(j);
            if (elNodoEsElemento(chestChild)) {
                switch (getNombre(chestChild)) {
                    case "capacity":
                        //<weaponsMAX>1</weaponsMAX>
                        capacity = Integer.parseInt(chestChild.getTextContent());
                        break;
                    case "items":
                        items = getItemsNode(chestChild, "//wizard/weareables/items/");
                        break;
                    default:
                        throw new IllegalArgumentException("NODO NO VÁLIDO EN //wizard/weareables/* : " + getNombre(chestChild));
                }
            }
        }
        JewelryBag jewelryBag = new JewelryBag(capacity);
        for (Item item : items) {
            jewelryBag.add(item);
        }
        JEWELRY_BAG_WIZARD = jewelryBag;

    }

    private static void createWereables(Node node) throws ItemCreationErrorException, ContainerUnacceptedItemException, ContainerFullException {
//        <weaponsMAX>1</weaponsMAX>
//			<necklacesMAX>1</necklacesMAX>
//			<ringsMAX>2</ringsMAX>
//			<items>
//				<item type="necklace">
//					<domain element="life"/>
//					<level value="1"/>
//				</item>
//				<item type="ring">
//					<domain element="air"/>
//					<level value="1"/>
//				</item>
//				<item type="weapon">
//					<domain element="none"/>
//					<level value="1"/>
//				</item>
//			</items>
        int weaponsMAX = 0;
        int necklacesMAX = 0;
        int ringsMAX = 0;
        List<Item> items = new ArrayList<>();
        NodeList wereablesChilds = node.getChildNodes();
        for (int j = 0; j < wereablesChilds.getLength(); j++) {
            Node chestChild = wereablesChilds.item(j);
            if (elNodoEsElemento(chestChild)) {
                switch (getNombre(chestChild)) {
                    case "weaponsMAX":
                        //<weaponsMAX>1</weaponsMAX>
                        weaponsMAX = Integer.parseInt(chestChild.getTextContent());
                        break;
                    case "necklacesMAX":
//                        <necklacesMAX>1</necklacesMAX>
                        necklacesMAX = Integer.parseInt(chestChild.getTextContent());
                        break;
                    case "ringsMAX":
                        //<weaponsMAX>1</weaponsMAX>
                        ringsMAX = Integer.parseInt(chestChild.getTextContent());
                        break;
                    case "items":
                        items = getItemsNode(chestChild, "//wizard/weareables/items/");
                        break;
                    default:
                        throw new IllegalArgumentException("NODO NO VÁLIDO EN //wizard/weareables/* : " + getNombre(chestChild));
                }
            }
        }
        Wearables wearables = new Wearables(weaponsMAX, necklacesMAX, ringsMAX);
        for (Item item : items) {
            wearables.add(item);
        }
        WEARABLES_WIZARD = wearables;
    }

    private static void getLifeValues(Node node) {
        NodeList listSinga = node.getChildNodes();
        Map<String, Integer> currentAndMaxValue = getCurrentValueAndMaxValue(listSinga);
        INITIAL_LIFE_WIZARD = currentAndMaxValue.get("currentValue");
        INITIAL_LIFE_MAX_WIZARD = currentAndMaxValue.get("maxValue");
    }

    private static void getEnergyValues(Node node) {
        NodeList listSinga = node.getChildNodes();
        Map<String, Integer> currentAndMaxValue = getCurrentValueAndMaxValue(listSinga);
        INITIAL_ENERGY_WIZARD = currentAndMaxValue.get("currentValue");
        INITIAL_ENERGY_MAX_WIZARD = currentAndMaxValue.get("maxValue");
    }

    private static void generateCrystalCarrier(Node node) throws ItemCreationErrorException, ContainerUnacceptedItemException, ContainerFullException {
//        <capacity>3</capacity>
//            <crystals>
//                <crystal>
//                    <singa>10</singa>
//                </crystal>
//            </crystals>
        NodeList chestChilds = node.getChildNodes();
        for (int j = 0; j < chestChilds.getLength(); j++) {
            Node chestChild = chestChilds.item(j);
            if (elNodoEsElemento(chestChild)) {
                switch (getNombre(chestChild)) {
                    case "capacity":
                        //<capacity>X</capacity>
                        INITIAL_CRYSTAL_CARRIER_CAPACITY = Integer.parseInt(chestChild.getTextContent());
                        break;
                    case "crystals":
                        CRYSTALS_CARRIED_WIZARD = getCrystalsNode(chestChild, "//wizard/crystalCarrier/crystals/");
                        System.out.println(CRYSTALS_CARRIED_WIZARD);
                        break;
                    default:
                        throw new IllegalArgumentException("NODO NO VÁLIDO EN //wizard/crystalCarrier/* : " + getNombre(chestChild));
                }
            }
        }
        CRYSTAL_CARRIER_WIZARD = new CrystalCarrier(INITIAL_CRYSTAL_CARRIER_CAPACITY);
        for (SingaCrystal crystal : CRYSTALS_CARRIED_WIZARD) {
            CRYSTAL_CARRIER_WIZARD.add(crystal);
        }
    }

    private static List<SingaCrystal> getCrystalsNode(Node node, String xPath) throws ItemCreationErrorException {
        List<SingaCrystal> crystals = new ArrayList<>();
        //            <crystals>
//                <crystal>
//                    <singa>10</singa>
//                </crystal>
//            </crystals>
        NodeList cristalChilds = node.getChildNodes();
        for (int k = 0; k < cristalChilds.getLength(); k++) {
            Node crystal = cristalChilds.item(k);
            if (elNodoEsElemento(crystal)) {
                switch (getNombre(crystal)) {
                    case "crystal":
                        //<singa>10</singa>
                        //el index es uno porque el primer elemento es mierda
                        int valorReal = Integer.parseInt(crystal.getChildNodes().item(1).getTextContent());
                        SingaCrystal x = SingaCrystal.createCrystal(valorReal);
                        while (valorReal != x.getValue()) {
                            x = SingaCrystal.createCrystal(valorReal);
                        }
                        crystals.add(x);
                        break;
                    default:
                        throw new IllegalArgumentException("NODO NO VÁLIDO EN " + xPath + "* : " + getNombre(crystal));

                }
            }
        }
        return crystals;

    }

    private static void createHome(Demiurge demiurge, Document baseXML, XPath xpath) throws XPathExpressionException, ItemCreationErrorException, ContainerUnacceptedItemException, ContainerFullException, ValueOverMaxException {
        // GETTING HOME CHILDREN --> [description, comfort, singa, chest, library]
        XPathExpression expr = xpath.compile("/demiurge/dungeon/home/*");
        NodeList list = (NodeList) expr.evaluate(baseXML, XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            switch (getNombre(node)) {
                case "description":
                    DESCRIPTION_HOME = node.getTextContent();
                    break;
                case "comfort":
                    //<comfort>X</comfort>
                    INITIAL_COMFORT_HOME = Integer.parseInt(node.getTextContent());
                    break;
                case "singa":
                    //<singa>
                    //  <currentValue>X</currentValue>
                    //  <maxValue>X</maxValue>
                    // </singa>
                    getHomeSingaValues(node);
                    break;
                case "chest":
                    generateChest(node);
                    break;
                case "library":
                    createLibrary(node);
                    break;
                default:
                    throw new IllegalArgumentException("NODO NO VÁLIDO EN //home/* : " + getNombre(node));
            }


//                    if (i%2 == 1) {
//                        System.out.println(node.getNodeName() + ": " + getAtributo(node, "value"));
//                    } else {
//                        System.out.println(node.getNodeName() + ": " + getAtributo(node, "element"));
//                    }
// SOUT                System.out.println(node.getNodeName());
        }
        HOME = new Home(DESCRIPTION_HOME, INITIAL_COMFORT_HOME, INITIAL_SINGA_HOME, INITIAL_SINGA_CAPACITY_HOME, CHEST_HOME, LIBRARY_HOME);
        demiurge.setHome(HOME);
        System.out.println(HOME);
    }

    private static void generateChest(Node node) throws ItemCreationErrorException, ContainerUnacceptedItemException, ContainerFullException {
        //<chest>
        //  <capacity>X</capacity>
        //  <items>
        //      <item type="X">
        //          <domain element="X" />
        //          <level value="X" />
        //      </item>
        //      <item...
        //      </item>
        //  </items>
        //</chest>
        NodeList chestChilds = node.getChildNodes();
        for (int j = 0; j < chestChilds.getLength(); j++) {
            Node chestChild = chestChilds.item(j);
            if (elNodoEsElemento(chestChild)) {
                switch (getNombre(chestChild)) {
                    case "capacity":
                        //<capacity>X</capacity>
                        INITIAL_CHEST_CAPACITY_HOME = Integer.parseInt(chestChild.getTextContent());
//                                            c = new Chest(INITIAL_CHEST_CAPACITY);
                        break;
                    case "items":
                        CHEST_ITEMS_HOME = getItemsNode(chestChild, "//home/chest/items/");
//SOUT                                        System.out.println(CHEST_ITEMS);
                        break;
                    default:
                        throw new IllegalArgumentException("NODO NO VÁLIDO EN //home/chest/* : " + getNombre(chestChild));
                }
            }
        }
        // Una vez que recorrimos todos los elemenos del chest, creamos el objeto
        defineChestAndAddItems();
    }

    private static void createLibrary(Node node) throws ValueOverMaxException {
        //                            <library>
//                                <spells>
//                                    <spell>
//                                        <domain element="X"/>
//                                        <level value="X"/>
//                                    </spell>
//                                    <spell>...</spell>
//                                </spells>
//                            </library>
        NodeList libraryChilds = node.getChildNodes();
        for (int j = 0; j < libraryChilds.getLength(); j++) {
            Node chestChild = libraryChilds.item(j);
            if (elNodoEsElemento(chestChild)) {
                switch (getNombre(chestChild)) {
                    case "spells":
                        LIBRARY_SPELLS_HOME = getSpellsNode(chestChild, "//home/library/spells/");
// SOUT                                        System.out.println(LIBRARY_SPELLS);
                        break;
                }
            }
        }
        addSpellsToLibrary();
    }

    private static void addSpellsToLibrary() {
        for (int j = 0; j < LIBRARY_SPELLS_HOME.size(); j++) {
            LIBRARY_HOME.add(LIBRARY_SPELLS_HOME.get(j));
        }
    }

    private static List<Spell> getSpellsNode(Node spellsNode, String xPath) throws ValueOverMaxException {
        List<Spell> spells = new ArrayList<>();
//        <spells>
//            <spell>
//                <domain element="X"/>
//                <level value="X"/>
//            </spell>
//            <spell>...</spell>
//        </spells>
        NodeList spellChilds = spellsNode.getChildNodes();
        for (int i = 0; i < spellChilds.getLength(); i++) {
            Node spellChild = spellChilds.item(i);
            if (elNodoEsElemento(spellChild)) {
                if (getNombre(spellChild).equalsIgnoreCase("spell")) {
                    //<spell>
                    //  <domain element="X"/>
                    //  <level value="X"/>
                    //</spell>
                    Map<String, Object> domainAndLevel = getDomainAndValue(spellChild.getChildNodes(), xPath + "spell[ " + (i + 1) + " ]/");
                    Domain domain = (Domain) domainAndLevel.get("domain");
                    int level = Integer.parseInt(domainAndLevel.get("level").toString());
                    Spell spell = createSpellInstance(domain);
                    if (spell != null) {
                        spell.improve(level - 1); // <-- level-1 porque el level empieza en uno en el constructor de arriba
                        spells.add(spell);
                    } else {
                        throw new RuntimeException("No se ha podido crear el spell[" + (i + 1) + "] de " + xPath);
                    }
                } else {
                    throw new IllegalArgumentException("NODO NO VÁLIDO EN " + xPath + "* : " + getNombre(spellChild));
                }

            }

        }
        return spells;
    }

    private static Spell createSpellInstance(Domain domain) {
        // Depende del nivel, creamos un ataque acorde
        Spell spell = null;
        switch (domain) {
            case FIRE:
                spell = new FireAttack();
                break;
            case ELECTRICITY:
                spell = new ElectricAttack();
                break;
            case AIR:
                spell = new AirAttack();
                break;
        }
        return spell;
    }

    private static void defineChestAndAddItems() throws ContainerUnacceptedItemException, ContainerFullException {
        CHEST_HOME = new Chest(INITIAL_CHEST_CAPACITY_HOME);
        for (int k = 0; k < CHEST_ITEMS_HOME.size(); k++) {
            CHEST_HOME.add(CHEST_ITEMS_HOME.get(k));
        }
    }

    private static List<Item> getItemsNode(Node itemsNode, String xPath) throws ItemCreationErrorException {
        List<Item> items = new ArrayList<>(); // <-- Listado con los items que se van a devolver
        // <items>
        //     <item type="X">
        //         <domain element="X" />
        //         <level value="X" />
        //     </item>
        //     <item...
        //     </item>
        // </items>
        NodeList itemsChilds = itemsNode.getChildNodes();
        for (int k = 0; k < itemsChilds.getLength(); k++) {
            Node itemChild = itemsChilds.item(k);
            if (elNodoEsElemento(itemChild)) {
                // <item type="X">
                //     <domain element="X" />
                //     <level value="X" />
                // </item>
                String type = getAtributo(itemChild, "type");
                NodeList itemChilds = itemChild.getChildNodes();
                Map<String, Object> domainAndLevel = getDomainAndValue(itemChilds, xPath + "/item[" + (k + 1) + "]");
                int level = Integer.parseInt(domainAndLevel.get("level").toString());
                switch (type.toLowerCase()) {
                    case "weapon":
                        items.add(new Weapon(level));
                        break;
                    case "necklace":
                        items.add(Necklace.createNecklace((Domain) domainAndLevel.get("domain"), level));
                        break;
                    case "ring":
                        items.add(Ring.createRing((Domain) domainAndLevel.get("domain"), level));
                        break;
                }
            }
        }
        return items;
    }

    private static Map<String, Object> getDomainAndValue(NodeList itemChilds, String xPath) {
        //     <domain element="X" />
        //     <level value="X" /> // puede ser null en caso de que type == weapon
        Domain domain;
        int level;
        Map<String, Object> domainAndLevel = new HashMap<>();
        for (int l = 0; l < itemChilds.getLength(); l++) {
            Node itemChildChild = itemChilds.item(l);
            if (elNodoEsElemento(itemChildChild)) {
                switch (getNombre(itemChildChild)) {
                    case "domain":
                        //<domain element="X" />
                        domain = Domain.valueOf(getAtributo(itemChildChild, "element").toUpperCase());
                        domainAndLevel.put("domain", domain);
                        break;
                    case "level":
                        //<level value="X" />
                        level = Integer.parseInt(getAtributo(itemChildChild, "value"));
                        domainAndLevel.put("level", level);
                        break;
                    default:
                        throw new IllegalArgumentException("NODO NO VÁLIDO EN " + xPath + " : " + getNombre(itemChildChild));
                }
            }
        }
        return domainAndLevel;
    }

    private static void getHomeSingaValues(Node node) {
        NodeList listSinga = node.getChildNodes();
        Map<String, Integer> currentAndMaxValue = getCurrentValueAndMaxValue(listSinga);
        INITIAL_SINGA_HOME = currentAndMaxValue.get("currentValue");
        INITIAL_SINGA_CAPACITY_HOME = currentAndMaxValue.get("maxValue");
    }

    private static Map<String, Integer> getCurrentValueAndMaxValue(NodeList list) {
        Map<String, Integer> values = new HashMap<>();
        for (int j = 0; j < list.getLength(); j++) {
            Node nodeSinga = list.item(j);
            if (elNodoEsElemento(nodeSinga)) {
                switch (getNombre(nodeSinga)) {
                    case "currentValue":
                        //<currentValue>X</currentValue>
                        values.put("currentValue", Integer.parseInt(nodeSinga.getTextContent()));
                        break;
                    case "maxValue":
                        //<maxValue>X</maxValue>
                        values.put("maxValue", Integer.parseInt(nodeSinga.getTextContent()));
                        break;
                    default:
                        throw new IllegalArgumentException("EL NODO DENTRO DE" + nodeSinga.getNodeName() + " NO ES VALIDO");
                }
            }
        }
        return values;
    }

    private static boolean elNodoEsElemento(Node no) {
        return no.getNodeType() == Element.ELEMENT_NODE;
    }

    private static String getNombre(Node node) {
        return node.getNodeName();
    }

    private static String getAtributo(Node node, String atributo) {
        return node.getAttributes().getNamedItem(atributo).getTextContent();
    }

    private String saveDay(Demiurge demiurge) {
        String day = String.valueOf(demiurge.getDay());
        StringBuilder dayBuilder = new StringBuilder();
        dayBuilder.append("<day value=\"")
                .append(day)
                .append("\"/>");
        return dayBuilder.toString();
    }

    private String saveConditions(Demiurge demiurge) {
        List<Condition> conditionList = demiurge.getEndChecker().getConditions();
        StringBuilder conditionsBuilder = new StringBuilder();
        String startConditions = "<conditions>";
        String endConditions = "</conditions>";


        conditionsBuilder.append(startConditions);
        conditionList.forEach(condition -> {
            if (!(condition instanceof SimpleCondition)) {
                conditionsBuilder.append("<condition type=\"");
                if (condition instanceof FindCreatureCondition) {
                    conditionsBuilder.append("FindCreature");
                } else if (condition instanceof FindObjectCondition) {
                    conditionsBuilder.append("FindObject");
                } else if (condition instanceof KillAllCreaturesCondition) {
                    conditionsBuilder.append("KillAllCreatures");
                } else if (condition instanceof VisitAllRoomsCondition) {
                    conditionsBuilder.append("VisitAllRooms");
                }
                conditionsBuilder.append("\"/>");
            }
        });

        conditionsBuilder.append(endConditions);
        return conditionsBuilder.toString();
    }

    private String saveHome(Demiurge demiurge) {
        Home home = demiurge.getHome();
        String description = "<description>" + home.getDescription() + "</description>";
        String comfort = "<comfort>" + home.getComfort() + "</comfort>";
        String singa = "<singa><currentValue>" + home.getSinga() + "</currentValue><maxValue>" +
                +home.getMaxSinga() + "</maxValue></singa>";

        StringBuilder itemBuilder = new StringBuilder();
        itemBuilder.append("<chest><capacity>")
                .append(home.getContainer().getValue())
                .append("</capacity><items>");

        Iterator<Item> items = home.getContainer().iterator();
        items.forEachRemaining(item -> {
            itemBuilder.append("<item type=\"");
            if (item instanceof Weapon) {
                itemBuilder.append("weapon");
            } else if (item instanceof Necklace) {
                itemBuilder.append("necklace");
            } else if (item instanceof Ring) {
                itemBuilder.append("ring");
            }
            itemBuilder.append("\"><domain element=\"")
                    .append(item.getDomain())
                    .append("\"/>")
                    .append("<level value=\"")
                    .append(item.getValue())
                    .append("\"/></item>");
        });
        itemBuilder.append("</items></chest>");
        String chest = itemBuilder.toString();

        Iterator<Spell> spells = home.getLibrary().iterator();
        StringBuilder libraryBuilder = new StringBuilder();
        libraryBuilder.append("<library><spells>");
        spells.forEachRemaining(spell -> {
            libraryBuilder.append("<spell><domain element=\"")
                    .append(spell.getDomain())
                    .append("\"/>")
                    .append("<level value=\"")
                    .append(spell.getLevel())
                    .append("\"/></spell>");
        });
        libraryBuilder.append("</spells></library>");
        String library = libraryBuilder.toString();


        return "<demiurge><dungeon><home>" +
                description +
                comfort +
                singa +
                chest +
                library +
                "</home>";
    }

    private String saveRooms(Demiurge demiurge) {
        Dungeon dungeon = demiurge.getDungeon();
        StringBuilder roomListBuilder = new StringBuilder();
        Iterator<Room> rooms = dungeon.iterator();
        rooms.forEachRemaining(room -> {
            StringBuilder roomBuilder = new StringBuilder();
            String id = "<id>" + room.getID() + "</id>";
            String exit = "<exit value=\"" + room.isExit() + "\"/>";
            String description = "<description>" + room.getDescription() + "</description>";
            String visited = "<visited>" + room.isVisited() + "</visited>";
            StringBuilder itemBuilder = new StringBuilder();
            itemBuilder.append("<items>");
            Iterator<Item> items = room.getContainer().iterator();
            items.forEachRemaining(item -> {
                itemBuilder.append("<item type=\"");
                if (item instanceof Weapon) {
                    itemBuilder.append("weapon");
                } else if (item instanceof Necklace) {
                    itemBuilder.append("necklace");
                } else if (item instanceof Ring) {
                    itemBuilder.append("ring");
                }
                itemBuilder.append("\"><domain element=\"")
                        .append(item.getDomain())
                        .append("\"/>")
                        .append("<level value=\"")
                        .append(item.getValue())
                        .append("\"/></item>");

            });
            itemBuilder.append("</items>");
            String itemList = itemBuilder.toString();

            StringBuilder creatureBuilder = new StringBuilder();
            creatureBuilder.append("<creatures>");
            if (room.getCreature() != null) {
                String name = "<name>" + room.getCreature().getName() + "</name>";
                String life = "<life>" + room.getCreature().getLife() + "</life>";
                String punch = "<punch>" + room.getCreature().getRandomAttack().getDamage() + "</punch>";
                creatureBuilder.append("<creature type=\"")
                        .append(room.getCreature().getDomain())
                        .append("\">")
                        .append(name)
                        .append(life)
                        .append(punch);
                Iterator<Spell> spells = room.getCreature().getMemory().iterator();
                StringBuilder spellsBuilder = new StringBuilder();
                spellsBuilder.append("<spells>");
                spells.forEachRemaining(spell -> {
                    spellsBuilder.append("<spell><domain element=\"")
                            .append(spell.getDomain())
                            .append("\"/>")
                            .append("<level value=\"")
                            .append(spell.getLevel())
                            .append("\"/></spell> </spells> </creature> </creatures>");
                });
                String spellsList = spellsBuilder.toString();
                creatureBuilder.append(spellsList);
            } else {
                creatureBuilder.append("</creatures>");
            }
            String creatures = creatureBuilder.toString();

            roomBuilder.append("<room>")
                    .append(id)
                    .append(exit)
                    .append(description)
                    .append(visited)
                    .append(itemList)
                    .append(creatures)
                    .append("</room>");
            roomListBuilder.append(roomBuilder);
        });
        String roomList = roomListBuilder.toString();
        return "<rooms>" +
                roomList +
                "</rooms></dungeon>";

    }


    private String saveWizard(Demiurge demiurge) {
        Wizard wizard = demiurge.getWizard();
        StringBuilder wizardXML;
        //nombre
        wizardXML = new StringBuilder("<wizard>" +
                "<name>" + wizard.getName() + "</name>");

        //hp
        wizardXML.append("<life>")
                .append("<currentValue>").append(wizard.getLife()).append("</currentValue>")
                .append("<maxValue>").append(DungeonLoaderManualXML.INITIAL_LIFE_MAX_WIZARD).append("</maxValue>")
                .append("</life>");

        //energy
        wizardXML.append("<energy>")
                .append("<currentValue>").append(wizard.getEnergy()).append("</currentValue>")
                .append("<maxValue>").append(DungeonLoaderManualXML.INITIAL_ENERGY_MAX_WIZARD).append("</maxValue>")
                .append("</energy>");

        //crytalCarrier
        wizardXML.append("<crystalCarrier>")
                .append("<capacity>").append(wizard.getCrystalCarrier().getValue()).append("</capacity>")
                .append("<crystals>");
        Iterator<Item> itemCrystalCarrier = wizard.getCrystalCarrier().iterator();
        itemCrystalCarrier.forEachRemaining(item -> {
            wizardXML.append("<crystal>")
                    .append("<singa>").append(item.getValue()).append("</singa>")
                    .append("</crystal>");
        });
        wizardXML.append("</crystals>")
                .append("</crystalCarrier>");

        //weareables
        wizardXML.append("<weareables>")
                .append("<weaponsMAX>").append(DungeonLoaderManualXML.MAX_WEAPONS).append("</weaponsMAX>")
                .append("<necklacesMAX>").append(DungeonLoaderManualXML.MAX_NECKLACES).append("</necklacesMAX>")
                .append("<ringsMAX>").append(DungeonLoaderManualXML.MAX_RINGS).append("</ringsMAX>")
                .append("<items>");
        StringBuilder itemBuilder = new StringBuilder();
        Iterator<Item> itemWearables = wizard.getWearables().iterator();
        itemWearables.forEachRemaining(item -> {
            if (item instanceof Weapon) {
                itemBuilder.append("weapon");
            } else if (item instanceof Necklace) {
                itemBuilder.append("necklace");
            } else if (item instanceof Ring) {
                itemBuilder.append("ring");
            }
            wizardXML.append("<item type=\"").append(itemBuilder).append("\">")
                    .append("<domain element=\"").append(item.getDomain()).append("\"/>")
                    .append("<level value=\"").append(item.getValue()).append("\"/>")
                    .append("</item>");
        });
        wizardXML.append("</items>")
                .append("</weareables>");
        //jewelryBag
        wizardXML.append("<jewelryBag>")
                .append("<capacity>").append(wizard.getJewelryBag().getValue()).append("</capacity>")
                .append("<items>");

        Iterator<Item> itemJewelry = wizard.getJewelryBag().iterator();
        StringBuilder itemBuilder2 = new StringBuilder();
        ;
        itemJewelry.forEachRemaining(item -> {
            if (item instanceof Necklace) {
                itemBuilder2.append("necklace");
            } else if (item instanceof Ring) {
                itemBuilder2.append("ring");
            }
            wizardXML.append("<item type=\"").append(itemBuilder2).append("\">")
                    .append("<domain element=\"").append(item.getDomain()).append("\"/>")
                    .append("<level value=\"").append(item.getValue()).append("\"/>")
                    .append("</item>");
        });
        wizardXML.append("</items>")
                .append("</jewelryBag>")
                .append("</wizard>");

        return wizardXML.toString();
    }

}

