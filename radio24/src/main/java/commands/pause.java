package commands;

import Music.GuildMusicManager;
import Music.PlayerManager;
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

public class pause implements radio24.cmd_int {

    @Override
    public void action(MessageReceivedEvent event, String timestamp) {
        GuildMusicManager man = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        if (!man.player.isPaused()) {
            man.player.setPaused(true);
            System.out.println(timestamp + "the music has been paused");
        } else {
            man.player.setPaused(false);
            System.out.println(timestamp + "the music has been unpaused");
        }
    }

    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
