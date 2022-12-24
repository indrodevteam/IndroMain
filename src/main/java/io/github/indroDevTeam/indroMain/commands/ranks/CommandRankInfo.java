package io.github.indroDevTeam.indroMain.commands.ranks;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.model.Rank;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandRankInfo extends SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Displays the info of your current rank";
    }

    @Override
    public String getSyntax() {
        return "/rank info";
    }

    @Override
    public void perform(CommandSender sender, String[] strings) {
        if (!(sender instanceof Player player)) {
            ChatUtils.notPlayerError(sender);
            return;
        }

        Profile profile;
        if (IndroMain.getDataManager().getProfile(player.getUniqueId()).isEmpty()) {
            IndroMain.getDataManager().createProfile(Profile.getNewProfile(player, "default"));
        }
        profile = IndroMain.getDataManager().getProfile(player.getUniqueId()).get();

        Rank rank = profile.getRank();

        StringBuilder builder = new StringBuilder();
        Map<Advancement, Boolean> map = rank.isPlayerPromotable(player);

        for (Advancement advancement: map.keySet()) {
            builder.append(map.get(advancement) ? ChatColor.GREEN : ChatColor.RED);
            builder.append(Objects.requireNonNull(advancement.getDisplay()).getTitle()).append(ChatColor.RESET).append(", ");
        }

        List<String> list = new ArrayList<>();

        list.add(ChatUtils.format("&9-------+ RANK DATA +-------"));
        list.add(ChatUtils.format("&9Rank Name - " + rank.getName()));
        list.add(ChatUtils.format("&9Displayed Chat Name - " + rank.getChatTag() + player.getName()));
        list.add(ChatUtils.format("&9Displayed Tab Name - " + rank.getTabTag() + player.getName()));
        list.add(ChatUtils.format(""));
        list.add(ChatUtils.format("&9Promotable Ranks - " + rank.getNextRanks().toString()));
        list.add(ChatUtils.format("&9Advancement Status - " + builder));
        list.add(ChatUtils.format(""));
        list.add(ChatUtils.format("&9Warp Distance - " + rank.getMaxDistance()));
        list.add(ChatUtils.format("&9Warp Delay - " + rank.getWarpDelay()));
        list.add(ChatUtils.format("&9Warp Cooldown - " + rank.getWarpCooldown()));
        list.add(ChatUtils.format("&9Max Points - " + rank.getWarpCap()));
        list.add(ChatUtils.format("&9Cross Teleport Permitted - " + rank.getWarpCooldown()));
        list.add(ChatUtils.format("&9-------+ END RANK DATA +-------"));

        for (String s: list) player.sendMessage(s);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return new ArrayList<>();
    }
}
