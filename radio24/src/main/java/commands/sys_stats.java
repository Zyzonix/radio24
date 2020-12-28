package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.NumberFormat;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 28.12.2020
 * java-v  | 14
 * -
 * project | radio24
 * package | commands
 */

public class sys_stats implements radio24.cmd_int {
    @Override
    public void action(MessageReceivedEvent event, String timestamp) {
        EmbedBuilder emb = new EmbedBuilder().setColor(Color.orange).setFooter("requested by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl());
        String memory = "";
        long total, free, used;
        int mb = 1024*1024;

        total = Runtime.getRuntime().totalMemory();
        free = Runtime.getRuntime().freeMemory();
        used = total - free;
        System.out.println("\nTotal Memory: " + total / mb + "MB");
        System.out.println("Memory Used: " + used / mb + "MB");
        System.out.println("Memory Free: " + free / mb + "MB");
        System.out.println("Percent Used: " + ((double)used/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%");



        event.getTextChannel().sendMessage(emb.setDescription(memory).build()).complete();

    }

    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
