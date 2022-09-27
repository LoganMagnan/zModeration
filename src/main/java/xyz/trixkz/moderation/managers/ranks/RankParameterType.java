package xyz.trixkz.moderation.managers.ranks;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.utils.ParameterType;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankParameterType implements ParameterType<Rank> {

    private Main main = Main.getInstance();

    public Rank transform(CommandSender sender, String string) {
        Rank rank = this.main.getRankManager().getRankByName(string);

        if (rank == null) {
            sender.sendMessage(Utils.translate("&c" + string + " does not already exist"));

            return null;
        }

        return rank;
    }

    public List<String> onTabComplete(Player player, Set<String> set, String string) {
        List<String> tabCompletions = new ArrayList<String>();

        for (Rank rank : this.main.getRankManager().getRanks().values()) {
            tabCompletions.add(rank.getName());
        }

        return tabCompletions;
    }
}
