package me.badbones69.crazyenvoy.controllers;

import me.badbones69.crazyenvoy.Methods;
import me.badbones69.crazyenvoy.api.CrazyEnvoy;
import me.badbones69.crazyenvoy.api.enums.Messages;
import me.badbones69.crazyenvoy.api.events.EnvoyStartEvent;
import me.badbones69.crazyenvoy.api.events.EnvoyStartEvent.EnvoyStartReason;
import me.badbones69.crazyenvoy.api.objects.EnvoySettings;
import me.badbones69.crazyenvoy.api.objects.Flare;
import me.badbones69.crazyenvoy.multisupport.Support;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class FlareControl implements Listener {
    
    private CrazyEnvoy envoy = CrazyEnvoy.getInstance();
    private EnvoySettings envoySettings = EnvoySettings.getInstance();
    
    @EventHandler
    public void onFlareActivate(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack flare = Methods.getItemInHand(player);
            if (flare != null && Flare.isFlare(flare)) {
                e.setCancelled(true);
                if (player.hasPermission("envoy.flare.use")) {
                    if (envoy.isEnvoyActive()) {
                        Messages.ALREADY_STARTED.sendMessage(player);
                    } else {
                        int online = Bukkit.getServer().getOnlinePlayers().size();
                        if (envoySettings.isMinPlayersEnabled() && envoySettings.isMinFlareEnabled() && online < envoySettings.getMinPlayers()) {
                            HashMap<String, String> placeholder = new HashMap<>();
                            placeholder.put("%amount%", online + "");
                            placeholder.put("%Amount%", online + "");
                            Messages.NOT_ENOUGH_PLAYERS.sendMessage(player, placeholder);
                            return;
                        }
                        boolean toggle = false;
                        if (Support.WORLD_EDIT.isPluginLoaded() && Support.WORLD_GUARD.isPluginLoaded()) {
                            if (envoySettings.isWorldMessagesEnabled()) {
                                for (String region : envoySettings.getFlaresRegions()) {
                                    if (envoy.getWorldGuardSupport().inRegion(region, player.getLocation())) {
                                        toggle = true;
                                    }
                                }
                            } else {
                                toggle = true;
                            }
                        } else {
                            toggle = true;
                        }
                        if (!toggle) {
                            Messages.NOT_IN_WORLD_GUARD_REGION.sendMessage(player);
                            return;
                        }
                        EnvoyStartEvent event = new EnvoyStartEvent(EnvoyStartReason.FLARE);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled() && envoy.startEnvoyEvent()) {
                            Messages.USED_FLARE.sendMessage(player);
                            Flare.takeFlare(player);
                        }
                        envoy.getHologramController().updateHolograms();
                    }
                } else {
                    Messages.CANT_USE_FLARES.sendMessage(player);
                }
            }
        }
    }
    
}