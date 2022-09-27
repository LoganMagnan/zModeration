package xyz.trixkz.moderation.commands.staff.ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.utils.Utils;

public class RankCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(this.main.getSettingsConfig().getConfig().getString("rank-permission-node"))) {
            sender.sendMessage(Utils.translate(this.main.getMessagesConfig().getConfig().getString("no-permission")));

            return true;
        }

        if (args.length == 0) {
            for (String string : this.main.getMessagesConfig().getConfig().getStringList("rank-usage")) {
                sender.sendMessage(Utils.translate(string));
            }
        } else {
            switch (args[0]) {
                case "set":
                    new SetCommand().executeAs(sender, cmd, label, args);

                    break;
                case "create":
                    new CreateCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setprefix":
                    new SetPrefixCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setsuffix":
                    new SetSuffixCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setcolor":
                    new SetColorCommand().executeAs(sender, cmd, label, args);

                    break;
                case "setweight":
                    new SetWeightCommand().executeAs(sender, cmd, label, args);

                    break;
                case "addpermission":
                    new AddPermissionCommand().executeAs(sender, cmd, label, args);

                    break;
                case "removepermission":
                    new RemovePermissionCommand().executeAs(sender, cmd, label, args);

                    break;
                case "addinheritance":
                    new AddInheritanceCommand().executeAs(sender, cmd, label, args);

                    break;
                case "removeinheritance":
                    new RemoveInheritanceCommand().executeAs(sender, cmd, label, args);

                    break;
                case "list":
                    new ListCommand().executeAs(sender, cmd, label, args);

                    break;
                case "info":
                    new InfoCommand().executeAs(sender, cmd, label, args);

                    break;
                case "rename":
                    new RenameCommand().executeAs(sender, cmd, label, args);

                    break;
            }
        }

        return true;
    }
}
