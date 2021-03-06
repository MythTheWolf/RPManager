package com.myththewolf.RPManager.lib.events;

import com.myththewolf.RPManager.lib.DataCache;
import com.myththewolf.RPManager.lib.RolePlay.DiscordRoleplay;
import com.myththewolf.RPManager.lib.User.DiscordUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.joda.time.DateTime;

import java.awt.*;
import java.io.File;
import java.util.UUID;

public class MessageEvent implements EventListener {
    boolean escape;
    boolean found;
    DiscordRoleplay target;
    DateTime lastcommand;

    public void onEvent(Event eve) {
        if (!(eve instanceof MessageReceivedEvent)) {
            return;
        }
        if (((MessageReceivedEvent) eve).getAuthor().isBot()) {
            return;
        }

        escape = false;
        found = false;
        MessageReceivedEvent event = (MessageReceivedEvent) eve;
        User self = event.getJDA().getUserById(event.getJDA().getSelfUser().getId());
        if (event.getMessage().getMentionedUsers().contains(self)) {
            event.getTextChannel().sendMessage(":wave: Hello there! I'm the server's butler. I do many different things to keep this server running smooth, and I have many features you (might) enjoy! \n My commands can be found by using `^help`, this will only print a list of commands. However, if you wish to know more about a command, you may use `!man <command name>` \n Example: `!man charsof` \n I hope I best serve you, and I look forward to helping you!").queue();
            return;
        }
        if (event.getMessage().getContent().startsWith("^")) {
            return;
        }
        DiscordUser user = DataCache.getDiscordUserByID(event.getAuthor().getId());
        found = false;
        target = null;
        DataCache.getRoleplayMap().forEach((id, rp) -> {
            if (event.getChannelType().equals(ChannelType.TEXT) && rp.getHostChannel().getId().equals(event.getTextChannel().getId())) {
                found = true;
                target = rp;
            }
        });

        if (found) {
            String message = event.getMessage().getRawContent();
            if (!message.startsWith("((") && !message.startsWith("_") && !message.startsWith("*") & !message.startsWith("(") && !message.startsWith("//")) {
                EmbedBuilder illegal = new EmbedBuilder();
                illegal.setColor(Color.RED);
                illegal.setTitle("Illegal action");
                illegal.addField("RP Name:", target.getRoleplayName(), false);
                illegal.addField("Illegal Content:", event.getMessage().getContent(), false);
                illegal.setDescription("All RP posts must be one of the following: \n ((\\*any text\\* for OCC messages \n \\_\\*Any text\\*\\_, or \\*Any text\\* for RP actions.");
                illegal.setFooter("See !man roleplaying", null);
                user.asPrivateChannel().sendMessage(illegal.build()).queue();
                event.getMessage().delete().queue();
                return;
            } else if (!target.getStagedCharacter().getCharacterOwner().equals(user) && (!message.startsWith("((") && !message.startsWith("//") && !message.startsWith("("))) {
                EmbedBuilder illegal = new EmbedBuilder();
                illegal.setColor(Color.RED);
                illegal.setTitle("Illegal action");
                illegal.addField("RP Name:", target.getRoleplayName(), false);
                illegal.addField("Illegal Content:", event.getMessage().getContent(), false);
                illegal.setDescription("It is not your turn to post. You may only post OOC actions.");
                illegal.setFooter("See !man roleplaying", null);
                user.asPrivateChannel().sendMessage(illegal.build()).queue();
                event.getMessage().delete().queue();
                return;
            } else if (target.getStagedCharacter().getCharacterOwner().equals(user) && (message.startsWith("_") || message.startsWith("*"))) {
                target.push();
                return;
            }
        } else if (user.getCharacterBuilder() != null && event.isFromType(ChannelType.PRIVATE)) {
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
                            escape = true;
                        }
                    });
                }
            }
            if (escape) {
                return;
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
