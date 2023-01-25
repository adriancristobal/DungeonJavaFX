package org.example;

import game.character.Wizard;
import game.demiurge.Demiurge;
import game.demiurge.DemiurgeDungeonManager;
import game.demiurge.DemiurgeHomeManager;
import game.dungeon.Dungeon;
import game.dungeon.Home;
import game.dungeon.Room;
import game.dungeon.Site;
import game.object.Item;
import game.object.Necklace;
import game.object.Ring;
import game.object.Weapon;
import game.spell.Spell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class MainSaveXML {
    public static void main(String[] args) {
        MainSaveXML save = new MainSaveXML();
        DEMIURGOEJEMPLO ejemplo = new DEMIURGOEJEMPLO();
        Demiurge demiurge = ejemplo.getDemiurgoDeEjemplo();
        save.saveHome(demiurge);
    }

    private void saveHome(Demiurge demiurge) {

        Home home = demiurge.getHome();
        String description = "<descripton>" + home.getDescription() + "</description>";
        String comfort = "<comfort>" + home.getComfort() + "</comfort>";
        String singa = "<singa><currectValue>" + home.getSinga() + "</currectValue><maxValue>" +
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


        String homeStr = "<home>" +
                description +
                comfort +
                singa +
                chest +
                library +
                "</home>";
        System.out.println(homeStr);
    }

//    public Either<String, String> save(Article article) {
//        Either<String, String> result;
//
//        Path file = Paths.get(ConfigData.getInstance().getProperty(ConstantsConfig.CONFIG_PATH_DATA_ARTICLES));
//        String articleString = article.setToFileLine() + ConstantsGeneral.LINE_BREAK;
//        try {
//            Files.write(file, articleString.getBytes(), StandardOpenOption.APPEND);
//            result = Either.right(SuccessMessage.ARTICLE_ADDED.getDescription());
//        } catch (IOException io) {
//            log.error(io.getMessage(), io);
//            result = Either.left(Error.FILE_NOT_FOUND.getDescription());
//        }
//        return result;
//    }

    private void saveRooms(Demiurge demiurge) {
        Dungeon dungeon = demiurge.getDungeon();
        for (Iterator<Room> it = dungeon.iterator(); it.hasNext(); ) {
            Room room = it.next();
            String id = "<id>" + room.getID() + "</id>";
            String exit = "<exit>" + room.isExit() + "</exit>";
            String description = "<description>" + room.getDescription() + "</description>";
            String visited = "<visited>" + room.isVisited() + "</visited>";
            for (Iterator<Item> iter = room.getContainer().iterator(); iter.hasNext(); ) {
                /*
                <item type="necklace">
                        <domain element="life"/>
                        <level value="5"/>
                    </item>
                 */
                //nose como acceder al type
                Item item = iter.next();
                String domain = "<domain element=" + item.getDomain() + "/>";
                String level = "<level" + item.getValue() + "</level>";
                String itemObj = "<item type=" + item + ">" + domain + level + "</item>";

            }
            /*
             <creature type="electricity">
                        <name>Big Monster</name>
                        <life>5</life>
                        <punch>1</punch>
                        <spells>
                            <spell>
                                <domain element="electricity"/>
                                <level value="1"/>
                            </spell>
                        </spells>
                    </creature>
             */

            String name = "<name>" + room.getCreature().getName() + "</name>";
            String life = "<life>" + room.getCreature().getLife() + "</life>";
            String punch = "<punch>" + room.getCreature().getRandomAttack().getDamage() + "</punch>";
            String spells = "";
            for (Iterator<Spell> iter = room.getCreature().getMemory().iterator(); iter.hasNext(); ) {
                Spell spell = iter.next();
                spells = "<spells> <spell> <domain element=" + spell.getDomain()+ "/> <level value="+spell.getLevel()+"/> </spell> </spells>";

            }
            String creature = "<creatures> <creature> type=" + room.getCreature() + ">" + name + life + punch + spells + "</creature> </creatures>";
        }

    }


    private void saveWizard(Demiurge demiurge) {
        Wizard wizard = demiurge.getWizard();
        String wizardXML = "<wizard>";
        wizardXML += "<name>" + wizard.getName() + "</name>";
        wizardXML += "<life><currentValue>" + wizard.getLife() + "</currentValue><maxValue>" + wizard.getLifeMax() + "</maxValue></life>";
        wizardXML += "<energy><currentValue>" + wizard.getEnergy() + "</currentValue><maxValue>" + wizard.getEnergyMax() + "</maxValue></energy>";
        wizardXML += "<crystalCarrier><capacity>" + wizard.getCrystalCarrier().getValue() + "</capacity><crystals><crystal><signa>" + wizard.getCrystalCarrier().get(0).getValue() + "</signa></crystal></crystals>";
        wizardXML += "<weareables><weaponsMax>" + wizard.getWearables().getValue() + "</weaponsMax><necklacesMAX>" + wizard.getWearables().getValue() + "</necklacesMAX><ringsMAX>" + wizard.getWearables().getValue() + "</ringsMAX>";
        wizardXML += "<items><item type=";
        wizardXML += "</weareables>";


//            <items>
//                <item type="necklace">
//                    <domain element="life"/>
//                    <level value="1"/>
//                </item>
//                <item type="ring">
//                    <domain element="air"/>
//                    <level value="1"/>
//                </item>
//                <item type="weapon">
//                    <domain element="none"/>
//                    <level value="1"/>
//                </item>
//            </items>
//        </weareables>

//        <jewelryBag>
//            <capacity>2</capacity>
//            <items>
//                <item type="necklace">
//                    <domain element="energy"/>
//                    <level value="1"/>
//                </item>
//            </items>
//        </jewelryBag>
//    </wizard>
        wizardXML += "</wizard>";
    }
}
