package xyz.trixkz.moderation.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import xyz.trixkz.moderation.managers.grants.Grant;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GrantExpireEvent extends BaseEvent {

    private OfflinePlayer player;
    private Grant grant;
}
