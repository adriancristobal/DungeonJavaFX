package org.example;

import game.Domain;
import game.character.Creature;
import game.character.Wizard;
import game.conditions.KillAllCreaturesCondition;
import game.conditions.VisitAllRoomsCondition;
import game.demiurge.Demiurge;
import game.dungeon.Door;
import game.dungeon.Dungeon;
import game.dungeon.Home;
import game.dungeon.Room;
import game.object.ItemCreationErrorException;
import game.object.Necklace;
import game.object.SingaCrystal;
import game.object.Weapon;
import game.objectContainer.*;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.AirAttack;
import game.spell.ElectricAttack;
import game.spell.FireAttack;
import game.spell.SpellUnknowableException;
import game.spellContainer.Knowledge;
import game.spellContainer.Library;

import static org.example.DungeonLoaderManualXML.*;

public class DEMIURGOEJEMPLO {
    public Demiurge getDemiurgoDeEjemplo(){
        Demiurge demiurge = new Demiurge();
        try {
            Knowledge library = new Library();
            library.add(new ElectricAttack());
            library.add(new FireAttack());
            library.add(new AirAttack());
            Home home = new Home("Home", 1, 10, 50, new Chest(4), library);
            home.addItem(new Weapon(2));
            demiurge.setHome(home);

            Dungeon dungeon = new Dungeon();
            Room room;
            int id = 0;

            room = new Room(id++, "Common room connected with HOME", new RoomSet(1));
            room.addItem(Necklace.createNecklace(Domain.LIFE, 5));
            dungeon.addRoom(room);

            room = new Room(id++, "Common room in the middle", new RoomSet(0));
            Creature creature = new Creature("Big Monster", 5, 1, Domain.ELECTRICITY);
            creature.addSpell(new ElectricAttack());
            room.setCreature(creature);
            dungeon.addRoom(room);

            room = new Room(id++, "Common room and Exit", new RoomSet(0), true);
            dungeon.addRoom(room);

            new Door(home, dungeon.getRoom(0));
            new Door(dungeon.getRoom(0), dungeon.getRoom(1));
            new Door(dungeon.getRoom(1), dungeon.getRoom(2));

            demiurge.setDungeon(dungeon);

            CrystalCarrier crystalCarrier = new CrystalCarrier(3);
            crystalCarrier.add(SingaCrystal.createCrystal(10));
            Wearables wearables = new Wearables(1, 1, 2);
            Wizard wizard = new Wizard("Merlin", 10, 10, 10, 10,
                    wearables, crystalCarrier, new JewelryBag(2));
            wizard.addSpell(new FireAttack());
            wizard.addItem(new Weapon(1));

            demiurge.setWizard(wizard);

            demiurge.addCondition(new VisitAllRoomsCondition(dungeon));
            demiurge.addCondition(new KillAllCreaturesCondition(dungeon));
        } catch (Exception | ContainerUnacceptedItemException | ContainerFullException e){
            e.printStackTrace();
        } catch (SpellUnknowableException | ItemCreationErrorException e) {
            throw new RuntimeException(e);
        }
        return demiurge;
    }
}
