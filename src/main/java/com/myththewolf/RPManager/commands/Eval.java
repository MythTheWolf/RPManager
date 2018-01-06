package com.myththewolf.RPManager.commands;

import com.myththewolf.BotServ.lib.API.command.CommandExecutor;
import com.myththewolf.BotServ.lib.API.command.DiscordCommand;
import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.User.DiscordUser;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Eval implements CommandExecutor{
    @Override
    public void onCommand(DiscordCommand discordCommand) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            engine.put("con", RPManagerLoader.getSQLConnection());
            DiscordUser dd = null;
            engine.put("user",dd);
        }catch (Exception e){
            RPManagerLoader.LogError(e);
        }
    }
}
