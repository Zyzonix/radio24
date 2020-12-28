package radio24;

import java.util.ArrayList;
import java.util.HashMap;

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

public class soundsourceservice {

    static HashMap<String, String> sourcelist() {
        HashMap<String, String> ret = new HashMap<>();
        String rhhlink = "http://stream.radiohamburg.de/rhh-live/mp3-128/stream.radiohamburg.de/";
        ret.put("radio hamburg", rhhlink);
        ret.put("rhh", rhhlink);
        ret.put("hamburg", rhhlink);

        String rsalink = "https://streams.rsa-sachsen.de/rsa-live/mp3-128/streams.rsa-sachsen.de/";
        ret.put("rsa radio", rsalink);
        ret.put("rsa", rsalink);
        ret.put("sachsen", rsalink);

        String rblink = "http://stream.radiobrocken.de/live/mp3-256/stream.radiobrocken.de/";
        ret.put("radio brocken", rblink);
        ret.put("rb", rblink);
        ret.put("brocken", rblink);

        String ilrlink = "https://www.ilovemusic.de/iloveradio.m3u";
        ret.put("ilr", ilrlink);
        ret.put("i love radio", ilrlink);
        ret.put("love", ilrlink);
        return ret;
    }

    public static HashMap<Boolean, String> collector(String key) {
        HashMap<Boolean, String> ret = new HashMap<>();
        if (sourcelist().containsKey(key)) {
            ret.put(true, sourcelist().get(key));
        } else {
            ret.put(false, key);
        }
        return ret;
    }
    public static String rhh() {
        String ret = "Radio Hamburg:";
        for (String i : sourcelist().keySet()) {
            if (sourcelist().get(i).equals(sourcelist().get("rhh"))) {
                ret += "\n-" + i;
            }
        }
        return ret;
    }
    public static String ilr() {
        String ret = "I Love Radio:";
        for (String i : sourcelist().keySet()) {
            if (sourcelist().get(i).equals(sourcelist().get("ilr"))) {
                ret += "\n-" + i;
            }
        }
        return ret;
    }
    public static String rb() {
        String ret = "Radio Brocken:";
        for (String i : sourcelist().keySet()) {
            if (sourcelist().get(i).equals(sourcelist().get("rb"))) {
                ret += "\n-" + i;
            }
        }
        return ret;
    }
    public static String rsa() {
        String ret = "RSA Radio:";
        for (String i : sourcelist().keySet()) {
            if (sourcelist().get(i).equals(sourcelist().get("rsa"))) {
                ret += "\n-" + i;
            }
        }
        return ret;
    }
}
