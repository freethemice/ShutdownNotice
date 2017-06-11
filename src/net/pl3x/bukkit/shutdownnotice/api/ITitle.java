package net.pl3x.bukkit.shutdownnotice.api;

import org.bukkit.entity.Player;

public abstract interface ITitle
{
  public abstract String getText();
  
  public abstract void setText(String paramString);
  
  public abstract TitleType getType();
  
  public abstract void setType(TitleType paramTitleType);
  
  public abstract int[] getTimes();
  
  public abstract void setTimes(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void send(Player paramPlayer);
  
  public abstract void broadcast();
}
