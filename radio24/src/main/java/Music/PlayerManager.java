package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 20/06/2020
 * -
 * project | radio24
 * package | Music
 */

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildID = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildID);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildID, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackURL) {
        LocalDateTime now = LocalDateTime.now();
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                String musictitle = "";
                if (audioTrack.getInfo().title.equals("Unknown title")) musictitle = ":radio:  Started playing your music, just enjoy!"; else musictitle = ":radio:  Now playing: " + audioTrack.getInfo().title;
                channel.sendMessage(new EmbedBuilder().setColor(Color.green).setTitle(musictitle).build()).complete();
                System.out.println("[INFO | " + dtf.format(now) + "] started music | " + audioTrack.getInfo().title);
                play(musicManager, audioTrack);
            }
            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack = audioPlaylist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = audioPlaylist.getTracks().get(0);
                }
                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + audioPlaylist.getName() +")").queue();
                play(musicManager, firstTrack);
            }
            @Override
            public void noMatches() {
                System.out.println("[ERROR | " + dtf.format(now) + "] no matches");
            }
            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println("[ERROR | " + dtf.format(now) + "] could not play the track");
                e.printStackTrace();
                channel.sendMessage(new EmbedBuilder().setColor(Color.red).setTitle(":warning: Couldn't load and play the track!").setDescription("Type -help for help!").build()).complete();

            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
