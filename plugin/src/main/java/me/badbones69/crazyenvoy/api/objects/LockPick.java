package me.badbones69.crazyenvoy.api.objects;

import me.badbones69.crazyenvoy.Methods;
import me.badbones69.crazyenvoy.api.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LockPick {

    private static ItemBuilder lockItemBuilder;

    public static void load() {
        FileConfiguration config = FileManager.Files.CONFIG.getFile();
        lockItemBuilder = new ItemBuilder()
                .setMaterial(config.getString("Settings.Lock-Pick.Item"))
                .setName(config.getString("Settings.Lock-Pick.Name"))
                .setLore(config.getStringList("Settings.Lock-Pick.Lore"));
    }

    public static ItemStack givePick() {
        return givePick(1);
    }

    public static ItemStack givePick(int amount) {
        return lockItemBuilder.setAmount(amount).build();
    }

    public static boolean isPick(ItemStack item) {
        return givePick().isSimilar(item);
    }

    public static void givePick(Player player) {
        givePick(player, 1);
    }

    public static void givePick(Player player, int amount) {
        if (Methods.isInvFull(player)) {
            player.getWorld().dropItem(player.getLocation(), givePick(amount));
        } else {
            player.getInventory().addItem(givePick(amount));
        }
    }

    public static void takePick(Player player) {
        player.getInventory().removeItem(givePick());
    }


}
