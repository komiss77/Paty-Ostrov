package ru.ostrov77.friends.bungee;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.komiss77.Enums.Data;
import ru.ostrov77.auth.bungee.Auth;
import ru.ostrov77.auth.bungee.Managers.BungeePM;
import ru.ostrov77.auth.bungee.Managers.Database;
import ru.ostrov77.auth.bungee.Bplayer;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.P_set;
import ru.ostrov77.friends.Party;










public class PFplayerB{
    
    public String nik;
    public final TreeSet <String> online_friends=new TreeSet<>();
    public final TreeSet <String> offline_friends=new TreeSet<>();
    public final TreeSet <String> blocked=new TreeSet<>();
    public final HashMap <F_set,Integer> settings=new HashMap<>();
    public final HashMap <P_set,Boolean> party_settings=new HashMap<>();
    public long last_msg_time,last_jump_time,last_offmsg_time=0;
    public String last_msg_write_to="";
    public final Set<String> jump_reqiest_to=new HashSet<>();
    public final Set<String> invite_to_friend=new HashSet<>();
    public final Set<String> invite_blacklist=new HashSet<>();
    
    boolean loaded=false;

    
    
    public PFplayerB(final String nik_) {
        nik=nik_;
        Load();
    }

    
    
    public boolean isFriend(String check) {
        return online_friends.contains(check) || offline_friends.contains(check);
    }
    
    public void delFriend(final String nik, final boolean notify) {
        if (online_friends.contains(nik)) online_friends.remove(nik);
        if (offline_friends.contains(nik)) offline_friends.remove(nik);
        if (notify) sendMessage( new TextComponent(MainB.friendsPrefix+"§fВы больше не дружите с §4"+nik) );
    }

    public void addFriend(final String friend) {
        if (ManagerB.GetProxyPlayer(friend)!=null) online_friends.add(friend);
        else offline_friends.add(friend);
        GetProxyPlayer().sendMessage(  new ComponentBuilder(MainB.friendsPrefix+"§2Вы подружились с §a"+friend+" §2!  §8<<Клик-написать сообщение")
        .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/fr m " + friend+" " ))
        .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик-написать сообщение!").create()))
        .create()
        );
    }

    
    
    
    
    public int getIntSettings(F_set e_set) {
        if (settings.containsKey(e_set)) return settings.get(e_set);
        else return e_set.default_value;
    }
    public boolean getSettings(F_set e_set) {
        if (settings.containsKey(e_set)) return settings.get(e_set)==1;
        else return e_set.default_value==1;
    }
    public boolean getPartySettings(P_set p_set) {
        if (party_settings.containsKey(p_set)) return party_settings.get(p_set);
        else return p_set.default_value;
    }
    
    
    public ProxiedPlayer GetProxyPlayer() {
        return ProxyServer.getInstance().getPlayer(nik);
    }

    public void sendMessage(final TextComponent tc) {
        if (ProxyServer.getInstance().getPlayer(nik)!=null) {
            ProxyServer.getInstance().getPlayer(nik).sendMessage(tc);
        } else {
            ManagerB.forceRemove(nik);
        }
    }
  
    
    
    
    
    
    
    
    
    
    
    
    
      
    
    
    private void Load() {
        
            final Bplayer bp=BungeePM.getBplayer(nik);
            List<String>split=new ArrayList<>();
            
                if (bp.getData(Data.FRIEND_F_SETTINGS).contains("_")) {
                    //split = Arrays.asList(bp.getData(Data.FRIEND_F_SETTINGS).split(","));
                    split.addAll(Arrays.asList(bp.getData(Data.FRIEND_F_SETTINGS).split(",")));
                        F_set set;
                        int tag=0;
                        int value=0;
                        for (String i:split) {
                            if (ManagerB.isInteger(i.split("_")[0])) tag=Integer.valueOf(i.split("_")[0]);
                            if (ManagerB.isInteger(i.split("_")[1])) value=Integer.valueOf(i.split("_")[1]);
                            set=F_set.настройка_по_метке(tag);
                            if (set!=F_set.нет && set.default_value!=value) settings.put(set, value);
                        }
                }
                split.clear();
//System.out.println("PFplayerB.Load() FRIEND_P_SETTINGS="+bp.getData(Data.FRIEND_P_SETTINGS));
                if (bp.getData(Data.FRIEND_P_SETTINGS).contains("_")) {
                    //split = Arrays.asList(bp.getData(Data.FRIEND_P_SETTINGS).split(","));
                    split.addAll(Arrays.asList(bp.getData(Data.FRIEND_P_SETTINGS).split(",")));
                        P_set pset;
                        int tag=0;
                        int value=0;
                        for (String i:split) {
                            if (ManagerB.isInteger(i.split("_")[0])) tag=Integer.valueOf(i.split("_")[0]);
                            if (ManagerB.isInteger(i.split("_")[1])) value=Integer.valueOf(i.split("_")[1]);
                            pset=P_set.настройка_по_метке(tag);
                            if (pset!=P_set.нет && pset.default_value!=(value==1)) party_settings.put(pset, value==1);
                        }
                }
                split.clear();
                
                if (!bp.getData(Data.FRIEND_FRIENDS).isEmpty()) {
                    //split = Arrays.asList(bp.getData(Data.FRIEND_FRIENDS).split(","));
                    split.addAll(Arrays.asList(bp.getData(Data.FRIEND_FRIENDS).split(",")));
                    split.stream().forEach((fr_) -> {
//System.out.println("Load() fr_="+fr_+" is_alive?="+Database.all_users.contains(fr_));                
                        
                        if (Database.all_users.contains(fr_)) {
                            if (ProxyServer.getInstance().getPlayer(fr_)!=null) online_friends.add(fr_);
                            else offline_friends.add(fr_);
                        } else {
                            blocked.add(fr_);
                        }
                    });
                }
                split.clear();
                        
                        
                if (getSettings(F_set.СООБЩЕНИЯ_ОФФЛАЙН)) {
                    MainB.getInstance().getProxy().getScheduler().runAsync(MainB.getInstance(), () -> {
                        TextComponent read=null;
                        Statement stmt = null;
                        ResultSet rs = null;

                        try {
                            stmt = Auth.GetConnection().createStatement();
                            rs = stmt.executeQuery("select COUNT(*) from `ostrov77`.`fr_messages` WHERE `reciever`=\'"+nik+"\' ");
                                if (rs.next() && rs.getInt("count(*)")>=1) {
                                    read = new TextComponent(MainB.friendsPrefix +"§f§kXXX§r §bВ вашем ящике есть письма! §e✉  §f("+rs.getInt("count(*)")+") §f§kXXX§r §8<клик-читать" );
                                    read.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик - начать просмотр").create() ));
                                    read.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fr mread" ) );
                                }
                                rs.close();
                                stmt.close();

                                if (read!=null) sendMessage(read);


                        } catch (SQLException ex) {
                            MainB.log_err("PFplayer СООБЩЕНИЯ_ОФФЛАЙН : "+ex.getMessage());
                        } finally {
                            try {
                                 if (rs!=null) rs.close();
                                 if (stmt!=null) stmt.close();
                            } catch (SQLException ex) {
                            }
                        }
                    });
                }
                
                bp.resetData(Data.FRIEND_FRIENDS, false);
                bp.resetData(Data.FRIEND_F_SETTINGS, false);
                bp.resetData(Data.FRIEND_P_SETTINGS, false);
                loaded=true;
                
                ManagerB.loaded(PFplayerB.this);
//System.out.println("Load() blocked="+blocked);                
                if (!blocked.isEmpty()) {
                    GetProxyPlayer().sendMessage(new TextComponent(MainB.friendsPrefix+"§cУдалены из друзей (не заходили > 3 мес.) :"));
                    GetProxyPlayer().sendMessage(new TextComponent("§e"+String.join("§e, ", blocked)));
                    blocked.stream().forEach( (to_del) -> {
                        ManagerB.delFriend(nik, to_del,false);
                    });
                }
                blocked.clear();
        
    }

    
    public boolean playerJoin(final String nik, final TextComponent join_msg) {
        if (nik.equals(this.nik)) return false;
        
        if (offline_friends.contains(nik)) {

            offline_friends.remove(nik);
            online_friends.add(nik);

            if (join_msg!=null && getSettings(F_set.ОПОВЕЩ_ВХОД_ПОЛУЧАТЬ)) {
                sendMessage(join_msg);
            }

            return !getSettings(F_set.СКРЫВАТЬ_ПРИСУТСТВИЕ);
        }
        return false;

    }

    public void playerQuit(final String nik, final TextComponent quit_msg) {
        if (nik.equals(this.nik)) return;
            if (online_friends.contains(nik)) {
                online_friends.remove(nik);
                offline_friends.add(nik);
                if (quit_msg!=null && getSettings(F_set.ОПОВЕЩ_ВЫХОД_ПОЛУЧАТЬ)) {
                    sendMessage(quit_msg);
                }
            }

    }

    public void onServerSwitch(final ProxiedPlayer pp) {
        if (getSettings(F_set.ОПОВЕЩ_ПЕРЕХОД_ОТПРАВЛЯТЬ) && !offline_friends.isEmpty()) {
//System.out.println("ss 222");            
            TextComponent msg=new TextComponent (MainB.friendsPrefix+"§7"+nik+" теперь на серверe "+pp.getServer().getInfo().getName()+" !");
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr jump "+nik));
            msg.addExtra(ManagerB.logo_mail(nik));
            msg.addExtra(ManagerB.logo_tp(nik));

            online_friends.stream().forEach((fr_) -> {
               if ( ManagerB.exist(fr_) && !blocked.contains(fr_) ) {
                    if (ManagerB.getPFplayer(fr_).getSettings(F_set.ОПОВЕЩ_ПЕРЕХОД_ПОЛУЧАТЬ)) {
                        ManagerB.getPFplayer(fr_).sendMessage(msg);
                    }
                }
            });
        }
        //проверка пати
        if (PartyManager.hasParty(nik)) {
            final Party party = PartyManager.Get_player_party(nik);
            if (party.getLeader().equals(nik)) {
            MainB.getInstance().getProxy().getScheduler().schedule(MainB.getInstance(), () -> {
                PFplayerB pfp_member;
                for (String member:party.getMembers()) {
                    if (!member.equals(nik) && ManagerB.exist(member)) {
                        pfp_member=ManagerB.getPFplayer(member);
                        if (pfp_member.getPartySettings(P_set.ТЕЛЕПОРТ_К_ЛИДЕРУ)) {
                            //PM.getOplayer(member).setData(Data.PARTY_LEADER_NAME, nik); //сохраняем ник лидера и тп на серв, можно без задержки
                            Auth.teleport(pfp_member.GetProxyPlayer(), pp);
                        }
                        if (pfp_member.getPartySettings(P_set.УВЕДОМЛЕНИЕ_ТП_ЛИДЕРА)) pfp_member.GetProxyPlayer().sendMessage(new TextComponent (MainB.partyPrefix+"§7Лидер команды "+nik+" §e-> §7сервер "+pp.getServer().getInfo().getName()));
                    }
                }
            }, 500, TimeUnit.MILLISECONDS);                
                
            }
        }
   }
    
  
   
    
    
    
    
    
    
}
