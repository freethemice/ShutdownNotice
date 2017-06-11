package net.pl3x.bukkit.shutdownnotice.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.pl3x.bukkit.shutdownnotice.api.ITitle;
import net.pl3x.bukkit.shutdownnotice.api.TitleType;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketHandler implements net.pl3x.bukkit.shutdownnotice.api.TitlePacket
{
  private net.minecraft.server.v1_12_R1.IChatBaseComponent component;
  private PacketPlayOutTitle.EnumTitleAction type;
  private int fadeIn = 20;
  private int stay = 60;
  private int fadeOut = 20;
  
  public PacketHandler(ITitle title) {
    setText(title.getText());
    setType(title.getType());
    int[] times = title.getTimes();
    setTimes(times[0], times[1], times[2]);
  }
  
  public void setText(String text) {
    if (text == null) {
      return;
    }
    this.component = net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + org.bukkit.ChatColor.translateAlternateColorCodes('&', text) + "\"}");
  }
  
  public void setType(TitleType type) {
    if (type == null) {
      return;
    }
    this.type = PacketPlayOutTitle.EnumTitleAction.a(type.name());
  }
  
  public void setTimes(int fadeIn, int stay, int fadeOut) {
    this.fadeIn = fadeIn;
    this.stay = stay;
    this.fadeOut = fadeOut;
  }
  
  public void send(Player player)
  {
    if ((player == null) || (!player.isOnline())) {
      return;
    }
    PacketPlayOutTitle packet = new PacketPlayOutTitle(this.type, this.component, this.fadeIn, this.stay, this.fadeOut);
    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
  }
  
  public void broadcast()
  {
    PacketPlayOutTitle packet = new PacketPlayOutTitle(this.type, this.component, this.fadeIn, this.stay, this.fadeOut);
    for (Player online : org.bukkit.Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
    }
  }
}
