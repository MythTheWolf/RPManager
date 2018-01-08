package com.myththewolf.RPManager.lib.RolePlay;

import com.myththewolf.RPManager.lib.RolePlay.Character.RolePlayCharacter;

import java.util.UUID;

public class RolePlayRequest {
    private DiscordRoleplay requestTo;
    private String id;

    public RolePlayRequest(DiscordRoleplay source) {
        requestTo = source;
        this.id = UUID.randomUUID().toString();
    }

    public void accept(RolePlayCharacter character) {
        requestTo.addCharacter(character);
        requestTo.recompile();
    }

    public String getId() {
        return id;
    }

    public DiscordRoleplay getRoleplay() {
        return requestTo;
    }
}
