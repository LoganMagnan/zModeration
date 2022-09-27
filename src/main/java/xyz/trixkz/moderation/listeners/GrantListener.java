package xyz.trixkz.moderation.listeners;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.events.GrantAddedEvent;
import xyz.trixkz.moderation.events.GrantExpireEvent;
import xyz.trixkz.moderation.managers.grants.Grant;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;

public class GrantListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onGrantAdded(GrantAddedEvent event) {
        OfflinePlayer player = event.getPlayer();

        Grant grant = event.getGrant();

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());
        playerData.getGrants().add(grant);
        playerData.checkGrants();

        if (player.getPlayer() == null) {
            return;
        }

        player.getPlayer().sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("grant-added").replace("(rank)", grant.getRank().getFormattedName()).replace("(duration)", Utils.makeTimeReadable(grant.getRemainingDurationAsALong()))));
    }

    @EventHandler
    public void onGrantExpire(GrantExpireEvent event) {
        OfflinePlayer player = event.getPlayer();

        Grant grant = event.getGrant();

        if (player.getPlayer() == null) {
            return;
        }

        player.getPlayer().sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("grant-expired").replace("(rank)", grant.getRank().getFormattedName())));
    }
}
