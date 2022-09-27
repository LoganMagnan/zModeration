package xyz.trixkz.moderation.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.trixkz.moderation.Main;

public class BaseEvent extends Event {

    private Main main = Main.getInstance();

    private static HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void call() {
        this.main.getServer().getPluginManager().callEvent(this);
    }
}
