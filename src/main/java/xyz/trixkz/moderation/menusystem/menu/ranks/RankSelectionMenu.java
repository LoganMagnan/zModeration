package xyz.trixkz.moderation.menusystem.menu.ranks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.Main;
import xyz.trixkz.moderation.managers.ranks.RankComparator;
import xyz.trixkz.moderation.menu.Button;
import xyz.trixkz.moderation.menu.pagination.PaginatedMenu;
import xyz.trixkz.moderation.playerdata.PlayerData;
import xyz.trixkz.moderation.utils.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RankSelectionMenu extends PaginatedMenu {

    private PlayerData playerData;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Utils.translate("&eGrant " + Main.getInstance().getServer().getOfflinePlayer(this.playerData.getUniqueId()).getName() + " &ea rank...");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();

        Main.getInstance().getRankManager().getRanks().values().stream().sorted(new RankComparator()).collect(Collectors.toList()).forEach(rank -> buttons.put(buttons.size(), new RankShowButton(rank, this.playerData)));

        return buttons;
    }
}
