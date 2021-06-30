package ru.ostrov77.friends.bungee.listener;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.auth.bungee.AuthCompleteEvent;
import ru.ostrov77.friends.bungee.PartyManager;


public class PlayerListenerB implements Listener {

    
    @EventHandler
    public void AuthComplete(AuthCompleteEvent e) {
//System.out.println("AuthComplete !!!!!!");
        ManagerB.getPFplayer(e.getPlayer());
    }
   
    
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        ManagerB.disconnect(e.getPlayer());
        if(PartyManager.hasParty(e.getPlayer())) PartyManager.removePlayerFromAllParty(e.getPlayer());
    }
    
    
    @EventHandler  //соединение с новым серв завершено
    public void ServerSwitchEvent(ServerSwitchEvent e) {
//System.out.println("Friend: ServerSwitchEvent serv="+e.getPlayer().getServer().getInfo().getName());
        if ( ManagerB.exist(e.getPlayer().getName())) { //если загружен как друг, значит авторизацию прошел!
            ManagerB.getPFplayer(e.getPlayer().getName()).onServerSwitch(e.getPlayer());
        }
    }



    
    

}
