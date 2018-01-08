package com.myththewolf.RPManager;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.myththewolf.BotServ.lib.API.invoke.BotPlugin;
import com.myththewolf.BotServ.lib.API.invoke.PluginAdapter;
import com.myththewolf.RPManager.commands.*;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.Services.LastPostCheckService;
import com.myththewolf.RPManager.lib.Services.RPExpirationDateService;
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
        botPlugin.getJDAInstance().addEventListener(new MessageEvent());
        try {
            botPlugin.registerCommand("^evaldev", new Eval());
            botPlugin.registerCommand("^charwizard", new CharacterWizard());
            botPlugin.registerCommand("init-data()", new minuwekk());
            botPlugin.registerCommand("^charsof", new charsof());
            botPlugin.registerCommand("^char", new character());
            botPlugin.registerCommand("^rpaccept", new AcceptRP());
            botPlugin.registerCommand("^newrp", new newRP());
            botPlugin.registerCommand("^invite", new invite());
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
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new LastPostCheckService(), 0, 3, TimeUnit.SECONDS);
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(new RPExpirationDateService(), 0, 24, TimeUnit.HOURS);
    }

    public void storeAllRPS() {
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("SELECT * FROM `Roleplays` WHERE `status` = ?");
            ps.setString(1, "ACTIVE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DataCache.addRP(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            LogError(e);
        }
    }
}
