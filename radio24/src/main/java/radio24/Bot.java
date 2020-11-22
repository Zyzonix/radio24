package radio24;

import Music.GuildMusicManager;
import Music.PlayerManager;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
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
    public static final double version = 1.2;
    static Timer timer = new Timer();
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    static int counter = 0;
    static class listeners extends ListenerAdapter {

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

            Date time = new Date();
            int minutes = time.getMinutes();
            String minutesupdated = "";
            if (minutes < 10) minutesupdated = "0";
            String tm = time.getHours() + ":" + minutesupdated + minutes;

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

        public void MusicStop(MessageReceivedEvent msgreceived, String starter) {
            if (msgreceived.getGuild().getAudioManager().getConnectedChannel() == null) return;
            else msgreceived.getGuild().getAudioManager().closeAudioConnection();
            PlayerManager manager = PlayerManager.getInstance();
            if (manager.getGuildMusicManager(msgreceived.getGuild()).player.getPlayingTrack() == null) return;
            jda.getPresence().setStatus(OnlineStatus.IDLE);
            manager.getGuildMusicManager(msgreceived.getGuild()).player.stopTrack();
            System.out.println("[INFO | " + dtf.format(LocalDateTime.now()) + "] the music has been stopped, reason: " + starter );
        }

        public void onMessageReceived(MessageReceivedEvent msgreceived) {
            if (!msgreceived.isFromGuild()) return;
            LocalDateTime now = LocalDateTime.now();
            EmbedBuilder answer = new EmbedBuilder().setFooter("requested by " + msgreceived.getAuthor().getName() + "#" + msgreceived.getAuthor().getDiscriminator(), msgreceived.getAuthor().getAvatarUrl());
            Message msg = msgreceived.getMessage();
            User auth = msgreceived.getAuthor();
            String msgcommand = "";
            String msgcontent = "";
            if (msg.getContentDisplay().contains(" ")) {
                String[] msgsplitted = msg.getContentDisplay().split(" ", 2);
                msgcommand = msgsplitted[0];
                msgcontent = msgsplitted[1].toLowerCase();
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

            String url = "";
            String authorfull = msgreceived.getAuthor().getName() + "#" + msgreceived.getAuthor().getDiscriminator();
            String guildname = msgreceived.getGuild().getName();

            //Linkressources
            HashSet<String> RadioBrocken = new HashSet<String>();
            RadioBrocken.add("radio brocken");
            RadioBrocken.add("rb");
            RadioBrocken.add("brocken");
            HashSet<String> RadioHamburg = new HashSet<String>();
            RadioHamburg.add("radio hamburg");
            RadioHamburg.add("rhh");
            RadioHamburg.add("hamburg");
            HashSet<String> RSARadio = new HashSet<String>();
            RSARadio.add("rsa radio");
            RSARadio.add("rsa");
            RSARadio.add("sachsen");
            HashSet<String> ILR = new HashSet<String>();
            ILR.add("i love radio");
            ILR.add("love");
            ILR.add("ilr");

            //Commandressources
            HashSet<String> commands = new HashSet<String>();
            commands.add("-poweroff");
            commands.add("-pause");
            commands.add("-help");
            commands.add("-stop");
            commands.add("-play");
            commands.add("-ping");

            //transform for help + content for help
            ArrayList<String> comarr = new ArrayList<String>();
            for (String i : commands) {
                comarr.add(i);
            }
            ArrayList<String> commandexpl = new ArrayList<String>();
            commandexpl.add("This command is only accessible for bot-administrators"); //-poweroff (1)
            commandexpl.add("You still entered the help menu, congrats!"); //-help (2)
            commandexpl.add("Usage: -pause \n The current song will be paused/unpaused"); //-pause (3)
            commandexpl.add("Usage: -stop \n This will stop the current song and the bot will leave your voicechannel"); //-stop (4)
            commandexpl.add("Usage: -play [link] \n or select your favorite radiostation: \n  - Radio Brocken " + RadioBrocken.toString() + "\n - Radio Hamburg " + RadioHamburg.toString() + "\n - R.SA Radio " + RSARadio.toString() + "\n - I Love Radio " + ILR.toString()); //-play (5)
            commandexpl.add("Usage: -ping \n -ping will print the current ping from ʀᴀᴅɪᴏ²⁴"); //-ping (0)

            if (msgreceived.getAuthor().isBot()) return;
            System.out.println("[INFO | " + dtf.format(now) + "] received Message: '" + msgreceived.getMessage().getContentDisplay() + "' by '" + authorfull + "'@" + msgreceived.getGuild().getName());
            if (msg.getContentDisplay().startsWith("-") && commands.contains(msgcommand)) {
                msg.delete().complete();
                if (msgcommand.equals("-play")) {
                    if (msgcontent.isEmpty()) {
                        msgreceived.getChannel().sendMessage(answer.setTitle(":warning: Please give me a parameter").setDescription("Type -help for help").setColor(Color.red).build()).complete();
                        return;
                    }
                    if (msgcontent.startsWith("http://") || msgcontent.startsWith("https://") && !msgcontent.contains(" ")) url = msgcontent;
                    if (PlayerManager.getInstance().getGuildMusicManager(msgreceived.getGuild()).player.getPlayingTrack() != null) PlayerManager.getInstance().getGuildMusicManager(msgreceived.getGuild()).player.stopTrack();
                    if (PlayerManager.getInstance().getGuildMusicManager(msgreceived.getGuild()).player.isPaused()) PlayerManager.getInstance().getGuildMusicManager(msgreceived.getGuild()).player.setPaused(false);
                    if (msg.getContentDisplay().contains(" ") && !msgcontent.startsWith("http://") && !msgcontent.startsWith("https://")) {
                        if (RadioBrocken.contains(msgcontent)) {
                            url = "http://stream.radiobrocken.de/live/mp3-256/stream.radiobrocken.de/";
                        } else if (RadioHamburg.contains(msgcontent)) {
                            url = "http://stream.radiohamburg.de/rhh-live/mp3-128/stream.radiohamburg.de/";
                        } else if (RSARadio.contains(msgcontent)) {
                            url = "https://streams.rsa-sachsen.de/rsa-live/mp3-128/streams.rsa-sachsen.de/";
                        } else if (ILR.contains(msgcontent)) {
                            url = "https://www.ilovemusic.de/iloveradio.m3u";
                        } else {
                            System.out.println("[ERROR " + dtf.format(now) + "] argument couldn't be resolved (argument: " + msgcontent + ")");
                            msgreceived.getChannel().sendMessage(answer.setColor(Color.red).setTitle(":warning: '" + msgcontent + "' is not a valid argument").build()).complete();
                        }
                    }
                    VoiceChannel vChan = msg.getGuild().getMemberById(msg.getAuthor().getId()).getVoiceState().getChannel();
                    if (vChan == null) {
                        msgreceived.getGuild().getAudioManager().openAudioConnection(msgreceived.getGuild().getVoiceChannelsByName("Radio - Music 24/7", true).get(0)); //RADIO-24-HOME-CHANNEL
                    } else {
                        msgreceived.getGuild().getAudioManager().openAudioConnection(vChan);
                    }
                    PlayerManager manager = PlayerManager.getInstance();
                    jda.getPresence().setStatus(OnlineStatus.ONLINE);
                    manager.loadAndPlay(msgreceived.getTextChannel(), url);
                    manager.getGuildMusicManager(msgreceived.getGuild()).player.setVolume(5);
                    guildVCHandler gvch = new guildVCHandler();
                    new Thread(gvch).run();
                    new guildVCHandler().starter(jda, msgreceived.getGuild(), vChan, msgreceived);
                } else if (msg.getContentDisplay().equals("-stop")) {
                    MusicStop(msgreceived, "got userinput '-stop'");
                } else if (msg.getContentDisplay().equals("-pause")) {
                    GuildMusicManager man = PlayerManager.getInstance().getGuildMusicManager(msg.getGuild());
                    if (!man.player.isPaused()) {
                        man.player.setPaused(true);
                        System.out.println("[INFO | " + dtf.format(now) + "] the music has been paused");
                    } else {
                        man.player.setPaused(false);
                        System.out.println("[INFO | " + dtf.format(now) + "] the music has been unpaused");
                    }
                } else if (msg.getContentDisplay().equals("-help")) { //-play,-stop,-pause,-now(playing),-restart,-poweroff,
                    EmbedBuilder helpemb = new EmbedBuilder().setColor(Color.green).setFooter(msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator(), msg.getAuthor().getAvatarUrl());
                    helpemb.setTitle("ʀᴀᴅɪᴏ²⁴ - Helpmenu");
                    helpemb.setThumbnail("https://www.clipartkey.com/mpngs/m/255-2554227_global-support-icon-png-clipart-png-download-international.png");
                    helpemb.addField("IMPORTANT:","This bot is amongst other things used for developmenttests, errors can happen! \n\n",true);
                    helpemb.addField(comarr.get(5),commandexpl.get(4), false);
                    helpemb.addField(comarr.get(4),commandexpl.get(3), false);
                    helpemb.addField(comarr.get(3),commandexpl.get(2), false);
                    helpemb.addField(comarr.get(1),commandexpl.get(0), false);
                    helpemb.addField(comarr.get(2),commandexpl.get(1), false);
                    helpemb.addField(comarr.get(0),commandexpl.get(5), false);
                    helpemb.addField("","It's not possilbe to change the volume via a command; you can change the volume by clicking with the right mouse button on ʀᴀᴅɪᴏ²⁴",false);
                    helpemb.addBlankField(false).addField("\n INFORMATION","This bot was developed and is owned by Zyzonix#1789. The ʀᴀᴅɪᴏ²⁴-Application is a private App, it's not available on other servers. \n \n Do you like ʀᴀᴅɪᴏ²⁴? --> Contact Zyzonix. \n \n Invitation to Zyzonix's server:  [Click me](https://discord.gg/DCtkDcr) \n \n Current version: " + version ,false);

                    msgreceived.getChannel().sendMessage(helpemb.build()).complete();
                    System.out.println("[INFO | " + dtf.format(now) + "] the help menu has been send successfully");
                } else if (msg.getContentDisplay().equals("-poweroff")) {
                    if (!msg.getAuthor().getId().equals("403946863608201217") || msg.getAuthor().getId().equals("412995111950090253")) return;
                    System.out.println("[INFO | " + dtf.format(now) + "] shutting down...");
                    System.exit(0);
                } else if (msg.getContentDisplay().equals("-ping")) {
                    msg.getChannel().sendMessage(new EmbedBuilder().setTitle(":satellite: Current ping: " + msg.getJDA().getGatewayPing() + "ms").setColor(Color.green).build()).complete();
                }
            System.out.println("[INFO | " + dtf.format(now) + "] executed command | " + msgcommand);
            } else if (msg.getContentDisplay().startsWith("-")){
                msgreceived.getTextChannel().sendMessage(answer.setColor(Color.red).setTitle(":warning: Command unknown!").setDescription("Type -help for help!").build()).complete();
                System.out.println("[ERROR | " + dtf.format(now) + "] Unknown command | '" + msg.getContentDisplay() + "' by " + authorfull + "@" + guildname);
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

    static class statusupdate extends TimerTask {
        @Override
        public void run() {
            long ping = jda.getGatewayPing();
            DateTimeFormatter dtfs = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();
            String tm = dtfs.format(now);

            String[] activity_list = new String[4];
            activity_list[0] = "the space...";
            activity_list[1] = "the clock: " + tm + " (CET)";
            activity_list[2] = "-help";
            activity_list[3] = "my ping: " + ping + "ms";
            Activity act;
            if (counter == 2) {
                act = Activity.listening(activity_list[counter]);
            } else {
                act = Activity.watching(activity_list[counter]);
            }
            jda.getPresence().setActivity(act);
            if (counter == 3) {
                counter = 0;
            } else {
                counter = counter + 1;
            }
        }
    }
}