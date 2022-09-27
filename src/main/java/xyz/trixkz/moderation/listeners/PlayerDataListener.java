package xyz.trixkz.moderation.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.managers.punishments.PunishmentType;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;

public class PlayerDataListener implements Listener {

	private Main main = Main.getInstance();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(event.getUniqueId());

		for (Punishment punishment : playerData.getPunishments()) {
			if (punishment.getType() != PunishmentType.BAN && punishment.getType() != PunishmentType.TEMPORARY_BAN && punishment.getType() != PunishmentType.BLACKLIST) {
				continue;
			}

			if (punishment.isExpired() || punishment.isUnbanned()) {
				continue;
			}

			switch (punishment.getType()) {
				case BAN:
					event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
					event.setKickMessage(Utils.translate("&cYou have been permanently banned\n\n" + "&7Reason: &f" + punishment.getReason() + "\n&7ID: &f" + punishment.getId()));

					break;
				case TEMPORARY_BAN:
					if (punishment.getRemainingDurationAsALong() <= 0L) {
						punishment.setExpired(true);

						return;
					}

					event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
					event.setKickMessage(Utils.translate("&cYou have been temporarily banned\n\n&7Reason: &f" + punishment.getReason() + "\n&7Duration: &f" + Utils.makeTimeReadable(punishment.getRemainingDurationAsALong()) + "\n&7ID: &f" + punishment.getId()));

					break;
				case BLACKLIST:
					event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
					event.setKickMessage(Utils.translate("&cYou have been blacklisted\n\n&7Reason: &f" + punishment.getReason() + "\n&7ID: &f" + punishment.getId()));

					break;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerLogin(PlayerLoginEvent event) {
		PlayerData playerData = this.main.getPlayerDataManager().getOrCreate(event.getPlayer().getUniqueId());

		if (playerData == null) {
			event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			event.setKickMessage("§cAn error has occurred while loading your profile. Please reconnect.");

			return;
		}

		if (!playerData.isLoaded()) {
			this.main.getPlayerDataManager().savePlayerData(playerData);

			event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			event.setKickMessage("§cAn error has occurred while loading your profile. Please reconnect.");
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Player player = event.getPlayer();

		PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

		this.handleLeave(player);
		this.handleDataSave(playerData);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);

		Player player = event.getPlayer();

		PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

		this.handleLeave(player);
		this.handleDataSave(playerData);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		Player player = event.getPlayer();

		PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());
	}

	private void handleLeave(Player player) {

	}

	private void handleDataSave(PlayerData playerData) {

	}
}
