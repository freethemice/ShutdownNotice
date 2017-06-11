package net.pl3x.bukkit.shutdownnotice;

import net.pl3x.bukkit.shutdownnotice.configuration.Config;

public class Logger
{
  private static void log(String msg)
  {
    msg = org.bukkit.ChatColor.translateAlternateColorCodes('&', "&3[&d" + ((ShutdownNotice)ShutdownNotice.getPlugin(ShutdownNotice.class)).getName() + "&3]&r " + msg);
    if (!Config.COLOR_LOGS.getBoolean()) {
      msg = org.bukkit.ChatColor.stripColor(msg);
    }
    org.bukkit.Bukkit.getServer().getConsoleSender().sendMessage(msg);
  }
  
  public static void debug(String msg) {
    if (Config.DEBUG_MODE.getBoolean()) {
      log("&7[&eDEBUG&7]&e " + msg);
    }
  }
  
  public static void warn(String msg) {
    log("&e[&6WARN&e]&6 " + msg);
  }
  
  public static void error(String msg) {
    log("&e[&4ERROR&e]&4 " + msg);
  }
  
  public static void info(String msg) {
    log("&e[&fINFO&e]&r " + msg);
  }
}
