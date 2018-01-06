package com.myththewolf.RPManager;

import com.myththewolf.BotServ.lib.API.invoke.BotPlugin;
import com.myththewolf.BotServ.lib.API.invoke.PluginAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RPManagerLoader implements PluginAdapter {
    public static BotPlugin INSTANCE;
    public static Connection con;

    public void onEnable(BotPlugin botPlugin) {
        INSTANCE = botPlugin;
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
        String HOST = INSTANCE.getJSONConfig().getString("SQL_HOST");
        String PORT = INSTANCE.getJSONConfig().getString("SQL_PORT");
        String DATABASE = INSTANCE.getJSONConfig().getString("SQL_DATABASE");
        String USER = INSTANCE.getJSONConfig().getString("SQL_USER");
        String PASS = INSTANCE.getJSONConfig().getString("SQL_PASS");
        if (con == null || con.isClosed()) {
            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
            con = DriverManager.getConnection(url, USER, PASS);
        }
        return con;

    }
}
