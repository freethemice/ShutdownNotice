package net.pl3x.bukkit.shutdownnotice.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.pl3x.bukkit.shutdownnotice.api.chat.BaseComponent;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChatComponentPacketHandler implements net.pl3x.bukkit.shutdownnotice.api.ChatComponentPacket
{
  @Override
  public void sendMessage(Player player, ChatMessageType position, BaseComponent... components)
  {
    if (player == null) {
      return;
    }
    net.minecraft.server.v1_12_R1.IChatBaseComponent component = net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer.a(net.pl3x.bukkit.shutdownnotice.chat.ComponentSerializer.toString(components));
    PacketPlayOutChat packet = new PacketPlayOutChat(component, position);
    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
  }

}
