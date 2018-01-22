package com.myththewolf.RPManager.lib;

import net.dv8tion.jda.core.EmbedBuilder;
import org.json.JSONArray;

import java.awt.*;

public class DiscordUtils {
    public static EmbedBuilder systemWarningMessage(String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("System Message");
        eb.setDescription(":warning: " + message);
        eb.setColor(Color.BLACK);
        return eb;
    }

    public static boolean isInt(String test) {
        try {
            Integer.parseInt(test);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean inJSONArray(String needle, JSONArray haystack) {
        return haystack != null ? haystack.toList().contains(needle) : false;
    }
}
