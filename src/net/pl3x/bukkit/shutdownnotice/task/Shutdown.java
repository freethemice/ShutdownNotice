package net.pl3x.bukkit.shutdownnotice.task;

import net.pl3x.bukkit.shutdownnotice.Logger;
import net.pl3x.bukkit.shutdownnotice.ServerStatus;
import net.pl3x.bukkit.shutdownnotice.ShutdownNotice;
import net.pl3x.bukkit.shutdownnotice.configuration.Config;
import net.pl3x.bukkit.shutdownnotice.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Shutdown extends org.bukkit.scheduler.BukkitRunnable
{
  private final ShutdownNotice plugin;
  
  public Shutdown(ShutdownNotice plugin)
  {
    this.plugin = plugin;
  }
  
  public void run()
  {
    ServerStatus status = ServerStatus.getStatus();
    ServerStatus.State state = status.getState();
    String reason = status.getReason();
    

    if (reason == null)
      reason = "";
    String action;
    if (state.equals(ServerStatus.State.RESTART))
    {
      Logger.debug("Creating restart file.");
      try {
        if (new java.io.File(this.plugin.getDataFolder(), "restart").createNewFile()) {
          Logger.debug("Creating restart file for startup script.");
        } else {
          Logger.warn("Something went wrong with trying to create the restart file!");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      action = Lang.RESTARTING.toString();
    } else {
      action = Lang.SHUTTING_DOWN.toString();
    }
    

    List<String> shutdownCommands = Config.SHUTDOWN_COMMANDS.getStringList();
    for (Iterator localIterator = shutdownCommands.iterator(); localIterator.hasNext();) {
      String command = (String)localIterator.next();
      command = command.replace("{action}", action).replace("{reason}", reason);
      Logger.debug("Performing command: " + command);
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
    
    String command;
    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
    

    String kickMessage = org.bukkit.ChatColor.translateAlternateColorCodes('&', Lang.KICK_MESSAGE.toString().replace("{action}", action).replace("{reason}", reason));
    for (Player player : Bukkit.getOnlinePlayers()) {
      Logger.debug("Kicking player: " + player.getName() + "(" + kickMessage + ")");
      player.kickPlayer(kickMessage);
    }
    

    Logger.debug("Performing command: stop");
    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "minecraft:stop");
  }
}
