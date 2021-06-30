package ru.ostrov77.friends.spigot.listener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.komiss77.ApiOstrov;
import ru.komiss77.enums.Operation;
import ru.komiss77.events.OstrovChanelEvent;
import ru.komiss77.modules.player.PM;

import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.P_set;
import ru.ostrov77.friends.spigot.ManagerS;
import ru.ostrov77.friends.spigot.PFplayerS;




public class OstrovChanelListener implements Listener {

    
    @EventHandler(ignoreCancelled = true,priority = EventPriority.NORMAL)
    public void onChanelMsg (OstrovChanelEvent e) {
        if (!(e.action.toString().startsWith("PF_"))) return;;
        final Player player = Bukkit.getPlayer(e.senderInfo);
        if (player==null || !player.isOnline() || !ManagerS.exist(player.getName()) ) return;
        final PFplayerS pfs= ManagerS.getPFplayer(player.getName());
        
    switch (e.action) {
        
            case PF_FRIENDS_ONLINE:
                pfs.online_friends.clear();
                if(!e.s1.isEmpty()) {
                   // for (String r_:raw.split("<>")) {
                    //    pfs.offline_friends
                    //}
                    pfs.online_friends.addAll(Arrays.asList(e.s1.split(",")));
                }
//System.out.println("--------online_friends="+pfs.online_friends);
                break;
                
            case PF_FRIENDS_OFFLINE:
                pfs.offline_friends.clear();
//System.out.println("------FRIENDS_OFFLINE ="+raw);
                if(!e.s1.isEmpty()) pfs.offline_friends.addAll(Arrays.asList(e.s1.split(",")));
//System.out.println("--------offline_friends="+pfs.offline_friends);
                break;
                
            case PF_FRIEND_SETTINGS:
                pfs.settings.clear();
//System.out.println("------SETTINGS_GET ="+raw);
                if(!e.s1.isEmpty()) {
                    List<String>settings_ = Arrays.asList(e.s1.split(","));
                    F_set set;
                    int tag=0;
                    int value=0;
                    for (String i:settings_) {
                        if (ManagerS.isInteger(i.split("_")[0])) tag=Integer.valueOf(i.split("_")[0]);
                        if (ManagerS.isInteger(i.split("_")[1])) value=Integer.valueOf(i.split("_")[1]);
                        set=F_set.настройка_по_метке(tag);
                        if (set!=F_set.нет && set.default_value!=value) pfs.settings.put(set, value);
                    }
                }
//System.out.println("--------offline_friends="+pfs.offline_friends);
                break;
                
                
            case PF_PARTY_MEMBER:
                if (PM.exist(player.getName())) PM.getOplayer(player.getName()).onPartyRecieved(player, e.s1, true);
//System.out.println("--------party_members="+pfs.party_members);
                break;
                
            case PF_PARTY_SETTINGS:
                pfs.party_settings.clear();
//System.out.println("------SETTINGS_GET ="+raw);
                if(!e.s1.isEmpty()) {
                    List<String>settings_ = Arrays.asList(e.s1.split(","));
                    P_set set;
                    int tag=0;
                    int value=0;
                    for (String i:settings_) {
                        if (ManagerS.isInteger(i.split("_")[0])) tag=Integer.valueOf(i.split("_")[0]);
                        if (ManagerS.isInteger(i.split("_")[1])) value=Integer.valueOf(i.split("_")[1]);
                        set=P_set.настройка_по_метке(tag);
                        if (set!=P_set.нет && set.default_value!=(value==1)) pfs.party_settings.put(set, (value==1));
                    }
                }
//System.out.println("--------offline_friends="+pfs.offline_friends);
                break;
                


            case PF_CALLBACK_RUN:
//System.out.println("CALLBACK_RUN 1");
                if (pfs.runable.containsKey(e.s1)) {
//System.out.println("CALLBACK_RUN 2");
                    pfs.runable.get(e.s1).run();
                    pfs.runable.remove(e.s1);
                }
                break;


        }
    
    
    }

    
    
    
    
    
    public static void request( final Player p, final Operation action, final String raw) {
        request(p, action, raw, null);
    }
   
    public static void request( final Player p, final Operation action, final String raw, final Runnable callback) {
//System.out.println("sendMessage type="+type.toString()+"  callback=null?"+(callback==null));  
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //DataOutputStream out = new DataOutputStream(stream);

        //try {
            if (callback!=null) {
                final String uuid=UUID.randomUUID().toString();
                ApiOstrov.sendMessage(p, action, p.getName(), uuid);
                //out.writeUTF(type.toString());
                //out.writeUTF(uuid);
                ManagerS.getPFplayer(p.getName()).runable.put(uuid, callback);
            } else {
                //out.writeUTF(type.toString());
                //out.writeUTF(raw);
                ApiOstrov.sendMessage(p, action, p.getName(), raw);
            }
        //} catch (IOException ex) {
        //    MainS.log_err("sendMessage : "+ex.getMessage());
        //}
//System.out.println("sendMessage done!  runable=null?"+(ManagerS.getPFplayer(p.getName()).runable==null));  

        //p.sendPluginMessage(MainS.main, Chanell.FRIEND.toString(), stream.toByteArray());
    }    
    
    
    
                        
}
