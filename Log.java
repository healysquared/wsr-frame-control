import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class handles all logging.
 */
public class Log {
    private static final DateFormat dateFormat = new SimpleDateFormat("[yyyy.MM.dd HH:mm:ss.SSS]");

    public static void info(String a) {
        System.out.println(dateFormat.format(new Date()) + " INFO: " + a);
    }

    public static void warning(String a) {
        System.out.println(dateFormat.format(new Date()) + " WARNING: " + a);
    }

    public static void error(String a) {
        System.out.println(dateFormat.format(new Date()) + " ERROR: " + a);
    }
}
