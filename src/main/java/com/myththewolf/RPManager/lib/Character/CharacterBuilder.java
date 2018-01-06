package com.myththewolf.RPManager.lib.Character;

import com.myththewolf.RPManager.RPManagerLoader;
import com.myththewolf.RPManager.lib.User.DiscordUser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterBuilder {
    private DiscordUser owner;
    private String NAME;

    private String BIO;
    private String GENDER;
    private String SPECIES;
    private String HEIGHT;
    private String COLORS;
    private String NOTES;
    private List<String> REFRENCES;
    private String SEXUALITY;
    private int step = 1;
    String b = "";

    public CharacterBuilder(DiscordUser DU) {
        this.owner = DU;
        REFRENCES = new ArrayList<>();
    }

    public void setNAME(String name) {
        this.NAME = name;
    }

    public void setBIO(String bio) {
        this.BIO = bio;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public void setCOLORS(String COLORS) {
        this.COLORS = COLORS;
    }

    public void setHEIGHT(String HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void setSPECIES(String SPECIES) {
        this.SPECIES = SPECIES;
    }

    public void addReference(String URL) {
        this.REFRENCES.add(URL);
    }

    public void setNOTES(String NOTES) {
        this.NOTES = NOTES;
    }

    public void setSEXUALITY(String SEXUALITY) {
        this.SEXUALITY = SEXUALITY;
    }

    public void compile() {
        try {
            PreparedStatement PS = RPManagerLoader.getSQLConnection().prepareStatement("INSERT INTO `Characters` (`ID`,`owner_discord_id`, `sexuality`, `name`, `bio`, `gender`, `species`, `height`, `colors`, `notes`, `image_urls`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?,?)");
            PS.setString(1, owner.getDiscordID());
            PS.setString(2, SEXUALITY);
            PS.setString(3, NAME);
            PS.setString(4, BIO);
            PS.setString(5, GENDER);
            PS.setString(6, SPECIES);
            PS.setString(7, HEIGHT);
            PS.setString(8, COLORS);
            PS.setString(9, NOTES);
            this.REFRENCES.forEach(d -> {
                b += d + ",";
            });
            PS.setString(10, b.substring(0, b.length() - 1));

            PS.executeUpdate();
        } catch (SQLException e) {
            RPManagerLoader.LogError(e);
        }
    }

    public int getStepNumber() {
        return step;
    }

    public void commit() {
        step++;
    }
}
