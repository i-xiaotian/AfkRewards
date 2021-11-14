package com.tanxi521.afkrewards.commands;

import com.tanxi521.afkrewards.data.AfkData;
import com.tanxi521.afkrewards.entity.AfkMap;
import com.tanxi521.afkrewards.entity.PlayerAfk;
import com.tanxi521.afkrewards.main.AfkRewards;
import com.tanxi521.afkrewards.util.BaseUtils;
import com.tanxi521.afkrewards.util.ConfigReader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AfkCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        AfkData.createPlayerData(player);

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            List<String> helps = BaseUtils.getLangList("help");
            for (String help : helps) {
                sender.sendMessage(help.replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§").replace("%min%", ConfigReader.getRewardsTime() + "")
                        .replace("%num%", ConfigReader.getMaxAfkPlayer() + ""));
            }
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
            List<String> types = ConfigReader.getTypes();
            sender.sendMessage(BaseUtils.getLang("type").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
            for (String type : types) {
                sender.sendMessage(ChatColor.GRAY + "-  " + ChatColor.GOLD + type);
            }
            sender.sendMessage(BaseUtils.getLang("start").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "此命令只能在客户端使用");
                return false;
            }
            PlayerAfk playerData = AfkData.getPlayerData(player);
            if (!playerData.getType().equals("未挂机")) {
                sender.sendMessage(BaseUtils.getLang("started").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
                return false;
            }
            List<String> types = ConfigReader.getTypes();
            String type = args[1];
            if (!types.contains(type)) {
                sender.sendMessage(BaseUtils.getLang("nosuchtype").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
                return false;
            }
            if (!sender.hasPermission("AfkRewards.start." + args[1])) {
                sender.sendMessage(BaseUtils.getLang("nopermission").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
                return false;
            }
            if (AfkMap.afkPlayer.size() >= ConfigReader.getMaxAfkPlayer()) {
                sender.sendMessage(BaseUtils.getLang("startmax").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
            }
            AfkData.setStart(player, type);
            AfkMap.addAfkPlayer(AfkData.getPlayerData(player));
            String startafk = BaseUtils.getLang("startafk");
            System.out.println(startafk);
            sender.sendMessage(BaseUtils.getLang("startafk").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§").replace("%player%", sender.getName()));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("end")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "此命令只能在客户端使用");
                return false;
            }
            PlayerAfk playerData = AfkData.getPlayerData(player);
            if (playerData.getType().equals("未挂机")) {
                sender.sendMessage(BaseUtils.getLang("notstart").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
                return false;
            }

            if (playerData.getEnd() == 0) {
                playerData.setEnd(System.currentTimeMillis());
                BaseUtils.giveRewards(playerData, sender);
                String afkTime = BaseUtils.totalAfkTime(playerData);
                int rewardsTime = BaseUtils.rewardsTime(playerData);
                AfkRewards.getInstance().getServer().broadcastMessage(BaseUtils.getLang("rewardsmsg")
                        .replace("%prefix", BaseUtils.getLang("prefix")).replace("&", "§")
                        .replace("%player%", sender.getName()).replace("%afktime%", afkTime)
                        .replace("%times%", rewardsTime + ""));
                BaseUtils.endPlayerAfk(playerData, rewardsTime);
                return true;
            }

            if (playerData.getEnd() != 0) {
                BaseUtils.giveRewards(playerData, sender);
                String afkTime = BaseUtils.totalAfkTime(playerData);
                int rewardsTime = BaseUtils.rewardsTime(playerData);
                sender.sendMessage(BaseUtils.getLang("offonlinemsg").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
                AfkRewards.getInstance().getServer().broadcastMessage(BaseUtils.getLang("rewardsmsg")
                        .replace("%prefix", BaseUtils.getLang("prefix")).replace("&", "§")
                        .replace("%player%", sender.getName()).replace("%afktime%", afkTime)
                        .replace("%times%", rewardsTime + ""));
                BaseUtils.endPlayerAfk(playerData, rewardsTime);
                AfkMap.removeAfkPlayer(sender.getName());
                return true;
            }
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            Set<String> strings = AfkMap.afkPlayer.keySet();
            sender.sendMessage(BaseUtils.getLang("afklisttitle").replace("&", "§"));
            for (String key : strings) {
                PlayerAfk playerAfk = AfkMap.afkPlayer.get(key);
                sender.sendMessage(BaseUtils.getLang("afklistbody").replace("%player%", playerAfk.getDisplayName())
                        .replace("%type%", playerAfk.getType()).replace("%afktime%", BaseUtils.afkTime(playerAfk))
                        .replace("&", "§"));
            }
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            List<String> strings = BaseUtils.rankList();
            sender.sendMessage(BaseUtils.getLang("afkranktitle").replace("&", "§"));
            for (String string : strings) {
                sender.sendMessage(string.replace("&", "§"));
            }
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp()) {
                sender.sendMessage(BaseUtils.getLang("reloadnopermission").replace("%prefix", BaseUtils.getLang("prefix"))
                        .replace("&", "§"));
                return false;
            }
            AfkRewards.getInstance().reloadConfig();
            AfkRewards.langYml = YamlConfiguration.loadConfiguration(AfkRewards.lang);
            sender.sendMessage(BaseUtils.getLang("reload").replace("%prefix", BaseUtils.getLang("prefix"))
                    .replace("&", "§"));
            return true;
        }
        sender.sendMessage(BaseUtils.getLang("uncorrect").replace("%prefix", BaseUtils.getLang("prefix"))
                .replace("&", "§"));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            String[] cmds = {"start", "end", "list", "top", "reload"};
            return Arrays.stream(cmds).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        if (args.length == 2) {
            List<String> type = ConfigReader.getTypes();
            String[] types = new String[type.size()];
            for (int i = 0; i < types.length; i++) {
                types[i] = type.get(i);
            }
            return Arrays.stream(types).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        }
        return null;
    }
}
