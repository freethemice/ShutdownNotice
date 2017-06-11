package net.pl3x.bukkit.shutdownnotice.listener;

import net.pl3x.bukkit.shutdownnotice.ServerStatus;
import net.pl3x.bukkit.shutdownnotice.configuration.Config;
import net.pl3x.bukkit.shutdownnotice.configuration.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements org.bukkit.event.Listener
{
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onServerListPing(ServerListPingEvent event)
  {
    if (!Config.UPDATE_PING_MOTD.getBoolean()) {
      return;
    }
    
    ServerStatus status = ServerStatus.getStatus();
    ServerStatus.State state = status.getState();
    Integer timeLeft = status.getTimeLeft();
    String reason = status.getReason();
    
    if ((state == null) || (state.equals(ServerStatus.State.RUNNING)) || (timeLeft == null)) {
      return;
    }
    
    if (reason == null) {
      reason = "";
    }
    
    int seconds = timeLeft.intValue() % 60;
    int minutes = timeLeft.intValue() / 60;
    
    String time = Lang.TIME_FORMAT.replace("{minutes}", String.format("%02d", new Object[] { Integer.valueOf(minutes) })).replace("{seconds}", String.format("%02d", new Object[] { Integer.valueOf(seconds) }));
    String action = state.equals(ServerStatus.State.SHUTDOWN) ? Lang.SHUTTING_DOWN.toString() : Lang.RESTARTING.toString();
    
    String pingMessage = Lang.PING_MESSAGE.replace("{action}", action).replace("{time}", time).replace("{reason}", reason);
    
    event.setMotd(org.bukkit.ChatColor.translateAlternateColorCodes('&', pingMessage));
  }
}
