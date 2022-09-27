package xyz.trixkz.moderation.managers;

import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.commands.staff.grants.GrantCommand;
import xyz.trixkz.moderation.commands.staff.punishments.*;
import xyz.trixkz.moderation.commands.staff.ranks.RankCommand;

/**
 * Made By Trixkz (LoganM) - trixkz.me
 * Project: Moderation
 */
public class CommandManager {

    private Main main = Main.getInstance();

    public CommandManager() {
        this.registerCommands();
    }

    private void registerCommands() {
        this.main.getCommand("rank").setExecutor(new RankCommand());
        this.main.getCommand("grant").setExecutor(new GrantCommand());
        this.main.getCommand("warn").setExecutor(new WarnCommand());
        this.main.getCommand("kick").setExecutor(new KickCommand());
        this.main.getCommand("mute").setExecutor(new MuteCommand());
        this.main.getCommand("tempmute").setExecutor(new TempMuteCommand());
        this.main.getCommand("ban").setExecutor(new BanCommand());
        this.main.getCommand("tempban").setExecutor(new TempBanCommand());
        this.main.getCommand("blacklist").setExecutor(new BlacklistCommand());
        this.main.getCommand("history").setExecutor(new HistoryCommand());
        this.main.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.main.getCommand("unban").setExecutor(new UnbanCommand());
    }
}
