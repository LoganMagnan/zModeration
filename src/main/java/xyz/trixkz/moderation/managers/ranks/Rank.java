package xyz.trixkz.moderation.managers.ranks;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Rank {

    private Main main = Main.getInstance();

    private UUID uuid;
    private String name;

    private String prefix = "";
    private String suffix = "";

    private ChatColor color = ChatColor.WHITE;

    private int weight;

    private List<String> permissions = new ArrayList<String>();
    private List<Rank> inheritedRanks = new ArrayList<Rank>();

    private boolean defaultRank;

    public Rank(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.main.getRankManager().getRanks().put(this.uuid, this);
    }

    public Rank(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Rank(UUID uuid, String name, String prefix, String suffix, ChatColor color, int weight, boolean defaultRank) {
        this.uuid = uuid;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.weight = weight;
        this.defaultRank = defaultRank;
        this.main.getRankManager().getRanks().put(this.uuid, this);
    }

    public void load() {
        this.load(this.main.getMongoManager().getRanks().find(Filters.eq("uuid", this.uuid.toString())).first());
    }

    public void load(Document document) {
        if (document == null) {
            return;
        }

        this.prefix = Utils.translate(document.getString("prefix"));
        this.suffix = Utils.translate(document.getString("suffix"));
        this.color = ChatColor.valueOf(document.getString("color"));
        this.weight = document.getInteger("weight");
        this.defaultRank = document.getBoolean("default-rank");
    }

    public void save() {
        Document document = new Document();
        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);
        document.put("prefix", this.prefix);
        document.put("suffix", this.suffix);
        document.put("color", this.color.name());
        document.put("weight", this.weight);
        document.put("permissions", this.main.getMongoManager().getGson().toJson(this.permissions));
        document.put("inherited-ranks", this.main.getMongoManager().getGson().toJson(this.inheritedRanks.stream().map(Rank :: getUuid).map(UUID :: toString).collect(Collectors.toList())));
        document.put("default-rank", this.defaultRank);

        this.main.getMongoManager().getRanks().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new UpdateOptions().upsert(true));
    }

    public void remove() {
        this.main.getRankManager().getRanks().remove(this.uuid);
        this.main.getMongoManager().getRanks().deleteOne(Filters.eq("uuid", this.uuid.toString()));
    }

    public String getFormattedName() {
        return this.color + this.name;
    }

    public List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<String>(this.permissions);

        for (Rank rank : this.inheritedRanks) {
            permissions.addAll(rank.getAllPermissions());
        }

        return permissions;
    }

    public boolean addPermission(String permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);

            return true;
        }

        return false;
    }

    public boolean removePermission(String permission) {
        return this.permissions.remove(permission);
    }

    public boolean hasPermission(String permission) {
        if (this.permissions.contains(permission)) {
            return true;
        }

        for (Rank rank : this.inheritedRanks) {
            if (rank.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    public boolean canInheritRank(Rank rank) {
        if (this.inheritedRanks.contains(rank) || rank.getInheritedRanks().contains(this)) {
            return false;
        }

        for (Rank inheritedRank : this.inheritedRanks) {
            if (!inheritedRank.canInheritRank(rank)) {
                return false;
            }
        }

        return true;
    }
}
