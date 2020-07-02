package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.PFplayerB;







public class Reject{

    public static void execute(final ProxiedPlayer sender, final String[] args) {

        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "не указан игрок!"));
            return;
        } 
        
        final String target = args[1];
        
        if (sender.getName().equals(target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§cСебе не отказать!"));
            return;
        }
        
        if (ManagerB.isFriends(sender.getName(), target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cУже не отказать!"));
            return;
        }
        
        final PFplayerB pf_sender=ManagerB.getPFplayer(sender.getName());
        
       if (pf_sender.invite_blacklist.contains(target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§c"+ target+" §cуже в списке отказников!"));
            return;
        }
        
       pf_sender.invite_blacklist.add(target);
       if (pf_sender.invite_to_friend.contains(target))pf_sender.invite_to_friend.remove(sender);
       
       sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cвы отказали "+ target+" в дружбе до перезахода!"));
        if (ManagerB.exist( target)) {
            ManagerB.getPFplayer(target).GetProxyPlayer().sendMessage(new TextComponent(MainB.friendsPrefix+"§c"+target+" отказал(а) Вам!"));
            if (ManagerB.getPFplayer(target).invite_to_friend.contains(sender.getName()))ManagerB.getPFplayer(target).invite_to_friend.remove(sender.getName());
            //return;
        }

    }
    
}
