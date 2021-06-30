package ru.ostrov77.friends.spigot;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.komiss77.enums.Operation;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.profile.E_Prof;
import ru.komiss77.modules.player.profile.mainHandler;


import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.ItemUtils;
import ru.ostrov77.friends.E_view;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.P_set;
import ru.ostrov77.friends.spigot.listener.OstrovChanelListener;








public class ManagerS {
        public static HashMap<String,PFplayerS>pf_players;
        //public static ItemStack allow;
        //public static ItemStack deny;

    public static void Init() {
        pf_players=new HashMap<>();
        //allow=new ItemBuilder(Material.CONCRETE,(byte)13)
    }
        
        
    
//********************* Менеджер игроков *************************************
    
    
    public static PFplayerS getPFplayer( final Player p) {
        return getPFplayer(p.getName());
    }
    
    public static PFplayerS getPFplayer( final String nik) {
        if (pf_players.containsKey(nik)) return pf_players.get(nik);
        else {
            if (Bukkit.getPlayer(nik)==null) return null; //всё же, подстраховка
            PFplayerS pfp=new PFplayerS(nik);
            pf_players.put(nik, pfp);
            return pfp;
        }
    }
   
    public static boolean exist(final String nik) {
        return pf_players.containsKey(nik);
    }
   
//***************************************************************************

    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void openFriendList(final Oplayer op, final int page, final boolean delete) {
        clean(op.profile);
        final PFplayerS pfp=getPFplayer(op.nik);
        pfp.friend_current_page=page;
        pfp.friend_delete_mode=delete;
        if (pfp.friend_current_page<0) pfp.friend_current_page=0;
        
//System.out.println("openFriendList 1");
        if (MainS.load_friends) {
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_FRIENDS_ONLINE, "" );
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_FRIENDS_OFFLINE, "" );
//System.out.println("openFriendList 2 p="+op.getPlayer().getName());
            
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_CALLBACK_RUN, "", () -> {
//System.out.println("!!!+++++++++++ Runnable ");
                if (pfp.online_friends.isEmpty() && pfp.offline_friends.isEmpty()) {//пока еще нет
                    op.profile.setItem(22, ItemUtils.friend_empty.clone());
                    op.getPlayer().updateInventory();
                    return;
                }

                int current=0;
                int begin=pfp.friend_current_page*36;
                int out=36;
                
                mainHandler.setcolorGlassLine(op.profile, E_Prof.ДРУЗЬЯ.mat);
                if (pfp.friend_current_page>0) op.profile.setItem(36, ItemUtils.previos_page);
                
                boolean next_page=false;
                
                for ( String raw:pfp.online_friends) {
                    if (current>=begin) {
//System.out.println("цикл online_friends size="+pfp.online_friends.size()+" begin="+begin+" current="+current+" out="+out+" raw_nik=>"+raw+"<");
                            op.profile.addItem(friendOnlineHead(raw, delete));
                            out--;
                            if (out==0) {
                                next_page=true;
                                break;
                            }
                    }
                    current++;
                }
                if (out>0) {
                    for ( String nik:pfp.offline_friends) {
                        if (current>=begin) {
//System.out.println("цикл offline_friends size="+pfp.offline_friends.size()+" begin="+begin+" current="+current+" out="+out+" nik=>"+nik+"<");
                                op.profile.addItem(friendOfflineHead(nik,delete));
                                out--;
                                if (out==0) {
                                    next_page=true;
                                    break;
                                }
                        }
                        current++;
                    }
                }
                
                if(next_page) op.profile.setItem(44, ItemUtils.next_page);
                op.getPlayer().updateInventory();
            });
            if (MainS.hide_mode) {
                OstrovChanelListener.request(op.getPlayer(), Operation.PF_FRIEND_SETTINGS, "" );
                OstrovChanelListener.request(op.getPlayer(), Operation.PF_CALLBACK_RUN, "", () -> {
                    pfp.updateViewMode(false);
                 });
            }
        }
    }

    public static void openFriendSettings(final Player p, final Oplayer op) {
        clean(op.profile);
        final PFplayerS pfp=getPFplayer(op.nik);
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_FRIEND_SETTINGS, "" );
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_CALLBACK_RUN, "", () -> {
//System.out.println("!!!+++++++++++ openFriendSettings ");
                for (F_set set_:F_set.values()) {
//System.out.println("-- set="+set_.toString()+" curr="+pfp.getIntSettings(set_));
                    injectSettingsItem(op, set_, pfp.getFriendSettings(set_));
                }
                
            });
        p.updateInventory();
    }
    
    public static void injectSettingsItem(final Oplayer op, final F_set set, final int value) {
        if (set==F_set.нет) return;
        if (set==F_set.ВИДЕТЬ_РЕЖИМ) {
            if (MainS.hide_mode) {
                for (E_view view:E_view.values()) {
    //System.out.println("inject E_view="+view.toString()+" slot="+view.inv_slot+" data="+((byte)(value==view.tag?4:0)));
                    //op.profile.setItem(view.inv_slot, new ItemBuilder(Material.STAINED_GLASS, (byte)(value==view.tag?4:0)).setName(view.item_name).setLore(ItemUtils.Gen_lore(null, view.item_lore, "")).build());
                    if (value==view.tag) op.profile.setItem(view.inv_slot, new ItemBuilder(Material.RED_STAINED_GLASS).setName(view.item_name).setLore(ItemUtils.Gen_lore(null, view.item_lore, "")).build());
                    else op.profile.setItem(view.inv_slot, new ItemBuilder(Material.BLACK_STAINED_GLASS).setName(view.item_name).setLore(ItemUtils.Gen_lore(null, view.item_lore, "")).build());
                }
            }
        } else {
//System.out.println("---- inject data="+((byte)(value==0?13:14))+" name="+set.item_name+" lore="+set.item_lore);
//System.out.println("------ inject is="+is);
            //if (set.inv_slot>=0) op.profile.setItem(set.inv_slot, new ItemBuilder(Material.CONCRETE, (byte)(value==1?13:14)).setName(set.item_name).setLore(ItemUtils.Gen_lore(null, set.item_lore, "")).build());
            if (set.inv_slot>=0) {
                if (value==1) op.profile.setItem(set.inv_slot, new ItemBuilder(Material.GREEN_CONCRETE).setName(set.item_name).setLore(ItemUtils.Gen_lore(null, set.item_lore, "")).build());
                else op.profile.setItem(set.inv_slot, new ItemBuilder(Material.RED_CONCRETE).setName(set.item_name).setLore(ItemUtils.Gen_lore(null, set.item_lore, "")).build());
            }
        }
    }
    
    public static ItemStack friendOnlineHead(final String raw, final boolean delete) {
        String[]data=raw.split("<>");
        if(data.length!=3) return null;
        ItemStack head = new ItemStack(Material.PLAYER_HEAD,1);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName( (delete?"§cУдалить §4":"§a")+data[0]);//meta.setDisplayName( "§a"+data[0]);
        head.setItemMeta(meta);
        SkullMeta metah = (SkullMeta) head.getItemMeta();
        metah.setOwner(data[0]);
        final boolean blocked=data[2].equals("1");
        metah.setLore( ItemUtils.Gen_lore(null, (blocked?"§c✕ блокировка":"")+"<br>§7На сервере §b"+data[1]+"<br>§5ЛКМ - запрос на телепорт<br>§5ПКМ - пригласить в команду<br>§5Shift+клик блок/разблок<br>§fЧтобы написать сообщение, наберите<br>§b/fr m "+data[0]+" сообщение", "") );
        //    else metah.setLore( ItemUtils.Gen_lore(null, "§4Не в сети", "") );
        head.setItemMeta(metah);
        return head;
    }
    public static ItemStack friendOfflineHead(final String nik, final boolean delete) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName( (delete?"§cУдалить ":"")+"§4"+nik);
        head.setItemMeta(meta);
        SkullMeta metah = (SkullMeta) head.getItemMeta();
        metah.setOwner(nik);
        metah.setLore( ItemUtils.Gen_lore(null, "§4Не в сети<br>§5ПКМ - оставить<br>§5оффлайн сообщение", "") );
        head.setItemMeta(metah);
        return head;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void openPartyList(final Oplayer op, final boolean delete) {
        clean(op.profile);
        final PFplayerS pfp=getPFplayer(op.nik);
        pfp.friend_delete_mode=delete;
        
//System.out.println("openFriendList 1");
        if (MainS.load_party) {
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_PARTY_MEMBER, "" );
//System.out.println("openFriendList 2 p="+op.getPlayer().getName());
            
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_CALLBACK_RUN, "", () -> {
//System.out.println("!!!+++++++++++ Runnable ");
                if (op.getPartyMembers().isEmpty()) {//пока еще нет
                    op.profile.setItem(22, ItemUtils.party_empty.clone());
                    op.getPlayer().updateInventory();
                    return;
                }
                mainHandler.setcolorGlassLine(op.profile, E_Prof.КОМАНДА.mat);
                
                //if (delete && !pfp.party_members.get(0).startsWith(op.nik)) {
                if (delete && !op.isPartyLeader()) {
                    op.getPlayer().sendMessage(MainS.friendsPrefix+"§cВыгонять может только лидер!");
                    return;
                }
                int pos=18;
                for ( String name:op.getPartyData().keySet()) {
//System.out.println("цикл online_friends size="+pfp.online_friends.size()+" begin="+begin+" current="+current+" out="+out+" raw_nik=>"+raw+"<");
                    //if (pfp.isPartyLeader()) {
                    if (op.party_leader.equals(name)) {
                        if (!delete) op.profile.setItem(13, partyHead(op, name, op.getPartyData().get(name), delete));
                    } else {
                        op.profile.setItem(pos, partyHead(op, name, op.getPartyData().get(name), delete));
                        pos++;
                    }
                }
                op.getPlayer().updateInventory();
            });
        }
    }

    public static ItemStack partyHead(final Oplayer op, final String name, final String server, final boolean delete) {
        //String[]data=raw.split("<>");
        final boolean leader_icon=name.equals(op.party_leader);//raw.equals(actor.party_members.get(0));
        //final boolean himself=data[0].equals(actor.nik);
        //final boolean leader_menu=actor.nik.equals(actor.party_members.get(0).split("<>")[0]);
        //if(data.length!=2) return null;
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName( (delete?"§cУдалить §4":"§a")+name);
        head.setItemMeta(meta);
        SkullMeta metah = (SkullMeta) head.getItemMeta();
        metah.setOwner(name);
//System.out.println("--partyHead owner="+owner+" raw="+raw+" leader?"+leader+" data[0]="+data[0]);        
        metah.setLore( ItemUtils.Gen_lore(null, (
                leader_icon?"§eЛидер"+(name.equals(op.nik)?" (Вы)":""):"§6Участник")+
                "<br>§7На сервере §b"+server +
                "<br>§5ЛКМ - командный чат"+
                (!leader_icon && op.isPartyLeader()?"<br>§5ПКМ - назначить лидером":"")+ //не иконка лидера, но просматривает лидер 
                ( leader_icon? (op.isPartyLeader() && op.getPartyMembers().size()==1?"<br>§5Shift+клик распустить команду":"<br>§5Shift+клик покинуть команду"):"" ),//на иконке лидера-покинуть
                "") );
        head.setItemMeta(metah);
        return head;
    }
    
    public static void openPartySettings(final Player p, final Oplayer op) {
        clean(op.profile);
        final PFplayerS pfp=getPFplayer(op.nik);
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_PARTY_SETTINGS, "" );
            OstrovChanelListener.request(op.getPlayer(), Operation.PF_CALLBACK_RUN, "", () -> {
//System.out.println("!!!+++++++++++ openFriendSettings ");
                for (P_set p_set:P_set.values()) {
                    injectPartySettingsItem(op, p_set, pfp.getPartySettings(p_set));
//System.out.println("-- set="+set_.toString()+" curr="+pfp.getIntSettings(set_));
                    //if (p_set.inv_slot>=0) op.profile.setItem(p_set.inv_slot, new ItemBuilder(Material.CONCRETE, (byte)(pfp.getPartySettings(p_set)?13:14)).setName(p_set.item_name).setLore(ItemUtils.Gen_lore(null, p_set.item_lore, "")).build());
                }
            });
        p.updateInventory();
    }
    
    public static void injectPartySettingsItem(final Oplayer op, final P_set p_set, final boolean value) {
        if (p_set.inv_slot>=0) {
            //op.profile.setItem(p_set.inv_slot, new ItemBuilder(Material.CONCRETE, (byte)(value?13:14)).setName(p_set.item_name).setLore(ItemUtils.Gen_lore(null, p_set.item_lore, "")).build());
            if (value)op.profile.setItem(p_set.inv_slot, new ItemBuilder(Material.GREEN_CONCRETE).setName(p_set.item_name).setLore(ItemUtils.Gen_lore(null, p_set.item_lore, "")).build());
            else op.profile.setItem(p_set.inv_slot, new ItemBuilder(Material.RED_CONCRETE).setName(p_set.item_name).setLore(ItemUtils.Gen_lore(null, p_set.item_lore, "")).build());
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private static void clean(Inventory inv) {
        for (int i=0;i<=35;i++) {
            inv.setItem(i, null);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    
    
    
        

}
