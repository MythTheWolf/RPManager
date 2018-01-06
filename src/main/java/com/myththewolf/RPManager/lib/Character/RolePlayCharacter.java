package com.myththewolf.RPManager.lib.Character;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RolePlayCharacter {
    public int ID;
    public String name;
    public String bio;
    public String gender;
    public String Species;
    public String height;
    public String colors;
    public String notes;
    public String sexuality;
    public List<DiscordRoleplay> activeRoleplays = new ArrayList<DiscordRoleplay>();
    public List<String> refs = new ArrayList<>();
    public DiscordUser characterOwner;

    public RolePlayCharacter(int charID) {
        try {
            PreparedStatement ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Characters` WHERE `ID` = ?");
            ps.setInt(1, charID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ID = charID;
                bio = rs.getString("bio");
                name = rs.getString("name");
                gender = rs.getString("gender");
                Species = rs.getString("species");
                height = rs.getString("height");
                colors = rs.getString("colors");
                notes = rs.getString("notes");
                sexuality = rs.getString("sexuality");
                Arrays.stream(rs.getString("image_urls").split(" ")).forEach(con -> this.refs.add(con));
                characterOwner = new DiscordUser("owner_discord_id");
                PreparedStatement search = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Roleplays` WHERE `character_ids` LIKE \"%," + charID + "\" OR `character_ids` LIKE \"%," + charID + ",%\" AND `status` = ?");
                search.setString(1, "ACTIVE");
                ResultSet two = search.executeQuery();
                while (two.next()) {
                    activeRoleplays.add(new DiscordRoleplay(two.getInt("ID")));
                }
            }
        } catch (SQLException e) {
            RPManagerLoader.LogError(e);
        }
    }
}
