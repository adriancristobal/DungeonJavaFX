package org.example;

import game.Domain;
import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Home;
import game.object.*;
import game.objectContainer.Chest;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.AirAttack;
import game.spell.ElectricAttack;
import game.spell.FireAttack;
import game.spell.Spell;
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
    static int INITIAL_COMFORT;
    static int INITIAL_SINGA;
    static int INITIAL_SINGA_CAPACITY;
    static int INITIAL_CHEST_CAPACITY;

    static List<Item> CHEST_ITEMS = new ArrayList<>();

    static Chest CHEST;


    static Knowledge LIBRARY = new Library();

    static List<Spell> LIBRARY_SPELLS = new ArrayList<>();


    //Wizard
    static int INITIAL_LIFE = 10;
    static int INITIAL_LIFE_MAX = 10;
    static int INITIAL_ENERGY = 10;
    static int INITIAL_ENERGY_MAX = 10;
    static int INITIAL_CRYSTAL_CARRIER_CAPACITY = 3;
    static int INITIAL_CRYSTAL_BAG_CAPACITY = 2;
    static int INITIAL_MAX_WEAPONS = 1;
    static int INITIAL_MAX_NECKLACES = 1;
    static int INITIAL_MAX_RINGS = 2;


    @Override
    public void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File xmlFile) {

    }

    @Override
    public void save(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File file) {

    }

    public static void main(String[] args) {
        Demiurge demiurge = new Demiurge();
        final File XMLFILE = new File("xml/dungeon-V.02.xml");
        final File XSDFILE = new File("xml/dungeon_schema.xsd");
        try {

            try {
                SchemaFactory factory =
                        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(XSDFILE);
                Validator validator = schema.newValidator();
                validator.validate(new StreamSource(XMLFILE));
            } catch (IOException | SAXException e) {
                System.out.println("Exception: "+e);
            }


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document baseXML = dBuilder.parse(XMLFILE);
            baseXML.getDocumentElement().normalize();
            System.out.println(baseXML.getFirstChild());


            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            // Si no se encuentra la expresión, devuelve "" pero NO NULL

            createHome(demiurge, baseXML, xpath);


        } catch (Exception e) {
            e.printStackTrace();
        } catch (ItemCreationErrorException e) {
            // da al crear el necklace
            e.printStackTrace();
        } catch (ContainerUnacceptedItemException e) {
            // da al meter el item en el chest
            e.printStackTrace();
        } catch (ContainerFullException e) {
            throw new RuntimeException(e);
        } catch (ValueOverMaxException e) {
            // da al improve los valores de un spell
            e.printStackTrace();
        }
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
                    INITIAL_COMFORT = Integer.parseInt(node.getTextContent());
                    break;
                case "singa":
                    //<singa>
                    //  <currentValue>X</currentValue>
                    //  <maxValue>X</maxValue>
                    // </singa>
                    getHomeSingaValues(node);
                    break;
                case "chest":
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
                                    INITIAL_CHEST_CAPACITY = Integer.parseInt(chestChild.getTextContent());
//                                            c = new Chest(INITIAL_CHEST_CAPACITY);
                                    break;
                                case "items":
                                    CHEST_ITEMS = getItemsNode(chestChild, "//home/chest/items/");
//SOUT                                        System.out.println(CHEST_ITEMS);
                                    break;
                                default:
                                    throw new IllegalArgumentException("NODO NO VÁLIDO EN //home/chest/* : " + getNombre(chestChild));
                            }
                        }
                    }
                    // Una vez que recorrimos todos los elemenos del chest, creamos el objeto
                    createChest();
                    break;
                case "library":
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
                                    LIBRARY_SPELLS = getSpellsNode(chestChild, "//home/library/spells/");
// SOUT                                        System.out.println(LIBRARY_SPELLS);
                                    break;
                            }
                        }
                    }
                    addSpellsToLibrary();
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
        Home home = new Home(DESCRIPTION_HOME, INITIAL_COMFORT, INITIAL_SINGA, INITIAL_SINGA_CAPACITY, CHEST, LIBRARY);
        demiurge.setHome(home);
        System.out.println(home);
    }

    private static void addSpellsToLibrary() {
        for (int j = 0; j < LIBRARY_SPELLS.size(); j++) {
            LIBRARY.add(LIBRARY_SPELLS.get(j));
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
                        spell.improve(level-1); // <-- level-1 porque el level empieza en uno en el constructor de arriba
                        spells.add(spell);
                    } else {
                        throw new RuntimeException("No se ha podido crear el spell[" + (i+1) + "] de " + xPath);
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
        switch (domain){
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

    private static void createChest() throws ContainerUnacceptedItemException, ContainerFullException {
        CHEST = new Chest(INITIAL_CHEST_CAPACITY);
        for (int k = 0; k < CHEST_ITEMS.size(); k++) {
            CHEST.add(CHEST_ITEMS.get(k));
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
        for (int j = 0; j < listSinga.getLength(); j++) {
            Node nodeSinga = listSinga.item(j);
            if (elNodoEsElemento(nodeSinga)) {
                switch (getNombre(nodeSinga)) {
                    case "currentValue":
                        //<currentValue>X</currentValue>
                        INITIAL_SINGA = Integer.parseInt(nodeSinga.getTextContent());
                        break;
                    case "maxValue":
                        //<maxValue>X</maxValue>
                        INITIAL_SINGA_CAPACITY = Integer.parseInt(nodeSinga.getTextContent());
                        break;
                    default:
                        throw new IllegalArgumentException("EL NODO DENTRO DE //home/singa/ " + nodeSinga.getNodeName() + " NO ES VALIDO");
                }
            }
        }
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

}

