package com.minecreatr.minigame;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 5/13/14
 */
public class Minigame extends JavaPlugin implements Listener{

    GameObject[] minigames = new GameObject[100];

    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable(){
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("minigame")){
            if (args[0].equalsIgnoreCase("createGame")){
                minigames[firstNull(minigames)] = new GameObject(args[1]);
                player.sendMessage("Created new Game instance with name "+args[1]);
                return true;
            }
            else if (args[0].equalsIgnoreCase("joinGame")){
                GameObject gameToJoin = getGameFromName(args[1]);
                if (gameToJoin!=null){
                    gameToJoin.addPlayer(player, 5);
                    player.sendMessage("Joined game with name "+args[1]);
                    return true;
                }
                else {
                    player.sendMessage("Invalid name "+args[1]);
                    return true;
                }
            }
            else if (args[0].equalsIgnoreCase("leaveGame")){
                GameObject gameToLeave = getGameFromPlayer(player);
                gameToLeave.leaveGame(player);
                player.sendMessage("You left the Game "+gameToLeave.getName());
                return true;
            }
            else if (args[0].equalsIgnoreCase("destroyGame")){
                minigames[getGameFromNameInteger(args[1])] = null;
                player.sendMessage("Destroyed Game Instance");
            }
            return false;
        }


        return false;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (isPlayerInGame(event.getEntity())){
            GameObject game = getGameFromName(event.getEntity().getName());
            if (game.getScore(event.getEntity())>=0){
                game.sendMessageToPlayers(event.getEntity().getName()+" has lost the game");
                game.leaveGame(event.getEntity());
                if (game.isLastPlayer()){
                    game.getLastPlayer().sendMessage("YOU WIN");
                    minigames[getGameFromPlayerInteger(game.getLastPlayer())] = null;
                }
            }
            game.setPlayerScore(event.getEntity(), game.getScore(event.getEntity())-1);
        }
    }

    public int firstNull(Object[] array){
        if (array==null){
            return 0;
        }
        for (int i=0;i<array.length;i++){
            if (array[i]==null){
                return i;
            }
        }
        return -1;
    }

    public GameObject getGameFromName(String name){
        for (int i=0;i<minigames.length;i++){
            if (minigames[i].getName().equalsIgnoreCase(name)){
                return minigames[i];
            }
        }
        return null;
    }

    public int getGameFromNameInteger(String name){
        for (int i=0;i<minigames.length;i++){
            if (minigames[i].getName().equalsIgnoreCase(name)){
                return i;
            }
        }
        return -1;
    }

    public int getGameFromPlayerInteger(Player player){
        for (int i=0;i<minigames.length;i++){
            if (minigames[i].isPlayerInGame(player)){
                return i;
            }
        }
        return -1;
    }

    public GameObject getGameFromPlayer(Player player){
        for (int i=0;i<minigames.length;i++){
            if (minigames[i].isPlayerInGame(player)){
                return minigames[i];
            }
        }
        return null;
    }

    public boolean isPlayerInGame(Player player){
        if (getGameFromPlayer(player)!=null){
            return true;
        }
        else {
            return false;
        }
    }
}
