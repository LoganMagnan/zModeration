package xyz.trixkz.moderation.managers;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.grants.Grant;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.playerdata.PlayerData;
import java.util.*;

public class PlayerDataManager {

	private final Main main = Main.getInstance();

	@Getter private final Map<UUID, PlayerData> players = new HashMap<>();

	public PlayerData getOrCreate(UUID uniqueId) {
		return this.players.computeIfAbsent(uniqueId, PlayerData::new);
	}

	public PlayerData getPlayerData(UUID uniqueId) {
		return this.players.getOrDefault(uniqueId, new PlayerData(uniqueId));
	}

	public Collection<PlayerData> getAllPlayers() {
		return this.players.values();
	}

	public void loadPlayerData(PlayerData playerData) {
		Document document = this.main.getMongoManager().getPlayers().find(Filters.eq("uniqueId", playerData.getUniqueId().toString())).first();
//		Bukkit.broadcastMessage("DOCUMENT - " + document);
		if (document != null) {
//			String grantsJson = document.getString("grants");
//			Bukkit.broadcastMessage("GRANTS JSON - " + grantsJson);
//			Grant[] grants = this.main.getMongoManager().getGson().fromJson(grantsJson, Grant[].class);
//
//			for (Grant grant : grants) {
//				playerData.getGrants().add(grant);
//			}

			String punishmentsJson = document.getString("punishments");
//			Bukkit.broadcastMessage("PUNISHMENTS JSON - " + punishmentsJson);
			Punishment[] punishments = this.main.getMongoManager().getGson().fromJson(punishmentsJson, Punishment[].class);

			for (Punishment punishment : punishments) {
				playerData.getPunishments().add(punishment);
			}
		}

		playerData.checkGrants();
		playerData.setLoaded(true);
	}

	public void savePlayerData(PlayerData playerData) {
		Document document = new Document();
		document.put("uniqueId", playerData.getUniqueId().toString());
//		System.out.println("GRANTS - " + document.get("grants"));
//		System.out.println("PUNISHMENTS - " + document.get("punishments"));
//		playerData.getGrants().forEach(grant -> System.out.println("PLAYER DATA GRANTS - " + grant.getRank().getFormattedName()));
//		System.out.println("PLAYER DATA GRANTS TO ARRAY - " + Arrays.toString(playerData.getGrants().toArray()));
//		document.put("grants", this.main.getMongoManager().getGson().toJson(playerData.getGrants().toArray()));
		document.put("punishments", this.main.getMongoManager().getGson().toJson(playerData.getPunishments().toArray()));

		this.main.getMongoManager().getPlayers().replaceOne(Filters.eq("uniqueId", playerData.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
	}

	public void deletePlayer(UUID uniqueId) {
		this.savePlayerData(getPlayerData(uniqueId));
		this.getPlayers().remove(uniqueId);
	}

	public MongoCursor<Document> getPlayersSorted(String stat, int limit) {
		final Document document = new Document();
		document.put(stat, -1);

		return this.main.getMongoManager().getPlayers().find().sort(document).limit(limit).iterator();
	}
}
