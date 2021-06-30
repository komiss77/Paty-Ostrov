package ru.ostrov77.friends.bungee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.auth.bungee.Auth;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.P_set;






public class ManagerB {
    
    private static ConcurrentHashMap<String,PFplayerB>pf_players;
    
    public ManagerB(MainB plugin) {
        pf_players=new ConcurrentHashMap<>();
    }
    
    public static void forceRemove(final String nik) {
        if (pf_players.containsKey(nik) && ProxyServer.getInstance().getPlayer(nik)==null) pf_players.remove(nik);
    }

    public static void join(final ProxiedPlayer pp) {
        getPFplayer(pp);
    }
    
    public static PFplayerB getPFplayer( final ProxiedPlayer pp) {
        return getPFplayer(pp.getName());
    }
    
    public static PFplayerB getPFplayer( final String nik) {
        if (pf_players.containsKey(nik)) {
            return pf_players.get(nik);
        } else {
            if (ProxyServer.getInstance().getPlayer(nik)==null) return null; //всё же, подстраховка
            PFplayerB pfp=new PFplayerB(nik);
            pf_players.put(nik, pfp);
            return pfp;
        }
    }
   
    public static boolean exist(final String nik) {
        if (!nik.isEmpty() && pf_players.containsKey(nik)) {
            if (ProxyServer.getInstance().getPlayer(nik)!=null) {
                return true;
            } else {
                pf_players.remove(nik);
                return false;
            }
        } else return false;
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    
//************************************************ ДРУЗЬЯ - УПРАВЛЕНИЕ ************************************************
    
    
    public static boolean isFriends(String nik, String check) {
        return exist(nik) && (pf_players.get(nik).isFriend(check));
    }
    
    public static TreeSet getOnlineFriends(final String nik) {      
        if(exist(nik)) return pf_players.get(nik).online_friends;
        else return new TreeSet();
    }
    
    public static void addFriend(final String кто, final String кого) {
        MainB.getInstance().getProxy().getScheduler().runAsync(MainB.getInstance(), () -> {
            //FM addFriend : Column count doesn't match value count at row 1  INSERT INTO `fr_friends` (`f1`, `f2`) VALUES ('cccc', 'cccc');
            PreparedStatement pst = null;
            try { 
                pst= Auth.GetConnection().prepareStatement("INSERT INTO `fr_friends` (`f1`, `f2`) values (\'"+кто+"\', \'"+кого+"\') " );
                pst.executeUpdate();
                
                
            } catch (SQLException ex) { 
                MainB.log_err("FM addFriend : "+ex.getMessage());
            } finally {
                
                if (exist(кто)) pf_players.get(кто).addFriend(кого);
                if (exist(кого)) pf_players.get(кого).addFriend(кто);
                
                try {
                    if (pst!=null) pst.close();
                } catch (SQLException ex) {
                    MainB.log_err("FM addFriend 2: "+ex.getMessage());
                }
            }
        });
    }
    

    public static void delFriend(final String кто, final String кого, final boolean notify) {
        MainB.getInstance().getProxy().getScheduler().runAsync(MainB.getInstance(), () -> {
            PreparedStatement pst = null;
            try {
                pst = Auth.GetConnection().prepareStatement("DELETE FROM `fr_friends` WHERE (f1 = \'"+кто+"\' AND f2=\'"+кого+"\') OR (f1 = \'"+кого+"\' AND f2=\'"+кто+"\') ");
                pst.execute();
                
                
            } catch (SQLException ex) { 
                MainB.log_err("FM delFriend : "+ex.getMessage());
            } finally {
                
                if (exist(кто)) pf_players.get(кто).delFriend(кого,notify);
                if (exist(кого)) pf_players.get(кого).delFriend(кто,notify);
                
                try {
                    if (pst!=null) pst.close();
                } catch (SQLException ex) {
                    MainB.log_err("FM addFriend 2: "+ex.getMessage());
                }
            }
        });
    }

//**************************************************************************************************
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//************************************************ ВХОД + ВЫХОД ************************************************

    
    public static void loaded(final PFplayerB join) {
        
//System.out.println("loaded 111 online_friends="+join.online_friends+"   offline_friends="+join.offline_friends+"  set="+join.settings);        
        if (join.offline_friends.isEmpty() && join.online_friends.isEmpty()) {
            join.sendMessage(new TextComponent(MainB.friendsPrefix +"§f§oДобавляйте друзей, создавайте команды через меню!" ));
            return;
        }
        
        final HoverEvent hover = new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик на зачки!").create() );
        TextComponent join_msg=null;
        if (join.getSettings(F_set.ОПОВЕЩ_ВХОД_ОТПРАВЛЯТЬ)) {
            join_msg = new TextComponent(MainB.friendsPrefix +"§2☺ §2"+ join.nik );
            join_msg.setHoverEvent(hover);
            join_msg.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/fr m "+join.nik+" " ) );
            join_msg.addExtra(logo_mail(join.nik));
            join_msg.addExtra(logo_tp(join.nik));
        }
        TextComponent online=new TextComponent(MainB.friendsPrefix+": ");
        TextComponent current=null;
        
        for (PFplayerB pf_ : pf_players.values() ) {
            if (pf_.playerJoin(join.nik,join_msg)){
                current=new TextComponent( "   §a"+pf_.nik );
                current.setHoverEvent(hover);
                current.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/fr m "+pf_.nik+" " ) );
                current.setColor(ChatColor.GREEN);
                current.addExtra(logo_mail(pf_.nik));
                current.addExtra(logo_tp(pf_.nik));
                online.addExtra(current);
            }
        }
                
        if (current==null) online.addExtra("§e☏ Все друзья оффлайн, но обещали скоро зайти. §f(͡๏̯͡๏)");
        join.sendMessage(online);
        
    }
                

    public static TextComponent logo_deny (final String nik) {
        final TextComponent block=new TextComponent(" §4✕");
        block.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr block "+nik));
        block.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Заблокировать "+nik+" до перезахода.").create() ) );
        return block;
    }

    public static TextComponent logo_mail (final String nik) {
        final TextComponent mail=new TextComponent(" §6✉");
        mail.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr m "+nik+" "));
        mail.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eНаписать "+nik+" сообщение.").create() ) );
        return mail;
    }
    
    public static TextComponent logo_tp (final String nik) {
        final TextComponent tp=new TextComponent(" §3✈");
        tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr jump "+nik));
        tp.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Отправить "+nik+" запрос на ТП.").create() ) );
        return tp;
    }

    
    public static void disconnect(final ProxiedPlayer pp) {
        if(!exist(pp.getName())) return;
        final PFplayerB exit=getPFplayer(pp);
        //final boolean notyfy = ;

        MainB.getInstance().getProxy().getScheduler().runAsync(MainB.getInstance(), () -> {
            TextComponent msg=null;
            if (exit.getSettings(F_set.ОПОВЕЩ_ВЫХОД_ОТПРАВЛЯТЬ)) {
                 msg = new TextComponent(MainB.friendsPrefix +" §4§m"+exit.nik+"§r  §8<клик-оставить оффлайн сообщение" );
                msg.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик - оставить оффлайн - сообщение!").create() ) );
                msg.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/fr m "+exit.nik+" " ) );
            }

                for (PFplayerB pf_ : pf_players.values() ) {
                    pf_.playerQuit(pp.getName(), msg);
                    //if (pf_.online_friends.contains(exit.nik)) {
                    //    pf_.online_friends.remove(exit.nik);
                    //    pf_.offline_friends.add(exit.nik);
                    //    if (notyfy && pf_.getSettings(F_set.ОПОВЕЩ_ВЫХОД_ПОЛУЧАТЬ)) {
                    //        pf_.sendMessage(msg);
                    //    }
                   // }
                }
            
        //PlayerParty party = PartyManager.getParty(pEvent.getPlayer());
        //if (party != null)  party.leaveParty(pEvent.getPlayer());
        
            String settings="";
            for (F_set set : exit.settings.keySet()) {
                if(set!=F_set.нет && exit.settings.get(set)!=set.default_value) settings=settings+","+set.tag+"_"+exit.settings.get(set);
            }
            final String fr_set=settings.replaceFirst(",", "");
            settings="";
            for (P_set set : exit.party_settings.keySet()) {
                if(set!=P_set.нет && exit.party_settings.get(set)!=set.default_value) settings=settings+","+set.tag+"_"+(exit.party_settings.get(set)?"1":"0");
            }
            final String p_set=settings.replaceFirst(",", "");

            
            PreparedStatement pst = null;
            try {
                pst = Auth.GetConnection().prepareStatement("INSERT INTO `fr_settings` (`name`, `settings`, `pset`) VALUES " 
                       + "('"+exit.nik+"','"+fr_set+"','"+p_set+"') " + 
                       "ON DUPLICATE KEY UPDATE " 
                       + "`settings`='"+fr_set+"', `pset`='"+p_set+"' ");
                pst.executeUpdate();
                
            } catch (SQLException ex) { 
                MainB.log_err("FM saveSettings : "+ex.getMessage());
            } finally {
                pf_players.remove(exit.nik);
                try {
                    if (pst!=null) pst.close();
                } catch (SQLException ex) {
                    MainB.log_err("FM saveSettings 2: "+ex.getMessage());
                }
            }
        });
    }
    
    
//***************************************************************************************************
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//************************************************ СООБЩЕНИЯ ************************************************
    
    public static boolean msgCoolDown(final String name, final int sec) {
        return !(Auth.currentTimeSec() - getPFplayer(name).last_msg_time > sec);
    }
    public static boolean msgTpCoolDown(final String name, final int sec) {
        return !(Auth.currentTimeSec() - getPFplayer(name).last_jump_time > sec);
    }
    public static boolean msgOffCoolDown(final String name, final int sec) {
        return !(Auth.currentTimeSec() - getPFplayer(name).last_offmsg_time > sec);
    }

    public static void trySengOflineMsg(final ProxiedPlayer sender, final String reciever, final String msg) {
        MainB.getInstance().getProxy().getScheduler().runAsync(MainB.getInstance(), () -> {
            boolean deny_off_msg=false;
            
            try (Statement stmt = Auth.GetConnection().createStatement(); //SELECT 'id' FROM `fr_settings` WHERE `name`='komiss77' AND`settings` LIKE '%10_1%' 
                 //ResultSet rs = stmt.executeQuery("select `offlinemsg` from `fr_settings` WHERE name='"+reciever+"' AND `offlinemsg`='1' ")) {
                    ResultSet rs = stmt.executeQuery("SELECT 'id' FROM `fr_settings` WHERE `name`='"+reciever+"' AND `settings` LIKE '%"+String.valueOf(F_set.СООБЩЕНИЯ_ОФФЛАЙН.tag)+"_0%' ")) {
                    deny_off_msg=rs.next();
//System.out.print("res="+deny_off_msg+" query=SELECT 'id' FROM `fr_settings` WHERE `name`='"+reciever+"' AND `settings` LIKE '%"+String.valueOf(F_set.СООБЩЕНИЯ_ОФФЛАЙН.tag)+"_0%' ");
                    rs.close();
                    stmt.close();
            } catch (SQLException ex) { 
                MainB.log_err("FM trySengOflineMsg 1 : "+ex.getMessage());
            }
            
            if (!deny_off_msg) {
                try (PreparedStatement pst = Auth.GetConnection().prepareStatement("insert into `fr_messages` values (?, ?, ?, ?)")) {
                    pst.setString(1, reciever);
                    pst.setString(2, sender.getName());
                    pst.setString(3, msg);
                    pst.setLong(4, Auth.currentTimeSec());
                    pst.executeUpdate();
                } catch (SQLException ex) { 
                    MainB.log_err("FM trySengOflineMsg 2 : "+ex.getMessage());
                }
                sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§aАдресат получит Ваше сообщение при входе!"));
            } else {
                sender.sendMessage(new TextComponent(MainB.friendsPrefix + "§5у адресата в настройках отключены оффлайн-сообщения."));
            }
            
        });
    }


    public static String findLastWriter(final String name) {
        for (PFplayerB pfp:pf_players.values()) {
            if (!pfp.nik.equals(name) && pfp.last_msg_write_to.equals(name)) return pfp.nik;
        }
        return "";
    }


    public static void readNextOfflineMsg(final ProxiedPlayer pp) {
        MainB.getInstance().getProxy().getScheduler().runAsync(MainB.getInstance(), () -> {
            int time=-1;
            TextComponent msg=null;
            
            try (Statement stmt = Auth.GetConnection().createStatement(); 
                 ResultSet rs = stmt.executeQuery("select * from `fr_messages` WHERE `reciever`='"+pp.getName()+"' LIMIT 1 ")) {
                    if(rs.next()) {
                        time=rs.getInt("time");
                        msg=new TextComponent("§6Сообщение от §e"+rs.getString("sender")+" §e: §f"+rs.getString("message")+" §8<клик-следущее");
                        msg.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("§5§oКлик - читать следущее \n§7Отправлено: §f"+time ) ));
                        msg.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fr mread" ) );
                    }
                    rs.close();
                    stmt.close();
                    
            } catch (SQLException ex) { 
                MainB.log_err("FM readOfflineMsg 1 : "+ex.getMessage());
            }
            
            
            if (time>0) {
                try (PreparedStatement pst = Auth.GetConnection().prepareStatement("DELETE FROM `fr_messages` WHERE `reciever`='"+pp.getName()+"' AND`time`='"+time+"' ")) {
                    pst.executeUpdate();
                } catch (SQLException ex) { 
                    MainB.log_err("FM readOfflineMsg 2 : "+ex.getMessage());
                }
                if (msg!=null) pp.sendMessage(msg);
            } else {
                msg=new TextComponent("§5Непрочитанных сообщений не осталось!");
                pp.sendMessage(msg);
            }
            
        });
    }

//****************************************************************************************************






















    
    
    
    
    
    
    public static ProxiedPlayer GetProxyPlayer(final String nik) {
        return ProxyServer.getInstance().getPlayer(nik);
    }
    
    
    
    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    
    
}
