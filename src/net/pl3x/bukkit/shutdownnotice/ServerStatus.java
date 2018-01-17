package net.pl3x.bukkit.shutdownnotice;

public class ServerStatus { private static ServerStatus status;
  
  public static enum State { RUNNING, 
    SHUTDOWN, 
    RESTART;
    
    private State() {}
  }
  
  public static ServerStatus getStatus() {
    if (status == null) {
      status = new ServerStatus();
      status.setStatus(State.RUNNING, null, null);
    }
    return status;
  }
  
  private State state;
  private Long timeLeft;
  private String reason;
  public void setStatus(State state, Long timeLeft, String reason)
  {
    this.state = state;
    this.timeLeft = timeLeft;
    this.reason = reason;
  }
  
  public State getState() {
    return this.state;
  }
  
  public Long getTimeLeft() {
    return this.timeLeft;
  }
  
  public String getReason() {
    return this.reason;
  }
}
