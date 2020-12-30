package radio24;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 30/12/2020
 * java-v  | 14
 * -
 * project | radio24
 * package | radio24
 */

public class iholder {
    static String rhhimage = "https://www.phonostar.de/images/auto_created/Radio_Hamburg_2184x184.png";
    static String rbimage = "https://static.radiome.com/logos/radiobrocken/radiobrocken-c.png";
    static String rsaimage = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Logo_RSA.png/200px-Logo_RSA.png";
    static String ilrimage = "https://cache.usercontentapp.com/logo/ilovemusic.png?format=png&enlarge=0&quality=90&width=960";

    public static EmbedBuilder request_confirm(String input) {
        EmbedBuilder ret = new EmbedBuilder();
        ret.setColor(Color.green);
        if (!(input.startsWith("https://") || input.startsWith("http://"))) {
            ret.setImage(img_confirm(input).split(" ", 2)[0]);
            Bot.status_inf_set(img_confirm(input).split(" ", 2)[1]);
        }
        ret.setTitle("\n\n:radio: Started playing your music\n\n");


        return ret;
    }
    static String img_confirm(String input) {
        HashMap<String, String> collectimage = new HashMap<>();
        HashMap<String, String> collectstring = new HashMap<>();
        HashMap<String, String> imports = soundsourceservice.sourcelist();
        collectimage.put("http://stream.radiohamburg.de/rhh-live/mp3-128/stream.radiohamburg.de/", rhhimage);
        collectimage.put("https://streams.rsa-sachsen.de/rsa-live/mp3-128/streams.rsa-sachsen.de/", rsaimage);
        collectimage.put("http://stream.radiobrocken.de/live/mp3-256/stream.radiobrocken.de/", rbimage);
        collectimage.put("https://www.ilovemusic.de/iloveradio.m3u", ilrimage);
        String link = imports.get(input);

        collectstring.put("http://stream.radiohamburg.de/rhh-live/mp3-128/stream.radiohamburg.de/", "Radio Hamburg");
        collectstring.put("https://streams.rsa-sachsen.de/rsa-live/mp3-128/streams.rsa-sachsen.de/", "RSA Radio");
        collectstring.put("http://stream.radiobrocken.de/live/mp3-256/stream.radiobrocken.de/", "Radio Brocken");
        collectstring.put("https://www.ilovemusic.de/iloveradio.m3u", "I Love Radio");

        String ret = collectimage.get(link) + " " + collectstring.get(link);
        return ret;
    }
}
