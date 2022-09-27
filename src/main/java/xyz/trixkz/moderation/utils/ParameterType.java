package xyz.trixkz.moderation.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Set;

public interface ParameterType<T> {

    T transform(CommandSender sender, String string);

    List<String> onTabComplete(Player player, Set<String> set, String string);
}
