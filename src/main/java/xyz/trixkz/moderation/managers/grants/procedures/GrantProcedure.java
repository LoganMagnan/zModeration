package xyz.trixkz.moderation.managers.grants.procedures;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.grants.Grant;
import xyz.trixkz.moderation.playerdata.PlayerData;

@Getter
@Setter
public class GrantProcedure {

    private Main main = Main.getInstance();

    private OfflinePlayer player;
    private PlayerData playerData;
    private Grant grant;
    private GrantProcedureType type;
    private GrantProcedureStage stage;

    public GrantProcedure(OfflinePlayer player, PlayerData playerData, GrantProcedureType type, GrantProcedureStage stage) {
        this.player = player;
        this.playerData = playerData;
        this.type = type;
        this.stage = stage;
        this.main.getGrantManager().getGrantProcedures().add(this);
    }
}
