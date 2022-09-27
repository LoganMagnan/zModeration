package xyz.trixkz.moderation.commands.staff.ranks;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.commands.BaseCommand;
import xyz.trixkz.moderation.managers.grants.Grant;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetCommand extends BaseCommand {

    private Main main = Main.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        OfflinePlayer offlinePlayer = this.main.getServer().getOfflinePlayer(args[1]);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(offlinePlayer.getUniqueId());

        String rankName = args[2];

        if (rankName == null) {
            return;
        }

        Rank rank = this.main.getRankManager().getRankByName(rankName);

        if (rank == null) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-does-not-exist")));

            return;
        }

        String durationString = args[3];

        long durationLong = Utils.parseTime(durationString);

        if (durationLong == -1L) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-duration-not-valid")));

            return;
        }

        String reason = Utils.getMessage(args, 4);

        UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;

        Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason, durationLong);

        playerData.getGrants().add(grant);

        sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("rank-set-to-player").replace("(rank)", rank.getFormattedName()).replace("(player)", this.main.getServer().getOfflinePlayer(playerData.getUniqueId()).getName()).replace("(duration)", Utils.makeTimeReadable(grant.getRemainingDurationAsALong()))));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
