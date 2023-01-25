package loader;

import connection.PoolConnection;
import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Dungeon;
import game.dungeon.Home;
import game.dungeon.Site;
import game.objectContainer.Chest;
import game.objectContainer.Container;
import game.spell.AirAttack;
import game.spell.ElectricAttack;
import game.spell.FireAttack;
import game.spellContainer.Knowledge;
import game.spellContainer.Library;
import jakarta.inject.Inject;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DungeonLoaderDataBase implements DungeonLoader {
    private final PoolConnection pool;


    @Inject
    public DungeonLoaderDataBase(PoolConnection pool) {
        this.pool = pool;
    }

    @Override
    public void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration) {
        Knowledge library = new Library();
        library.add(new ElectricAttack());
        library.add(new FireAttack());
        library.add(new AirAttack());
        Home home = new Home(Constants.HOME_NAME, Constants.INITIAL_COMFORT, Constants.INITIAL_SINGA, Constants.INITIAL_SINGA_CAPACITY, new Chest(Constants.INITIAL_CHEST_CAPACITY), library);


    }

    private Demiurge get(LocalDateTime savingTime) {
        Demiurge result;
        try (Connection con = pool.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT id FROM dungeon where saving_time = ?")) {

            preparedStatement.setDate(1, Date.valueOf(savingTime.toString()));
            ResultSet rs = preparedStatement.executeQuery();

            Demiurge demiurge = new Demiurge();
            if (rs.next()) {
                int id = rs.getInt("id_dungeon");

            }
            result = demiurge;
        } catch (SQLException ex) {
            Logger.getLogger(DungeonLoaderDataBase.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        return result;
    }

    private List<Site> readRS(ResultSet rs) {
        List<Site> sites = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String description = rs.getString(2);
                int id_owner = rs.getInt(3);

                sites.add(new Site(id, description, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sites;
    }

    public List<Site> getAll() {
        List<Site> result;
        try (Connection con = pool.getConnection();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            ResultSet rs = statement.executeQuery("Select * from site where id_dungeon = ?");

            result = readRS(rs);
        } catch (SQLException ex) {
            Logger.getLogger(DungeonLoaderDataBase.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        return result;
    }

    public int addDungeon(Date saving_date_dungeon) {
        int result;

        try (Connection con = pool.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO dungeon (saving_time) VALUES (?)")) {

            preparedStatement.setDate(1, saving_date_dungeon);
            preparedStatement.executeUpdate();
            result = 1;


        } catch (SQLException ex) {
            Logger.getLogger(DungeonLoaderDataBase.class.getName()).log(Level.SEVERE, null, ex);
            result = -1;
        }
        return result;
    }


}
