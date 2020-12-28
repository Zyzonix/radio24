package radio24;

import Music.GuildMusicManager;
import Music.PlayerManager;
import commands.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 14/05/2020
 * java-v  | 14
 * -
 * project | radio24
 * package | radio24
 */

public class Bot {
    public static JDA jda;
    public static final double version = 2.0;
    static Timer timer = new Timer();
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static HashMap<String, cmd_int> command_cont = new HashMap<>();
    static HashMap<Guild, String> data_holder = new HashMap<>();
    public static class listeners extends ListenerAdapter {

        @Override
        public void onReady(ReadyEvent ready) throws NullPointerException {
            LocalDateTime now = LocalDateTime.now();
            ready.getJDA().getPresence().setActivity(Activity.playing("starting..."));
            ready.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            System.out.println("\n[INFO | " + dtf.format(now) + "] Bot started successfully");
            String out = "[INFO | " + dtf.format(now) + "] Radio-24 is running on following servers: \n";
            for (Guild g : ready.getJDA().getGuilds()) {
                out += "- " + g.getName() + " [ID: " + g.getId() + "] \n";
            }
            long ping = ready.getJDA().getGatewayPing();
            out += "[INFO | " + dtf.format(now) + "] ping is: " + ping + "ms";
            System.out.println(out);
            jda.getPresence().setStatus(OnlineStatus.IDLE);
        }

        public void onPrivateMessageReceived(PrivateMessageReceivedEvent privmsg) {
            if (privmsg.getAuthor().isBot() || !privmsg.getMessage().getContentDisplay().startsWith("-")) return;
            LocalDateTime now = LocalDateTime.now();
            System.out.println("[PRIV-INFO | " + dtf.format(now) + "] received private message");
            privmsg.getChannel().sendMessage(privmsg.getAuthor().getId()).complete();
            Guild g = privmsg.getJDA().getGuildById("405355363345498112");
            String go = g.getOwnerId();
            if (!privmsg.getAuthor().getMutualGuilds().contains(g) && !privmsg.getAuthor().getId().equals(go) || !privmsg.getMessage().getContentDisplay().contains(" ")) {
                System.out.println("[PRIV-ERROR | " + dtf.format(now) + "] unauthorized user tried to access the system / arguments missing");
                return;
            }
            Message msg = privmsg.getMessage();
            String[] msgsplitted = msg.getContentDisplay().split(" ", 2);
            String command = msgsplitted[0];
            String content = " ";
            content = msgsplitted[1];

            //command ressources
            HashSet<String> commands = new HashSet<String>();
            commands.add("-update");
            commands.add("-mbreak");


            if (commands.contains(command)) {
                String messagecontent = "";
                if (command.equals("-update")) {
                    if (!content.contains(" ")) {
                        privmsg.getChannel().sendMessage("-update-Usage: -update [new perks] (split by ; - example: Listener;Answer").complete();
                        System.out.println("[PRIV-ERROR | " + dtf.format(now) + "] arguments missing for update-news");
                        return;
                    }
                    String[] upcont = content.split(" ", 2);
                    String[] perkinf = null;
                    String perkinfo = "";
                    if (upcont[1].contains(";")) {
                        perkinf = upcont[1].split(";");
                        for (String i : perkinf) {
                            perkinfo += "- " + i + "\n";
                        }
                    } else {
                        perkinfo = "- " + upcont[1];
                    }
                    messagecontent += ":pencil: **Good news! A new update has been released, ʀᴀᴅɪᴏ²⁴ is back online.** \n";
                    messagecontent += "```Updated from version " + upcont[0] + " to version " + version + "\n";
                    messagecontent += "New Perks: \n" + perkinfo + "```";

                } else if (command.equals("-mbreak")) {
                    messagecontent += ":tools: **Next maintenance break upcoming: " + content + " (CET)**\n";
                    messagecontent += "Until then the bot is available as normal. If the maintenance break is finished, the bot will be back online.";
                }
                System.out.println("[PRIV-INFO | " + dtf.format(now) + "] building answer | " + command);
                privmsg.getChannel().sendMessage(":white_check_mark: sending message...").complete();
                //send to all guilds
                for (Guild guild : privmsg.getJDA().getGuilds()) {
                    guild.getDefaultChannel().sendMessage(messagecontent).complete();
                }
            } else {
                privmsg.getChannel().sendMessage("This are my commands: " + commands.toString()).complete();
                System.out.println("[PRIV-ERROR | " + dtf.format(now) + "] unexpected command received");
            }
        }

        public static void MusicStop(MessageReceivedEvent msgreceived, String reason) {
            if (msgreceived.getGuild().getAudioManager().getConnectedChannel() == null) return;
            else msgreceived.getGuild().getAudioManager().closeAudioConnection();
            PlayerManager manager = PlayerManager.getInstance();
            if (manager.getGuildMusicManager(msgreceived.getGuild()).player.getPlayingTrack() == null) return;
            jda.getPresence().setStatus(OnlineStatus.IDLE);
            manager.getGuildMusicManager(msgreceived.getGuild()).player.stopTrack();
            System.out.println("[INFO | " + dtf.format(LocalDateTime.now()) + "] the music has been stopped | reason: " + reason);
        }

        public void onMessageReceived(MessageReceivedEvent msgreceived) {
            LocalDateTime now = LocalDateTime.now();
            EmbedBuilder answer = new EmbedBuilder().setFooter("requested by " + msgreceived.getAuthor().getName() + "#" + msgreceived.getAuthor().getDiscriminator(), msgreceived.getAuthor().getAvatarUrl());
            Message msg = msgreceived.getMessage();
            User auth = msgreceived.getAuthor();
            String authorfull = msgreceived.getAuthor().getName() + "#" + msgreceived.getAuthor().getDiscriminator();
            String guildname = msgreceived.getGuild().getName();
            String msgcommand = "";
            if (msg.getContentDisplay().contains(" ")) {
                String[] msgsplitted = msg.getContentDisplay().split(" ", 2);
                msgcommand = msgsplitted[0];
            } else {
                msgcommand = msg.getContentDisplay();
            }


            //Automated Messagedeletion
            boolean isBot = auth.isBot();
            if (isBot == true && auth.equals(msgreceived.getJDA().getSelfUser())) {
                MessageHandler.sendInfo(msg.getTextChannel(),msg.getId(),msg);
                MessageHandler msghan = new MessageHandler();
                Thread thmsg = new Thread(msghan);
                thmsg.start();
            }
            if (msgreceived.getAuthor().isBot()) return;
            System.out.println("[INFO | " + dtf.format(now) + "] received message: '" + msgreceived.getMessage().getContentDisplay() + "' by " + authorfull + "@" + msgreceived.getGuild().getName());
            if (msgcommand.startsWith("-")) {
                msg.delete().complete();
                String command_raw = msgcommand.replace("-", "");
                if (command_cont.containsKey(command_raw)) {
                    try {
                        command_cont.get(command_raw).action(msgreceived, "[INFO | " + dtf.format(now) + "] ");
                        command_cont.get(command_raw).executed("[INFO | " + dtf.format(now) + "] ", msgcommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("[ERROR | " + dtf.format(now) + "] something went wrong");
                    }

                } else {
                    msgreceived.getTextChannel().sendMessage(answer.setColor(Color.red).setTitle(":warning: Command ["+ msgcommand + "] unknown!").setDescription("Type -help for help!").build()).complete();
                    System.out.println("[ERROR | " + dtf.format(now) + "] Unknown command | '" + msg.getContentDisplay() + "' by " + authorfull + "@" + guildname);
                    return;
                }
            }
        }
    }
    public static void main(String[] args) {

        //Console logging
        LogWriter logWriter = new LogWriter();
        Thread ThLg = new Thread(logWriter);
        ThLg.start();

        LocalDateTime now = LocalDateTime.now();

        //boot system
        System.out.println("[INFO | " + dtf.format(now) + "] started booting... ");
        JDABuilder builder = JDABuilder.createDefault("NzQ0MjMzMjA0MzY5NzE5Mjk3.XzgPLw.pYyKzduYrFboQaAKmjxlR5YI_ic");
        builder.setAutoReconnect(true)
                .setStatus(OnlineStatus.DO_NOT_DISTURB);
                //common token: NTg2MjA0MTY4MjM0OTkxNjI4.XPknLA.oEXPaY2aQ76iSHaan6NQFXjzOj4
                //dev token: NzQ0MjMzMjA0MzY5NzE5Mjk3.XzgPLw.pYyKzduYrFboQaAKmjxlR5YI_ic

        //Adding listeners
        addCommands();
        builder.addEventListeners(new listeners());

        try {
            jda = builder.build();
        } catch (LoginException e) {
            System.out.println("[ERROR | " + dtf.format(now) + "] Build failed:");
            e.printStackTrace();
        }
        timer.schedule(new statusupdate(), 10000, 20000);
        System.out.println("[INFO | " + dtf.format(now) + "] started statusupdate()");
    }

    static void addCommands(){
        command_cont.put("poweroff", new poweroff());
        command_cont.put("ping", new ping());
        command_cont.put("pause", new pause());
        command_cont.put("stop", new stop());
        command_cont.put("play", new play());
        command_cont.put("help", new help());
        command_cont.put("playing", new currentplaying());
        command_cont.put("stats", new currentplaying());
    }

    static class statusupdate extends TimerTask {
        @Override
        public void run() {
            //getting date this way is deprecated; newer version available!!
            long ping = jda.getGatewayPing();
            Date time = new Date();
            int minutes = time.getMinutes();
            String minutesupdated = "";
            if (minutes < 10) minutesupdated = "0";
            String tm = time.getHours() + ":" + minutesupdated + minutes;
            Activity act = jda.getPresence().getActivity();
            String activity1 = "the space...";
            String activity2 = "the clock: " + tm + " (CET)";
            String activity3 = "-help";
            String activity4 = "my ping: " + ping + "ms";
            String actcont = "";
            if (jda.getPresence().getActivity().getName() == null) {
                actcont = "the clock:";
            }
            if (Objects.equals(jda.getPresence().getActivity(), Activity.watching(activity1))) {
                jda.getPresence().setActivity(Activity.watching(activity2));
            } else if (actcont.contains("the clock:")) {
                jda.getPresence().setActivity(Activity.listening(activity3));
            } else if (jda.getPresence().getActivity().getName().contains("-help")) {
                jda.getPresence().setActivity(Activity.watching(activity4));
            } else if (jda.getPresence().getActivity().getName().contains("my ping:")) {   // <-- Add other values here
                jda.getPresence().setActivity(Activity.watching(activity1));                    // <-- Add other values here
            } else {
                jda.getPresence().setActivity(Activity.watching(activity1));
            }
        }
    }

    public static HashMap<Guild, String> dholder(Guild guild, String data) {
        HashMap<Guild, String> dh = new HashMap<>();
        if (data.isEmpty()) {
            try {
                dh.put(guild, data_holder.get(guild));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.equals("delete")) {
            data_holder.remove(guild);
        } else {
            data_holder.put(guild, data);
            dh = data_holder;
        }
        return dh;
    }
}