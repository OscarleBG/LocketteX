package pro.dracarys.LocketteX;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pro.dracarys.LocketteX.commands.MainCommand;
import pro.dracarys.LocketteX.config.Config;
import pro.dracarys.LocketteX.config.file.ConfigFile;
import pro.dracarys.LocketteX.config.file.MessageFile;
import pro.dracarys.LocketteX.hooks.HookManager;
import pro.dracarys.LocketteX.hooks.claim.ClaimPlugin;
import pro.dracarys.LocketteX.listener.*;
import pro.dracarys.LocketteX.utils.Util;
import pro.dracarys.configlib.ConfigLib;

import java.util.logging.Level;

public class LocketteX extends JavaPlugin {

    public static LocketteX plugin;

    public static LocketteX getInstance() {
        return plugin;
    }

    private ClaimPlugin claimPlugin;

    public ClaimPlugin getClaimPlugin() {
        return claimPlugin;
    }

    private HookManager hookManager;

    public HookManager getHookManager() {
        return hookManager;
    }

    @Override
    public void onEnable() {
        try {
            int pluginId = 7307;
            new Metrics(this, pluginId);
        } catch (Exception ex) {
            getServer().getLogger().log(Level.SEVERE, "Error while trying to register Metrics (bStats)");
        }
        plugin = this;
        initConfig();
        loadConfig();
        checkServerVersion();
        PluginCommand cmd = this.getCommand("lockettex");
        MainCommand executor = new MainCommand();
        if (cmd != null) {
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }
        printPluginInfo();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            hookManager = new HookManager(this);
            claimPlugin = new ClaimPlugin().setup(this);
            registerListeners(new InventoryOpen(), new BlockBreak(), new BlockPlace(), new SignChange(), new Explosions());
            if (Config.USE_INV_MOVE.getOption()) registerListeners(new InventoryMoveItem());
        }, 1);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        plugin = null;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    // Tell IntelliJ to not format this, by enabling formatter markers in comments (Pref-> Editor-> Code Style)
    // Made this way for easy editing/char replacing, using equal size chars for all consoles compatibility.
    //@formatter:off
    private void printPluginInfo() {
        Util.sendConsole(("\n" +
                " ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬛⬛⬛⬜⬜⬜⬜⬜⬛⬛⬛\n" +
                " ⬜⬛⬜⬜⬜⬜⬜⬛⬛⬛⬜⬛⬛⬛⬛⬜⬛⬜⬜⬛⬜⬛⬛⬛⬛⬛⬜⬛⬛⬛⬛⬛⬜⬛⬛⬛⬛⬛⬜⬛⬛⬛⬛⬛⬜⬜⬜⬛⬛⬜⬜⬜⬛⬛⬜⬜\n" +
                " ⬜⬛⬜⬜⬜⬜⬛⬛⬜⬛⬜⬛⬜⬛⬛⬜⬛⬜⬛⬛⬜⬛⬜⬜⬜⬛⬜⬜⬜⬛⬜⬛⬜⬜⬜⬛⬜⬛⬜⬛⬜⬜⬜⬛⬜⬜⬜⬜⬛⬛⬜⬛⬛⬜⬜⬜\n" +
                " ⬜⬛⬜⬜⬜⬜⬛⬜⬜⬛⬜⬛⬜⬜⬛⬜⬛⬛⬜⬛⬜⬛⬛⬛⬜⬜⬜⬜⬜⬛⬜⬜⬜⬜⬜⬛⬜⬜⬜⬛⬛⬛⬜⬜⬜⬜⬜⬜⬜⬛⬛⬛⬜⬜⬜⬜\n" +
                " ⬜⬛⬜⬜⬜⬜⬛⬜⬜⬛⬜⬛⬜⬜⬜⬜⬛⬛⬜⬜⬜⬛⬜⬜⬜⬜⬜⬜⬜⬛⬜⬜⬜⬜⬜⬛⬜⬜⬜⬛⬜⬜⬜⬜⬜⬜⬜⬜⬜⬛⬛⬛⬜⬜⬜⬜\n" +
                " ⬜⬛⬜⬜⬛⬜⬛⬜⬛⬛⬜⬛⬜⬜⬛⬜⬛⬜⬛⬛⬜⬛⬜⬜⬛⬛⬜⬜⬜⬛⬜⬜⬜⬜⬜⬛⬜⬜⬜⬛⬜⬜⬛⬛⬜⬜⬜⬜⬛⬛⬜⬛⬛⬜⬜⬜\n" +
                " ⬜⬛⬛⬛⬛⬜⬛⬛⬛⬜⬜⬛⬛⬛⬛⬜⬛⬜⬜⬛⬜⬛⬛⬛⬛⬜⬜⬜⬜⬛⬜⬜⬜⬜⬜⬛⬜⬜⬜⬛⬛⬛⬛⬜⬜⬜⬜⬛⬛⬜⬜⬜⬛⬛⬜⬜\n" +
                " ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬛⬛⬛⬜⬜⬜⬜⬜⬛⬛⬛\n"
        ).replace("⬜", "&0█").replace("⬛", "&f█") + "\n" +
                " &f-->  &c" + getDescription().getName() + " &7v" + getDescription().getVersion() + "&a Enabled" + "\n" +
                " &f-->  &f&o" + getDescription().getDescription() + "\n" +
                " &f-->  &eMade with &4♥ &eby &f" + getDescription().getAuthors().get(0) + "\n");
        if (getDescription().getVersion().contains("-DEV"))
            Util.sendConsole("&f&l[!] &cThis is a BETA, report any unexpected behaviour to the Author!" + "\n");
    }
    //@formatter:on

    private void initConfig() {
        ConfigLib.setPlugin(this);
        ConfigLib.addFile(new ConfigFile());
        ConfigLib.addFile(new MessageFile());
    }

    public void loadConfig() {
        ConfigLib.initAll();
    }

    private static int ver;

    private void checkServerVersion() {
        ver = Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].replace("1_", "").substring(1).replaceAll("_R\\d", ""));
    }

    public static int getServerVersion() {
        return ver;
    }
}
