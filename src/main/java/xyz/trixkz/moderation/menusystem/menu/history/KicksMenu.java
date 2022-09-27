package xyz.trixkz.moderation.menusystem.menu.history;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.punishments.Punishment;
import xyz.trixkz.moderation.managers.punishments.PunishmentType;
import xyz.trixkz.moderation.menusystem.ItemStackButton;
import xyz.trixkz.moderation.menusystem.PaginatedMenu;
import xyz.trixkz.moderation.menusystem.PlayerMenuUtil;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class KicksMenu extends PaginatedMenu {

    private Main main = Main.getInstance();

    private OfflinePlayer offlinePlayer;

    private List<Punishment> punishments = new ArrayList<Punishment>();

    private int count = 1;
    private int punishmentsShown = 0;

    public KicksMenu(PlayerMenuUtil playerMenuUtil, OfflinePlayer offlinePlayer) {
        super(playerMenuUtil);

        this.offlinePlayer = offlinePlayer;

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(this.offlinePlayer.getUniqueId());

        for (Punishment punishment : playerData.getPunishments()) {
            if (punishment.getType() != PunishmentType.KICK) {
                continue;
            }

            this.punishments.add(punishment);
        }
    }

    @Override
    public String getMenuName() {
        return Utils.translate("&eLook at " + this.offlinePlayer.getName() + "'s kicks...");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equalsIgnoreCase(Utils.translate("&eLook at " + this.offlinePlayer.getName() + "'s kicks..."))) {
            switch (event.getCurrentItem().getType()) {
                case WOOD_BUTTON:
                    if (page == 0) {
                        player.sendMessage(Utils.translate("&aYou are on the first page"));
                    } else {
                        this.count -= this.punishmentsShown + 45;
                        this.punishmentsShown = 0;

                        page--;

                        super.open(player);
                    }

                    break;
                case STONE_BUTTON:
                    if (!((index + 1) >= this.punishments.size())) {
                        this.punishmentsShown = 0;

                        page++;

                        super.open(player);
                    } else {
                        player.sendMessage(Utils.translate("&aYou are on the last page"));
                    }

                    break;
                case BARRIER:
                    new HistoryMenu(this.main.getPlayerMenuUtil(player), this.offlinePlayer).open(player);

                    break;
            }
        }
    }

    @Override
    public void setMenuItems(Player player) {
        this.addMenuBorder();

        for (int i = 0; i < this.getMaxItemsPerPage(); i++) {
            this.index = this.getMaxItemsPerPage() * this.page + i;

            if (this.index >= this.punishments.size()) {
                break;
            }

            if (this.punishments.get(i) != null) {
                Punishment punishment = this.punishments.get(i);

                ItemStackButton kick = new ItemStackButton(
                        "&d&l#" + this.count + " &7(&f" + punishment.getId() + "&7)",
                        new String[]{
                                "",
                                "&fStaff member: &d" + (punishment.isConsole() ? "Console" : this.main.getServer().getOfflinePlayer(punishment.getStaffMember()).getName()),
                                "&fReason: &d" + punishment.getReason(),
                                "&fAdded at: &d" + Utils.getAddedAtDate(punishment.getAddedAt())
//                            "",
//                            "&fExpired: &d" + (punishment.isExpired() ? "Yes" : "No")
//                            "&fUnbanned: &d" + (punishment.isUnbanned() ? "Yes" : "No"),
//                            "",
//                            (punishment.isUnbanned() ? "&fUnbanned by: &d" + this.main.getServer().getOfflinePlayer(punishment.getUnbannedBy()).getName() : ""),
//                            (punishment.isUnbanned() ? "&fUnbanned at: &d" + Utils.getAddedAtDate(punishment.getUnbannedAt()) : ""),
//                            (punishment.isUnbanned() ? "&fUnbanned reason: &d" + punishment.getUnbannedReason() : "")
                        },
                        Material.NETHER_STAR,
                        0,
                        1);

                this.count++;
                this.punishmentsShown++;
                this.inventory.addItem(kick.makeItemStack());
            }
        }
    }
}
