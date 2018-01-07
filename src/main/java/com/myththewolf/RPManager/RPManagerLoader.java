package com.myththewolf.RPManager;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.myththewolf.BotServ.lib.API.invoke.BotPlugin;
import com.myththewolf.BotServ.lib.API.invoke.PluginAdapter;
import com.myththewolf.RPManager.commands.CharacterWizard;
import com.myththewolf.RPManager.commands.Eval;
import com.myththewolf.RPManager.commands.minuwekk;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.events.MessageEvent;
import net.dv8tion.jda.client.managers.EmoteManager;
import net.dv8tion.jda.core.EmbedBuilder;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import sun.rmi.runtime.Log;

import java.awt.*;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RPManagerLoader implements PluginAdapter {
    public static BotPlugin INSTANCE;
    public static Connection con;

    public void onEnable(BotPlugin botPlugin) {
        INSTANCE = botPlugin;
        DataCache.makeMaps();
        storeAllRPS();
        startRPWatcherService();
        startRPTurnWatcerService();
        botPlugin.getJDAInstance().addEventListener(new MessageEvent());
        try {
            botPlugin.registerCommand("^evaldev", new Eval());
            botPlugin.registerCommand("^charwizard", new CharacterWizard());
            botPlugin.registerCommand("init-data()", new minuwekk());
        } catch (Exception e) {
            LogError(e);
        }

    }

    public void onDisable() {

    }

    public static void LogError(Exception e) {
        e.printStackTrace();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setTitle(":bomb: The bot generated a exception");
        eb.addField(":wrench: Exception Type", e.getClass().getName(), false);
        eb.addField(":printer: Message", "```" + e.getMessage() + "```", false);
        INSTANCE.getJDAInstance().getTextChannelById(INSTANCE.getJSONConfig().getString("BOT-LOG-CHANNEL")).sendMessage(eb.build()).queue();
    }

    public static Connection getSQLConnection() throws SQLException {
        try {

        } catch (Exception e) {
            LogError(e);
        }

        String HOST = INSTANCE.getJSONConfig().getString("SQL_HOST");
        String PORT = INSTANCE.getJSONConfig().getString("SQL_PORT");
        String DATABASE = INSTANCE.getJSONConfig().getString("SQL_DATABASE");
        String USER = INSTANCE.getJSONConfig().getString("SQL_USER");
        String PASS = INSTANCE.getJSONConfig().getString("SQL_PASS");
        if (con == null || con.isClosed()) {
            String url = "";
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser(USER);
            dataSource.setPassword(PASS);
            dataSource.setServerName(HOST);
            dataSource.setDatabaseName(DATABASE);

            con = dataSource.getConnection();
        }
        return con;

    }

    public void startRPWatcherService() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            DataCache.getRoleplayMap().forEach((key, val) -> {
                if (!val.expired() && Days.daysBetween(val.getExpireDate(), new DateTime()).getDays() <= 1 && Days.daysBetween(val.getLastPostDate(), new DateTime()).getDays() >= 7) {
                    EmbedBuilder WARNING = new EmbedBuilder();
                    WARNING.setColor(Color.RED);
                    WARNING.setTitle(":warning: RP About to expire :warning:");
                    WARNING.addField("RP Name", "```" + val.getRoleplayName() + "```", false);
                    WARNING.addField("Character Name", "```" + val.getStagedCharacter().getName() + "```", false);
                    WARNING.setDescription("This roleplay will expire in 1 day, and it's your turn to post. Please leave or commit to the roleplay.");
                    val.getStagedCharacter().getCharacterOwner().asPrivateChannel().sendMessage(WARNING.build()).queue();
                } else if (val.expired()) {
                    val.getCharacterList().forEach(character -> {
                        EmbedBuilder cl = new EmbedBuilder();
                        cl.setTitle("RP Closed");
                        cl.addField("RP Name", "```" + val.getRoleplayName() + "```", false);
                        cl.addField("Character Name", "```" + character.getName() + "```", false);
                        cl.setColor(Color.RED);
                        cl.setDescription("This roleplay has been closed due to inactivity");
                        character.getCharacterOwner().asPrivateChannel().sendMessage(cl.build()).queue();
                    });
                }
            });
        }, 0, 12, TimeUnit.HOURS);
    }

    public void startRPTurnWatcerService() {
        Thread T = new Thread(() -> {
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                System.out.println("poll");
                DataCache.getRoleplayMap().forEach((key, val) -> {
                    System.out.println((Hours.hoursBetween(val.getLastPostDate(), new DateTime()).getHours() >= 16) + "&&"+ (val.getLastPing() == null || (val.getLastPing() != null && Hours.hoursBetween(val.getLastPing(), new DateTime()).getHours() <= 16)));
                    if ((Hours.hoursBetween(val.getLastPostDate(), new DateTime()).getHours() >= 16) && (val.getLastPing() == null || (val.getLastPing() != null && Hours.hoursBetween(val.getLastPing(), new DateTime()).getHours() <= 16))) {
                        val.setLastPing(new DateTime());
                        EmbedBuilder cl = new EmbedBuilder();
                        cl.setTitle("RP Waiting for your post");
                        cl.addField("RP Name", "```" + val.getRoleplayName() + "```", false);
                        cl.addField("Character Name", "```" + val.getStagedCharacter().getName() + "```", false);
                        cl.setColor(Color.RED);
                        cl.setDescription("The RP has been thread-blocked because it is waiting on your post. Please leave the RP or make your post!");
                        val.getStagedCharacter().getCharacterOwner().asPrivateChannel().sendMessage(cl.build()).queue();
                    }
                });
            }, 0, 5, TimeUnit.SECONDS);
        });
        T.start();
    }

    public void storeAllRPS() {
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("SELECT * FROM `Roleplays`");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DataCache.getRoleplayById(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            LogError(e);
        }
    }
}
