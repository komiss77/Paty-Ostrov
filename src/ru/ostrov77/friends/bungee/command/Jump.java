package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.auth.bungee.Auth;



public class Jump  {
    
    
    public static void execute(final ProxiedPlayer sender, final String[] args) {

        
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix+ "не указан целевой игрок!"));
            sender.sendMessage(new TextComponent("§5/fr jump <ник> §7- телепорт к игроку"));
            return;
        } 
        
        final String target = args[1];
        
        if (sender.getName().equals(target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix+ "§5Вы не можете телепортироваться к себе!"));
            return;
        }
        
        if (!ManagerB.isFriends(sender.getName(), target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix+ "§5телепортироваться можно только к друзьям!"));
            return;
        }
        
        jumpRequest(sender, target);
        
    }
    
    
    
    
    public static void jumpRequest(final ProxiedPlayer sender, final String target) {

        
        if (!ManagerB.exist(target)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix+" §c"+target+ " §5нет на сервере!"));
            return;
        }
        
        if (ManagerB.getPFplayer(sender.getName()).blocked.contains(target)) {
            sender.sendMessage(new ComponentBuilder(MainB.friendsPrefix+ "§5Вы не можете ТП к заблокированному другу!  §8<< клик-разблокировать")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-разблокировать").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr unblock "+target))
                .create()
            );
            return;
        }
        
        if (ManagerB.getPFplayer(target).blocked.contains(sender.getName())) { 
            sender.sendMessage(new TextComponent(MainB.friendsPrefix+"§c"+target+" временно заблокировал(а) Вас!"));
            return;
        }
        
        if (!ManagerB.getPFplayer(target).getSettings(F_set.ТЕЛЕПОРТ_КО_МНЕ)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix+ "§cВ настройках у "+target+" запрещён телепорт!"));
            ManagerB.getPFplayer(target).GetProxyPlayer().sendMessage(new ComponentBuilder(MainB.friendsPrefix+ "§cДруг "+sender.getName()+" пытался ТП к вам, но в настройках запрещён ТП!  §8< клик-разрешить")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-разрешить").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ft set ТЕЛЕПОРТ_КО_МНЕ allow"))
                .create()
            );
            return;
        }
        
        if (ManagerB.msgTpCoolDown(sender.getName(),10)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§5Интервал между запросами 10 секунд!"));
            return;
        }
        ManagerB.getPFplayer(sender.getName()).last_jump_time=Auth.currentTimeSec();
        
        
        
        
        TextComponent message = new TextComponent(MainB.friendsPrefix);
            //TextComponent add=new TextComponent(" §4✕ ");
            //add.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr block "+sender.getName()));
            //add.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Заблокировать отправителя до перезахода.").create() ) );
        //message.addExtra(add);
        message.addExtra(ManagerB.logo_deny(sender.getName()));
            TextComponent add= new TextComponent( " §f§kXXX§r §b"+sender.getName()+" §eпросит принять запрос на ТП!  §f§kXXX§r " );
            add.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fr jaccept "+sender.getName() ) );
            add.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик-принять запрос на ТП! \n§4крестик - блокировать").create() ) );
        message.addExtra(add);
        message.addExtra(ManagerB.logo_mail(sender.getName()));
        
        ManagerB.getPFplayer(sender.getName()).jump_reqiest_to.add(target);
        ManagerB.getPFplayer(target).GetProxyPlayer().sendMessage(message);
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+"§3Вы отправили "+target+" запрос на ТП."));
        //FM.getPFplayer(sender).jumpToFriend(target);
     
    }
   
    


}

