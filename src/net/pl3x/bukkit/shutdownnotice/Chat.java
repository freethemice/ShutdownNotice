//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.pl3x.bukkit.shutdownnotice;

import net.pl3x.bukkit.shutdownnotice.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Chat {
    private final String message;

    public Chat(Lang lang) {
        this(lang.toString());
    }

    public Chat(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public void send(CommandSender recipient) {
        if(this.message != null && !ChatColor.stripColor(this.message).isEmpty()) {
            String[] var2 = this.message.split("\n");
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String part = var2[var4];
                recipient.sendMessage(part);
            }

        }
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(this::send);
        this.send(Bukkit.getConsoleSender());
    }
}
