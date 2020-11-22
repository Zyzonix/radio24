package radio24;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static radio24.guildVCHandler.*;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 14/11/2020
 * java-v  | 14
 * -
 * project | radio24
 * package | radio24
 */

public class guildVCHandler extends Thread {
    public static JDA jda;
    public static Guild g;
    public static VoiceChannel vChn;
    public static MessageReceivedEvent msgres;
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static void starter(JDA Jda, Guild guild, VoiceChannel vc, MessageReceivedEvent msgreceived) {
        System.out.println("[INFO | " + dtf.format(LocalDateTime.now()) + "] started disconnect-listener");
        g = guild;
        jda = Jda;
        vChn = vc;
        msgres = msgreceived;
        new Timer().schedule(new check(), 100, 10000);
    }
}
class check extends TimerTask {
    @Override
    public void run() {
        VoiceChannel v = jda.getAudioManagers().get(0).getConnectedChannel();
        if (v == null) return;
        System.out.println("[INFO | " + dtf.format(LocalDateTime.now()) + "] checking if VC is empty");
        List<VoiceChannel> conn_User = v.getGuild().getVoiceChannels();
        if (conn_User.isEmpty()) {
            disc();
            return;
        }
        List<Member> lm = v.getMembers();
        if (lm.isEmpty() || lm.size() == 1) {
            disc();
        }
        return;

    }
    void disc() {
        new Bot.listeners().MusicStop(msgres, "VC is empty, saving bandwith...");
    }
}
