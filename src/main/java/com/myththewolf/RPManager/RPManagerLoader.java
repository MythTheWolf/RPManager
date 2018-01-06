package com.myththewolf.RPManager;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.myththewolf.BotServ.lib.API.invoke.BotPlugin;
import com.myththewolf.BotServ.lib.API.invoke.PluginAdapter;
import com.myththewolf.RPManager.commands.CharacterWizard;
import com.myththewolf.RPManager.commands.Eval;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import sun.rmi.runtime.Log;

import java.awt.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RPManagerLoader implements PluginAdapter {
    public static BotPlugin INSTANCE;
    public static Connection con;

    public void onEnable(BotPlugin botPlugin) {
        INSTANCE = botPlugin;
        DataCache.makeMaps();
        botPlugin.getJDAInstance().addEventListener(new MessageEvent());
        try {
            botPlugin.registerCommand("^evaldev", new Eval());
            botPlugin.registerCommand("^charwizard", new CharacterWizard());
        } catch (Exception e) {
            LogError(e);
        }

    }

    public void onDisable() {

    }

    public static void LogError(Exception e) {
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
}
