package de.randomdrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;

public class main extends JavaPlugin implements Listener {

    public static HashMap Materials = new HashMap<Integer, Material>();

    static String PluginName = "RandomDrop";

    @Override
    public void onEnable () {

        try {
            if (!loadBlocks()) {
                shiftBlocks();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[" + PluginName + "] FATAL: " + e);
            shiftBlocks();
        }

        onCommand myExecutor = new onCommand(this);
        getCommand("drop").setExecutor(myExecutor);

        getServer().getPluginManager().registerEvents(this, this);

        register();

        Player p = null;

        System.out.println("The " + PluginName + " plugin was started successfully.");
    }

    public void onDisable () {
        System.out.println("The " + PluginName + " plugin was successfully terminated.");
    }

    /*
        This method loads the serialized HashMap and fills the "Materials" HashMap.
        It returns the success of the process. If the HashMap is zero or there is another error, the values are regenerated.
     */

    public boolean loadBlocks () throws IOException, ClassNotFoundException {

        //check if file exists
        File f = new File("MaterialsShift.ser");
        if (!f.exists()) {
            return false;
        }

        try {
            FileInputStream fis = new FileInputStream("MaterialsShift.ser");
            BukkitObjectInputStream ois = new BukkitObjectInputStream(fis);

            Materials = (HashMap) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[" + PluginName + "] Waring! " + e.toString());
            System.out.println("[" + PluginName + "] Look out! There was an error when reading the values of the mixed materials.");
            return false;
        }

        return Materials != null;
    }

    /*
       This method takes a Block object as an attribute and checks whether a drop has already been defined for the block (indicated by its hash value).
       If this is the case, the defined block is returned.If no drop has been defined, a new block is searched for until it can be dropped and has not yet been defined.
       Finally, the entire HashMap is saved again.
     */

    public static Material getRandomMaterial (Block b) {
        if (Materials.get(b.getBlockData().hashCode()) == null) {
            int i = 0;
            while ( Materials.get(b.getBlockData().hashCode()) == null ) {
                Material randomMaterial = Material.values()[(new Random()).nextInt(Material.values().length)];
                if (randomMaterial.isItem() && !Materials.containsValue(randomMaterial) || i>1200 && randomMaterial.isItem()){
                    Materials.put(b.getBlockData().hashCode(), randomMaterial);
                }
                i++;
            }

            try {
                FileOutputStream fos = new FileOutputStream("MaterialsShift.ser");
                BukkitObjectOutputStream oos = new BukkitObjectOutputStream(fos);

                oos.writeObject(Materials);

                oos.close();
                fos.close();
            } catch(IOException ioe) {
                System.out.println(ioe);
                System.out.println("Look out! There was an error when saving the values of the mixed materials.");
            }
        }

        return (Material) Materials.get(b.getBlockData().hashCode());
    }

    /*
        This method clears the HashMap with the hash values of the block types and materials and stores them so that new values are generated when the block is destroyed again.
     */

    public static void shiftBlocks () {

        Materials.clear();

        try {
            FileOutputStream fos = new FileOutputStream("MaterialsShift.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(Materials);

            oos.close();
            fos.close();
        } catch(IOException ioe) {
            System.out.println("Look out! There was an error when saving the values of the mixed materials.");
        }
    }

    /*
        If a block is destroyed, the following method is executed. It deactivates the event and drops another block at the position of the block.
     */

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        event.setDropItems(false);
        Location l = event.getBlock().getLocation();

        World w = event.getPlayer().getWorld();

        ItemStack i = new ItemStack(getRandomMaterial(event.getBlock()));
        i.setAmount(1);

        try {
            w.dropItemNaturally(l, i);
        } catch (Exception e) {
            System.out.println("[" + PluginName + "] FATAL: " + e);
            System.out.println("[" + PluginName + "] FATAL: The Material of the Type + " + getRandomMaterial(event.getBlock()) + " could not get dropped.");
        }
    }

    /*
        Opens a connection to my server to create statistics.
     */

    public static void register() {
        try {
            URL url = new URL("http://www.stats.konstantinmarx.de/randomdrop");
            URLConnection yc = url.openConnection();
            yc.connect();
        } catch (IOException e) {

        }
    }
}
