package commands;

import Music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;

import static radio24.Bot.jda;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 27.12.2020
 * java-v  |
 * -
 * project | radio24
 * package | commands
 */

public class currentplaying implements radio24.cmd_int {
    @Override

    public void action(MessageReceivedEvent event, String timestamp) {
        EmbedBuilder emb = new EmbedBuilder().setFooter("requested by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl());
        if (playing(event) != false) {

            String track = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.getPlayingTrack().getInfo().title;
            emb.setTitle("Currently playing: " + track).setColor(Color.green);
            emb.addField("", collect_data(event), false);
        } else {
            emb.setTitle("Currently playing nothing...").setColor(Color.red);
        }
        event.getTextChannel().sendMessage(emb.build()).complete();

    }

    boolean playing(MessageReceivedEvent event) {
        boolean paused = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.isPaused();
        AudioTrack playing = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.getPlayingTrack();
        boolean connected = jda.getGuildById(event.getGuild().getId()).getAudioManager().isConnected();

        if (paused == false && playing != null && connected == true) {
            return true;
        }
        return false;
    }

    String collect_data(MessageReceivedEvent event) {
        HashMap<Guild, String> inp = radio24.Bot.dholder(event.getGuild(), "");
        String ret = inp.get(event.getGuild());
        return ret;
    }
    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
