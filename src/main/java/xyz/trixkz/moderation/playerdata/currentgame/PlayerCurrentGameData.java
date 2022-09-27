package xyz.trixkz.moderation.playerdata.currentgame;

import lombok.Data;
import xyz.trixkz.moderation.Main;

@Data
public class PlayerCurrentGameData {

    private transient Main main = Main.getInstance();
}
