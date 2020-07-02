package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;



public class BlockFriend {
    
    
    public static void execute(final ProxiedPlayer sender, final String[] args) {

        
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "не указан блокируемый игрок!"));
            sender.sendMessage(new TextComponent("§5/fr block <ник> §7- блокировать сообщения и запросы от игрока"));
            return;
        } 
        
        final String target = args[1];
        
        if (ManagerB.getPFplayer(sender).blocked.contains(target)) {
            sender.sendMessage(new TextComponent("§5Вы уже заблокировали этого игрока!"));
            return;
        }
        
        if (sender.getName().equals(target)) {
            sender.sendMessage(new TextComponent("§5Вы не можете блокировать себя!"));
            return;
        }
        
        if (!ManagerB.isFriends(sender.getName(), target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§5блокировать можно только друзей!"));
            return;
        }
        
        block(sender, target);
        
    }
    
    
     public static void block(final ProxiedPlayer sender, final String target) {
        if (ManagerB.exist(target)) {       //на сервере
            ManagerB.getPFplayer(target).GetProxyPlayer().sendMessage(new TextComponent(MainB.friendsPrefix+" §c"+sender+" временно заблокировал(а) Вас!"));
        }
        
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+" §cВы временно заблокировали "+target+" !"));
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+" §5§oСнятие блокировки в настройках или перезаход."));
        ManagerB.getPFplayer(sender).blocked.add(target);
    }
   
    


}

