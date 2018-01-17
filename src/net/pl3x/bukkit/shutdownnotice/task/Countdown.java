package net.pl3x.bukkit.shutdownnotice.task;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.pl3x.bukkit.shutdownnotice.ComponentSender;
import net.pl3x.bukkit.shutdownnotice.ServerStatus;
import net.pl3x.bukkit.shutdownnotice.ShutdownNotice;
import net.pl3x.bukkit.shutdownnotice.Title;
import net.pl3x.bukkit.shutdownnotice.api.TitleType;
import net.pl3x.bukkit.shutdownnotice.api.chat.BaseComponent;
import net.pl3x.bukkit.shutdownnotice.api.chat.TextComponent;
import net.pl3x.bukkit.shutdownnotice.configuration.Config;
import net.pl3x.bukkit.shutdownnotice.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Countdown extends BukkitRunnable
{
  private final ShutdownNotice plugin;
  private boolean firstRun = true;
  
  public Countdown(ShutdownNotice plugin) {
    this.plugin = plugin;
  }
  
  public void run()
  {
    ServerStatus status = ServerStatus.getStatus();
    ServerStatus.State state = status.getState();
    Long timeLeft = status.getTimeLeft();
    String reason = status.getReason();


    if ((state == null) || (state.equals(ServerStatus.State.RUNNING)) || (timeLeft == null)) {
      status.setStatus(ServerStatus.State.RUNNING, null, null);
      cancel();
      return;
    }
    
    if (reason == null) {
      reason = "";
    }
    timeLeft = (timeLeft - System.currentTimeMillis()) / 1000;


    String time = Lang.TIME_FORMAT.replace("{minutes}", String.format("%02d", new Object[] { Integer.valueOf(timeLeft.intValue() / 60) })).replace("{seconds}", String.format("%02d", new Object[] { Integer.valueOf(timeLeft.intValue() % 60) }));
    String action = state.equals(ServerStatus.State.SHUTDOWN) ? Lang.SHUTTING_DOWN.toString() : Lang.RESTARTING.toString();
    
    if (timeLeft.intValue() <= 0) {
      String rightNow = Lang.RIGHT_NOW.toString();
      
      broadcastActionbar(action, rightNow, reason);
      broadcastTitle(action, rightNow, reason);
      broadcastChat(action, rightNow, reason);
      
      new Shutdown(this.plugin).runTaskLater(this.plugin, 20L);
      cancel();
      return;
    }
    
    boolean broadcast = false;
    ScriptEngine engine; if (this.firstRun) {
      broadcast = true;
    } else {
      ScriptEngineManager factory = new ScriptEngineManager();
      engine = factory.getEngineByName("JavaScript");
      for (String condition : Config.DISPLAY_INTERVALS.getStringList()) {
        try {
          engine.eval("timeLeft = " + timeLeft);
          if (((Boolean)engine.eval(condition)).booleanValue()) {
            broadcast = true;
            break;
          }
        } catch (ScriptException e) {
          e.printStackTrace();
        }
      }
    }
    

    broadcastActionbar(action, time, reason);
    

    if (broadcast) {
      broadcastTitle(action, time, reason);
      broadcastChat(action, time, reason);
    }
    
    //status.setStatus(state, Integer.valueOf(timeLeft.intValue() - 1), reason);
    this.firstRun = false;
  }
  
  private void broadcastActionbar(String action, String time, String reason) {
    BaseComponent[] actionComponent = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', Lang.ACTIONBAR_TXT
    
      .replace("{action}", action)
      .replace("{time}", time)
      .replace("{reason}", reason)));
    for (Player online : Bukkit.getOnlinePlayers()) {
      ComponentSender.sendMessage(online, ChatMessageType.GAME_INFO, new TextComponent(BaseComponent.toLegacyText(actionComponent)));
    }
  }
  
  private void broadcastTitle(String action, String time, String reason) {
    Title titleReset = new Title(TitleType.RESET, null);
    Title titleTimings = new Title(5, 60, 10);
    Title titleText = new Title(TitleType.TITLE, ChatColor.translateAlternateColorCodes('&', Lang.TITLE_TXT
    
      .replace("{action}", action)
      .replace("{time}", time)
      .replace("{reason}", reason)), 5, 60, 10);
    
    Title titleSubtext = new Title(TitleType.SUBTITLE, ChatColor.translateAlternateColorCodes('&', Lang.SUBTITLE_TXT
    
      .replace("{action}", action)
      .replace("{time}", time)
      .replace("{reason}", reason)), 5, 60, 10);
    
    for (Player online : Bukkit.getOnlinePlayers()) {
      titleReset.send(online);
      titleTimings.send(online);
      titleText.send(online);
      titleSubtext.send(online);
    }
  }
  


  private void broadcastChat(String action, String time, String reason)
  {
    String chatTxt = Lang.CHAT_TXT.replace("{action}", action).replace("{time}", time).replace("{reason}", reason);
    BaseComponent[] chatText = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', chatTxt));
    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', chatTxt));
    for (Player online : Bukkit.getOnlinePlayers()) {
      ComponentSender.sendMessage(online, chatText);
    }
    
    if (this.plugin.getBotHook() != null) {
      this.plugin.getBotHook().sendToDiscord("*" + chatTxt.trim() + "*");
    }
  }
}
