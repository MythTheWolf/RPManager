package com.myththewolf.RPManager.lib.RolePlay;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.DataCache;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.awt.*;
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
    private String hostChannelID;
    private String status;
    private String serial = "";
    private int turn = 0;
    private DateTime lastPost;
    private DateTime lastPing;

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
                this.lastPost = DateTime.parse(rs.getString("last_post_date"), DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT));
                Arrays.stream(rs.getString("character_ids").split(",")).forEach(it -> {
                    if (!it.isEmpty()) {
                        this.characterList.add(new RolePlayCharacter(Integer.parseInt(it)));
                    }
                });
                this.hostChannelID = rs.getString("channel_id");
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
        return RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(this.hostChannelID);
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
        sendBoardMessage(character.getCharacterOwner().asRawDiscordUser().getName() + " has joined this RP as their character '" + character.getName() + "'");
        getHostChannel().createPermissionOverride(RPManagerLoader.INSTANCE.getJDAInstance().getGuilds().get(0).getMemberById(character.getCharacterOwner().getDiscordID())).setAllow(Permission.ALL_TEXT_PERMISSIONS).complete();
    }

    public void recompile() {
        try {
            PreparedStatement up = RPManagerLoader.getSQLConnection().prepareStatement("UPDATE `Roleplays` SET `expire_date` = ?, `name` = ?, `last_post_date` = ?, `character_ids` = ?  WHERE `ID` = ?");
            up.setString(1, DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(getExpireDate()));
            up.setString(2, getRoleplayName());
            up.setString(3, DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(getLastPostDate()));
            up.setString(4, getSerializedCharacterString());
            up.setInt(5, getId());
            up.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataCache.updateRolePlay(this);
    }

    public boolean expired() {
        return this.expireDate.isBeforeNow();
    }

    public void push() {
        this.lastPost = new DateTime();
        if (turn + 1 > this.getCharacterList().size() - 1) {
            turn = 0;
        } else {
            turn++;
        }
        System.out.println("TURN::"+turn);
        recompile();
    }

    public DateTime getLastPostDate() {
        return lastPost;
    }

    public RolePlayCharacter getStagedCharacter() {
        return getCharacterList().get(turn);
    }


    public boolean equals(Object obj) {
        if (obj instanceof DiscordRoleplay) if (((DiscordRoleplay) obj).getId() == this.getId()) return true;
        return false;
    }

    public DateTime getLastPing() {
        return lastPing;
    }

    public void setLastPing(DateTime lastPing) {
        this.lastPing = lastPing;
    }

    public void sendBoardMessage(String value) {
        EmbedBuilder t = new EmbedBuilder();
        t.setTitle("System Message");
        t.setDescription(value);
        t.setColor(Color.BLACK);
        getHostChannel().sendMessage(t.build()).queue();
    }

    public void archive() {
        this.status = "ARCHIVED";
        recompile();
    }
}
