package xyz.trixkz.moderation.menusystem.menu.history;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.menusystem.ItemStackButton;
import xyz.trixkz.moderation.menusystem.Menu;
import xyz.trixkz.moderation.menusystem.PlayerMenuUtil;
import xyz.trixkz.moderation.utils.Utils;

public class HistoryMenu extends Menu {

    private Main main = Main.getInstance();

    private OfflinePlayer offlinePlayer;

    public HistoryMenu(PlayerMenuUtil playerMenuUtil, OfflinePlayer offlinePlayer) {
        super(playerMenuUtil);

        this.offlinePlayer = offlinePlayer;
    }

    @Override
    public String getMenuName() {
        return Utils.translate("&eLook at " + this.offlinePlayer.getName() + "'s history...");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equalsIgnoreCase(Utils.translate("&eLook at " + this.offlinePlayer.getName() + "'s history..."))) {
            switch (event.getCurrentItem().getType()) {
                case WOOL:
                    switch (event.getCurrentItem().getDurability()) {
                        case 13:
                            new WarnsMenu(this.main.getPlayerMenuUtil(player), this.offlinePlayer).open(player);

                            break;
                        case 5:
                            new KicksMenu(this.main.getPlayerMenuUtil(player), this.offlinePlayer).open(player);

                            break;
                        case 1:
                            new MutesMenu(this.main.getPlayerMenuUtil(player), this.offlinePlayer).open(player);

                            break;
                        case 14:
                            new BansMenu(this.main.getPlayerMenuUtil(player), this.offlinePlayer).open(player);

                            break;
                        case 11:
                            new BlacklistsMenu(this.main.getPlayerMenuUtil(player), this.offlinePlayer).open(player);

                            break;
                    }

                    break;
            }
        }
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStackButton warns = new ItemStackButton(
                "&d&lWarns",
                new String[]{
                        "",
                        "&9Look at " + this.offlinePlayer.getName() + "'s warns"
                },
                Material.WOOL,
                13,
                1);

        this.inventory.setItem(9, warns.makeItemStack());

        ItemStackButton kicks = new ItemStackButton(
                "&d&lKicks",
                new String[]{
                        "",
                        "&9Look at " + this.offlinePlayer.getName() + "'s kicks"
                },
                Material.WOOL,
                5,
                1);

        this.inventory.setItem(11, kicks.makeItemStack());

        ItemStackButton mutes = new ItemStackButton(
                "&d&lMutes",
                new String[]{
                        "",
                        "&9Look at " + this.offlinePlayer.getName() + "'s mutes"
                },
                Material.WOOL,
                1,
                1);

        this.inventory.setItem(13, mutes.makeItemStack());

        ItemStackButton bans = new ItemStackButton(
                "&d&lBans",
                new String[]{
                        "",
                        "&9Look at " + this.offlinePlayer.getName() + "'s bans"
                },
                Material.WOOL,
                14,
                1);

        this.inventory.setItem(15, bans.makeItemStack());

        ItemStackButton blacklists = new ItemStackButton(
                "&d&lBlacklists",
                new String[]{
                        "",
                        "&9Look at " + this.offlinePlayer.getName() + "'s blacklists"
                },
                Material.WOOL,
                11,
                1);

        this.inventory.setItem(17, blacklists.makeItemStack());
    }
}
