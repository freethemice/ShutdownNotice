package net.pl3x.bukkit.shutdownnotice;

import net.pl3x.bukkit.shutdownnotice.command.CmdShutdown;
import net.pl3x.bukkit.shutdownnotice.hook.Pl3xBotHook;
import net.pl3x.bukkit.shutdownnotice.listener.CommandListener;
import net.pl3x.bukkit.shutdownnotice.listener.PingListener;
import org.bukkit.Bukkit;

import java.io.File;

public class ShutdownNotice extends org.bukkit.plugin.java.JavaPlugin
{
  private Pl3xBotHook botHook = null;
  
  public void onEnable()
  {
    saveDefaultConfig();
    
    net.pl3x.bukkit.shutdownnotice.configuration.Lang.reload();
    
    if (Bukkit.getPluginManager().isPluginEnabled("Pl3xBot")) {
      Logger.info("Found Pl3xBot. Initiating hook.");
      this.botHook = new Pl3xBotHook();
    }
    
    if (new File(getDataFolder(), "restart").delete()) {
      Logger.debug("Deleting restart file.");
    }
    
    Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
    Bukkit.getPluginManager().registerEvents(new PingListener(), this);
    
    getCommand("shutdown").setExecutor(new CmdShutdown(this));
    
    Logger.info(getName() + " v" + getDescription().getVersion() + " enabled!");
  }
  
  public void onDisable()
  {
    Logger.info(getName() + " Disabled.");
  }
  
  public Pl3xBotHook getBotHook() {
    return this.botHook;
  }
}
