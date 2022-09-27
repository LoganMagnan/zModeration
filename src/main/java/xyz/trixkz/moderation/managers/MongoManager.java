package xyz.trixkz.moderation.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.utils.Utils;
import xyz.trixkz.moderation.utils.config.file.Config;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class MongoManager {

    private final MongoManager instance;
    private final Main main = Main.getInstance();

    private final Config configFile = this.main.getSettingsConfig();
    private final FileConfiguration fileConfig = configFile.getConfig();
    private final ConfigurationSection config = fileConfig.getConfigurationSection("mongo");

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final String host = config.getString("host");
    private final int port = config.getInt("port");
    private final String database = config.getString("database");
    private final boolean auth = config.getBoolean("auth.enabled");
    private final String user = config.getString("auth.username");
    private final String password = config.getString("author.password");
    private final String authDatabase = config.getString("auth.auth-database");

    private boolean connected;

    private MongoCollection<Document> players;
    private MongoCollection<Document> ranks;

    private Gson gson = new Gson();

    private Type type = (new TypeToken<List<String>>() {

    }).getType();

    public MongoManager() {
        instance = this;
        try {
            if (auth) {
                final MongoCredential credential = MongoCredential.createCredential(user, authDatabase, password.toCharArray());
                mongoClient = new MongoClient(new ServerAddress(host, port), Collections.singletonList(credential));
            } else {
                mongoClient = new MongoClient(host, port);
            }
            connected = true;
            mongoDatabase = mongoClient.getDatabase(database);
            Bukkit.getConsoleSender().sendMessage(Utils.translate("&d[Moderation] &aSuccessfully connected to the database!"));
            this.players = this.mongoDatabase.getCollection("players");
            this.ranks = this.mongoDatabase.getCollection("ranks");
        } catch (Exception exception) {
            connected = false;
            Bukkit.getConsoleSender().sendMessage(Utils.translate("&d[Moderation] &cFailed to connect to the database!"));
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this.main);
            Bukkit.getConsoleSender().sendMessage(Utils.translate("&b[Moderation] &cDisabling Moderation..."));
        }
    }

    public void disconnect() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            this.connected = false;
            Bukkit.getConsoleSender().sendMessage(Utils.translate("&d[Moderation] &aSuccessfully disconnected from the database!"));
        }
    }

    public List<Document> getAllDocuments(MongoCollection<Document> mongoCollection) {
        List<Document> documents = new ArrayList<Document>();

        FindIterable<Document> findIterable = mongoCollection.find();

        MongoCursor<Document> mongoCursor = findIterable.iterator();

        try {
            while (mongoCursor.hasNext()) {
                documents.add(mongoCursor.next());
            }
        } finally {
            mongoCursor.close();
        }

        return documents;
    }
}
