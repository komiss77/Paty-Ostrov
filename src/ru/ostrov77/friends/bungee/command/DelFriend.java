package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.MainB;







public class DelFriend{

    public static void execute(final ProxiedPlayer sender, final String[] args) {
//System.out.println("DelFriend.execute() args.length="+args.length+"  "+args[0]+"  "+args[1]);
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "не указан игрок! §5/fr del <ник>§7- удалить"));
            return;
        } 
        
        final String target = args[1];
        
        //if (!ManagerB.exist( target)) {
        //    sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§c"+target+" нет на сервере!"));
        //    return;
        //}
        
        if (sender.getName().equals(target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§cСебя не удалить!"));
            return;
        }
        
        if (!ManagerB.isFriends(sender.getName(), target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cвы не друзья!"));
            return;
        }

        ManagerB.delFriend(sender.getName(), target, true);
        
        

    }
    
    
    
}

