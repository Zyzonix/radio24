package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

public class poweroff implements radio24.cmd_int{

    @Override
    public void action(MessageReceivedEvent event, String timestamp) {
        if (!event.getAuthor().getId().equals("403946863608201217") || event.getAuthor().getId().equals("412995111950090253")) return;
        System.out.println(timestamp + "shutting down...");
        System.exit(0);
    }

    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
