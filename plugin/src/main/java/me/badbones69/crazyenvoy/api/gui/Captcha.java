package me.badbones69.crazyenvoy.api.gui;

import me.badbones69.crazyenvoy.Methods;
import me.badbones69.crazyenvoy.api.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Captcha {

    private List<Material> materialList = getMaterials();

    public Inventory createCaptchaInventory() {

        if (materialList.isEmpty())
            getMaterials();
        List<Material> usedMaterials = new ArrayList<>();

        Random random = new Random();

        Collections.shuffle(materialList);

        Material correctItem = materialList.get(random.nextInt(27));

        Inventory inv = Bukkit.createInventory(null, 27, Methods.color("&4Click: " + Methods.getFriendlyName(correctItem)));

        int count = 0;
        for (Material m : materialList) {
            if (count <27) {
                ItemStack item = new ItemStack(m, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Methods.color("&f" + Methods.getFriendlyName(m)));
                item.setItemMeta(meta);
                inv.setItem(count, item);
                count++;
            } else
                break;
        }

        return inv;
    }

    private ArrayList<Material> getMaterials() {

        ArrayList<Material> materials = new ArrayList<>();

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        for (String line : config.getStringList("captchaBlocks")) {
            Material material = Material.getMaterial(line);
            if (material != null)
                materials.add(material);
        }

        return materials;

    }
}
