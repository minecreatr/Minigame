package com.minecreatr.minigame;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 5/13/14
 */
public class GameObject {

    HashMap<String, Integer> scores = new HashMap<String, Integer>();
    String name;

    public GameObject(String name){
        this.name=name;
    }

    public void setPlayerScore(Player player, int score){
        scores.put(player.getName(), score);
    }

    public void addPlayer(Player player, int score){
        scores.put(player.getName(), score);
    }

    public int getScore(Player player){
        return scores.get(player.getName());
    }

    public boolean isPlayerInGame(Player player){
        return scores.containsKey(player.getName());
    }

    public String getName(){
        return this.name;
    }

    public void leaveGame(Player player){
        scores.remove(player.getName());
    }

    public void sendMessageToPlayers(String message){
        String[] players = (String[]) scores.keySet().toArray();
        for (int i=0;i<players.length;i++){
            Player player = Bukkit.getServer().getPlayer(players[i]);
            if (player!=null){
                player.sendMessage(message);
            }
        }
    }

    public int getPlayerAmount(){
        return scores.size();
    }

    public boolean isLastPlayer(){
        if (getPlayerAmount()<=1){
            return true;
        }
        else {
            return false;
        }
    }

    public Player getLastPlayer(){
        return (Player) scores.keySet().toArray()[0];
    }
}
