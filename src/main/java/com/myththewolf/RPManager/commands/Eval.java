package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;

public class Eval implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            engine.put("con", RPManagerLoader.getSQLConnection());
            DiscordUser dd = null;
           engine.put("user", dd);
            String bb = "";
            for (String S : discordCommand.getArgs()) {
                bb += S + " ";
            }
            Object res = engine.eval(bb);
            EmbedBuilder EB = new EmbedBuilder();
            EB.setTitle("Java Evalutation");
            EB.setColor(Color.BLACK);
            EB.addField(":printer: Result", "```" + res.toString() + "```", false);
            EB.addField(":wrench: ResultType", "```" + res.getClass() + "```", false);
            discordCommand.reply(EB);
        } catch (Exception e) {
            e.printStackTrace();
            RPManagerLoader.LogError(e);
        }
    }
}
