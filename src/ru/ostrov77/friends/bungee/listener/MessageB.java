package ru.ostrov77.friends.bungee.listener;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ru.ostrov77.auth.bungee.BungeeChanelEvent;
import ru.ostrov77.auth.bungee.Listener.BungeeMsgHandler;

import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.P_set;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.PartyManager;




public class MessageB implements Listener {

    
    // BUNGEE !!!
    
    @EventHandler
    public void onMessage(final BungeeChanelEvent e) {
        
        
        String res;

        switch(e.action) {
            
            case PF_FRIENDS_ONLINE:
                if (!checkPlayer(e)) return;
                res="";
                for (String nik:ManagerB.getPFplayer(e.getPlayer().getName()).online_friends) {
                    if (BungeeCord.getInstance().getPlayer(nik)!=null) res=","+nik+"<>"+BungeeCord.getInstance().getPlayer(nik).getServer().getInfo().getName()+"<>"+(ManagerB.getPFplayer(e.getPlayer().getName()).blocked.contains(nik)?"1":"0");
                }
                res=res.replaceFirst(",", "");
                //sendMessage(pp, type, String.join(",", pfb.online_friends));
                BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, res);
                return;

            case PF_FRIENDS_OFFLINE:
                if (!checkPlayer(e)) return;
                BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, String.join(",", ManagerB.getPFplayer(e.getPlayer().getName()).offline_friends));
                return;
                
            //case FRIEND_COMMAND:
//System.out.println("--onPluginMessage FRIEND_COMMAND raw="+"fr "+raw);                            
                //MainB.getInstance().getProxy().getPluginManager().dispatchCommand(pp, "fr "+raw);
                //Fr.proccesFrCommand(pp, raw);
                //return;
                
            case PF_FRIEND_SETTINGS:
                if (!checkPlayer(e)) return;
                res="";
                for (F_set set : ManagerB.getPFplayer(e.getPlayer().getName()).settings.keySet()) {
                    if(set!=F_set.нет && ManagerB.getPFplayer(e.getPlayer().getName()).settings.get(set)!=set.default_value) res=res+","+set.tag+"_"+ManagerB.getPFplayer(e.getPlayer().getName()).settings.get(set);
                }
                res=res.replaceFirst(",", "");
                BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, res);
                return;
                
            case PF_PARTY_MEMBER:
                if (!checkPlayer(e)) return;
                if (PartyManager.hasParty(e.getPlayer())) BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, PartyManager.Get_player_party(e.getPlayer()).memberAndServerToString());
                else BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, "");
                return;
                
            //case PARTY_COMMAND:
//System.out.println("--onPluginMessage PARTY_COMMAND raw="+"party "+raw);                            
                //PartyCmd.proccesPartyCommand(pp, raw);
                //MainB.getInstance().getProxy().getPluginManager().dispatchCommand(pp, "party "+raw);
                //return;
                
            case PF_PARTY_SETTINGS:
                if (!checkPlayer(e)) return;
                res="";
                for (P_set set : ManagerB.getPFplayer(e.getPlayer().getName()).party_settings.keySet()) {
                    if(set!=P_set.нет && ManagerB.getPFplayer(e.getPlayer().getName()).party_settings.get(set)!=set.default_value) res=res+","+set.tag+"_"+(ManagerB.getPFplayer(e.getPlayer().getName()).party_settings.get(set)?"1":"0");
                }
                res=res.replaceFirst(",", "");
                BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, res);
                return;
                
                
                
                
            case PF_CALLBACK_RUN:
                if (!checkPlayer(e)) return;
                BungeeMsgHandler.sendBungeeMessage(e.getPlayer(), e.action, e.senderInfo, e.s1);
                return;
                
                
        } 
        
        
    }

    
    private static boolean checkPlayer (final BungeeChanelEvent e) {
        if (e.getPlayer()==null) {
            MainB.log_err("onMessage pp=null,  from="+e.senderInfo+", raw="+e.s1);
            return false;
        }
        return true;

    }
    
    
    /*    
private void toDo(String oReceived, Server pServer) {
        try {
            StringTokenizer e = new StringTokenizer(oReceived, "|");
            String instructions = e.nextToken();
            String playerNameSender = e.nextToken();

            ProxiedPlayer pl;
            //String s;
            int j;
            String[] args;
            
            
            
            
            
            
            
    switch (instructions.hashCode()) {
         
        
        
    case 303781755:
        if (instructions.equals("getHideMode")) {
                this.sendMessage("openHideMenu|" + playerNameSender + "|" + FM.getPFplayer(playerNameSender).getIntSettings(F_set.ВИДЕТЬ_РЕЖИМ), pServer.getInfo());
        }
                    break;
                
        
        
        
    case -874576970:
    //0 Видеть всех
    //1 Видеть друзей и участников команды
    //2 Видеть только друзей
    //3 Видеть только участников команды
    //4 Скрыть всех

        if (instructions.equals("verstecktespielerbekommen")) {  //обновление режима показа
               ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(playerNameSender);
               String s = "/";
                j = FM.getPFplayer(playerNameSender).getIntSettings(F_set.ВИДЕТЬ_РЕЖИМ);

                switch (j) {
                    case 1:
                        {
                            TreeSet <String> online = FM.getPFplayer(playerNameSender).online_friends;
                            List <String> pt;
                            if (PartyManager.getParty(sender) != null)  pt = PartyManager.getParty(sender).getAllPlayers();
                            else pt = new ArrayList<>();
                            //List <String> res = joinLists(online, pt);
                            //if ( !res.isEmpty() ) {
                            //    s = res.toString().replaceAll("\\[|\\]|\\s", "");
                            //    s = s.replaceAll(",", "<:>");
                            //}     
                            break;
                        }
                    case 2:
                        {
                            TreeSet <String> online = FM.getPFplayer(playerNameSender).online_friends;
                            if ( !online.isEmpty() ) {
                                s = online.toString().replaceAll("\\[|\\]|\\s", "");
                                s = s.replaceAll(",", "<:>");
                            }       break;
                        }
                    case 3:
                        {
                            List <String> pt;
                            if (PartyManager.getParty(sender) != null)  pt = PartyManager.getParty(sender).getAllPlayers();
                            else pt = new ArrayList<>();
                            if ( !pt.isEmpty() ) {
                                s = pt.toString().replaceAll("\\[|\\]|\\s", "");
                                s = s.replaceAll(",", "<:>");
                            }       break;
                        }
                    default:
                        break;
                }

                this.sendMessage("hidePlayers|" + playerNameSender + "|" + j + "|" + s, ProxyServer.getInstance().getPlayer(playerNameSender).getServer().getInfo());
        }
                break;
            
        
        
        
        
    case 1077589643:        //получить друзей
        if (instructions.equals("Hauptinventar")) {
                ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(playerNameSender);
            
                TreeSet <String> friend = FM.getPFplayer(playerNameSender).online_friends;
                String l1="/";
                String l2="/";
                
                for (String fr : friend) {
                    ProxiedPlayer f = ProxyServer.getInstance().getPlayer(fr);
                    if (f != null && FM.getPFplayer(f).getSettings(F_set.СКРЫВАТЬ_ПРИСУТСТВИЕ)) { //онлайн и не скрытые
                        l1 = l1 + fr + "%" + f.getServer().getInfo().getName() + "<:>";
                    } else {
                        l2 = l2 + fr + "%-" + "<:>";
                    }  
                }
                    
                    if (l1.length()>5) l1= l1.substring(1,l1.length()-3);
                    if (l2.length()>5) l2= l2.substring(1,l2.length()-3);
                    
                this.sendMessage( "Hauptinventar|" + playerNameSender+"<>"+sender.getServer().getInfo().getName() + "|" + l1+","+l2 + "|" + e.nextToken() , sender.getServer().getInfo());
        }
                break;
                
        
        
    case -651735572:                                                //режим показа
        if (instructions.equals("setEinstellungVersteckte")) { 
                int l = Integer.parseInt(e.nextToken());

                FM.getPFplayer(playerNameSender).settings.put(F_set.ВИДЕТЬ_РЕЖИМ, l);
                //MySQL.setSetting(playerNameSender, 6, l);
                ProxiedPlayer proxiedplayer = ProxyServer.getInstance().getPlayer(playerNameSender);

                if (MainB.getInstance().getConfig().getString("GUI.ChangedHideModeMessage").equalsIgnoreCase("true")) {
                    switch (l) {
                    case 0:
                        proxiedplayer.sendMessage(new TextComponent("§fТеперь вы видете всех игроков!"));
                        break;

                    case 1:
                        proxiedplayer.sendMessage(new TextComponent("§fВы видете только друзей!"));
                        break;

                    case 2:
                        proxiedplayer.sendMessage(new TextComponent("§fВы видете друзей и членов вашей команды!"));
                        break;

                    case 3:
                        proxiedplayer.sendMessage(new TextComponent("§fВы видете только членов вашей команды!"));
                        break;

                    case 4:
                        proxiedplayer.sendMessage(new TextComponent("§fВсе игроки скрыты!"));
                    }
                }
        }
                break;
                
        
        
        
    case -1700815835:
        if (instructions.equals("schieckeFreundschaftsAnfrage")) {
                String s = e.nextToken();
              //  if (!MySQL.isAFriendOf(ProxyServer.getInstance().getPlayer(playerNameSender).getName(), ProxyServer.getInstance().getPlayer(s).getName())) {
                    args = new String[] { "add", s};
                    MainB.getInstance().getFriendsCommand().execute(ProxyServer.getInstance().getPlayer(playerNameSender), args);
                //}
        }
                    break;
                
        
        
    case 1955373352:
        if (instructions.equals("Accept")) {
                args = new String[] { "accept", e.nextToken()};
                MainB.getInstance().getFriendsCommand().execute(ProxyServer.getInstance().getPlayer(playerNameSender), args);
        }
                break;
           
        
        
    case -1087964458:
        if (instructions.equals("Decline")) {
                args = new String[] { "deny", e.nextToken()};
                MainB.getInstance().getFriendsCommand().execute(ProxyServer.getInstance().getPlayer(playerNameSender), args);
        }
                break;
           

        
        
    case 432970218:
        if (instructions.equals("EinstellungenAendern")) {
                args = new String[] { "settings", e.nextToken()};
                MainB.getInstance().getFriendsCommand().execute(ProxyServer.getInstance().getPlayer(playerNameSender), args);
        }
                    break;
           
        
        
    case -888285836:
        if (instructions.equals("partyEinladen")) {
               pl = ProxyServer.getInstance().getPlayer(playerNameSender);
                args = new String[] { "invite", e.nextToken()};
                MainB.getInstance().getPartyCommand().execute(pl, args);
        }
                    break;
            
        
        
    case 340785551:
        if (instructions.equals("loesche")) {
                args = new String[] { "remove", e.nextToken()};
                MainB.getInstance().getFriendsCommand().execute(ProxyServer.getInstance().getPlayer(playerNameSender), args);
        }
                    break;
              
        
        
    case 1429417299:
        if (instructions.equals("springeZu")) {
                args = new String[] { "jump", e.nextToken()};
                MainB.getInstance().getFriendsCommand().execute( ProxyServer.getInstance().getPlayer(playerNameSender), args );
        }
                    break;
        
        
        
    case 2001657672:
        if (instructions.equals("writemessage")) {
                args = new String[] { "msg", e.nextToken()};
                ProxyServer.getInstance().getPlayer(playerNameSender).unsafe().sendPacket(new Chat("{\"text\":\""
                        + MainB.friendsPrefix + "§e§l** §f§lКликните сюда, чтобы написать сообщение другу §e§l**"
                        + "\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/" + "fm " + args[1] + " "
                        +"\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" 
                        + "§5§oКликните на сообщение, чтобы написать!" + "\"}]}}}"));
        }
                    break;
                
        
    default:
        break;
                
            }
            

        } catch (NullPointerException nullpointerexception) {
           // nullpointerexception.printStackTrace();
        }

    }
*/






}
