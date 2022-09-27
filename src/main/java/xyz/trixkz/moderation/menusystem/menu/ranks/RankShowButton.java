package xyz.trixkz.moderation.menusystem.menu.ranks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.grants.Grant;
import xyz.trixkz.moderation.managers.ranks.Rank;
import xyz.trixkz.moderation.menu.Button;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.ItemBuilder;
import xyz.trixkz.moderation.utils.SpigotUtils;
import xyz.trixkz.moderation.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RankShowButton extends Button {

    private Rank rank;
    private PlayerData playerData;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<String>();
        lore.add(Utils.translate(Utils.scoreboardBar));
        lore.add(Utils.translate("&9Grant " + this.playerData.getColoredName() + " &9the " + this.rank.getFormattedName() + " &9rank"));
        lore.add(Utils.translate(Utils.scoreboardBar));

        ChatColor chatColor = ChatColor.getByChar(this.rank.getColor().getChar());

        int color = SpigotUtils.toDyeColor(chatColor);

        return new ItemBuilder(Material.WOOL).name(Utils.translate("&e" + this.rank.getName())).lore(lore).durability(color).build();
    }

    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        player.closeInventory();

        Button button = new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER).name(Utils.translate("&eGrant " + RankShowButton.this.rank.getName() + " &9to " + Main.getInstance().getServer().getOfflinePlayer(RankShowButton.this.playerData.getUniqueId()).getName() + "?")).lore(Arrays.asList(Utils.translate(""), Utils.translate("&9Are you sure"), Utils.translate("&9that you would like"), Utils.translate("&9to grant " + RankShowButton.this.rank.getName()), Utils.translate("&9to " + Main.getInstance().getServer().getPlayer(RankShowButton.this.playerData.getUniqueId()).getName() + "?"))).build();
            }
        };

        Grant[] grant = new Grant[1];

        Button[] buttons = {button, button, button};

        new RankConfirmMenu(Utils.translate("&eConfirm grant?"), information -> {
            if (information) {
                grant[0] = new Grant(UUID.randomUUID(), this.rank, player.getUniqueId(), System.currentTimeMillis(), "Granted", -1L);

                player.sendMessage(Utils.translate(Main.getInstance().getMessagesConfig().getConfig().getString("grant-added-to-player").replace("(rank)", this.rank.getFormattedName()).replace("(player)", Main.getInstance().getServer().getOfflinePlayer(this.playerData.getUniqueId()).getName())));
            }
        }, true, buttons).openMenu(player);
    }
}
