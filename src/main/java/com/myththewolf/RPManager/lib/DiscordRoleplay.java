package com.myththewolf.RPManager.lib;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;
import net.dv8tion.jda.core.entities.TextChannel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordRoleplay {
    private int id;
    private DateTime creationDate;
    private DateTime expireDate;
    private String roleplayName;
    private List<RolePlayCharacter> characterList = new ArrayList<>();
    private TextChannel hostChannel;
    private String status;
    private String serial = "";

    public DiscordRoleplay(int id) {
        try {
            PreparedStatement ps = RPManagerLoader.getSQLConnection().prepareStatement("SELECT * FROM `Roleplays` WHERE `ID` = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.id = id;
                this.creationDate = DateTime.parse(rs.getString("creation_date"), DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT));
                this.expireDate = DateTime.parse(rs.getString("expire_date"), DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT));
                this.roleplayName = rs.getString("name");

                Arrays.stream(rs.getString("character_ids").split(",")).forEach(it -> {
                    if (!it.isEmpty()) {
                        this.characterList.add(DataCache.getCharacterByID(Integer.parseInt(it)));
                    }
                });
                this.hostChannel = RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(rs.getString("channel_id"));
                this.status = rs.getString("status");
            }
        } catch (Exception e) {
            RPManagerLoader.LogError(e);
        }
    }

    public String getStatus() {
        return status;
    }

    public String getRoleplayName() {
        return roleplayName;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public DateTime getExpireDate() {
        return expireDate;
    }

    public int getId() {
        return id;
    }

    public List<RolePlayCharacter> getCharacterList() {
        return characterList;
    }

    public TextChannel getHostChannel() {
        return hostChannel;
    }

    public void updateCreationDate() {
        this.expireDate = this.expireDate.plusWeeks(1);
        recompile();
    }

    public String getSerializedCharacterString() {
        serial = "";
        getCharacterList().forEach(character -> {
            this.serial += character.getID() + ",";
        });
        return this.serial.substring(0, this.serial.length() - 1);
    }

    public void addCharacter(RolePlayCharacter character) {
        if (!getCharacterList().contains(character)) {
            this.characterList.add(character);
            recompile();
        }
    }

    public void recompile() {
        try {
            PreparedStatement up = RPManagerLoader.getSQLConnection().prepareStatement("UPDATE `Roleplays` SET `expire_date` = ?, `name` = ?, `character_ids` = ?  WHERE `ID` = ?");
            up.setString(1, DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(getExpireDate()));
            up.setString(2, getRoleplayName());
            up.setString(3, getSerializedCharacterString());
            up.setInt(4, getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        DataCache.updateRolePlay(this);
    }

    public boolean expired() {
        return this.expireDate.isBeforeNow();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiscordRoleplay) if (((DiscordRoleplay) obj).getId() == this.getId()) return true;
        return false;
    }
}
