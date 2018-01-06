package com.myththewolf.RPManager.lib.events;

import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class MessageEvent implements EventListener {
    public void onEvent(Event eve) {
        if (!(eve instanceof MessageReceivedEvent)) {
            return;
        }
        if (((MessageReceivedEvent) eve).getAuthor().isBot()) {
            return;
        }
        MessageReceivedEvent event = (MessageReceivedEvent) eve;
        DiscordUser user = DataCache.getDiscordUserByID(event.getAuthor().getId());
        System.out.println("bok:" + user.getCharacterBuilder() == null);
        if (user.getCharacterBuilder() != null && event.isFromType(ChannelType.PRIVATE)) {
            switch (user.getCharacterBuilder().getStepNumber()) {
                case 1:
                    user.getCharacterBuilder().setNAME(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please type out your character's bio.").queue();
                    break;
                case 2:
                    user.getCharacterBuilder().setBIO(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: What is your character's gender?").queue();
                    break;
                case 3:
                    user.getCharacterBuilder().setGENDER(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: In about a sentence or two, describe your character's colors and patterns").queue();
                    break;
                case 4:
                    user.getCharacterBuilder().setCOLORS(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please provide your character's height").queue();
                    break;
                case 5:
                    user.getCharacterBuilder().setHEIGHT(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please provide your character's species.").queue();
                    break;
                case 6:
                    user.getCharacterBuilder().setSPECIES(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please provide any direct image URLs that represent your character, seperated by spaces.").queue();
                    break;
                case 7:
                    user.getCharacterBuilder().setREFRENCES(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please type out any additional notes you would like to add.").queue();
                    break;
                case 8:
                    user.getCharacterBuilder().setNOTES(event.getMessage().getContent());
                    user.getCharacterBuilder().compile();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: I have recorded everything I needed, and your character has been added!").queue();
                    user.setCharacterBuilder(null);
                    break;
                default:
                    break;
            }
        }
    }
}
