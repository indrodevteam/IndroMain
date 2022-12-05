package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.commands.Command;
import io.github.indroDevTeam.indroMain.configs.Homes;
import io.github.indroDevTeam.indroMain.configs.Profiles;
import io.github.indroDevTeam.indroMain.model.Profile;
import org.bukkit.plugin.java.JavaPlugin;

import me.kodysimpson.simpapi.menu.MenuManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private final Homes homes = new Homes(this);
    private final Profiles profiles = new Profiles(this);

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdir()) {
                getLogger().info("Config folder created successfully!");
                saveDefaultConfig();
            } else {
                getLogger().severe("Could not create config folder");
                getPluginLoader().disablePlugin(this);
                return;
            }
        }


        // pre-render SimpAPI menu manager
        MenuManager.setup(getServer(), this);

        // load data
        loadCommands();
        loadEvents();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);

    }

    private void loadCommands() {
        this.getCommand("home").setExecutor(new Command());
        this.getCommand("home").setTabCompleter(new Command());
    }

    private void loadEvents() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////


    public Homes getHomes() {
        return homes;
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public static IndroMain getInstance() {
        return instance;
    }
}