package com.cookietom;

import com.cookietom.commands.ForceReport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  private static Main instance;

  public static Main getInstance() {
    return instance;
  }


  @Override
  public void onEnable() {

    instance = this;

    for (Player all : Bukkit.getServer().getOnlinePlayers()) {
      if (all.isOp()) {

        all.sendMessage("§4(Debug) Reload Detected!");
        all.sendMessage("§4(Debug) §aAttempting to enable " +
            "Core" +
            " plugin...");
        all.sendMessage("§4(Debug) §aCore plugin enabled.");
      }
    }

    PluginManager pm = Bukkit.getServer().getPluginManager();

    pm.registerEvents(new ForceReport(), this);

    getServer().getConsoleSender()
        .sendMessage(ChatColor.GREEN + "\n\n[Reports] Plugin are active!\n");

    Bukkit.getPluginCommand("forcereport").setExecutor((new ForceReport()));

  }

  @Override
  public void onDisable() {
    for (Player all : Bukkit.getServer().getOnlinePlayers()) {
      if (all.isOp()) {

        all.sendMessage("§4(Debug) Reload Detected!");
        all.sendMessage("§4(Debug) §cAttempting to disable " +
            "Core" +
            " plugin...");
        all.sendMessage("§4(Debug) §cCore plugin disabled.");
      }
    }

    getServer().getConsoleSender()
        .sendMessage(ChatColor.GREEN + "\n\n[Reports] Plugin is inactive! " +
            "(like yite)\n");
  }
}
