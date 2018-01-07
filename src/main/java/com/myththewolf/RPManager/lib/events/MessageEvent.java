package com.myththewolf.RPManager.lib.events;

import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.io.File;
import java.util.UUID;

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
        if (user.getCharacterBuilder() != null && event.isFromType(ChannelType.PRIVATE)) {
            if (user.getCharacterBuilder().getStepNumber() == 7) {
                if (event.getMessage().getContent().equals("done")) {
                    user.getCharacterBuilder().commit();
                } else {
                    event.getMessage().getAttachments().forEach(attachment -> {
                        if (attachment.isImage()) {
                            File out = new File("/var/www/cdnmythserver/" + event.getAuthor().getId());
                            if (!out.exists()) {
                                out.mkdir();
                            }
                            String uuid = UUID.randomUUID().toString();
                            File fin = new File(out.getAbsolutePath() + File.separator + uuid + ".png");
                            attachment.download(fin);
                            System.out.println(fin.getAbsolutePath());
                            user.getCharacterBuilder().addReference("https://cdn.mythserver.ml/" + event.getAuthor().getId() + "/" + uuid + ".png");
                           // event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please upload any images you would like to have displayed of your character. Type `done` when complete.").queue();
                            return;
                        }
                    });
                }
            }
            switch (user.getCharacterBuilder().getStepNumber()) {
                case 1:
                    user.getCharacterBuilder().setNAME(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please type out your character's bio.").queue();
                    break;
                case 2:
                    user.getCharacterBuilder().setBIO(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: What is your character's gender? Please include which pronoun(s) you prefer!").queue();
                    break;
                case 3:
                    user.getCharacterBuilder().setGENDER(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: What is your character's sexuality? Please be specific!").queue();
                    break;
                case 4:
                    user.getCharacterBuilder().setSEXUALITY(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: In about a sentence or two, describe your character's colors and patterns").queue();
                    break;
                case 5:
                    user.getCharacterBuilder().setCOLORS(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please provide your character's height").queue();
                    break;
                case 6:
                    user.getCharacterBuilder().setHEIGHT(event.getMessage().getContent());
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please provide your character's species.").queue();
                    break;
                case 7:
                    user.getCharacterBuilder().setSPECIES(event.getMessage().getContent());
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please upload any images you would like to have displayed of your character. Type `done` when complete.").queue();
                    break;
                case 8:
                    user.getCharacterBuilder().commit();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(":ok_hand: Please type out any additional notes you would like to add.").queue();
                    break;
                case 9:
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
