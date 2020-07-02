package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;




public class MsgReply{

    
    public static void execute(final ProxiedPlayer sender, final String[] args) {
        

        final String last_writer=ManagerB.findLastWriter(sender.getName());
        

        if (last_writer.isEmpty()) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "Никто не отправлял вам сообщений."));
            return;
        } 

        if (args.length < 1) {
            sender.sendMessage(new TextComponent(MainB.friendsPrefix + "пустой ответ!  §5/fr r <сообщение> §7- ответить последнему отправителю"));
            return;
        }
        
        MsgSend.send(sender, last_writer, args, 1);
        
    }
    
    
    
    
    
    }

