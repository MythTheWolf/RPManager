package com.myththewolf.RPManager.lib.User;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.RolePlay.Character.CharacterBuilder;
import com.myththewolf.RPManager.lib.RolePlay.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.RolePlay.RolePlayRequest;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordUser {
    private String ID;
    private List<RolePlayCharacter> characters = null;
    private String status;
    private int max_rps;
    private int reputaion;
    private CharacterBuilder CB = null;
    private List<RolePlayRequest> requests = new ArrayList<>();

    public DiscordUser(String discordID) {
        PreparedStatement ps;
        try {
            ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Users` WHERE `discord_id` = ?");
            ps.setString(1, discordID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ID = discordID;
                status = rs.getString("status");
                max_rps = rs.getInt("max_concurrent_rps");
                reputaion = rs.getInt("reputation");
            }
        } catch (Exception e) {
            RPManagerLoader.LogError(e);
        }
    }

    public List<RolePlayRequest> getRPRequests() {
        return requests;
    }

    public void setCharacterBuilder(CharacterBuilder b) {
        this.CB = b;
    }

    public String getName() {
        return RPManagerLoader.INSTANCE.getJDAInstance().getUserById(getDiscordID()).getName();
    }

    public CharacterBuilder getCharacterBuilder() {
        return CB;
    }

    public List<RolePlayCharacter> getCharacters() {
        if (characters == null) {
            characters = new ArrayList<RolePlayCharacter>();
            PreparedStatement ps;
            try {
                ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Users` WHERE `discord_id` = ?");
                ps.setString(1, getDiscordID());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    for (String character_id : rs.getString("character_ids").split(",")) {
                        if (character_id.isEmpty()) {
                            continue;
                        }
                        characters.add(new RolePlayCharacter(Integer.parseInt(character_id)));
                    }
                }
            } catch (SQLException e) {
                RPManagerLoader.LogError(e);
            }
        }
        return characters;
    }


    public String getStatus() {
        return status;
    }

    public int getMaxRPs() {
        return max_rps;
    }

    public String getDiscordID() {
        return ID;
    }

    public int getReputaion() {
        return reputaion;
    }

    public RolePlayRequest getRPRequestByID(String id) {
        return getRPRequests().stream().filter(rolePlayRequest -> rolePlayRequest.getId().equals(id)).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }

    public RolePlayCharacter getCharacterByID(int id) {
        return getCharacters().stream().filter(character -> character.getID() == id).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }

    public PrivateChannel asPrivateChannel() {
        return RPManagerLoader.INSTANCE.getJDAInstance().getUserById(getDiscordID()).openPrivateChannel().complete();
    }

    public User asRawDiscordUser() {
        return RPManagerLoader.INSTANCE.getJDAInstance().getUserById(getDiscordID());
    }

    public void addRPReuqest(RolePlayRequest request) {
        this.requests.add(request);
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Roleplay Request");
        b.addField("RP Name", "```" + request.getRoleplay().getRoleplayName() + "```", false);
        b.setColor(Color.BLUE);
        b.setDescription("Use `^rpaccept " + request.getId() + " {{character id}}` where `{{character id}} is the ID of the character you wish to use to join.");
        b.setFooter("Dont know your character IDs? Use '^mychars' to find them!", null);
        asPrivateChannel().sendMessage(b.build()).queue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiscordUser) if (((DiscordUser) obj).getDiscordID().equals(this.getDiscordID())) return true;
        return false;
    }
}
