package com.myththewolf.RPManager.lib;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class DiscordUtils {
    public static EmbedBuilder systemWarningMessage(String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("System Message");
        eb.setDescription(":warning: " + message);
        eb.setColor(Color.BLACK);
        return eb;
    }
}
