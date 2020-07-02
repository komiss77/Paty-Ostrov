package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.PFplayerB;




public class ListFriend  {



    public static void execute(final ProxiedPlayer pp, final String[] args) {
        
        final PFplayerB pfp=ManagerB.getPFplayer(pp);
        
        if (pfp.online_friends.isEmpty() && pfp.offline_friends.isEmpty()) {
            pp.sendMessage(new TextComponent(MainB.friendsPrefix +"§f: у вас пока нет друзей."));
        } else if (pfp.online_friends.isEmpty()) {
            pp.sendMessage(new TextComponent(MainB.friendsPrefix +"§f: все друзья оффлайн."));
        } else {
            //pp.sendMessage(new TextComponent(MainB.friendsPrefix+ " §a(на сервере) §8(скрыт)" ));
            
            String res = "";
            for (String fr_:pfp.online_friends) {
                if (ManagerB.exist(fr_)) {
                    if (pfp.blocked.contains(fr_)) {
                        res=res+" §c"+fr_;
                    } else if (ManagerB.getPFplayer(fr_).getSettings(F_set.СКРЫВАТЬ_ПРИСУТСТВИЕ)) {
                        res=res+" §8"+fr_;
                    } else {
                        res=res+" §a"+fr_;
                    }
                }
            }
            
            TextComponent txt=new TextComponent(MainB.friendsPrefix + ":" + res ); 
            txt.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a(на сервере) §8(скрыт) §c(блокирован)").create() ) );
            pp.sendMessage( txt);
        }
    }

    

    
    
}