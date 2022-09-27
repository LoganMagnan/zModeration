package xyz.trixkz.moderation;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.trixkz.moderation.listeners.GrantListener;
import xyz.trixkz.moderation.listeners.MenuListener;
import xyz.trixkz.moderation.listeners.PlayerDataListener;
import xyz.trixkz.moderation.listeners.RandomListeners;
import xyz.trixkz.moderation.managers.CommandManager;
import xyz.trixkz.moderation.managers.MongoManager;
import xyz.trixkz.moderation.managers.PlayerDataManager;
import xyz.trixkz.moderation.managers.grants.GrantManager;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.managers.ranks.RankManager;
import xyz.trixkz.moderation.menusystem.PlayerMenuUtil;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;
import xyz.trixkz.moderation.utils.config.FileConfig;
import xyz.trixkz.moderation.utils.config.file.Config;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: Moderation
 */
@Getter
@Setter
public class Main extends JavaPlugin {

    @Getter private static Main instance;

    private Config settingsConfig;
    private FileConfig messagesConfig;

    private Utils utils;

    private CommandManager commandManager;
    private MongoManager mongoManager;
    private PlayerDataManager playerDataManager;
    private RankManager rankManager;
    private GrantManager grantManager;

    private HashMap<Player, PlayerMenuUtil> playerMenuUtilMap = new HashMap<Player, PlayerMenuUtil>();

    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.settingsConfig = new Config("config", this);
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.utils = new Utils();

        Bukkit.getConsoleSender().sendMessage("------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(Utils.translate("&dModeration &7- &av" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(Utils.translate("&7Made by &eTrixkz &7(&fDiscord: LoganM#3465&7)"));
        Bukkit.getConsoleSender().sendMessage("------------------------------------------------");

        this.loadManagers();
        this.loadListeners();
        this.loadRunnables();
        this.rankManager.initRanks();

        for (Document document : this.mongoManager.getAllDocuments(this.mongoManager.getPlayers())) {
            UUID uuid = UUID.fromString(document.get("uniqueId").toString());

            PlayerData playerData = this.playerDataManager.getPlayerData(uuid);
//
//            if (document.get("grants") == null) {
//                document.put("grants", "");
//
//                this.mongoManager.getPlayers().replaceOne(Filters.eq("uniqueId", playerData.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
//            }

            if (document.get("punishments") == null) {
                document.put("punishments", "");

                this.mongoManager.getPlayers().replaceOne(Filters.eq("uniqueId", playerData.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
            }

            this.playerDataManager.getOrCreate(playerData.getUniqueId());

            for (Punishment punishment : playerData.getPunishments()) {
                switch (punishment.getType()) {
                    case WARN:
                    case TEMPORARY_MUTE:
                    case TEMPORARY_BAN:
                        if (punishment.getRemainingDurationAsALong() <= 0L) {
                            punishment.setExpired(true);

                            return;
                        }

                        break;
                }
            }
        }
    }

    public void onDisable() {
        instance = null;

        for (PlayerData playerData : this.playerDataManager.getAllPlayers()) {
            this.playerDataManager.savePlayerData(playerData);
        }

        this.mongoManager.disconnect();
    }

    private void loadManagers() {
        this.commandManager = new CommandManager();
        this.mongoManager = new MongoManager();
        this.playerDataManager = new PlayerDataManager();
        this.rankManager = new RankManager();
        this.grantManager = new GrantManager();
    }

    private void loadListeners() {
        Arrays.asList(
                new RandomListeners(),
                new MenuListener(),
                new xyz.trixkz.moderation.menu.MenuListener(),
                new PlayerDataListener(),
                new GrantListener()
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadRunnables() {
        // new Aether(this, new ScoreboardProvider());
    }

    public PlayerMenuUtil getPlayerMenuUtil(Player player) {
        PlayerMenuUtil playerMenuUtil;

        if (playerMenuUtilMap.containsKey(player)) {
            return playerMenuUtilMap.get(player);
        } else {
            playerMenuUtil = new PlayerMenuUtil(player);

            playerMenuUtilMap.put(player, playerMenuUtil);

            return playerMenuUtil;
        }
    }
}
