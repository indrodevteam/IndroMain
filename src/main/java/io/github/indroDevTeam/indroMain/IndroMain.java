package io.github.indroDevTeam.indroMain;

import java.sql.SQLException;

import io.github.indroDevTeam.indroMain.commands.CommandHome;
import io.github.indroDevTeam.indroMain.model.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.kodysimpson.simpapi.menu.MenuManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class IndroMain extends JavaPlugin {
    private static IndroMain instance;
    private static SessionFactory factory;

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

        // Hibernate specific configuration class
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        // Here we tell Hibernate that we annotated our User class
        MetadataSources sources = new MetadataSources(standardRegistry);
        sources.addAnnotatedClass( Profile.class );
        Metadata metadata = sources.buildMetadata();

        // This is what we want, a SessionFactory!
        factory = metadata.buildSessionFactory();


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
        this.getCommand("home").setExecutor(new CommandHome());
        this.getCommand("home").setTabCompleter(new CommandHome());
    }

    private void loadEvents() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-Type Methods
    ///////////////////////////////////////////////////////////////////////////

    public static void sendParsedMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1[&9IndroMain&1]&r " + message));
    }

    public static SessionFactory getFactory() {
        return factory;
    }

    public static IndroMain getInstance() {
        return instance;
    }
}