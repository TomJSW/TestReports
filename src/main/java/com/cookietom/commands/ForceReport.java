package com.cookietom.commands;

import com.cookietom.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ForceReport implements Listener, CommandExecutor {

  private static List<String> DOINGREPORT = new ArrayList<>();

  // THIS IS A HASHMAP
  private static Map<String, String> STAFFANDTARGET = new HashMap<>();


  @Override
  public boolean onCommand(CommandSender sender, Command cmd,
                           String label, String[] args) {
    if (sender instanceof Player) {
      if (cmd.getName().equalsIgnoreCase("forcereport")) {
        if (sender.isOp()) {
          if (args.length == 0) {
            sender.sendMessage("idot mention a player");
          } else {
            if (Bukkit.getPlayer(args[0]) != null) {
              Player p = (Player) sender;
              p.sendMessage(ChatColor.GREEN + "Force-adding " + args[0] +
                  " " +
                  "to report queue" +
                  "...");

              STAFFANDTARGET.put(sender.getName(), args[0]);

              // CREATES DELAY BEFORE ENTERING REPORT QUEUE ON SPECIFIED PLAYER
              Main.getInstance().getServer().getScheduler().runTaskLater(
                  Main.getInstance(), () -> {


                    DOINGREPORT.add(p.getName());
                    p.getInventory().clear();

                    p.teleport(Bukkit.getPlayer(args[0]));

                    p.getInventory()
                        .setItem(4, new ItemStack(Material.NETHER_STAR));

                  }, 60);
            } else {
              sender.sendMessage(ChatColor.RED + "Sorry, but you can only " +
                  "forcereport on online players.");
            }
          }
        }
      }
    }
    return true;
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    if (DOINGREPORT.contains(e.getWhoClicked().getName())) {
      Player p = (Player) e.getWhoClicked();
      if (e.getInventory().getName()
          .equals(e.getWhoClicked().getInventory().getName())) {
        e.setCancelled(true);
      } else if (e.getInventory().getName().equals("§4Verdict System")) {
        e.setCancelled(true);
        if (e.getSlot() == 11) {
          e.getView().close();
          p.sendMessage("§8| §bStaff §8| §7You have marked report against "
              + STAFFANDTARGET.get(e.getWhoClicked().getName()) +
              " §7as" +
              " §aAccepted.");
          e.getInventory().clear();
          STAFFANDTARGET.remove(e.getWhoClicked().getName());
        } else if (e.getSlot() == 15) {
          e.getView().close();
          p.sendMessage("§8| §bStaff §8| §7You have marked report against "
              + STAFFANDTARGET.get(e.getWhoClicked().getName()) +
              " §7as" +
              " §cDenied.");
          STAFFANDTARGET.remove(e.getWhoClicked().getName());
          e.getInventory().clear();
        }
      }
    }
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent e) {
    if (DOINGREPORT.contains(e.getPlayer().getName())) {
      Bukkit.broadcastMessage("eee");
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    if (DOINGREPORT.contains(e.getPlayer().getName())) {
      if (e.getAction() == Action.RIGHT_CLICK_AIR) {
        if (e.getPlayer().getItemInHand()
            .equals(new ItemStack(Material.NETHER_STAR))) {
          e.getPlayer().sendMessage("Opening report GUI");

          Player p = e.getPlayer();
          Inventory punishGUI;
          punishGUI = Bukkit.getServer().createInventory(p, 27,
              "§4Verdict System");


          ItemStack ref2 = new ItemStack(Material.GREEN_RECORD);
          ItemMeta metaref2 = ref2.getItemMeta();
          List<String> lore2 = new ArrayList<>();
          lore2.add("§aClick to accept this report.");
          assert metaref2 != null;
          metaref2.setLore(lore2);
          metaref2.setDisplayName("§aAccept");
          ref2.setItemMeta(metaref2);
          punishGUI.setItem(11, ref2);

          ItemStack ref3 = new ItemStack(Material.GREEN_RECORD);
          ItemMeta metaref3 = ref3.getItemMeta();
          List<String> lore3 = new ArrayList<>();
          lore3.add("§cClick to deny this report.");
          assert metaref3 != null;
          metaref3.setLore(lore3);
          metaref3.setDisplayName("§cDeny");
          ref3.setItemMeta(metaref3);
          punishGUI.setItem(15, ref3);

          p.openInventory(punishGUI);
        }
      }
    }
  }
}
