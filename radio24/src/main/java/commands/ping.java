package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

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

public class ping implements radio24.cmd_int {

    @Override
    public void action(MessageReceivedEvent event, String timestamp) {
        event.getChannel().sendMessage(new EmbedBuilder().setTitle(":satellite: Current ping: " + event.getJDA().getGatewayPing() + "ms").setColor(Color.green).build()).complete();
    }

    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
