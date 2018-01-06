package com.myththewolf.RPManager.commands;

import bsh.Interpreter;
import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class Eval implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        try {
            DiscordUser d;
            Interpreter engine = new Interpreter();
            engine.set("con", RPManagerLoader.getSQLConnection());
            engine.set("cmd", discordCommand);
            engine.set("user", new DiscordUser(discordCommand.getSender().getId()));
            String bb = "";
            bb += "";

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
