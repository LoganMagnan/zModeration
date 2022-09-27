package xyz.trixkz.moderation.utils;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.trixkz.moderation.Main;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: Moderation
 */
public class Utils {

    private Main main = Main.getInstance();

    public static String scoreboardBar = org.bukkit.ChatColor.GRAY.toString() + org.bukkit.ChatColor.STRIKETHROUGH + "----------------------";
    public static String chatBar = org.bukkit.ChatColor.GRAY.toString() + org.bukkit.ChatColor.STRIKETHROUGH + "--------------------------------------------";

    public static boolean DEBUG_MESSAGE;
    public static String ADMIN_PERMISSION_NODE;
    public static String NO_PERMISSION_MESSAGE;
    public static String WARN_USAGE_MESSAGE;
    public static String PLAYER_NOT_ONLINE_MESSAGE;
    public static String PLAYER_BANNED_MESSAGE;
    public static String PLAYER_ALREADY_BANNED_MESSAGE;

    private Timer timer;

    public Utils() {
        DEBUG_MESSAGE = this.main.getSettingsConfig().getConfig().getBoolean("debug");
        ADMIN_PERMISSION_NODE = this.main.getSettingsConfig().getConfig().getString("admin-permission-node");
        NO_PERMISSION_MESSAGE = this.main.getSettingsConfig().getConfig().getString("no-permission-message");
        WARN_USAGE_MESSAGE = this.main.getMessagesConfig().getConfig().getString("warn-usage-message");
        PLAYER_NOT_ONLINE_MESSAGE = this.main.getMessagesConfig().getConfig().getString("player-not-online-message");
        PLAYER_BANNED_MESSAGE = this.main.getMessagesConfig().getConfig().getString("player-banned-message");
        PLAYER_ALREADY_BANNED_MESSAGE = this.main.getMessagesConfig().getConfig().getString("player-already-banned-message");
    }

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> translate(List<String> lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            strings.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return strings;
    }

    public static List<String> translate(String[] lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                strings.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return strings;
    }

    public static void debug(String message) {
        if (DEBUG_MESSAGE) {
            System.out.println(message);
        }
    }

    public static String getMessage(String[] args, int number) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = number; i < args.length; i++) {
            stringBuilder.append(args[i]).append(number >= args.length - 1 ? "" : " ");
        }

        return stringBuilder.toString();
    }

    public static String makeTimeReadable(Long time) {
        if (time == null)
            return "";
        StringBuilder sb = new StringBuilder();
        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(time));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));
        if (days != 0L)
            sb.append(" ").append(days).append("d");
        if (hours != 0L)
            sb.append(" ").append(hours).append("h");
        if (minutes != 0L)
            sb.append(" ").append(minutes).append("m");
        if (seconds != 0L)
            sb.append(" ").append(seconds).append("s");
        return sb.toString().trim();
    }

    public static long parseTime(String input) {
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else if (Character.isLetter(c) && number.length() > 0) {
                result += convert(Integer.parseInt(number.toString()), c);
                number = new StringBuilder();
            }
        }
        return result;
    }

    private static long convert(long value, char unit) {
        switch (unit) {
            case 'd':
                return value * 1000L * 60L * 60L * 24L;
            case 'h':
                return value * 1000L * 60L * 60L;
            case 'm':
                return value * 1000L * 60L;
            case 's':
                return value * 1000L;
        }
        return 0L;
    }

    public static String getAddedAtDate(long addedAt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        return simpleDateFormat.format(new Date(addedAt));
    }

    public static boolean isNumeric(String string) {
        return regexNumeric(string).length() == 0;
    }

    public static String regexNumeric(String string) {
        return string.replaceAll("[0-9]", "").replaceFirst("\\.", "");
    }

    public void startTimer() {
        if (this.timer == null) {
            this.timer = new Timer(main);
        }
    }

    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public int getElapsedTime() {
        return this.timer.getElapsedTime();
    }
}

class Timer extends BukkitRunnable {

    private Main main;

    private int elapsedTime = 1;

    public Timer(Main main) {
        this.main = main;

        this.runTaskTimer(main, 0, 1);
    }

    public void run() {
        this.elapsedTime += 1;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}