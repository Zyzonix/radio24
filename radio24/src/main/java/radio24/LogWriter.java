package radio24;

import org.apache.commons.io.output.TeeOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * written by @author ZyzonixDev
 * published by ZyzonixDevelopments
 * -
 * date    | 11/08/2020
 * -
 * project | radio24
 * package | radio24
 */

public class LogWriter extends Thread {

    static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void run() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
            LocalDateTime cur = LocalDateTime.now();
            File f = new File("C:/DEVELOPMENT-TESTS/" + dtf.format(cur) + "_radio24_log.txt"); // C:/DEVELOPMENT-TESTS/ | RPI: /home/pi/radio24_logs/
            f.createNewFile();
            System.out.println("[INFO | " + dtf1.format(now) + "] started logging to logfile | filename: " + f.getName());
            FileOutputStream fos = new FileOutputStream(f);
            TeeOutputStream myOut = new TeeOutputStream(System.out, fos);
            PrintStream ps = new PrintStream(myOut, true);
            System.setOut(ps);
            System.setErr(ps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
