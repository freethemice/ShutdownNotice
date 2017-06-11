package net.pl3x.bukkit.shutdownnotice.api.chat;

import java.util.Arrays;

public final class HoverEvent { private final Action action;
  private final BaseComponent[] value;
  
  public String toString() { return "HoverEvent(action=" + getAction() + ", value=" + Arrays.deepToString(getValue()) + ")"; } @java.beans.ConstructorProperties({"action", "value"})
  public HoverEvent(Action action, BaseComponent[] value) { this.action = action;this.value = value;
  }
  

  public Action getAction() { return this.action; }
  public BaseComponent[] getValue() { return this.value; }
  

  public static enum Action
  {
    SHOW_TEXT, 
    SHOW_ACHIEVEMENT, 
    SHOW_ITEM, 
    SHOW_ENTITY;
    
    private Action() {}
  }
}
