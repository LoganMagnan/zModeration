package xyz.trixkz.moderation.managers.grants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.grants.procedures.GrantProcedure;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GrantManager {

    private Main main = Main.getInstance();

    private Set<GrantProcedure> grantProcedures = new HashSet<GrantProcedure>();

    public GrantProcedure getGrantProcedureByPlayer(OfflinePlayer player) {
        for (GrantProcedure grantProcedure : this.grantProcedures) {
            if (grantProcedure.getPlayer().equals(player)) {
                return grantProcedure;
            }
        }

        return null;
    }
}
