package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import radio24.Bot;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 27.12.2020
 * java-v  | 14
 * -
 * project | radio24
 * package | commands
 */

public class help implements radio24.cmd_int {
    @Override
    public void action(MessageReceivedEvent event, String timestamp) {
        HashMap<String, String> commands = new HashMap<>();
        commands.put("play", "Usage: -play [link] \n ʀᴀᴅɪᴏ²⁴ provides the following radiostations, type -play [name]:");
        commands.put("stop", "Usage: -stop \n ʀᴀᴅɪᴏ²⁴ will stop the current song and leave your voicechannel\n");
        commands.put("help", "Usage: -help \n You already entered the help-menu \n(-help -a will print a list of all commands)\n");
        commands.put("ping", "Usage: -ping \n ʀᴀᴅɪᴏ²⁴ will print his current ping\n");
        commands.put("pause", "Usage: -pause \n ʀᴀᴅɪᴏ²⁴ will pause/unpause the current song\n");
        commands.put("poweroff", "This command is only accessible for bot-administrators!\n");
        commands.put("playing", "ʀᴀᴅɪᴏ²⁴ will give information about the last song request\n");
        commands.put("stats", "placeholder");
        EmbedBuilder helpemb = new EmbedBuilder().setColor(Color.green).setFooter("requested by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl());
        helpemb.setTitle("ʀᴀᴅɪᴏ²⁴ - Helpmenu");
        helpemb.setThumbnail("https://www.clipartkey.com/mpngs/m/255-2554227_global-support-icon-png-clipart-png-download-international.png");
        helpemb.addField("IMPORTANT:", "This bot is amongst other things used for developmenttests, errors can happen! \n\n current bot-version: ```" + Bot.version + "```", true);
        helpemb.addBlankField(false);
        ArrayList<String> real_cmds = cmd_collector();
        for (String key : commands.keySet()) {
            if (commands.get(key).equals("placeholder")) {
                real_cmds.remove(key);
            }
        }
        for (Object i : real_cmds) {
            helpemb.addField("\n```-" + i.toString() + "```", commands.get(i), false);
            if (i.toString().equals("play")) {
                helpemb.addField("", radio24.soundsourceservice.rhh(),true);
                helpemb.addField("", radio24.soundsourceservice.rb(),true);
                helpemb.addField("", radio24.soundsourceservice.rsa(),true);
                helpemb.addField("", radio24.soundsourceservice.ilr(),true);
                helpemb.addBlankField(false);
            }
        }
        helpemb.addBlankField(false);
        event.getTextChannel().sendMessage(helpemb.build()).complete();
        if (event.getMessage().getContentDisplay().split(" ", 2).length != 1 && event.getMessage().getContentDisplay().split(" ", 2)[1].equals("-a")) {
            String description = "[";
            for (String i : Bot.command_cont.keySet()) {
                description += "-" + i + " | ";
            }
            description += "]";
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green).setTitle("all commands:").setDescription(description).build()).complete();
        }
    }


    ArrayList<String> cmd_collector() {
        ArrayList<String> command_cont = new ArrayList<>();
        HashMap<String, radio24.cmd_int> commands = Bot.command_cont;
        for (String key : commands.keySet()) {

            command_cont.add(key);
        }
        return command_cont;
    }
    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
