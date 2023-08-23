package othello.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    public static void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = timestamp + " - " + message;
        System.out.println(logMessage);
    }
    
}