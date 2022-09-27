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

public class TempMuteCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                this.main.getServer().getConsoleSender().sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("tempmute-usage")));

                return true;
            }

            OfflinePlayer offlinePlayer = this.main.getServer().getOfflinePlayer(args[0]);

            String duration = args[1];
            String reason = Utils.getMessage(args, 2);

            PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(offlinePlayer.getUniqueId());

            if (playerData == null) {
                return true;
            }

            if (playerData.getPunishments().stream().anyMatch(punishment -> ((punishment.getType() == PunishmentType.MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && !punishment.isExpired()))) {
                this.main.getServer().getConsoleSender().sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("muted-already")));

                return true;
            }

            Punishment punishment = new Punishment(
                    offlinePlayer.getUniqueId(),
                    PunishmentType.TEMPORARY_MUTE,
                    reason,
                    duration,
                    System.currentTimeMillis()
            );

            punishment.executeAs(sender);

            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(this.main.getMessagesConfig().getConfig().getString("tempmute-permission-node"))) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("no-permission")));

            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("tempmute-usage")));

            return true;
        }

        OfflinePlayer offlinePlayer = this.main.getServer().getOfflinePlayer(args[0]);

        String duration = args[1];
        String reason = Utils.getMessage(args, 2);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(offlinePlayer.getUniqueId());

        if (playerData == null) {
            return true;
        }

        if (playerData.getPunishments().stream().anyMatch(punishment -> ((punishment.getType() == PunishmentType.MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && !punishment.isExpired()))) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("muted-already")));

            return true;
        }

        Punishment punishment = new Punishment(
                offlinePlayer.getUniqueId(),
                player.getUniqueId(),
                PunishmentType.TEMPORARY_MUTE,
                reason,
                duration,
                System.currentTimeMillis()
        );

        punishment.executeAs(sender);

        return true;
    }
}
