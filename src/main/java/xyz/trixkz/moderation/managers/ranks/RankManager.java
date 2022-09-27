package xyz.trixkz.moderation.managers.ranks;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCursor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.utils.Utils;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RankManager {

    private Main main = Main.getInstance();

    private Map<UUID, Rank> ranks = new HashMap<UUID, Rank>();

    public void initRanks() {
        Map<Rank, List<UUID>> inheritanceReferences = new HashMap<Rank, List<UUID>>();

        try (MongoCursor<Document> mongoCursor = this.main.getMongoManager().getRanks().find().iterator()) {
            while (mongoCursor.hasNext()) {
                Document document = mongoCursor.next();

                Rank rank = new Rank(UUID.fromString(document.getString("uuid")), document.getString("name"));
                rank.load(document);

                ((List) this.main.getMongoManager().getGson().fromJson(document.getString("permissions"), this.main.getMongoManager().getType())).forEach(permission -> rank.getPermissions().add(String.valueOf(permission)));

                List<UUID> ranksToInherit = new ArrayList<UUID>();

                for (JsonElement jsonElement : new JsonParser().parse(document.getString("inherited-ranks")).getAsJsonArray()) {
                    ranksToInherit.add(UUID.fromString(jsonElement.getAsString()));
                }

                inheritanceReferences.put(rank, ranksToInherit);

                this.ranks.put(rank.getUuid(), rank);
            }
        }

        for (Rank rank : this.ranks.values()) {
            this.main.getServer().getConsoleSender().sendMessage(Utils.translate("&7[&dModeration&7] &fLoaded the " + rank.getFormattedName() + " &frank"));
        }

        this.getDefaultRank();
    }

    public Rank getDefaultRank() {
        for (Rank rank : this.ranks.values()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }

        Rank rank = new Rank("Default");
        rank.setDefaultRank(true);
        rank.save();

        this.ranks.put(rank.getUuid(), rank);

        return rank;
    }

    public Rank getRankByName(String name) {
        for (Rank rank : this.ranks.values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return null;
    }

    public Rank getRankByUUID(UUID uuid) {
        return this.ranks.get(uuid);
    }
}
