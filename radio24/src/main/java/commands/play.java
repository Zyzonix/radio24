package commands;

import Music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import radio24.Bot;
import radio24.guildVCHandler;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static radio24.Bot.jda;

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

public class play implements radio24.cmd_int{
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public void action(MessageReceivedEvent event, String timestamp) {
        LocalDateTime now = LocalDateTime.now();
        String url = "";
        String[] msg_content = event.getMessage().getContentDisplay().split(" ", 2);
        if (msg_content[1].startsWith("https://") || msg_content[1].startsWith("http://")) {
            url = msg_content[1];
        } else {
            HashMap<Boolean, String> source = radio24.soundsourceservice.collector(msg_content[1]);
            if (source.containsKey(false)){
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle(":warning: '" + msg_content[1] + "' is not a valid argument").build()).complete();
                System.out.println("[ERROR | " + dtf.format(now) + "] couldn't resolve key (wrong parameter)");
                return;
            } else {
                url = source.get(true);
            }
        }

        if (PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.getPlayingTrack() != null)
            PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.stopTrack();
        if (PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.isPaused())
            PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.setPaused(false);

        VoiceChannel vChan = event.getGuild().getMemberById(event.getAuthor().getId()).getVoiceState().getChannel();
        if (vChan == null) {
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelsByName("Radio - Music 24/7", true).get(0)); //RADIO-24-HOME-CHANNEL
        } else {
            event.getGuild().getAudioManager().openAudioConnection(vChan);
        }

        PlayerManager manager = PlayerManager.getInstance();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        manager.loadAndPlay(event.getTextChannel(), url, msg_content[1]);
        manager.getGuildMusicManager(event.getGuild()).player.setVolume(5);
        guildVCHandler gvch = new guildVCHandler();
        new Thread(gvch).run();
        new guildVCHandler().starter(jda, event.getGuild(), vChan, event);
        radio24.Bot.dholder(event.getGuild(), "- requested by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + "\n- request: " + event.getMessage().getContentDisplay());
    }

    @Override
    public void executed(String timestamp, String command) {
        System.out.println(timestamp + "executed '" + command + "'");
    }
}
