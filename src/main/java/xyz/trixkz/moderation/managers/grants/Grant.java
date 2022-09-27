package xyz.trixkz.moderation.managers.grants;

import lombok.Getter;
import lombok.Setter;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.utils.TimeUtil;
import xyz.trixkz.moderation.utils.TimeUtils;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class Grant {

    private transient Main main = Main.getInstance();

    private UUID uuid;
    private Rank rank;
    private UUID addedBy;
    private long addedAt;
    private String addedReason;
    private long durationLong;

    private UUID removedBy;
    private long removedAt;
    private String removedReason;

    private boolean removed;

    public Grant(UUID uuid, Rank rank, UUID addedBy, long addedAt, String addedReason, long durationLong) {
        this.uuid = uuid;
        this.rank = rank;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.durationLong = durationLong;
        this.removed = false;
    }

    public long getRemainingDurationAsALong() {
        if (this.removed) {
            return 0L;
        }

        return (this.addedAt + this.durationLong) - System.currentTimeMillis();
    }

    public boolean isPermanent() {
        return this.durationLong == -1L;
    }

    public boolean hasExpired() {
        return !this.isPermanent() && System.currentTimeMillis() >= this.addedAt + this.durationLong;
    }

    public String getExpiresAtDate() {
        return this.isPermanent() ? "&4Never" : TimeUtil.dateToString(new Date(this.addedAt + this.durationLong));
    }

    public String getRemainingDurationAsAString() {
        if (this.removed) {
            return "&cExpired";
        }

        if (this.isPermanent()) {
            return "&4Permanent";
        }

        return TimeUtils.formatLongIntoDetailedString(this.durationLong + this.addedAt - System.currentTimeMillis() / 1000L);
    }

    @Override
    public boolean equals(Object object) {
        return (object != null && object instanceof Grant && ((Grant) object).getUuid().equals(this.uuid));
    }
}
