package xyz.trixkz.moderation.menusystem.menu.ranks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.trixkz.moderation.menu.Button;
import xyz.trixkz.moderation.utils.ItemBuilder;
import xyz.trixkz.moderation.utils.TypeCallback;
import xyz.trixkz.moderation.utils.Utils;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RankConfirmButton extends Button {

    private boolean rankConfirm;
    private TypeCallback<Boolean> callback;
    private boolean closeAfterCallback;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL).name(Utils.translate(this.rankConfirm ? "&aConfirm" : "&4Cancel")).durability(this.rankConfirm ? 5 : 14).build();
    }

    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (this.closeAfterCallback) {
            player.closeInventory();
        }

        this.callback.callback(this.rankConfirm);
    }
}
