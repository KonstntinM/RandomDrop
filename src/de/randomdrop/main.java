package de.randomdrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;

public class main extends JavaPlugin implements Listener {

    public static HashMap Materials = new HashMap<Material, Material>();

    static String PluginName = "RandomDrop";
    static boolean dropItems;
    private onCommand myExecutor;

    @Override
    public void onEnable () {

        try {
            if (!loadBlocks()) {
                shiftBlocks();
            }
        } catch (IOException e) {
            e.printStackTrace();
            shiftBlocks();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            shiftBlocks();
        }

        loadPreferences();

        myExecutor = new onCommand(this);
        getCommand("drop").setExecutor(myExecutor);

        getServer().getPluginManager().registerEvents(this, this);

        register();

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
            ObjectInputStream ois = new ObjectInputStream(fis);

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

    public void loadPreferences () {
        File f = new File("RandomDrop.ser");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream("RandomDrop.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);

                dropItems = (boolean) ois.readObject();

                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[" + PluginName + "] Waring! " + e.toString());
                System.out.println("[" + PluginName + "] Look out! There was an error when reading your preferences.");
            }
        }
    }

    /*
        This method sets the Local, Temporary variable "dropItems" and serializes it to save the settings for the long term.
     */

    public static void setDropItems (boolean b) {

        dropItems = b;

        try {
            FileOutputStream fos = new FileOutputStream("RandomDrop.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dropItems);
            oos.close();
            fos.close();
        } catch(IOException ioe) {
            System.out.println("Look out! There was an error when saving your preferences.");
        }
    }

    /*
        When the method is executed, two arrays of all materials are created next. The second one is mixed. Now the not solid materials are getting deleted, because they can't get dropped.
        Then the HashMap "Materials" is filled with the values of the two arrays at the same places.
        Then the EventListener can use the block of the event as key to get the random block.
        At the end the HashMap is serialized and saved as a file so that the distribution of the materials remains the same when the server is restarted.
     */

    public static void shiftBlocks() {

        Material[] m = Material.values();
        Material[] ms  = Material.values();

        Random r = new Random();

        for (int i = 0; i < ms.length; i++) {
            if (!dropItems){
                if ( !ms[i].isBlock() || !ms[i].isSolid() || ms[i].isAir()) {
                    ms[i] = m[r.nextInt((ms.length))];
                    i--;
                }
            }
            else {
                if ( !ms[i].isItem() && !ms[i].isBlock() || ms[i].isAir()) {
                    ms[i] = m[r.nextInt((ms.length))];
                    i--;
                }
            }
        }

        for (int i = 0; i < ms.length; i++) {
            int randomIndexToSwap = r.nextInt(ms.length);
            Material temp = ms[randomIndexToSwap];
            ms[randomIndexToSwap] = ms[i];
            ms[i] = temp;
        }

        Materials.clear();

        for (int i = 0; i < m.length; i++) {
            Materials.put(m[i], ms[i]);
        }

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

        ItemStack i = new ItemStack((Material) Materials.get(event.getBlock().getType()));
        i.setAmount(1);

        try {
            w.dropItemNaturally(l, i);
        } catch (Exception e) {
            i = new ItemStack(Material.REDSTONE_ORE);
            i.setAmount(1);

            w.dropItemNaturally(l, i);
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
