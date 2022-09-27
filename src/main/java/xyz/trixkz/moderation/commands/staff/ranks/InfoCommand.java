package xyz.trixkz.moderation.commands.staff.ranks;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.commands.BaseCommand;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends BaseCommand {

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

        List<String> inheritedRanks = new ArrayList<String>();
        List<String> inheritedPermissions = new ArrayList<String>();

        rank.getInheritedRanks().forEach(inheritedRank -> inheritedRanks.add(inheritedRank.getName()));

        for (Rank inheritedRank : rank.getInheritedRanks()) {
            inheritedPermissions.addAll(inheritedRank.getAllPermissions());
        }

        sender.sendMessage(Utils.translate(Utils.chatBar));
        sender.sendMessage(Utils.translate(rank.getName() + "'s information"));
        sender.sendMessage(Utils.translate(Utils.chatBar));
        sender.sendMessage(Utils.translate("&fName: &d" + rank.getFormattedName()));
        sender.sendMessage(Utils.translate("&fPrefix: &d" + rank.getPrefix()));
        sender.sendMessage(Utils.translate("&fSuffix: &d" + rank.getSuffix()));
        sender.sendMessage(Utils.translate("&fColor: &d" + rank.getColor() + rank.getColor().name().toLowerCase()));
        sender.sendMessage(Utils.translate("&fWeight: &d" + rank.getWeight()));
        sender.sendMessage(Utils.translate("&fPermissions: &d" + StringUtils.join(rank.getPermissions(), ", ")));
        sender.sendMessage(Utils.translate("&fInherited ranks: &d" + StringUtils.join(inheritedPermissions, ", ")));
        sender.sendMessage(Utils.translate(Utils.chatBar));

    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
