package xyz.trixkz.moderation.menusystem.menu.ranks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.trixkz.moderation.menu.Button;
import xyz.trixkz.moderation.menu.Menu;
import xyz.trixkz.moderation.utils.TypeCallback;
import xyz.trixkz.moderation.utils.Utils;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RankConfirmMenu extends Menu {

    private String name;
    private TypeCallback<Boolean> callback;
    private boolean closeAfterCallback;
    private Button[] centerButtons;

    @Override
    public String getTitle(Player player) {
        return Utils.translate(this.name);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons.put(this.getSlot(i, j), new RankConfirmButton(true, this.callback, this.closeAfterCallback));
                buttons.put(this.getSlot(8 - i, j), new RankConfirmButton(false, this.callback, this.closeAfterCallback));
            }
        }

        if (this.centerButtons != null) {
            for (int i = 0; i < this.centerButtons.length; i++) {
                if (this.centerButtons[i] != null) {
                    buttons.put(this.getSlot(4, i), this.centerButtons[i]);
                }
            }
        }

        return buttons;
    }
}
