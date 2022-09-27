package xyz.trixkz.moderation.menusystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.trixkz.moderation.utils.ItemBuilder;
import xyz.trixkz.moderation.utils.Utils;
import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemStackButton {

    private String name;
    private String[] lore;
    private Material material;
    private int data;
    private int amount;

    public ItemStack makeItemStack() {
        return new ItemBuilder(this.material).name(Utils.translate(this.name)).lore(Utils.translate(Arrays.asList(this.lore))).durability(this.data).amount(this.amount).build();
    }
}
