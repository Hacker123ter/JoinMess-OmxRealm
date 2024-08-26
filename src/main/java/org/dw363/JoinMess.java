package org.dw363;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinMess extends JavaPlugin implements Listener {

    private String joinMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        joinMessage = getConfig().getString("join");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        joinMessage = getConfig().getString("join");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("omxrealm.joinserver")) {
            String message = PlaceholderAPI.setPlaceholders(player, joinMessage)
                    .replace("{player}", player.getName());
            message = translateHexColorCodes(message);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(null);
        }
    }

    private String translateHexColorCodes(String message) {
        StringBuilder builder = new StringBuilder();
        char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 7 < chars.length && chars[i + 1] == '#' &&
                    isHexChar(chars[i + 2]) && isHexChar(chars[i + 3]) &&
                    isHexChar(chars[i + 4]) && isHexChar(chars[i + 5]) &&
                    isHexChar(chars[i + 6]) && isHexChar(chars[i + 7])) {
                builder.append("§x")
                        .append('§').append(chars[i + 2])
                        .append('§').append(chars[i + 3])
                        .append('§').append(chars[i + 4])
                        .append('§').append(chars[i + 5])
                        .append('§').append(chars[i + 6])
                        .append('§').append(chars[i + 7]);
                i += 7;
            } else {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    private boolean isHexChar(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }
}