//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.pl3x.bukkit.shutdownnotice;

import net.pl3x.bukkit.shutdownnotice.api.ITitle;
import net.pl3x.bukkit.shutdownnotice.api.TitlePacket;
import net.pl3x.bukkit.shutdownnotice.api.TitleType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Title implements ITitle {
    private String text;
    private TitleType type;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title(String text) {
        this(TitleType.TITLE, text);
    }

    public Title(TitleType type, String text) {
        this(type, text, 20, 60, 20);
    }

    public Title(int faceIn, int stay, int fadeOut) {
        this(TitleType.TIMES, (String)null, faceIn, stay, fadeOut);
    }

    public Title(TitleType type, String text, int fadeIn, int stay, int fadeOut) {
        this.text = text;
        this.type = type;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TitleType getType() {
        return this.type;
    }

    public void setType(TitleType type) {
        this.type = type;
    }

    public int[] getTimes() {
        return new int[]{this.fadeIn, this.stay, this.fadeOut};
    }

    public void setTimes(int fadeIn, int stay, int fadeOut) {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void send(Player player) {
        TitlePacket packet = this.getPacket();
        if(packet != null) {
            packet.send(player);
        }
    }

    public void broadcast() {
        TitlePacket packet = this.getPacket();
        if(packet != null) {
            Bukkit.getOnlinePlayers().forEach(packet::send);
        }
    }

    private TitlePacket getPacket() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf(46) + 1);
        String path = this.getClass().getPackage().getName() + ".nms." + version;

        try {
            Class e = Class.forName(path + ".PacketHandler");
            if(TitlePacket.class.isAssignableFrom(e)) {
                return (TitlePacket)e.getConstructor(new Class[]{ITitle.class}).newInstance(new Object[]{this});
            }
        } catch (Exception var5) {
            Bukkit.getLogger().info("[ERROR] This plugin is not compatible with this server version (" + version + ").");
            Bukkit.getLogger().info("[ERROR] Could not send title packet!");
            var5.printStackTrace();
        }

        return null;
    }
}
