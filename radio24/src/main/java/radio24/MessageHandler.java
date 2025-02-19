package radio24;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 24.11.2020
 * java-v  | 14
 * -
 * project | radio24
 * package | radio24
 */

public class MessageHandler extends Thread {
    public static String messageID;
    public static TextChannel tchannel;
    public static Message message;
    public static Message deletmsg;
    public int delay = 30;
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    String msgid;
    public static void sendInfo(TextChannel channel, String ID, Message originalmessage) {
        messageID = ID;
        tchannel = channel;
        message = originalmessage;
    }
    public void run() {
        deletmsg = tchannel.retrieveMessageById(messageID).complete();
        LocalDateTime now = LocalDateTime.now();
        if (/**!message.equals(deletmsg) ||*/ message.getContentDisplay().contains("Next") || message.getContentDisplay().contains("Good")) {
            System.out.println("[ERROR | " + dtf.format(now) + "] couldn't delete the last message; wrong message (ID not identical/unknown message) / important message");
            return;
        } else {
            msgid = messageID;
            deletmsg.delete().completeAfter(delay, TimeUnit.SECONDS);
            System.out.println("[INFO | " + dtf.format(now) + "] deleted last message; id (" + msgid + ")");
        }
    }
}
