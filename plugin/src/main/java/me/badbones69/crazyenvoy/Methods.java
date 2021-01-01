package me.badbones69.crazyenvoy;

import me.badbones69.crazyenvoy.api.CrazyEnvoy;
import me.badbones69.crazyenvoy.api.FileManager.Files;
import me.badbones69.crazyenvoy.controllers.FireworkDamageAPI;
import me.badbones69.crazyenvoy.multisupport.Version;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Methods {
    
    private static CrazyEnvoy envoy = CrazyEnvoy.getInstance();
    private static Random random = new Random();
    
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public static String removeColor(String msg) {
        return ChatColor.stripColor(msg);
    }
    
    public static String getPrefix() {
        return color(Files.CONFIG.getFile().getString("Settings.Prefix"));
    }
    
    public static String getPrefix(String message) {
        return color(Files.CONFIG.getFile().getString("Settings.Prefix") + message);
    }
    
    @SuppressWarnings({"deprecation", "squid:CallToDeprecatedMethod"})
    public static ItemStack getItemInHand(Player player) {
        if (Version.isNewer(Version.v1_8_R3)) {
            return player.getInventory().getItemInMainHand();
        } else {
            return player.getItemInHand();
        }
    }
    
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static boolean isOnline(String name) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public static Player getPlayer(String name) {
        return Bukkit.getServer().getPlayer(name);
    }
    
    public static boolean isInvFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }
    
    public static Entity fireWork(Location loc, List<Color> colors) {
        Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffects(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(colors).trail(false).flicker(false).build());
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
        FireworkDamageAPI.addFirework(firework);
        detonate(firework);
        return firework;
    }
    
    private static void detonate(final Firework f) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(envoy.getPlugin(), f :: detonate, 2);
    }
    
    public static Color getColor(String color) {
        if (color != null) {
            switch (color.toUpperCase()) {
                case "AQUA":
                    return Color.AQUA;
                case "BLACK":
                    return Color.BLACK;
                case "BLUE":
                    return Color.BLUE;
                case "FUCHSIA":
                    return Color.FUCHSIA;
                case "GRAY":
                    return Color.GRAY;
                case "GREEN":
                    return Color.GREEN;
                case "LIME":
                    return Color.LIME;
                case "MAROON":
                    return Color.MAROON;
                case "NAVY":
                    return Color.NAVY;
                case "OLIVE":
                    return Color.OLIVE;
                case "ORANGE":
                    return Color.ORANGE;
                case "PURPLE":
                    return Color.PURPLE;
                case "RED":
                    return Color.RED;
                case "SILVER":
                    return Color.SILVER;
                case "TEAL":
                    return Color.TEAL;
                case "WHITE":
                    return Color.WHITE;
                case "YELLOW":
                    return Color.YELLOW;
            }
            try {
                String[] rgb = color.split(",");
                return Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            } catch (Exception ignore) {
            }
        }
        return Color.WHITE;
    }
    
    public static List<String> getPage(List<String> list, Integer page) {
        List<String> locations = new ArrayList<>();
        if (page <= 0) {
            page = 1;
        }
        int max = 10;
        int index = page * max - max;
        int endIndex = index >= list.size() ? list.size() - 1 : index + max;
        for (; index < endIndex; index++) {
            if (index < list.size()) {
                locations.add(list.get(index));
            }
        }
        for (; locations.isEmpty(); page--) {
            if (page <= 0) {
                break;
            }
            index = page * max - max;
            endIndex = index >= list.size() ? list.size() - 1 : index + max;
            for (; index < endIndex; index++) {
                if (index < list.size()) {
                    locations.add(list.get(index));
                }
            }
        }
        return locations;
    }
    
    public static boolean isSuccessful(int min, int max) {
        if (max <= min || max <= 0) {
            return true;
        }
        int chance = 1 + random.nextInt(max);
        return chance >= 1 && chance <= min;
    }
    
    public static void hasUpdate() {
        try {
            HttpURLConnection c = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=32870").getBytes(StandardCharsets.UTF_8));
            String oldVersion = envoy.getPlugin().getDescription().getVersion();
            String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine().replace("[a-zA-Z ]", "");
            if (!newVersion.equals(oldVersion)) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + color("&cYour server is running &7v" + oldVersion + "&c and the newest is &7v" + newVersion + "&c."));
            }
        } catch (Exception e) {
        }
    }
    
    public static void hasUpdate(Player player) {
        try {
            HttpURLConnection c = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=32870").getBytes(StandardCharsets.UTF_8));
            String oldVersion = envoy.getPlugin().getDescription().getVersion();
            String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine().replace("[a-zA-Z ]", "");
            if (!newVersion.equals(oldVersion)) {
                player.sendMessage(getPrefix() + color("&cYour server is running &7v" + oldVersion + "&c and the newest is &7v" + newVersion + "&c."));
            }
        } catch (Exception e) {
        }
    }
    
    @SuppressWarnings({"deprecation", "squid:CallToDeprecatedMethod"})
    public static List<Entity> getNearbyEntities(Location loc, double x, double y, double z) {
        List<Entity> out = new ArrayList<>();
        if (loc.getWorld() != null) {
            FallingBlock ent = loc.getWorld().spawnFallingBlock(loc.subtract(0, 0, 0), Material.AIR, (byte) 0);
            out = ent.getNearbyEntities(x, y, z);
            ent.remove();
        }
        return out;
    }

    public static String capitalizeFully(String name) {
        if (name != null) {
            if (name.length() > 1) {
                if (name.contains("_")) {
                    StringBuilder sbName = new StringBuilder();
                    for (String subName : name.split("_"))
                        sbName.append(subName.substring(0, 1).toUpperCase() + subName.substring(1).toLowerCase())
                                .append(" ");
                    return sbName.toString().substring(0, sbName.length() - 1);
                } else {
                    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                }
            } else {
                return name.toUpperCase();
            }
        } else {
            return "";
        }
    }

    public static String getFriendlyName(Material material) {
        return material == null ? "Air" : getFriendlyName(new ItemStack(material), false);
    }

    private static Class<?> localeClass = null;
    private static Class<?> craftItemStackClass = null, nmsItemStackClass = null, nmsItemClass = null;
    private static String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
    private static String NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");

    public static String getFriendlyName(ItemStack itemStack, boolean checkDisplayName) {
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return "Air";
        try {
            if (craftItemStackClass == null)
                craftItemStackClass = Class.forName(OBC_PREFIX + ".inventory.CraftItemStack");
            Method nmsCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);

            if (nmsItemStackClass == null)
                nmsItemStackClass = Class.forName(NMS_PREFIX + ".ItemStack");
            Object nmsItemStack = nmsCopyMethod.invoke(null, itemStack);

            Object itemName = null;
            if (checkDisplayName) {
                Method getNameMethod = nmsItemStackClass.getMethod("getName");
                itemName = getNameMethod.invoke(nmsItemStack);
            } else {
                Method getItemMethod = nmsItemStackClass.getMethod("getItem");
                Object nmsItem = getItemMethod.invoke(nmsItemStack);

                if (nmsItemClass == null)
                    nmsItemClass = Class.forName(NMS_PREFIX + ".Item");

                Method getNameMethod = nmsItemClass.getMethod("getName");
                Object localItemName = getNameMethod.invoke(nmsItem);

                if (localeClass == null)
                    localeClass = Class.forName(NMS_PREFIX + ".LocaleI18n");
                Method getLocaleMethod = localeClass.getMethod("get", String.class);

                Object localeString = localItemName == null ? "" : getLocaleMethod.invoke(null, localItemName);
                itemName = ("" + getLocaleMethod.invoke(null, localeString.toString() + ".name")).trim();
            }
            return itemName != null ? itemName.toString()
                    : capitalizeFully(itemStack.getType().name().replace("_", " ").toLowerCase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return capitalizeFully(itemStack.getType().name().replace("_", " ").toLowerCase());
    }
    
}