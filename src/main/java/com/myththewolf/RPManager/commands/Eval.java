package com.myththewolf.RPManager.commands;

import bsh.Interpreter;
import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.Color;

public class Eval implements CommandExecutor {
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        try {
            DiscordUser d;
            discordCommand.e.getTextChannel().getId();
            Interpreter engine = new Interpreter();
            engine.set("con", RPManagerLoader.getSQLConnection());
            engine.set("cmd", discordCommand);
           for(TextChannel t : discordCommand.e.getJDA().getGuildById("332702257214914561").getTextChannels()){
               t.sendMessage("I say hi!").queue();
               try{
                   Thread.sleep(5000);
               }catch (Exception e){
                   RPManagerLoader.LogError(e);
               }
           }
            engine.set("user", new DiscordUser(discordCommand.getSender().getId()));
            String bb = "";
            bb += "";

            for (String S : discordCommand.getArgs()) {
                bb += S + " ";
            }
            Object res = engine.eval(bb);
            EmbedBuilder EB = new EmbedBuilder();
            EB.setTitle("Java Evalutation");
            EB.setColor(Color.GREEN);
            EB.addField(":printer: Result", "```" + res.toString() + "```", false);
            EB.addField(":wrench: ResultType", "```" + res.getClass() + "```", false);
            discordCommand.reply(EB);
        } catch (Exception e) {
            e.printStackTrace();
            RPManagerLoader.LogError(e);
        }
    }
}
