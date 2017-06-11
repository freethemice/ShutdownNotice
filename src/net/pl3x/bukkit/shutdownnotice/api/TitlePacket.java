package net.pl3x.bukkit.shutdownnotice.api;

import org.bukkit.entity.Player;

public abstract interface TitlePacket
{
  public abstract void setText(String paramString);
  
  public abstract void setType(TitleType paramTitleType);
  
  public abstract void setTimes(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void send(Player paramPlayer);
  
  public abstract void broadcast();
}
