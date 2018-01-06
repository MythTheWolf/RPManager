package com.myththewolf.RPManager.lib.Character;

import com.myththewolf.RPManager.lib.DiscordRoleplay;

import java.util.ArrayList;
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
    public List<DiscordRoleplay> activeRoleplays = new ArrayList<DiscordRoleplay>();
    public RolePlayCharacter(String charID){

    }
}
