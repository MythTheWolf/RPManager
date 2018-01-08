package com.myththewolf.RPManager.lib.User;

import com.myththewolf.RPManager.lib.Character.RolePlayCharacter;

import java.util.UUID;

public class RolePlayRequest {
    private DiscordRoleplay requestTo;
    private String id;
    public RolePlayRequest(DiscordRoleplay source){
        requestTo = source;
        this.id = UUID.randomUUID().toString();
    }
    public void accept(RolePlayCharacter character){
        requestTo.addCharacter(character);
        requestTo.recompile();
    }

    public String getId() {
        return id;
    }
}
