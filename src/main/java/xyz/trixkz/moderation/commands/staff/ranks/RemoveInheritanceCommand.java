package xyz.trixkz.moderation.commands.staff.ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.commands.BaseCommand;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class RemoveInheritanceCommand extends BaseCommand {

    private Main main = Main.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        String rankName = args[1];

        if (rankName == null) {
            return;
        }

        Rank rank = this.main.getRankManager().getRankByName(rankName);

        if (rank == null) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-does-not-exist")));

            return;
        }

        String inheritedRankName = args[2];

        if (inheritedRankName == null) {
            return;
        }

        Rank inheritedRank = this.main.getRankManager().getRankByName(inheritedRankName);

        if (inheritedRank == null) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-does-not-exist")));

            return;
        }

        if (!rank.getInheritedRanks().contains(inheritedRank)) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-does-not-have-inheritance")));

            return;
        }

        rank.getInheritedRanks().remove(inheritedRank);
        rank.save();

        sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-removed-inheritance")));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
