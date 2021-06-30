package ru.ostrov77.friends.spigot;



import java.util.HashMap;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.komiss77.ApiOstrov;
import ru.komiss77.Ostrov;
import ru.komiss77.enums.Operation;

import ru.ostrov77.friends.E_view;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.P_set;











public class PFplayerS{
    
    public String nik;
    public TreeSet <String> online_friends=new TreeSet<>();
    public TreeSet <String> offline_friends=new TreeSet<>();
    public TreeSet <String> blocked=new TreeSet<>();
    public HashMap <F_set,Integer> settings=new HashMap<>();
    public HashMap <P_set,Boolean> party_settings=new HashMap<>();
    public HashMap <String,Runnable> runable=new HashMap<>();
    public int friend_current_page=0;
    public boolean friend_delete_mode=false;
    private long last_interact;
    
    
    
    public PFplayerS(final String nik_) {
        
        nik=nik_;
        last_interact=ApiOstrov.currentTimeSec();
    }

    public boolean interact_cooldown() {
//System.out.println("--interact_cooldown d="+(Timer.Единое_время()-last_interact)+"  res="+(Timer.Единое_время()-last_interact<1000));        
        if (ApiOstrov.currentTimeSec()-last_interact<600) return true;
        else {
            last_interact=ApiOstrov.currentTimeSec();
            return false;
        }
    }
    
    
    public boolean isFriend(final String check) {
        return online_friends.contains(check) || offline_friends.contains(check);
    }
    
    
    
    
    
    public int getFriendSettings(final F_set e_set) {
        if (settings.containsKey(e_set)) return settings.get(e_set);
        else return e_set.default_value;
    }
    public boolean getPartySettings(final P_set p_set) {
        if (party_settings.containsKey(p_set)) return party_settings.get(p_set);
        else return p_set.default_value;
    }
    //public boolean getFriendSettings(final F_set e_set) {
    //    if (settings.containsKey(e_set)) return settings.get(e_set)==1;
    //    else return e_set.default_value==1;
    //}
    
    
    public Player GetBukkitPlayer() {
        return Bukkit.getPlayer(nik);
    }

    public void updateViewMode(final boolean on_login) {
        int tag=F_set.ВИДЕТЬ_РЕЖИМ.default_value;
        //E_view view_mode=E_view.ВИДЕТЬ_ВСЕХ;
        if (settings.containsKey(F_set.ВИДЕТЬ_РЕЖИМ)) {
            //view_mode=E_view.настройка_по_метке(settings.get(F_set.ВИДЕТЬ_РЕЖИМ));
            tag=(settings.get(F_set.ВИДЕТЬ_РЕЖИМ));
        }
        E_view view_mode=E_view.настройка_по_метке(tag);
        final Player actor=GetBukkitPlayer();
        
        switch(view_mode) {
            case ВИДЕТЬ_ВСЕХ: 
                Bukkit.getOnlinePlayers().stream().forEach((p)->{
                    if (!p.getName().equals(nik)) actor.showPlayer(Ostrov.instance, p);
                });
                break;
            case ВИДЕТЬ_ДРУЗЕЙ_И_КОМАНДУ: 
                Bukkit.getOnlinePlayers().stream().forEach((p)->{
                    if (!p.getName().equals(nik)) {
                        if (ApiOstrov.isInParty(actor, p) || isFriend(p.getName())) actor.showPlayer(Ostrov.instance, p);
                        else actor.hidePlayer(Ostrov.instance, p);
                    }
                    ApiOstrov.sendActionBar(nik, "§eНастройка видимости: "+view_mode.item_name);
                });
                break;
            case ВИДЕТЬ_ДРУЗЕЙ: 
                Bukkit.getOnlinePlayers().stream().forEach((p)->{
                    if (!p.getName().equals(nik)) {
                        if (isFriend(p.getName())) actor.showPlayer(Ostrov.instance, p);
                        else actor.hidePlayer(Ostrov.instance, p);
                    }
                });
                    ApiOstrov.sendActionBar(nik, "§eНастройка видимости: "+view_mode.item_name);
                break;
            case ВИДЕТЬ_КОМАНДУ: 
                Bukkit.getOnlinePlayers().stream().forEach((p)->{
                    if (!p.getName().equals(nik)) {
                        if (ApiOstrov.isInParty(actor, p)) actor.showPlayer(Ostrov.instance, p);
                        else actor.hidePlayer(Ostrov.instance, p);
                    }
                });
                    ApiOstrov.sendActionBar(nik, "§eНастройка видимости: "+view_mode.item_name);
                break;
            case СКРЫТЬ_ВСЕХ: 
                Bukkit.getOnlinePlayers().stream().forEach((p)->{
                    if (!p.getName().equals(nik)) actor.hidePlayer(Ostrov.instance, p);
                });
                    ApiOstrov.sendActionBar(nik, "§eНастройка видимости: "+view_mode.item_name);
                break;
        }
        
    }

    public void updateViewMode(final Player target) {
        E_view view_mode=E_view.ВИДЕТЬ_ВСЕХ;
        if (settings.containsKey(F_set.ВИДЕТЬ_РЕЖИМ)) {
            view_mode=E_view.настройка_по_метке(settings.get(F_set.ВИДЕТЬ_РЕЖИМ));
        }
        final Player actor=GetBukkitPlayer();
        
        switch(view_mode) {
            case ВИДЕТЬ_ВСЕХ: 
                actor.showPlayer(Ostrov.instance, target);
                break;
            case ВИДЕТЬ_ДРУЗЕЙ_И_КОМАНДУ: 
                if (ApiOstrov.isInParty(actor, target) || isFriend(target.getName())) actor.showPlayer(Ostrov.instance, target);
                else actor.hidePlayer(Ostrov.instance, target);
                break;
            case ВИДЕТЬ_ДРУЗЕЙ: 
                if (isFriend(target.getName())) actor.showPlayer(Ostrov.instance, target);
                else actor.hidePlayer(Ostrov.instance, target);
                break;
            case ВИДЕТЬ_КОМАНДУ: 
                if (ApiOstrov.isInParty(actor, target)) actor.showPlayer(Ostrov.instance, target);
                else actor.hidePlayer(Ostrov.instance, target);
                break;
            case СКРЫТЬ_ВСЕХ: 
                actor.hidePlayer(Ostrov.instance, target);
                break;
        }
        
    }

    public void setViewMode(final E_view view) {
        //MessageS.request(GetBukkitPlayer(), DataType.FRIEND_COMMAND, "set "+F_set.ВИДЕТЬ_РЕЖИМ.toString()+" "+view.toString());
        ApiOstrov.sendMessage(GetBukkitPlayer(), Operation.EXECUTE_BUNGEE_CMD, nik, "fr set "+F_set.ВИДЕТЬ_РЕЖИМ.toString()+" "+view.toString());
        if (view.tag!=F_set.ВИДЕТЬ_РЕЖИМ.default_value) settings.put(F_set.ВИДЕТЬ_РЕЖИМ, view.tag);
        else settings.remove(F_set.ВИДЕТЬ_РЕЖИМ);
        updateViewMode(false);
    }


   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
