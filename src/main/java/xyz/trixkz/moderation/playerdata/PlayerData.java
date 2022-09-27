package xyz.trixkz.moderation.playerdata;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.events.GrantExpireEvent;
import xyz.trixkz.moderation.managers.PlayerDataManager;
import xyz.trixkz.moderation.managers.grants.Grant;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.managers.punishments.PunishmentType;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.playerdata.currentgame.PlayerCurrentGameData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private transient Main main = Main.getInstance();

    private transient PlayerDataManager playerDataManager = this.main.getPlayerDataManager();
    private PlayerState playerState = PlayerState.SPAWN;

    private PlayerSettings playerSettings = new PlayerSettings();
    private PlayerCurrentGameData currentGameData = new PlayerCurrentGameData();

    private final UUID uniqueId;
    private boolean loaded;

    private List<Grant> grants = new ArrayList<Grant>();

    private Grant currentGrant;

    private List<Punishment> punishments = new ArrayList<Punishment>();

    public PlayerData(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.loaded = false;
        this.main.getPlayerDataManager().loadPlayerData(this);
    }

    public void checkGrants() {
        for (Grant grant : this.grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemoved(true);
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Expired");

                if (this.currentGrant != null && this.currentGrant.equals(grant)) {
                    this.currentGrant = null;
                }

                Player player = this.main.getServer().getPlayer(this.uniqueId);

                if (player != null) {
                    new GrantExpireEvent(player, grant).call();
                }
            }
        }

        if (this.currentGrant == null) {
            this.enableNextGrant();

            if (this.currentGrant != null) {
                return;
            }

            Grant grant = new Grant(UUID.randomUUID(), this.main.getRankManager().getDefaultRank(), null, System.currentTimeMillis(), "Default rank", -0L);

            this.grants.add(grant);
            this.setCurrentGrant(grant);
        }
    }

    public void enableNextGrant() {
        List<Grant> grants = new ArrayList<Grant>(this.grants);
        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));

        for (Grant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                this.setCurrentGrant(grant);
            }
        }
    }

    public String getName() {
        return this.getCurrentRank().getPrefix() + this.getCurrentRank().getColor() + this.main.getServer().getPlayer(this.uniqueId).getName();
    }

    public String getColoredName() {
        return this.getCurrentRank().getColor() + this.main.getServer().getPlayer(this.uniqueId).getName();
    }

    public Rank getCurrentRank() {
        return this.currentGrant.getRank();
    }

    public void setCurrentGrant(Grant grant) {
        this.currentGrant = grant;

        Player player = this.main.getServer().getPlayer(this.uniqueId);

        if (player != null) {
            player.setDisplayName(this.currentGrant.getRank().getPrefix() + player.getName() + grant.getRank().getSuffix());
        }
    }

    public void setupPermissionsAttachment(Player player) {
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            if (permissionAttachmentInfo.getAttachment() == null) {
                continue;
            }

            permissionAttachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> permissionAttachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment permissionAttachment = player.addAttachment(this.main);

        for (String permission : this.getCurrentRank().getAllPermissions()) {
            permissionAttachment.setPermission(permission, true);
        }

        player.recalculatePermissions();
    }

    public Punishment getActiveMute() {
        for (Punishment punishment : this.punishments) {
            if ((punishment.getType() == PunishmentType.MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public Punishment getActiveBan() {
        for (Punishment punishment : this.punishments) {
            if ((punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.TEMPORARY_BAN || punishment.getType() == PunishmentType.BLACKLIST) && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }
}
