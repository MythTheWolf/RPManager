package com.myththewolf.RPManager.lib;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;
import net.dv8tion.jda.core.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscordRoleplay {
    private int id;
    private Date CreationDate;
    private Date ExpireDate;
    private String roleplayName;
    private List<RolePlayCharacter> characterList = new ArrayList<>();
    private TextChannel hostChannel;
    private String status;

    public DiscordRoleplay(int id) {
        try {
            PreparedStatement ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Roleplays` WHERE `ID` = ?");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                this.id = id;

            }
        }catch (Exception e){
            RPManagerLoader.LogError(e);
        }
    }
}
