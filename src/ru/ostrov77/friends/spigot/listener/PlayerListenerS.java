package ru.ostrov77.friends.spigot.listener;


import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.komiss77.ApiOstrov;
import ru.komiss77.Events.BungeeDataRecieved;
import ru.komiss77.Ostrov;

import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.ItemUtils;
import ru.ostrov77.friends.E_view;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.spigot.MainS;
import ru.ostrov77.friends.spigot.ManagerS;
import ru.ostrov77.friends.spigot.PFplayerS;






public class PlayerListenerS implements Listener {

    private static boolean give_head;
    private static int head_slot;
    private static boolean give_hideitem;
    private static int hide_slot;
    private static ItemStack hide_item;
    private static boolean give_partyitem;
    private static int party_slot;
    private static ItemStack party_item;


    public PlayerListenerS(MainS aThis) {
        give_head = MainS.main.getConfig().getBoolean("give_head"); 
        head_slot=  MainS.main.getConfig().getInt("head_slot");
        give_hideitem  = MainS.main.getConfig().getBoolean("give_hideitem");
        hide_slot=  MainS.main.getConfig().getInt("hide_slot");
        hide_item=new ItemBuilder(Material.BLAZE_ROD).setName("§6Скрыть игроков").setLore(ItemUtils.Gen_lore(null, "§7Правый клик на игрока<br>§7этим предметом<br>§7будет менять режим видимости<br>§7игроков вокруг Вас.", "§7")).build();
        give_partyitem = MainS.main.getConfig().getBoolean("give_partyitem"); 
        party_slot=  MainS.main.getConfig().getInt("party_slot");
        party_item=new ItemBuilder(Material.WHITE_BANNER).setName("§2Команда").setLore(ItemUtils.Gen_lore(null, "§7Правый клик на игрока<br>§7этим предметом -<br>§7пригласить в Вашу команду,<br>Приседание +ПКМ -<br>§7принять приглашение.<br>§5Также Вы можете использовать<br>§7команду §b/party", "§7")).build();
    }

    
    
    
    
@EventHandler(priority = EventPriority.MONITOR) 
    public void onPlayerJoin(PlayerJoinEvent e) {
//System.out.println("onPlayerJoin 1");        
        ManagerS.getPFplayer(e.getPlayer());
        //отслеживать hide_mode
    }    
    
@EventHandler(priority = EventPriority.MONITOR) 
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (ManagerS.pf_players.containsKey(e.getPlayer().getName())) ManagerS.pf_players.remove(e.getPlayer().getName());
    }   
    
    
@EventHandler(priority = EventPriority.NORMAL) 
    public void onBungeeDataRecieved(BungeeDataRecieved e) {
//System.out.println("onBungeeDataRecieved Party");  
        if (MainS.load_friends) {
            OstrovChanelListener.request(e.getPlayer(), ru.komiss77.Enums.Action.PF_FRIENDS_ONLINE, "" );
            OstrovChanelListener.request(e.getPlayer(), ru.komiss77.Enums.Action.PF_FRIENDS_OFFLINE, "");
        }
        if (MainS.load_party) {
            OstrovChanelListener.request(e.getPlayer(), ru.komiss77.Enums.Action.PF_PARTY_MEMBER, "");
        }
        if (Ostrov.lobby_items.item_lobby_mode) {
            if (give_head) e.getPlayer().getInventory().setItem(head_slot, getSlotHead(e.getPlayer().getName()));
            if (give_hideitem) e.getPlayer().getInventory().setItem(hide_slot, hide_item.clone());
            if (give_partyitem) {
                ItemStack flag=party_item.clone();
                flag.setDurability((short)ApiOstrov.randInt(0, 15));
                e.getPlayer().getInventory().setItem(party_slot, flag);
            }
        } else {
            if (give_head) ItemUtils.Add_to_inv(e.getPlayer(), head_slot, getSlotHead(e.getPlayer().getName()), false, false);//{
            if (give_hideitem) ItemUtils.Add_to_inv(e.getPlayer(), hide_slot, hide_item.clone(), false, false);
            if (give_partyitem) {
                ItemStack flag=party_item.clone();
                flag.setDurability((short)ApiOstrov.randInt(0, 15));
                ItemUtils.Add_to_inv(e.getPlayer(), party_slot, flag, false, false);
            }
        }
        
        ManagerS.pf_players.values().stream().forEach((pfp) -> {
            if (!pfp.nik.equals(e.getPlayer().getName())) pfp.updateViewMode(e.getPlayer());
        }); 
        ManagerS.getPFplayer(e.getPlayer().getName()).updateViewMode(true);
    }   
    
    
@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {   
        if (e.getAction()==Action.PHYSICAL) return; //добавить лкм голова

        if ( e.getItem()==null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName()) return;
        final PFplayerS pfp=ManagerS.getPFplayer(e.getPlayer().getName());
        if (pfp.interact_cooldown()) return;

        if ( MainS.hide_mode && give_hideitem && e.getItem().getType()==hide_item.getType() && e.getItem().getItemMeta().getDisplayName().equals(hide_item.getItemMeta().getDisplayName())) {
            e.setCancelled(true);
            if ( e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK ) {
                int mode = pfp.getFriendSettings(F_set.ВИДЕТЬ_РЕЖИМ);
                E_view view = E_view.настройка_по_метке(mode);
                pfp.setViewMode(E_view.next(view));
            } else {
                ApiOstrov.sendActionBarDirect(e.getPlayer(), "§fПравый клик - менять режим видимости.");
            }
        } else if ( MainS.load_friends && give_head && e.getItem().getType()==hide_item.getType() && e.getItem().getItemMeta().getDisplayName().equals("§5Друзья")) {
            e.setCancelled(true);
            if ( e.getAction()==Action.LEFT_CLICK_AIR || e.getAction()==Action.LEFT_CLICK_BLOCK ) {
                ApiOstrov.sendActionBarDirect(e.getPlayer(), "§fПравый клик на игрока - подружиться!");
            }
        } else if ( MainS.load_party && give_partyitem && e.getItem().getType()==party_item.getType() && e.getItem().getItemMeta().getDisplayName().equals(party_item.getItemMeta().getDisplayName())) {
            e.setCancelled(true);
            if ( e.getAction()==Action.LEFT_CLICK_AIR || e.getAction()==Action.LEFT_CLICK_BLOCK ) {
                ApiOstrov.sendActionBarDirect(e.getPlayer(), "§fПКМ-пригласить, Shifh+ПКМ-принять");
            }
        }
        //if ( !e.getItem().hasItemMeta() ) return;
        //if ( !e.getItem().getItemMeta().hasDisplayName() ) return;
        //if ( !e.getItem().getItemMeta().getDisplayName().equals(hide_item.getItemMeta().getDisplayName())) return;

        //e.setCancelled(true);
        //final PFplayerS pfp=ManagerS.getPFplayer(e.getPlayer().getName());
        //if (pfp.interact_cooldown()) return;

    }
    
    
@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onInteractEntity(PlayerInteractEntityEvent e) {   
        if ( e.getPlayer().getItemInHand()==null || !e.getPlayer().getItemInHand().hasItemMeta() || !e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) return;
        final PFplayerS pfp=ManagerS.getPFplayer(e.getPlayer().getName());
        if (pfp.interact_cooldown()) return;
        
        if ( MainS.load_friends && give_head && e.getPlayer().getItemInHand().getType()==Material.PLAYER_HEAD && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§5Друзья")) {
            e.setCancelled(true);
            if ( e.getRightClicked().getType()==EntityType.PLAYER ) {
                //MessageS.request(e.getPlayer(), DataType.FRIEND_COMMAND, "add "+e.getRightClicked().getName() );
                ApiOstrov.sendMessage(e.getPlayer(), ru.komiss77.Enums.Action.OSTROV_BUNGEE_CMD, "fr add "+e.getRightClicked().getName());
            } else {
                ApiOstrov.sendActionBarDirect(e.getPlayer(), "§fПравый клик на игрока - подружиться!");
            }
        } else if ( MainS.load_party && give_partyitem && e.getPlayer().getItemInHand().getType()==party_item.getType() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(party_item.getItemMeta().getDisplayName())) {
            e.setCancelled(true);
            if ( e.getRightClicked().getType()==EntityType.PLAYER ) {
                if (e.getPlayer().isSneaking()) ApiOstrov.sendMessage(e.getPlayer(), ru.komiss77.Enums.Action.OSTROV_BUNGEE_CMD, "party accept "+e.getRightClicked().getName());//MessageS.request(e.getPlayer(), DataType.PARTY_COMMAND, "accept "+e.getRightClicked().getName() );
                else ApiOstrov.sendMessage(e.getPlayer(), ru.komiss77.Enums.Action.OSTROV_BUNGEE_CMD, "party invite "+e.getRightClicked().getName());//MessageS.request(e.getPlayer(), DataType.PARTY_COMMAND, "invite "+e.getRightClicked().getName() );
            } else {
                ApiOstrov.sendActionBarDirect(e.getPlayer(), "§fПКМ-пригласить, Shifh+ПКМ-принять");
            }
        }
        
            //if ( e.getPlayer().getItemInHand()==null || e.getPlayer().getItemInHand().getType()!=Material.SKELETON_SKULL) return;
            //if ( !e.getPlayer().getItemInHand().hasItemMeta() ) return;
            //if ( !e.getPlayer().getItemInHand().getItemMeta().hasDisplayName() ) return;
            //if ( !e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§5Друзья")) return;
            
            //e.setCancelled(true);
            //if (ManagerS.getPFplayer(e.getPlayer().getName()).interact_cooldown()) return;
            
            //if ( e.getRightClicked().getType()==EntityType.PLAYER ) {
            //    MessageS.request(e.getPlayer(), DataType.FRIEND_COMMAND, "add<:>"+e.getRightClicked().getName() );
            //} else {
            //    Ostrov.sendActionBarDirect(e.getPlayer(), "§fПравый клик на игрока - подружиться!");
            //}
    }
    
 
    
    
    
    
    
    
    
    
    
    
    
    
//@EventHandler( priority = EventPriority.MONITOR )
//    public void onLoad (MysqlDataLoaded e) {
//System.out.println("++ MysqlDataLoaded");        
//    }
   
        
        
   
  
    
    public static ItemStack getSlotHead(final String nik) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName("§5Друзья");
        head.setItemMeta(meta);
        SkullMeta metah = (SkullMeta) head.getItemMeta();
        metah.setOwner(nik);
        metah.setLore(Arrays.asList( "§7Чтобы добавить друга,","§7возьмите эту голову в руку","§7и правый клик на человека.","§7Чтобы принять предложение","§7клик на сообщение в чате","§7или тоже правый клик головой.","§5Также Вы можете набрать §b/fr") );
        head.setItemMeta(metah);
        return head;
    }













}
