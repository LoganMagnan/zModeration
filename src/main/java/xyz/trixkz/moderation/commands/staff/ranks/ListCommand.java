package xyz.trixkz.moderation.commands.staff.ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.commands.BaseCommand;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.managers.ranks.RankComparator;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends BaseCommand {

    private Main main = Main.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        List<Rank> ranks = this.main.getRankManager().getRanks().values().stream().sorted(new RankComparator()).collect(Collectors.toList());

        sender.sendMessage(Utils.translate(Utils.chatBar));
        sender.sendMessage(Utils.translate("&9List of ranks"));
        sender.sendMessage(Utils.translate(Utils.chatBar));

        for (Rank rank : ranks) {
            sender.sendMessage(Utils.translate(rank.getFormattedName() + (rank.isDefaultRank() ? " &7[&fDefault&7]" : "") + " &7[&fWeight: &d" + rank.getWeight() + "&7]"));
        }

        sender.sendMessage(Utils.translate(Utils.chatBar));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
