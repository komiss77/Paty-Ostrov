package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;



public class UnblockFriend {
    
    
    public static void execute(final ProxiedPlayer sender, final String[] args) {

        
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "не указан разблокируемый игрок!"));
            sender.sendMessage(new TextComponent("§5/fr unblock <ник> §7- разблокировать сообщения и запросы от игрока"));
            return;
        } 
        
        final String target = args[1];
        
        if (!ManagerB.getPFplayer(sender).blocked.contains(target)) {
            sender.sendMessage(new TextComponent("§5Данный игрок не заблокирован!"));
            return;
        }
        
        if (sender.getName().equals(target)) {
            sender.sendMessage(new TextComponent("§5Вы не можете разблокировать себя!"));
            return;
        }
        
        unBlock(sender, target);
        //if (!ManagerB.isFriends(sender.getName(), target)) {
        //    sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§5блокировать можно только друзей!"));
        //    return;
        //}
        
        
    }
    
    

    public static void unBlock(final ProxiedPlayer sender, final String target) {
        if (ManagerB.exist(target)) {       //на сервере
            ManagerB.getPFplayer(target).GetProxyPlayer().sendMessage(new TextComponent(MainB.friendsPrefix+" §2"+sender+" разблокировал(а) Вас!"));
        }
        
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+" §2Вы разблокировали игрока "+target+" !"));
        ManagerB.getPFplayer(sender).blocked.remove(target);
    }


    
    


}

