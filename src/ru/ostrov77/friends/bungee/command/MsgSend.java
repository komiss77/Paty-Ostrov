package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.PFplayerB;
import ru.ostrov77.auth.bungee.Auth;
import ru.ostrov77.auth.bungee.Managers.QuerryManager;



public class MsgSend {
    

    
    
    public static void execute(final ProxiedPlayer sender, final String[] args) {
        
         
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "не указан получатель! §5/fr m <ник> <текст> §7-написать"));
            return;
        } 
        
        if (args.length < 3) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "пустое сообщение!"));
            sender.sendMessage(new TextComponent("§5/fr m <ник> <сообщение> §7- отправить сообщение"));
            return;
        } 
        
        final String reciever = args[1];
        
        if (sender.getName().equals(reciever)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§5Вы пишете сами себе!"));
            return;
        }
        
        if (!ManagerB.isFriends(sender.getName(), reciever)) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§5сообщения можно отправить только друзьям!"));
            return;
        }
        
        
        send( sender, reciever, args,2);

    }
    
    public static void send(final ProxiedPlayer sender, final String reciever, final String[] args, final int start_pos) {
        
        
        
        if (ManagerB.exist(reciever)) {       //на сервере
            
            if (ManagerB.msgCoolDown(sender.getName(),10)) {
                sender.sendMessage(new TextComponent(MainB.friendsPrefix +"§5Интервал между сообщениями 10 секунд!"));
                return;
            }
            
            
            final PFplayerB pf_reciever = ManagerB.getPFplayer(reciever);
            
            if (!pf_reciever.getSettings(F_set.СООБЩЕНИЯ_ПОЛУЧАТЬ)) {
                sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§5у адресата в настройках отключены сообщения!"));
                return; 
            }
            
            if (pf_reciever.blocked.contains(sender.getName())) {
                sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§cАдресат временно заблокировал Вас!"));
                return; 
            }
            
            if (ManagerB.getPFplayer(sender).blocked.contains(reciever)) {
                sender.sendMessage(
                    new ComponentBuilder("§cОтправить сообщение заблокированному нельзя!  §8<< клик-разблокировать")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-разблокировать").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr unblock "+reciever))
                    .create()
                );
                return;
            }
            
            final String msg=message_from_args(args, start_pos, true);
            
            //TextComponent message = new TextComponent( MainB.friendsPrefix+"§e"+sender.getName()+" §5-> §f"+msg+"  §8<<клик-ответ" );
            TextComponent message = new TextComponent(MainB.friendsPrefix);
            //TextComponent block=new TextComponent(" §4✕ ");
            //block.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr block "+sender.getName()));
            //block.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Заблокировать отправителя до перезахода.").create() ) );
            //message.addExtra(block);
            message.addExtra(ManagerB.logo_deny(sender.getName()));
            TextComponent block= new TextComponent( "§e"+sender.getName());
            block.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/fr r " ) );
            block.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик на значки! \n§4крестик - заблокировать").create() ) );
            message.addExtra(block);
            message.addExtra(ManagerB.logo_mail(sender.getName()));
            message.addExtra(ManagerB.logo_tp(sender.getName()));
            message.addExtra(" §5-> §f"+msg+"  §8<<клик-ответ"); 
            
            pf_reciever.GetProxyPlayer().sendMessage(message);

            message = new TextComponent( MainB.friendsPrefix + "§eВы §5->§e "+ reciever +"§5: §f"+ msg +"  §8<<Клик-написать ещё" );
            message.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/fr m " + reciever+" " ) );
            message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик-написать ещё!").create() ) );
            sender.sendMessage(message);
            
            ManagerB.getPFplayer(sender.getName()).last_msg_time=Auth.currentTimeSec();
            
            
        } else {        //off
            if (ManagerB.msgOffCoolDown(sender.getName(),60)) {
                sender.sendMessage(new TextComponent("§5Интервал между оффлайн-сообщениями 1 минута!"));
                return;
            }
            
            final String msg=message_from_args(args, start_pos, false);
            
            ManagerB.trySengOflineMsg(sender,reciever,msg);
            ManagerB.getPFplayer(sender.getName()).last_offmsg_time=Auth.currentTimeSec();
            
        }
        
        ManagerB.getPFplayer(sender).last_msg_write_to=reciever;
        
        

    }
/*
    private static String message_from_args(final String[] arg, final String replace) {
        String msg = "";
        
        for (String s: arg) {
            msg=msg+" "+s;
        }
        msg=msg.replaceFirst(replace, "").replaceFirst("m ", "").replaceFirst("r ", "").trim();
        if(msg.length()>256) msg=msg.substring(0,255);
        return msg;
    }   */ 
    
    public static String message_from_args (final String[]arg, final int start_pos, final boolean for_chat) {
        String msg="";
        if (for_chat) msg="§5";
    //System.out.print(" GetReason >>>>>>>>>> 1111 "+arg.length);
        for (int i=start_pos; i<arg.length; i++){
    //System.out.print(" GetReason >>>>>>>>>> "+i+ " >> "+arg[i]+" >> "+r);
    //не давать скобки
            msg=msg+arg[i]+(for_chat?" §5":" ");
        }
        if(msg.length()>250) msg=msg.substring(0,249);
        return msg;
    }



}

