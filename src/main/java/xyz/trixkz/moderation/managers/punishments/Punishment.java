package xyz.trixkz.moderation.managers.punishments;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.TimeUtils;
import xyz.trixkz.moderation.utils.Utils;
import java.util.UUID;

@Getter
@Setter
public class Punishment {

    private transient Main main = Main.getInstance();

    private UUID player;
    private UUID staffMember;
    private PunishmentType type;
    private String reason;
    private long durationLong;
    private String durationString;
    private String id;
    private long addedAt;

    private UUID unmutedBy;
    private long unmutedAt;
    private String unmutedReason;

    private UUID unbannedBy;
    private long unbannedAt;
    private String unbannedReason;

    private boolean expired;
    private boolean unmuted;
    private boolean unbanned;

    private boolean console;
    private boolean unmutedByConsole;
    private boolean unbannedByConsole;

    public Punishment(UUID player, PunishmentType type, String reason, String durationString, long addedAt) {
        this.player = player;
        this.type = type;
        this.reason = reason;
        this.durationString = durationString;
        this.addedAt = addedAt;
        this.expired = false;
        this.unmuted = false;
        this.unbanned = false;
        this.console = true;

        switch (this.type) {
            case WARN:
                this.durationLong = Utils.parseTime("7d");

                break;
            case KICK:
                this.durationLong = -1L;
                this.expired = true;

                break;
            case MUTE:
            case BAN:
            case BLACKLIST:
                this.durationLong = -1L;

                break;
            case TEMPORARY_MUTE:
            case TEMPORARY_BAN:
                this.durationLong = Utils.parseTime(this.durationString);

                break;
        }

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        this.id = RandomStringUtils.random(8, characters);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(this.player);
        playerData.getPunishments().add(this);
    }

    public Punishment(UUID player, UUID staffMember, PunishmentType type, String reason, String durationString, long addedAt) {
        this.player = player;
        this.staffMember = staffMember;
        this.type = type;
        this.reason = reason;
        this.durationString = durationString;
        this.addedAt = addedAt;
        this.expired = false;
        this.unmuted = false;
        this.unbanned = false;
        this.console = false;

        switch (this.type) {
            case WARN:
                this.durationLong = Utils.parseTime("7d");

                break;
            case KICK:
                this.durationLong = -1L;
                this.expired = true;

                break;
            case MUTE:
            case BAN:
            case BLACKLIST:
                this.durationLong = -1L;

                break;
            case TEMPORARY_MUTE:
            case TEMPORARY_BAN:
                this.durationLong = Utils.parseTime(this.durationString);

                break;
        }

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        this.id = RandomStringUtils.random(8, characters);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(this.player);
        playerData.getPunishments().add(this);
    }

    public void executeAs(CommandSender sender) {
        switch (this.type) {
            case WARN:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getPlayer(this.player).sendMessage(Utils.chatBar);
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&cYou have been warned"));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&cThis warn will expire in &c&l" + Utils.makeTimeReadable(this.getRemainingDurationAsALong())));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate(""));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&7Reason: &f" + this.reason));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&7ID: &f" + this.id));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.chatBar);

                break;
            case KICK:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getScheduler().runTask(this.main, () -> this.main.getServer().getPlayer(this.player).kickPlayer(Utils.translate("&cYou have been kicked\n\n&7Reason: &f" + this.reason + "\n&7ID: &f" + this.id)));

                break;
            case MUTE:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getPlayer(this.player).sendMessage(Utils.chatBar);
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&cYou have been permanently muted"));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&cThis mute will not expire at anytime"));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate(""));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&7Reason: &f" + this.reason));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&7ID: &f" + this.id));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.chatBar);

                break;
            case TEMPORARY_MUTE:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getPlayer(this.player).sendMessage(Utils.chatBar);
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&cYou have been temporarily muted"));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&cThis mute will expire in &c&l" + Utils.makeTimeReadable(this.getRemainingDurationAsALong())));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate(""));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&7Reason: &f" + this.reason));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.translate("&7ID: &f" + this.id));
                this.main.getServer().getPlayer(this.player).sendMessage(Utils.chatBar);

                break;
            case BAN:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getScheduler().runTask(this.main, () -> this.main.getServer().getPlayer(this.player).kickPlayer(Utils.translate("&cYou have been permanently banned\n\n&7Reason: &f" + this.reason + "\n&7ID: &f" + this.id)));

                break;
            case TEMPORARY_BAN:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getScheduler().runTask(this.main, () -> this.main.getServer().getPlayer(this.player).kickPlayer(Utils.translate("&cYou have been temporarily banned\n\n&7Reason: &f" + this.reason + "\n&7Duration: &f" + Utils.makeTimeReadable(this.getRemainingDurationAsALong()) + "\n&7ID: &f" + this.id)));

                break;
            case BLACKLIST:
                if (this.main.getServer().getPlayer(this.player) == null) {
                    break;
                }

                this.main.getServer().getScheduler().runTask(this.main, () -> this.main.getServer().getPlayer(this.player).kickPlayer(Utils.translate("&cYou have been blacklisted\n\n&7Reason: &f" + this.reason + "\n&7ID: &f" + this.id)));

                break;
        }

        if (sender instanceof Player) {
            this.main.getServer().getPlayer(this.staffMember).sendMessage(Utils.translate("&aYou have given &a&l" + this.main.getServer().getOfflinePlayer(this.player).getName() + " &athe following punishment:"));
            this.main.getServer().getPlayer(this.staffMember).sendMessage(Utils.translate("  &7Type: &f" + this.type.name().toLowerCase()));
            this.main.getServer().getPlayer(this.staffMember).sendMessage(Utils.translate("  &7Reason: &f" + this.reason));
            this.main.getServer().getPlayer(this.staffMember).sendMessage(Utils.translate("  &7Duration: &f" + Utils.makeTimeReadable(this.durationLong)));
            this.main.getServer().getPlayer(this.staffMember).sendMessage(Utils.translate("  &7ID: &f" + this.id));
        } else {
            this.main.getServer().getConsoleSender().sendMessage(Utils.translate("&aYou have given &a&l" + this.main.getServer().getOfflinePlayer(this.player).getName() + " &athe following punishment:"));
            this.main.getServer().getConsoleSender().sendMessage(Utils.translate("  &7Type: &f" + this.type.name().toLowerCase()));
            this.main.getServer().getConsoleSender().sendMessage(Utils.translate("  &7Reason: &f" + this.reason));
            this.main.getServer().getConsoleSender().sendMessage(Utils.translate("  &7Duration: &f" + Utils.makeTimeReadable(this.durationLong)));
            this.main.getServer().getConsoleSender().sendMessage(Utils.translate("  &7ID: &f" + this.id));
        }
    }

    public long getRemainingDurationAsALong() {
        if (this.expired) {
            return 0L;
        }

        return (this.addedAt + this.durationLong) - System.currentTimeMillis();
    }

    public boolean isActive() {
        return !this.expired && (this.isPermanent() || this.getRemainingDurationAsALong() >= 0L);
    }

    public boolean isPermanent() {
        return this.durationLong == -1L;
    }

    public String getRemainingDurationAsAString() {
        if (this.expired) {
            return "&aResolved";
        }

        if (this.isPermanent()) {
            return "&4Permanent";
        }

        if (this.isActive()) {
            return "&cExpired";
        }

        return TimeUtils.formatLongIntoDetailedString(this.durationLong + this.addedAt - System.currentTimeMillis() / 1000L);
    }
}
