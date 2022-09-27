package xyz.trixkz.moderation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import xyz.trixkz.moderation.menusystem.Menu;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: Moderation
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();

        if (inventoryHolder instanceof Menu) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) {
                return;
            }

            Menu menu = (Menu) inventoryHolder;
            menu.handleMenu(event);
        }
    }
}
