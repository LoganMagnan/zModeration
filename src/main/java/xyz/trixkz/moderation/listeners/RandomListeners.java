package xyz.trixkz.moderation.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.managers.punishments.PunishmentType;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: Moderation
 */
public class RandomListeners implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (playerData == null) {
            return;
        }

        for (Punishment punishment : playerData.getPunishments()) {
            if (punishment.getType() != PunishmentType.MUTE && punishment.getType() != PunishmentType.TEMPORARY_MUTE) {
                continue;
            }

            if (punishment.isExpired() || punishment.isUnmuted()) {
                continue;
            }

            switch (punishment.getType()) {
                case MUTE:
                    event.setCancelled(true);

                    player.sendMessage(Utils.chatBar);
                    player.sendMessage(Utils.translate("&cYou have been permanently muted"));
                    player.sendMessage(Utils.translate("&cThis mute will not expire at anytime"));
                    player.sendMessage(Utils.translate(""));
                    player.sendMessage(Utils.translate("&7Reason: &f" + punishment.getReason()));
                    player.sendMessage(Utils.translate("&7ID: &f" + punishment.getId()));
                    player.sendMessage(Utils.chatBar);

                    break;
                case TEMPORARY_MUTE:
                    if (punishment.getRemainingDurationAsALong() <= 0L) {
                        punishment.setExpired(true);

                        return;
                    }

                    event.setCancelled(true);

                    player.sendMessage(Utils.chatBar);
                    player.sendMessage(Utils.translate("&cYou have been temporarily muted"));
                    player.sendMessage(Utils.translate("&cThis mute will expire in &c&l" + Utils.makeTimeReadable(punishment.getRemainingDurationAsALong())));
                    player.sendMessage(Utils.translate(""));
                    player.sendMessage(Utils.translate("&7Reason: &f" + punishment.getReason()));
                    player.sendMessage(Utils.translate("&7ID: &f" + punishment.getId()));
                    player.sendMessage(Utils.chatBar);

                    break;
            }
        }
    }
}
