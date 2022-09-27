package xyz.trixkz.moderation.menusystem;

import org.bukkit.entity.Player;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: Moderation
 */
public class PlayerMenuUtil {

    private Player owner;

    public PlayerMenuUtil(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
