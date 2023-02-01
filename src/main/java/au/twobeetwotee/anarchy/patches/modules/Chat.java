package au.twobeetwotee.anarchy.patches.modules;

import au.twobeetwotee.anarchy.patches.Module;
import au.twobeetwotee.anarchy.utils.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

public class Chat extends Module {

    public boolean isEnabled() {
        return Config.ANTISPAM;
    }

    public Module onEnable() {
        return this;
    }

    @EventHandler
    private void on(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

//        message = ChatUtils.removeDiacriticalMarks(message);
//        message = ChatUtils.removeNonASCII(message);

        if (message.startsWith("!tps") || message.contains("!tps")) {
            e.setCancelled(true);
            player.performCommand("tps");
            this.log("§cReason: !tps >:( Player: " + player.getName() + " Message: " + message);
        }

        e.setMessage(message);
    }
}
