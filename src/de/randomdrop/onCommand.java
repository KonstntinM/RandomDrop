package de.randomdrop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class onCommand implements CommandExecutor {

    private main plugin;

    public onCommand(main plugin) {
        this.plugin = plugin;
    }

    /*
        This method is executed when a player executes one of the plugin commands. It checks the arguments from the 'args' array and executes the "shiftBlocks()" method.
     */

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

        //Befehl für Countdown
        if(cmd.getName().equalsIgnoreCase("drop")){
            if (args == null || args.length == 0){
                sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] You have to give an argument! Look at the description on https://github.com/KonstntinM/RandomDrop .");
            }
            else if (args[0].contentEquals("reload")){
                main.shiftBlocks();
                sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] The materials were successfully mixed.");
                System.out.println("[" + main.PluginName + "] The materials were successfully mixed.");
                return true;
            }
            else if (args[0].contentEquals("items")){
                if (args[1] == null) {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] You have to give an argument! Look at the description on https://github.com/KonstntinM/RandomDrop .");
                }
                else if (args[1].contentEquals("drop")){
                    main.setDropItems(true);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] Okay, from now on, items are dropped. To apply the settings execute the \"/drop reload\" command.");
                }

                else if (args[1].contentEquals("disabled")){
                    main.setDropItems(false);
                    sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] Okay, from now on, items aren't dropped. To apply the settings execute the \"/drop reload\" command.");
                }
                else {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] Whoops! There was a problem. Look at the description on https://github.com/KonstntinM/RandomDrop .");
                }

                return true;
            }
            else {
                sender.sendMessage(ChatColor.DARK_PURPLE + "[" + main.PluginName + "] Whoops! There was a problem. Look at the description on https://github.com/KonstntinM/RandomDrop .");
            }
        }

        return false;
    }
}