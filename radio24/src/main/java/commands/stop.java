package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import radio24.Bot;

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

public class stop implements radio24.cmd_int {
    @Override
    public void action(MessageReceivedEvent event, String timestamp) {
        Bot.listeners.MusicStop(event, "received -stop command");
        Bot.dholder(event.getGuild(), "delete");
    }

    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
