package me.badbones69.crazyenvoy.api.events;

import me.badbones69.crazyenvoy.api.objects.Prize;
import me.badbones69.crazyenvoy.api.objects.Tier;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class OpenCaptchaEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Location location;
    private Block block;
    private Player player;
    private Tier tier;
    private List<Prize> prizes;

    public OpenCaptchaEvent(Player player, Block block, Tier tier, List<Prize> prizes) {
        this(player, block.getLocation(), tier, prizes);
    }

    public OpenCaptchaEvent(Player player, Location location, Tier tier, List<Prize> prizes) {
        this.player = player;
        this.location = location;
        this.block = location.getBlock();
        this.tier = tier;
        this.prizes = prizes;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }


}
