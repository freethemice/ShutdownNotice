//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.pl3x.bukkit.shutdownnotice.command;

import net.pl3x.bukkit.shutdownnotice.Chat;
import net.pl3x.bukkit.shutdownnotice.ServerStatus;
import net.pl3x.bukkit.shutdownnotice.ServerStatus.State;
import net.pl3x.bukkit.shutdownnotice.ShutdownNotice;
import net.pl3x.bukkit.shutdownnotice.configuration.Config;
import net.pl3x.bukkit.shutdownnotice.configuration.Lang;
import net.pl3x.bukkit.shutdownnotice.task.Countdown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdShutdown implements TabExecutor {
    private final ShutdownNotice plugin;

    public CmdShutdown(ShutdownNotice plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList list = new ArrayList();
        if(args.length == 0) {
            list.addAll((Collection)Stream.of(new String[]{"cancel", "abort", "stop", "off", "0", "version", "reload"}).filter((possible) -> {
                return possible.startsWith(args[0].toLowerCase());
            }).collect(Collectors.toList()));
        }

        return list;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("command.shutdown")) {
            (new Chat(Lang.COMMAND_NO_PERMS)).send(sender);
            return true;
        } else if(args.length < 1) {
            (new Chat(Lang.COMMAND_MISSING_ARGS)).send(sender);
            return false;
        } else {
            ServerStatus status = ServerStatus.getStatus();
            State state = status.getState();
            if(!args[0].equalsIgnoreCase("cancel") && !args[0].equalsIgnoreCase("abort") && !args[0].equalsIgnoreCase("stop") && !args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("0")) {
                if(args[0].equalsIgnoreCase("version")) {
                    (new Chat(Lang.VERSION.replace("{plugin}", this.plugin.getName()).replace("{version}", this.plugin.getDescription().getVersion()))).send(sender);
                    return true;
                } else if(args[0].equalsIgnoreCase("reload")) {
                    if(!sender.hasPermission("command.shutdown.reload")) {
                        (new Chat(Lang.COMMAND_NO_PERMS)).send(sender);
                        return true;
                    } else {
                        Config.reload();
                        Lang.reload(true);
                        status.setStatus(State.RUNNING, (Long)null, (String)null);
                        (new Chat(Lang.RELOAD.replace("{plugin}", this.plugin.getName()).replace("{version}", this.plugin.getDescription().getVersion()))).send(sender);
                        return true;
                    }
                } else if(!state.equals(State.RUNNING)) {
                    (new Chat(Lang.PROCESS_ALREADY_IN_ACTION)).send(sender);
                    return true;
                } else {
                    int var13;
                    try {
                        var13 = Integer.valueOf(args[0]).intValue();
                    } catch (NumberFormatException var12) {
                        (new Chat(Lang.INVALID_TIME)).send(sender);
                        return true;
                    }

                    if(var13 <= 0) {
                        (new Chat(Lang.TIME_NOT_POSITIVE)).send(sender);
                        return true;
                    } else {
                        State newState;
                        if(label.equalsIgnoreCase("shutdown")) {
                            newState = State.SHUTDOWN;
                        } else if(label.equalsIgnoreCase("restart")) {
                            newState = State.RESTART;
                        } else {
                            if(!label.equalsIgnoreCase("reboot")) {
                                (new Chat(Lang.UNKNOWN_COMMAND)).send(sender);
                                return true;
                            }

                            newState = State.RESTART;
                        }

                        String reason = null;
                        if(args.length > 1) {
                            StringBuilder sb = new StringBuilder();

                            for(int i = 1; i < args.length; ++i) {
                                sb.append(args[i]).append(" ");
                            }

                            reason = sb.toString().trim();
                        }
                        Long timeleft = System.currentTimeMillis() + Long.valueOf(var13) * 1000;
                        status.setStatus(newState, timeleft, reason);
                        (new Countdown(this.plugin)).runTaskTimer(this.plugin, 0L, 20L);
                        return true;
                    }
                }
            } else {
                String time = "";
                switch(state.ordinal()) {
                    case 1:
                        (new Chat(Lang.NOTHING_TO_CANCEL)).send(sender);
                        return true;
                    case 2:
                        time = Lang.SHUTDOWN_CANCELLED.toString();
                        break;
                    case 3:
                        time = Lang.RESTART_CANCELLED.toString();
                }

                (new Chat(time)).broadcast();
                if(this.plugin.getBotHook() != null) {
                    this.plugin.getBotHook().sendToDiscord("*" + time.trim() + "*");
                }

                status.setStatus(State.RUNNING, (Long)null, (String)null);
                return true;
            }
        }
    }
}
