<img alt="random drop logo" src="https://i.imgur.com/Ujzf5Mv.png">

# RandomDrop
Minecraft Bukkit plugin that drops a different block when a block is removed.

## Installation
To use this plugin on your Minecraft server you have to follow these steps.

1. download the .jar file of the plugin [here](https://github.com/KonstntinM/RandomDrop/releases)
2. open the directory of your server, open the "plugins" folder and copy the .jar file to it
3. restart your server

## Minecraft Commands
- /drop reload
   - *Remixes the connections between the real and the dropped block.*
   - *available for Op's*

## Short code description
The plugin listens to the *BlockBreakEvent*. When a block is destroyed, the EventListener accesses the event object and executes the method *setDropItems*, thus deactivating the regular dropping of an item. Then the method "getRandomMaterial()" is called and checks whether a material has already been defined. If this is not the case, a new one is defined. In the end this material is dropped at the place of the destroyed block.

<ins>Feel free to have a look into the code, I have commented a relatively detailed description for most of the methods.</ins>

## The License
You may download this plugin or source code, modify it and become part of your Minecraft project. You must always mention me and this website as the author in a clearly visible place. It is strictly prohibited to use this plugin commercially or resell it, even if modified, without prior agreement.

### Questions?
If you have further questions, ideas or wishes, just contact me via the *issues* feature of the project.

### About the libraries used
I used the [__bukkit library__](https://dev.bukkit.org/) of version *1.15.2* to develop the plugin. I also used the [__Java SDK 11__](https://www.java.com/en/download/).

----
Made with ❤️ Bukkit

<img width="50" alt="bukkit logo" src="http://i.imgur.com/igYbvzR.png">
