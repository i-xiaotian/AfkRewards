package com.tanxi521.afkrewards.listener;

import com.tanxi521.afkrewards.data.AfkData;
import com.tanxi521.afkrewards.entity.AfkMap;
import com.tanxi521.afkrewards.entity.PlayerAfk;
import com.tanxi521.afkrewards.util.BaseUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AfkListener implements Listener {

    public static Map<UUID, Float> playerSpeed = new HashMap<>();
    //服务器如果崩溃，再次启动时记录本时间作为所有在挂机的玩家的最后挂机结束时间
    public static long time = System.currentTimeMillis();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (AfkMap.hasPlayerIn(e.getPlayer().getName())) {
            float moveSpeed = e.getPlayer().getWalkSpeed();
            if (moveSpeed > 0.0001) {
                playerSpeed.put(player.getUniqueId(), moveSpeed);
                e.getPlayer().setWalkSpeed((float) 0.0001);
            }
            if (e.getFrom().distance(e.getTo()) != 0) {
                e.setCancelled(true);
            }
        } else {
            if (playerSpeed.containsKey(player.getUniqueId())) {
                e.getPlayer().setWalkSpeed(playerSpeed.get(player.getUniqueId()));
                playerSpeed.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (AfkMap.hasPlayerIn(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BaseUtils.getLang("afkwarning").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
        }
    }

    @EventHandler
    public void onHurt(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (AfkMap.hasPlayerIn(e.getEntity().getName())) {
                e.setCancelled(true);
                e.getEntity().sendMessage(BaseUtils.getLang("afkwarning").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
            }
        }
    }

    @EventHandler
    public void noBreak(BlockBreakEvent e) {
        if (AfkMap.hasPlayerIn(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BaseUtils.getLang("afkwarning").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
        }
    }

    @EventHandler
    public void noInventory(InventoryOpenEvent e) {
        if (AfkMap.hasPlayerIn(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BaseUtils.getLang("afkwarning").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
        }
    }

    @EventHandler
    public void noTeleport(PlayerTeleportEvent e) {
        if (AfkMap.hasPlayerIn(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BaseUtils.getLang("afkwarning").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        PlayerAfk playerData = AfkData.getPlayerData(player);
        if ((!playerData.getType().equals("未挂机")) && playerData.getEnd() == 0) {
            AfkData.setEnd(player, time);
        }
    }

    @EventHandler
    public void onExit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerAfk playerData = AfkData.getPlayerData(player);
        if ((!playerData.getType().equals("未挂机")) && playerData.getEnd() == 0) {
            AfkData.setEnd(player, System.currentTimeMillis());
        }
    }

}
