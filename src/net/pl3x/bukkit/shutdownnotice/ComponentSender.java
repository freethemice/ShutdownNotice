//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.pl3x.bukkit.shutdownnotice;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.pl3x.bukkit.shutdownnotice.api.ChatComponentPacket;
import net.pl3x.bukkit.shutdownnotice.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ComponentSender {
  public ComponentSender() {
  }

  public static void sendMessage(Player player, BaseComponent component) {
    sendMessage(player, ChatMessageType.CHAT, new BaseComponent[]{component});
  }

  public static void sendMessage(Player player, BaseComponent... components) {
    sendMessage(player, ChatMessageType.CHAT, components);
  }

  public static void sendMessage(Player player, ChatMessageType position, BaseComponent component) {
    sendMessage(player, position, new BaseComponent[]{component});
  }

  public static void sendMessage(Player player, ChatMessageType position, BaseComponent... components) {
    if(player != null) {
      ChatComponentPacket packet = getPacket();
      if(packet != null) {
        packet.sendMessage(player, position, components);
      }
    }
  }

  private static ChatComponentPacket getPacket() {
    String packageName = Bukkit.getServer().getClass().getPackage().getName();
    String version = packageName.substring(packageName.lastIndexOf(46) + 1);
    String path = ComponentSender.class.getPackage().getName() + ".nms." + version;

    try {
      Class clazz = Class.forName(path + ".ChatComponentPacketHandler");
      if(ChatComponentPacket.class.isAssignableFrom(clazz)) {
        return (ChatComponentPacket)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
      }
    } catch (Exception var4) {
      ;
    }

    Bukkit.getLogger().info("[ERROR] This plugin is not compatible with this server version (" + version + ").");
    Bukkit.getLogger().info("[ERROR] Could not send chat packet!");
    return null;
  }
}
