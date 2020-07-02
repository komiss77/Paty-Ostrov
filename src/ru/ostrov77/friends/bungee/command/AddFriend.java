package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.PFplayerB;
import ru.ostrov77.auth.bungee.Auth;
import ru.ostrov77.friends.F_set;








public class AddFriend {

    public static void execute(final ProxiedPlayer sender, final String[] args) {

        final PFplayerB pf_sender=ManagerB.getPFplayer(sender.getName());
        
        if (pf_sender.online_friends.size()+pf_sender.online_friends.size()>=100) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cдрузей не может быть больше 100! Пора почистить авдеевы конюшни!"));
            return;
        } 
        
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cне указан игрок! §5/fr add <ник>§7- подружиться"));
            return;
        } 
        
        final String reciever = args[1];
        
        if (sender.getName().equals(reciever)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§cВы и так с собой дружите!"));
            return;
        }
        
        if (ManagerB.isFriends(sender.getName(), reciever)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cвы уже старые добрые друзья!"));
            return;
        }
        
        if (!ManagerB.exist( reciever)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§c"+reciever+" нет на сервере!"));
            return;
        }
        
        final PFplayerB pf_reciever=ManagerB.getPFplayer(reciever);
        
        if (pf_reciever.online_friends.size()+pf_reciever.online_friends.size()>=100) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cу "+pf_reciever.nik+" слишком много друзей!"));
            return;
        } 
        
//System.out.println("AddFriend 1111 pf_sender="+pf_sender.nik);        
//System.out.println("AddFriend 222 invite_to_friend="+pf_sender.invite_to_friend);        
        if (pf_sender.invite_to_friend.contains(reciever)) { //если предлагающему уже оставили приглашение
            pf_sender.invite_to_friend.remove(reciever);
            ManagerB.addFriend(sender.getName(), reciever);//дружимся
            return;
        }
        
        
        if (!pf_reciever.getSettings(F_set.ПРИНИМАТЬ_ДРУЖБУ)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cУ "+ reciever+" §cв настройках выключено добавление друзей!"));
            return;
        }
        
        if (pf_reciever.invite_blacklist.contains(sender.getName())) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§c"+ reciever+" §cотверг ваше предложение до перезахода!"));
            return;
        }
        
        if (ManagerB.msgCoolDown(sender.getName(),10)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§5Интервал между предложениями 10 секунд!"));
            return;
        }
        ManagerB.getPFplayer(sender.getName()).last_msg_time=Auth.Единое_время()/1000;
        
        sender.sendMessage(new TextComponent(MainB.friendsPrefix +"Вы предложили дружить §b"+reciever));
        
        TextComponent message = new TextComponent(MainB.friendsPrefix);
        TextComponent block=new TextComponent(" §4✕ ");
        block.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr reject "+sender.getName()));
        block.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Заблокировать отправителя до перезахода.").create() ) );
        message.addExtra(block);
        block= new TextComponent( "§b"+sender.getName()+" §eпредлагает дружить!  §8<<клик-принять" );
        block.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fr add "+sender.getName() ) );
        block.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик-подружиться! \n§4крестик - отказать").create() ) );
        message.addExtra(block);
        pf_reciever.GetProxyPlayer().sendMessage(message);
        
        pf_reciever.invite_to_friend.add(sender.getName());
    }
    
    
    
    
    
}
