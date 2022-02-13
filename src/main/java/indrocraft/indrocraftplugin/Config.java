package indrocraft.indrocraftplugin;

import io.github.indroDevTeam.indroLib.datamanager.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config implements TabExecutor {

    ConfigUtils config;

    public Config(JavaPlugin plugin) {
        config = new ConfigUtils(plugin, "config.yml");

        this.ranks = config.getConfig().getBoolean("useFeature.ranks");
        this.warps = config.getConfig().getBoolean("useFeature.warps");
        this.homes = config.getConfig().getBoolean("useFeature.homes");
        this.passive = config.getConfig().getBoolean("useFeature.passive");
        this.useSQLite = config.getConfig().getBoolean("sql.sqlite");

        this.serverName = config.getConfig().getString("serverName");

        this.database = config.getConfig().getString("sql.database");
        this.host = config.getConfig().getString("sql.host");
        this.port = config.getConfig().getString("sql.port");
        this.username = config.getConfig().getString("sql.user");
        this.password = config.getConfig().getString("sql.password");
    }

    public Boolean isRanks() {
        return ranks;
    }

    public Boolean isWarps() {
        return warps;
    }

    public Boolean isHomes() {
        return homes;
    }

    public Boolean isPassive() {
        return passive;
    }

    public Boolean isSQLite() {
        return useSQLite;
    }

    public String getServerName() {
        return serverName;
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private final boolean ranks;
    private final boolean warps;
    private final boolean homes;
    private final boolean passive;

    private final boolean useSQLite;
    private final String database;
    private final String host;
    private final String port;
    private final String username;
    private final String password;

    private final String serverName;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You almost got me!! You've gotta be an admin to use this.");
                return true;
            }
        }
        switch (args[0].toLowerCase()) {
            case "servername":
                StringBuilder name = null;
                for (String s : args) name.append(s).append(" ");
                config.getConfig().set("serverName", name.toString());
                break;
            case "ranks":
                config.getConfig().set("useFeature.ranks", args[1].equalsIgnoreCase("true"));
                break;
            case "homes":
                config.getConfig().set("useFeature.homes", args[1].equalsIgnoreCase("true"));
                break;
            case "warps":
                config.getConfig().set("useFeature.warps", args[1].equalsIgnoreCase("true"));
                break;
            case "passive":
                config.getConfig().set("useFeature.passive", args[1].equalsIgnoreCase("true"));
                break;
            case "sql":
                switch (args[1].toLowerCase()) {
                    case "database":
                        config.getConfig().set("sql.database", args[2]);
                        break;
                    case "port":
                        config.getConfig().set("sql.port", args[2]);
                        break;
                    case "host":
                        config.getConfig().set("sql.host", args[2]);
                        break;
                    case "user":
                        config.getConfig().set("sql.user", args[2]);
                        break;
                    case "password":
                        config.getConfig().set("sql.password", args[2]);
                        break;
                    case "sqlite":
                        config.getConfig().set("sql.sqlite", args[2].equalsIgnoreCase("true"));
                        break;
                }
                break;
        }
        config.saveConfig();
        config.reloadConfig();
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return new ArrayList<>(Arrays.asList("ranks", "warps", "homes", "passive", "servername", "sql"));
        else if (args[0].equalsIgnoreCase("sql") && args.length == 2)
            return new ArrayList<>(Arrays.asList("database", "port", "host", "user", "password", "sqlite"));
        else if (args[0].equalsIgnoreCase("servername") ||
                args[1].equalsIgnoreCase("database") ||
                args[1].equalsIgnoreCase("port") ||
                args[1].equalsIgnoreCase("host") ||
                args[1].equalsIgnoreCase("user") ||
                args[1].equalsIgnoreCase("password")
        ) return null;
        else
            return new ArrayList<>(Arrays.asList("true", "false"));
    }
}
