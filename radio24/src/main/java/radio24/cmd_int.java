package radio24;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 27.12.2020
 * java-v  | 14
 * -
 * project | radio24
 * package | radio24
 */


public interface cmd_int {
    public abstract void action(MessageReceivedEvent event, String timestamp);
    public abstract void executed(String timestamp, String command);
}
