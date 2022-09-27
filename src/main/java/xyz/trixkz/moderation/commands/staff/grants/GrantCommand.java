package xyz.trixkz.moderation.commands.staff.grants;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.menusystem.menu.ranks.RankSelectionMenu;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;

public class GrantCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(this.main.getSettingsConfig().getConfig().getString("grant-permission-node"))) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("no-permission")));

            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("grant-usage")));

            return true;
        }

        OfflinePlayer offlinePlayer = this.main.getServer().getOfflinePlayer(args[0]);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(offlinePlayer.getUniqueId());

        if (playerData == null) {
            return true;
        }

        new RankSelectionMenu(playerData).openMenu(player);

        return true;
    }
}
