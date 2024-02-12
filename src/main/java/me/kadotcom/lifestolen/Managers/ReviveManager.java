package me.kadotcom.lifestolen.Managers;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.kadotcom.lifestolen.LifeStolen;
import net.quazar.offlinemanager.api.data.entity.IPlayerData;

public class ReviveManager {
    private static final LifeStolen plugin = LifeStolen.getPlugin(LifeStolen.class);

    public static void revivePlayer(Player caller, String playerName) {
        if (plugin.getConfig().getBoolean("death.banOnDeath")) {
            OfflinePlayer sName = Bukkit.getOfflinePlayer(playerName);
            System.out.println(sName);
            if (sName != null) {
                if (!sName.hasPlayedBefore()) {
                    caller.sendMessage("§f[§cLifeStolen§f] Mention a player that has joined.");
                } else if (Bukkit.getBanList(BanList.Type.NAME).isBanned(sName.getName())) {
                    BanManager.unban(sName);

                    caller.sendMessage("§f[§cLifeStolen§f] You revived " + sName.getName() + ".");
                } else {
                    caller.sendMessage("§f[§cLifeStolen§f] Player you mentioned isn't banned.");
                }
            }
        } else {
            Player sName = Bukkit.getPlayer(playerName);

            if (sName != null) {
                if (sName.getGameMode() == GameMode.SPECTATOR){
                    World world = Bukkit.getServer().getWorlds().get(0);
                    Location spawn = world.getSpawnLocation();
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = "gamemode survival " + sName.getName();
                    String command2 = "tp " + sName.getName() + " " + spawn.getX() + " " + spawn.getY() + " " + spawn.getZ();

                    Bukkit.dispatchCommand(console, command);
                    Bukkit.dispatchCommand(console, command2);

                    caller.sendMessage("§f[§cLifeStolen§f] You revived " + playerName + ".");
                } else {
                    caller.sendMessage("§f[§cLifeStolen§f] Mention a player that is dead.");
                }
            } else {
                if (LifeStolen.OfflineManagerAPI != null) {
                    if (!LifeStolen.OfflineManagerAPI.getStorage().hasPlayer(playerName)) {
                        caller.sendMessage("§f[§cLifeStolen§f] Mention a player that has joined.");
                    } else {
                        IPlayerData playerData = LifeStolen.OfflineManagerAPI.getPlayerData(playerName);
                        GameMode gameMode = playerData.getGameMode();
                        World world = Bukkit.getServer().getWorlds().get(0);
                        Location spawn = world.getSpawnLocation();

                        if (gameMode == GameMode.SPECTATOR) {
                            playerData.setGameMode(GameMode.SURVIVAL);
                            playerData.setLocation(new Location(world, spawn.getX(), spawn.getY(), spawn.getZ()));
                            playerData.save();

                            caller.sendMessage("§f[§cLifeStolen§f] You revived " + playerName + ".");
                        } else {
                            caller.sendMessage("§f[§cLifeStolen§f] Mention a player that is dead.");
                        }
                    }
                } else {
                    caller.sendMessage("§f[§cLifeStolen§f] Offline players cannot be resurrected.");
                }
            }
        }
    }
}
