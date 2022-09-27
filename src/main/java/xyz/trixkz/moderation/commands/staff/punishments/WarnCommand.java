package xyz.trixkz.moderation.commands.staff.punishments;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.managers.punishments.PunishmentType;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;

public class WarnCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                this.main.getServer().getConsoleSender().sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("warn-usage")));

                return true;
            }

            OfflinePlayer offlinePlayer = this.main.getServer().getOfflinePlayer(args[0]);

            String reason = Utils.getMessage(args, 1);

            PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(offlinePlayer.getUniqueId());

            if (playerData == null) {
                return true;
            }

            Punishment punishment = new Punishment(
                    offlinePlayer.getUniqueId(),
                    PunishmentType.WARN,
                    reason,
                    "",
                    System.currentTimeMillis()
            );

            punishment.executeAs(sender);

            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(this.main.getMessagesConfig().getConfig().getString("warn-permission-node"))) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("no-permission")));

            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("warn-usage")));

            return true;
        }

        OfflinePlayer offlinePlayer = this.main.getServer().getOfflinePlayer(args[0]);

        String reason = Utils.getMessage(args, 1);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(offlinePlayer.getUniqueId());

        if (playerData == null) {
            return true;
        }

        Punishment punishment = new Punishment(
                offlinePlayer.getUniqueId(),
                player.getUniqueId(),
                PunishmentType.WARN,
                reason,
                "",
                System.currentTimeMillis()
        );

        punishment.executeAs(sender);

        return true;
    }
}
