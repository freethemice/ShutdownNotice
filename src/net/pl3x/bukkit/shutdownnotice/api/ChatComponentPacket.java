//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.pl3x.bukkit.shutdownnotice.api;

import net.pl3x.bukkit.shutdownnotice.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public interface ChatComponentPacket {
    void sendMessage(Player var1, net.minecraft.server.v1_12_R1.ChatMessageType var2, BaseComponent... var3);
}
