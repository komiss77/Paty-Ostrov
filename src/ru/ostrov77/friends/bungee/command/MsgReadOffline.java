package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.ManagerB;




public class MsgReadOffline  {

    public static void execute(final ProxiedPlayer sender, final String[] args) {

        
        ManagerB.readNextOfflineMsg(sender);
        
    }
    
    
    
    
    
    }

