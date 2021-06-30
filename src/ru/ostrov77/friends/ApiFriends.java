package ru.ostrov77.friends;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.komiss77.ApiOstrov;
import ru.komiss77.enums.Operation;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;
import ru.komiss77.utils.ItemUtils;

import ru.ostrov77.friends.spigot.MainS;
import ru.ostrov77.friends.spigot.ManagerS;
import ru.ostrov77.friends.spigot.PFplayerS;








public class ApiFriends {

    public ApiFriends() {}

    public static void onFriendProfileIconClick(final Player p, final InventoryClickEvent e) {
//System.out.println("---->>>> onFriendProfileIconClick p="+p.getName());
        final Oplayer op = PM.getOplayer(p.getName());
        if (!MainS.load_friends) {
            op.profile.setItem(22, ItemUtils.profile_deny.clone());
            p.updateInventory();
            return;
        }
        if (e.isShiftClick()) {
            ManagerS.openFriendList(op, 0,true); //удаление
        } else if (e.isRightClick()) {
            ManagerS.openFriendSettings(p, op);
        } else if (e.isLeftClick()) {
            ManagerS.openFriendList(op, 0, false); //список
        }
    } 

    public static void onFriendFieldClick(final Player p, final InventoryClickEvent e) {
//System.out.println("---->>>> onFriendFieldClick p="+p.getName());
        if ( e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            final Oplayer op = PM.getOplayer(p.getName());
            final PFplayerS pfp=ManagerS.getPFplayer(p.getName());

            switch (e.getSlot()) {
                
                case 36:
                    ManagerS.openFriendList(PM.getOplayer(p.getName()), pfp.friend_current_page-1, pfp.friend_delete_mode);
                    break;
                    
                case 44:
                    ManagerS.openFriendList(PM.getOplayer(p.getName()), pfp.friend_current_page+1, pfp.friend_delete_mode);
                    break;
                    
                default:
                    ItemStack is=e.getCurrentItem();
                    ItemMeta im=is.getItemMeta();
                    List<String>lore=im.getLore();
                    boolean upd_item=false;
                    final String item_name=im.getDisplayName();
                    
                        if (is.getType()==Material.PLAYER_HEAD) {
//System.out.println("onFriendFieldClick 111 nik_with_color="+nik_with_color);
                            String nik;
                            if (item_name.startsWith("§cУдалить ")) {
                                nik=ChatColor.stripColor(item_name.replaceFirst("§cУдалить ", ""));
//System.out.println("onFriendFieldClick 222 Удалить nik="+nik);
                                if (!e.isShiftClick()) {
                                    p.sendMessage(MainS.friendsPrefix+"§cДля безопасности, удаление - кл.Shift+клик !");
                                } else {
                                    ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "fr del "+nik);
                                    //MessageS.request(p, DataType.FRIEND_COMMAND, "del "+nik );
                                    op.profile.setItem(e.getSlot(), null);
                                    p.updateInventory();
                                }
                            } else {
                                nik=ChatColor.stripColor(item_name);
                                if(item_name.startsWith("§a")){ //онлайн
//System.out.println("onFriendFieldClick 333 online nik="+nik);
                                    if (e.isShiftClick()) {
                                        final boolean blocked= lore.size()>=1 && lore.get(0).equals("§c✕ блокировка");
                                        if (blocked) {
                                            ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "fr unblock "+nik);
                                            //MessageS.request(p, DataType.FRIEND_COMMAND, "unblock "+nik );
                                            lore.set(0, "");
                                        } else {
                                            //MessageS.request(p, DataType.FRIEND_COMMAND, "block "+nik );
                                            ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "fr block "+nik);
                                            lore.set(0, "§c✕ блокировка");
                                        }
                                        upd_item=true;
                                    } else if (e.isLeftClick()) {
                                        p.closeInventory();
                                        ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "fr jump "+nik);
                                        //MessageS.request(p, DataType.FRIEND_COMMAND, "jump "+nik );
                                    } else {
                                        ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "fr partyinvite "+nik);
                                        //MessageS.request(p, DataType.FRIEND_COMMAND, "partyinvite "+nik );
                                    }
                                } else {
//System.out.println("onFriendFieldClick 244422 offline nik="+nik);
                                    if (e.isRightClick()) {
                                        p.closeInventory();
                                        p.spigot().sendMessage(new ComponentBuilder("§e§l** §f§lКликните сюда, чтобы написать сообщение §e§l**")
                                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr m "+nik+" "))
                                                .create()
                                        );
                                    } else p.sendMessage(MainS.friendsPrefix+"§7ПКМ - оставить сообщение.");
                                }
                            }
                            
                    } else { //не работает видимлсть!
                        //if (is.getType()==Material.CONCRETE) {
                        if (is.getType().toString().contains("CONCRETE")) {
                            F_set set=F_set.настройка_по_названию_предмета(item_name);
                            if (set!=F_set.нет) {
                                int value=F_set.nextValue(set, pfp.getFriendSettings(set));
                                ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "fr set "+set.toString());
                                //MessageS.request(p, DataType.FRIEND_COMMAND, "set "+);
    //System.out.println("F_set="+set.toString()+" curr="+pfp.getIntSettings(set)+" new_value="+value);
                                ManagerS.injectSettingsItem(op, set, value);
                                if (value!=set.default_value) pfp.settings.put(set, value);
                                else pfp.settings.remove(set);
                            }
                        //} else if (is.getType()==Material.STAINED_GLASS) {
                        } else if (is.getType().toString().contains("STAINED_GLASS")) {
                            E_view view=E_view.видимость_по_названию_предмета(item_name);
                            //if (set!=F_set.нет) {
                            //    int value=F_set.nextValue(set, pfp.getIntSettings(set));
                                //MessageS.request(p, DataType.COMMAND_EXECUTE, "set<:>"+F_set.ВИДЕТЬ_РЕЖИМ.toString()+"<:>"+view.toString());
    //System.out.println("F_set="+set.toString()+" curr="+pfp.getIntSettings(set)+" new_value="+value);
                                pfp.setViewMode (view);
                                ManagerS.injectSettingsItem(op, F_set.ВИДЕТЬ_РЕЖИМ, view.tag);
                                //if (view.tag!=F_set.ВИДЕТЬ_РЕЖИМ.default_value) pfp.settings.put(F_set.ВИДЕТЬ_РЕЖИМ, view.tag);
                                //else pfp.settings.remove(F_set.ВИДЕТЬ_РЕЖИМ);
                                //pfp.updateViewMode();
                            //}
                        }
                        p.updateInventory();
                    }
                        
                    if (upd_item) {
                        im.setLore(lore);
                        is.setItemMeta(im);
                        op.profile.setItem(e.getSlot(), is);
                        p.updateInventory();
                    }
                    break;







            }
        
        }
    } 
    
                                        //p.spigot().sendMessage(new ComponentBuilder("§e§l** §f§lКликните сюда, чтобы написать сообщение §e§l**")
                                        //        .event(new ClickEvent(ClickEvent.Operation.RUN_COMMAND, "/fr m "+nik+" "))
                                        //        .create()
                                        //);
    
                                        //p.closeInventory();
                                        //MessageS.request(p, DataType.COMMAND_EXECUTE, "unblock<:>"+nik );
                                        //p.spigot().sendMessage(new ComponentBuilder("§e§l** §f§lКликните сюда, чтобы написать сообщение §e§l**")
                                        //        .event(new ClickEvent(ClickEvent.Operation.SUGGEST_COMMAND, "/fr m "+nik+" "))
                                        //        .create()
                                        //);

    
    
    
    public static void onPartyProfileIconClick(final Player p, final InventoryClickEvent e) {
//System.out.println("---->>>> onPartyProfileIconClick p="+p.getName());
        final Oplayer op = PM.getOplayer(p.getName());
        if (!MainS.load_party) {
            op.profile.setItem(22, ItemUtils.profile_deny.clone());
            p.updateInventory();
            return;
        }
        if (e.isShiftClick()) {
            ManagerS.openPartyList(op, true); //удаление
        } else if (e.isRightClick()) {
            ManagerS.openPartySettings(p, op);
        } else if (e.isLeftClick()) {
            ManagerS.openPartyList(op, false); //список
        }
    } 

    public static void onPartyFieldClick(final Player p, final InventoryClickEvent e) {
        if ( !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) return;
            final Oplayer op = PM.getOplayer(p.getName());
            final PFplayerS pfp=ManagerS.getPFplayer(p.getName());

                    
            final String item_name=e.getCurrentItem().getItemMeta().getDisplayName();
                    
                if (e.getCurrentItem().getType()==Material.PLAYER_HEAD) {
//System.out.println("onFriendFieldClick 111 nik_with_color="+nik_with_color);
                    String nik;
                    if (item_name.startsWith("§cУдалить ")) {
                        nik=ChatColor.stripColor(item_name.replaceFirst("§cУдалить ", ""));
//System.out.println("onFriendFieldClick 222 Удалить nik="+nik);
                        if (!e.isShiftClick()) {
                            p.sendMessage(MainS.friendsPrefix+"§cДля безопасности, удаление - кл.Shift+клик !");
                        } else {
                            //MessageS.request(p, DataType.PARTY_COMMAND, "kick "+nik );
                            ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "party kick "+nik);
                            op.profile.setItem(e.getSlot(), null);
                            p.updateInventory();
                        }
                    } else {
                        nik=ChatColor.stripColor(item_name);
//System.out.println("onFriendFieldClick 333 online nik="+nik);
                        if (e.isShiftClick()) {
                            p.closeInventory();
                            //MessageS.request(p, DataType.PARTY_COMMAND, "leave" );
                            ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "party leave");
                        } else if (e.isLeftClick()) {
                            p.closeInventory();
                            p.spigot().sendMessage(new ComponentBuilder("§e§l** §f§lКликните сюда, чтобы написать команде§e§l**")
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party msg "))
                            .create()
                            );
                            //MessageS.request(p, DataType.FRIEND_COMMAND, "jump "+nik );
                        } else {
                            //MessageS.request(p, DataType.PARTY_COMMAND, "leader "+nik );
                            ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "party leader "+nik);
                        }
                    }

            } else if (e.getCurrentItem().getType().toString().contains("CONCRETE")) {
                P_set set=P_set.настройка_по_названию_предмета(item_name);
                if (set!=P_set.нет) {
                    boolean value = !pfp.getPartySettings(set);
                    //MessageS.request(p, DataType.PARTY_COMMAND, "set "+);
                    ApiOstrov.sendMessage(p, Operation.EXECUTE_BUNGEE_CMD, p.getName(), "party set "+set.toString());
//System.out.println("F_set="+set.toString()+" curr="+pfp.getIntSettings(set)+" new_value="+value);
                    ManagerS.injectPartySettingsItem(op, set, value);
                    if (value!=set.default_value) pfp.party_settings.put(set, value);
                    else pfp.party_settings.remove(set);
                    p.updateInventory();
                }
            } 

        
    } 

/*
    public static boolean hasParty(final Player p) {
//System.out.println("ApiFriends.hasParty()?"+!ManagerS.getPFplayer(p).party_members.isEmpty());
        return !ManagerS.getPFplayer(p).party_members.isEmpty();
    }
    public static boolean isInParty(final Player p1, final Player p2) {
        return hasParty(p1) && getPartyPlayers(p1).contains(p2.getName());
    }
    public static List<String> getPartyPlayers(final Player p) {
        return new ArrayList(ManagerS.getPFplayer(p).party_members.keySet());
    }
    public static boolean isPartyLeader(final Player p) {
        //return hasParty(p) && ManagerS.getPFplayer(p).party_leader.equals(p.getName());
        return ManagerS.getPFplayer(p).isPartyLeader();
    }
    public static String getPartyLeader(final Player p) {
//System.out.println("== ApiFriends.getPartyLeader() hasParty?"+hasParty(p)+" party_members="+ManagerS.getPFplayer(p).party_members+" party_leader="+ManagerS.getPFplayer(p).party_leader);
        return ManagerS.getPFplayer(p).party_leader;
        //if (hasParty(p)) return ManagerS.getPFplayer(p).party_members.get(0);
        //else return "";
    }*/
    public static boolean isFriend(final Player p1, final Player p2) {
        return ManagerS.getPFplayer(p1).isFriend(p2.getName());
    }
    


    
/* 
    //подмена меню
@EventHandler(ignoreCancelled = true,priority = EventPriority.LOW) //уже отменено, нужно скопировать низ и открыть новое меню
    public void InventoryProfileMain (InventoryClickEvent e) {
//System.out.println("click friend 1");            
        if (e.getCurrentItem()== null || !e.getInventory().getName().startsWith("Профиль")) return;
//System.out.println("click friend 2");            
        if (e.getInventory().getType()!=InventoryType.CHEST || e.getSlot() <0 || e.getSlot() > 35) return;
        final Oplayer op=PM.getOplayer(e.getWhoClicked().getName());
        
        if (e.getInventory().getName().equals("Профиль")) {
//System.out.println("click friend 3");            
            Inventory friend = Bukkit.createInventory( null, 54,  "Профиль: ДРУЗЬЯ" ); //подменяем чтобы было другое название
            for (int i=36;i<=53;i++) {
                friend.setItem(1, op.profile.getItem(i));
            }
        //добавть головы
            e.getWhoClicked().openInventory(friend);
            
        } else if (e.getInventory().getName().equals("Профиль: ДРУЗЬЯ")) {
//System.out.println("click Профиль: ДРУЗЬЯ");            
        }
        

    }
    
*/
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
