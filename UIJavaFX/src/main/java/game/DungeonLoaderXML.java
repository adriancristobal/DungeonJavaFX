package game;

import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.object.ItemCreationErrorException;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.SpellUnknowableException;
import game.util.ValueOverMaxException;

import java.io.File;

public interface DungeonLoaderXML {
    void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File xmlFile) throws Exception, ValueOverMaxException, ContainerFullException, ContainerUnacceptedItemException, ItemCreationErrorException, SpellUnknowableException;
    void save(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File file) throws Exception;
}
