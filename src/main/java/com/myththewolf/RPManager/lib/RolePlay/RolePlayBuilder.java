package com.myththewolf.RPManager.lib.RolePlay;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.Character.RolePlayCharacter;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.ChannelManager;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class RolePlayBuilder {
    public DateTime CreationDate;
    public DateTime ExpireDate;
    public String name;
    public int turn;
    public List<RolePlayCharacter> characters;
    public TextChannel channel;
    public String STATUS;
    public String cat_id;
    public DateTime last_post;
    private String chars = "";
    private DiscordUser owner;

    public RolePlayBuilder(DiscordUser own) {
        owner = own;
        CreationDate = new DateTime();
        ExpireDate = CreationDate.plusDays(7);
        last_post = CreationDate.plusHours(1);
        turn = 0;
        cat_id = RPManagerLoader.INSTANCE.getJSONConfig().isNull("RP-CHANNEL-CATEGORY-ID") ? RPManagerLoader.INSTANCE.getJDAInstance().getGuilds().get(0).getCategories().get(0).getId() : RPManagerLoader.INSTANCE.getJSONConfig().getString("RP-CHANNEL-CATEGORY-ID");
        characters = new ArrayList<>();
        STATUS = "ACTIVE";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initCharacter(RolePlayCharacter c) {
        this.characters.add(c);
    }

    public void compile() {
        try {
            PreparedStatement ps = RPManagerLoader.getSQLConnection().prepareStatement("INSERT INTO `Roleplays` (`ID`, `creation_date`, `expire_date`, `name`, `turn`, `character_ids`, `channel_id`, `status`, `last_post_date`, `owner`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(CreationDate));
            ps.setString(2, DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(ExpireDate));
            ps.setString(3, name);
            ps.setInt(4, 0);
            chars = "";
            this.characters.forEach(c -> chars += c.getID() + ",");
            ps.setString(5, chars.substring(0, chars.length() - 1));
            String tid = RPManagerLoader.INSTANCE.getJDAInstance().getCategoryById(this.cat_id).createTextChannel(name).complete().getId();
            TextChannel create = RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(tid);
            ChannelManager man = new ChannelManager(create);
            man.setNSFW(true).queue();
            RPManagerLoader.INSTANCE.getJDAInstance().getGuilds().get(0).getRoles().forEach(role -> {
                try {
                    if (RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(tid).getPermissionOverride(role) == null) {
                        RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(tid).createPermissionOverride(role).setDeny(Permission.ALL_TEXT_PERMISSIONS).setAllow(Permission.MESSAGE_READ).queue();
                    }

                } catch (Exception e) {
                    RPManagerLoader.LogError(e);
                }
            });

            this.characters.forEach(character -> {
                try {
                    Member mem = RPManagerLoader.INSTANCE.getJDAInstance().getGuilds().get(0).getMemberById(character.getCharacterOwner().getDiscordID());
                    if (RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(tid).getPermissionOverride(mem) == null) {
                        RPManagerLoader.INSTANCE.getJDAInstance().getTextChannelById(tid).createPermissionOverride(mem).setAllow(Permission.ALL_TEXT_PERMISSIONS).complete();
                    }
                } catch (Exception e) {
                    RPManagerLoader.LogError(e);
                }

            });
            String chan = create.getId();
            ps.setString(6, chan);
            ps.setString(7, STATUS);
            ps.setString(8, DateTimeFormat.forPattern(DataCache.SYSTEM_DATE_FORMAT).print(this.last_post));
            ps.setString(9, owner.getDiscordID());
            ps.executeUpdate();

            DataCache.clearRPCache();
            RPManagerLoader.storeAllRPS();
        } catch (Exception e) {
            RPManagerLoader.LogError(e);
        }
    }
}
