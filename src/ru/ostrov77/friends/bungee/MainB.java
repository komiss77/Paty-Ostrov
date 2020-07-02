package ru.ostrov77.friends.bungee;

import java.util.logging.Level;

import net.md_5.bungee.api.plugin.Plugin;

import ru.ostrov77.friends.bungee.listener.PlayerListenerB;
import ru.ostrov77.friends.bungee.listener.MessageB;
import ru.ostrov77.friends.bungee.command.Fr;
import ru.ostrov77.friends.bungee.command.PartyCmd;







public class MainB extends Plugin {

    private static MainB instance;
    public static String friendsPrefix;
    public static String partyPrefix;
    public static String allowed_chars;
    
    public static ManagerB fm;
    public static PartyManager party_manager;
    
    

    @Override
    public void onEnable() {
        MainB.instance = this;
        //try {
        //    this.config = Config.loadConfig();
        //} catch (IOException ioexception) {
            //ioexception.printStackTrace();
        //}
        
        friendsPrefix = "§a§lД§d§lр§c§lу§e§lз§9§lь§b§lя §f";
        partyPrefix = "§6[§eКоманда§6] §3";
        allowed_chars = "@_!0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        
        fm=new ManagerB(this);
        party_manager=new PartyManager(this);
        

        this.getProxy().getPluginManager().registerListener(this, new PlayerListenerB());
        this.getProxy().getPluginManager().registerListener(this, new MessageB());
        
        //this.getProxy().registerChannel(Chanell.withSeparator(Chanell.FRIEND));
        
        this.getProxy().getPluginManager().registerCommand(this, new Fr());
        this.getProxy().getPluginManager().registerCommand(this, new PartyCmd());
        
        log_ok("Плагин друзья загружен!");
        
    }



    public static MainB getInstance() {
        return MainB.instance;
    }

     
   
    
    
public static void log_ok(final String s) {   instance.getLogger().log(Level.INFO, "{0}{1}", new String[]{friendsPrefix+"§2 ", s}); }
public static void log_err(final String s) {   instance.getLogger().log(Level.SEVERE, "{0}{1}", new String[]{friendsPrefix+"§4 ", s}); }










    public String getPartyPrefix() {
        return partyPrefix;
    }

    public Object getConfig() {
        return instance.getConfig();
    }
    
    
}
