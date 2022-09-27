package xyz.trixkz.moderation.utils;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class SpigotUtils {

    public static final ArrayList<ChatColor> woolColors = new ArrayList<>(Arrays.asList(new ChatColor[] {
            ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.DARK_AQUA,
            ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.RESET, ChatColor.DARK_GREEN, ChatColor.RED, ChatColor.BLACK }));

    public static int toDyeColor(ChatColor color) {
        if (color == ChatColor.DARK_RED)
            color = ChatColor.RED;
        if (color == ChatColor.DARK_BLUE)
            color = ChatColor.BLUE;
        return woolColors.indexOf(color);
    }

    public static String getName(PotionEffectType potionEffectType) {
        if (potionEffectType.getName().equalsIgnoreCase("fire_resistance"))
            return "Fire Resistance";
        if (potionEffectType.getName().equalsIgnoreCase("speed"))
            return "Speed";
        if (potionEffectType.getName().equalsIgnoreCase("weakness"))
            return "Weakness";
        if (potionEffectType.getName().equalsIgnoreCase("slowness"))
            return "Slowness";
        return "Unknown";
    }

    public static Player getDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player)
            return (Player)event.getDamager();
        if (event.getDamager() instanceof Projectile && (
                (Projectile)event.getDamager()).getShooter() instanceof Player)
            return (Player)((Projectile)event.getDamager()).getShooter();
        return null;
    }
}