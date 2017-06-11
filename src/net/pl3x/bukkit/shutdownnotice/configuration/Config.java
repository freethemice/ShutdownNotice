package net.pl3x.bukkit.shutdownnotice.configuration;

import net.pl3x.bukkit.shutdownnotice.ShutdownNotice;

public enum Config
{
  COLOR_LOGS(Boolean.valueOf(true)), 
  DEBUG_MODE(Boolean.valueOf(false)), 
  LANGUAGE_FILE("lang-en.yml"), 
  UPDATE_PING_MOTD(Boolean.valueOf(true)), 
  SHUTDOWN_COMMANDS(null), 
  DISPLAY_INTERVALS(null);
  
  private final ShutdownNotice plugin;
  private final Object def;
  
  private Config(Object def) {
    this.plugin = ((ShutdownNotice)ShutdownNotice.getPlugin(ShutdownNotice.class));
    this.def = def;
  }
  
  public static void reload() {
    ((ShutdownNotice)ShutdownNotice.getPlugin(ShutdownNotice.class)).reloadConfig();
  }
  
  private String getKey() {
    return name().toLowerCase().replace("_", "-");
  }
  
  public String getString() {
    return this.plugin.getConfig().getString(getKey(), (String)this.def);
  }
  
  public boolean getBoolean() {
    return this.plugin.getConfig().getBoolean(getKey(), ((Boolean)this.def).booleanValue());
  }
  
  public java.util.List<String> getStringList() {
    return this.plugin.getConfig().getStringList(getKey());
  }
}
