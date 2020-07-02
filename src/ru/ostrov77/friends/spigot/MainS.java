package ru.ostrov77.friends.spigot;


import ru.ostrov77.friends.spigot.listener.OstrovChanelListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.komiss77.Ostrov;

import ru.ostrov77.friends.ApiFriends;
import ru.ostrov77.friends.spigot.listener.PlayerListenerS;





public class MainS extends JavaPlugin {

    public static MainS main;
    public static String friendsPrefix;
    public static String partyPrefix;
    public static boolean load_friends;
    public static boolean load_party;
    public static boolean hide_mode;
    
    
    @Override
    public void onEnable() {
        MainS.main = this;
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        friendsPrefix = "§a§lД§d§lр§c§lу§e§lз§9§lь§b§lя §f";
        partyPrefix = "§6[§eКоманда§6] §3";
        
        load_friends = getConfig().getBoolean("load_friends"); 
        load_party = getConfig().getBoolean("load_party");
        hide_mode = getConfig().getBoolean("hide_mode");
        ManagerS.Init();
        
        Ostrov.api_friends=new ApiFriends();
        
        //this.getServer().getMessenger().registerOutgoingPluginChannel(this, Chanell.withSeparator(Chanell.FRIEND));
        //this.getServer().getMessenger().registerIncomingPluginChannel(this, Chanell.withSeparator(Chanell.FRIEND), new MessageS());
        
        //this.getServer().getPluginManager().registerEvents(new OnInvClick(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListenerS(this), this);
        this.getServer().getPluginManager().registerEvents(new OstrovChanelListener(), this);
        //this.getServer().getPluginManager().registerEvents(new InvListener(), this);
        log_ok("Загружен!");

    }

  
 /*   
@Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if (sender instanceof Player) {
            if (command.equalsIgnoreCase("friends")) {
                //PartyFriendsAPI.openMainInventory((Player) sender, 0);
            }
            //} else if (this.getConfig().getBoolean("General.EnableHideCommand")) {
            //        PartyFriendsAPI.openHideInventory((Player) sender);
            //    }
        } 

        return true;
    }*/

    
    public static void log_ok(String s) {   Bukkit.getConsoleSender().sendMessage(friendsPrefix +"§2"+ s); }
    public static void log_err(String s) {   Bukkit.getConsoleSender().sendMessage(friendsPrefix +"§c"+ s); }


    
    
    
    
    
    
    
    
    
    
    


















    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
/*
public ItemStack blockOutput(int itemNumber, int inventoryNumber) {
        ItemStack item;
        ItemMeta name;
        String output;
        String name1;

        switch (inventoryNumber) {
        case -1:
            ItemStack name2 = new ItemStack(Material.IRON_DOOR, 1);
            this.setItemName(name2, "§5назад");
            return name2;

        case 0:
            switch (itemNumber) {
            case 0:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.SettingsItem")), 1);
                this.setItemName(item, "§7Настройки");
                return item;

            case 1:
                ItemStack output2 = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.FriendRequestItem")), 1);
                this.setItemName(output2, "§5Заявки на дружбу");
                return output2;
            }

        case 1:
        default:
            break;

        case 2:
            if (itemNumber == 0) {
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aAccept friend request";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.AcceptFriendRequests");
                } else {
                    name1 = "§aDie Freundschaftsanfrage akzeptieren";
                }

                this.setItemName(item, name1);
                return item;
            }

            if (itemNumber == 1) {
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§cDecline the friend request";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.DeclineFriendRequests");
                } else {
                    name1 = "§cDie Freundschaftsanfrage ablehnen";
                }

                this.setItemName(item, name1);
                return item;
            }
            break;

        case 3:
            if (itemNumber == 0) {
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.HideToolItem")), 1);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§6Hide Players";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.NameOfTheHideItem");
                } else {
                    name1 = "§6Verstecke Spieler";
                }

                this.setItemName(item, name1);
                return item;
            }
            break;

        case 4:
            switch (itemNumber) {
            case 0:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.FriendDeleteItem")), 1);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§cRemove this friend";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.RemoveThisFriendItem");
                } else {
                    name1 = "§cEntferne diesen Freund";
                }

                this.setItemName(item, name1);
                return item;

            case 1:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.InviteItem")), 1);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§5Invite this player into your party";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.InviteIntoYourPartyItem");
                } else {
                    name1 = "§5Lade diesen Spieler in deine Party ein";
                }

                this.setItemName(item, name1);
                return item;

            case 2:
                if (!this.getConfig().getBoolean("General.DisableJump")) {
                    item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.JumpItem")), 1);
                    name1 = "";
                    if (getLanguage().equalsIgnoreCase("English")) {
                        name1 = "§6Jump to this Player";
                    } else if (getLanguage().equalsIgnoreCase("own")) {
                        name1 = this.getConfig().getString("Messages.JumpToThisFriendItem");
                    } else {
                        name1 = "§6Springe zu diesem Spieler";
                    }

                    this.setItemName(item, name1);
                    return item;
                }

                return null;

            default:
                return null;
            }

        case 5:
            ItemMeta output1;

            if (itemNumber == 0) {
                item = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
                output1 = item.getItemMeta();
                output1.setDisplayName("§aВидеть всех");
                item.setItemMeta(output1);
                return item;
            }

            if (itemNumber == 1) {
                item = new ItemStack(Material.STAINED_GLASS, 1, (short) 4);
                output1 = item.getItemMeta();
                output1.setDisplayName("§eВидеть друзей и участников команды");
                item.setItemMeta(output1);
                return item;
            }

            if (itemNumber == 2) {
                item = new ItemStack(Material.STAINED_GLASS, 1, (short) 1);
                output1 = item.getItemMeta();
                output1.setDisplayName("§6Видеть только друзей");
                item.setItemMeta(output1);
                return item;
            }

            if (itemNumber == 3) {
                item = new ItemStack(Material.STAINED_GLASS, 1, (short) 10);
                output1 = item.getItemMeta();
                output1.setDisplayName("§5Видеть только участников команды");
                item.setItemMeta(output1);
                return item;
            }

            if (itemNumber == 4) {
                item = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
                output1 = item.getItemMeta();
                output1.setDisplayName("§cСкрыть всех");
                item.setItemMeta(output1);
                return item;
            }
            break;

        case 6:
            switch (itemNumber) {
            case 0:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aYou receive friend requests";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YouReceiveFriendRequests");
                } else {
                    name1 = "§aSie erhalten Freundschaftsanfragen";
                }

                this.setItemName(item, name1);
                return item;

            case 1:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§cYou don§t receive friend requests";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YouReceiveNoFriendRequests");
                } else {
                    name1 = "§cSie erhalten keine Freundschaftsanfragen";
                }

                this.setItemName(item, name1);
                return item;

            case 2:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aYou receive party invitations from anyone";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YouReceivePartyInvitations");
                } else {
                    name1 = "§aSie erhalten Party Einladungen von jedem";
                }

                this.setItemName(item, name1);
                return item;

            case 3:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§cYou receive party invitations only from friends";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YouReceivePartyInvitationsOnlyFromFriends");
                } else {
                    name1 = "§cSie erhalten Party Einladungen nur von Freunden";
                }

                this.setItemName(item, name1);
                return item;

            case 4:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aYour online status will be shown";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YourStatusWillBeShownOnline");
                } else {
                    name1 = "§aIhr Online Status wird angezeigt";
                }

                this.setItemName(item, name1);
                return item;

            case 5:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§cYour online status will not be shown";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YourStatusWillBeShownOffline");
                } else {
                    name1 = "§cIhr Online Status wird nicht angezeigt";
                }

                this.setItemName(item, name1);
                return item;

            case 6:
                if (!this.getConfig().getBoolean("General.DisableJump")) {
                    item = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
                    name1 = "";
                    if (getLanguage().equalsIgnoreCase("English")) {
                        name1 = "§aFriends can jump to you";
                    } else if (getLanguage().equalsIgnoreCase("own")) {
                        name1 = this.getConfig().getString("Messages.JumpAllowed");
                    } else {
                        name1 = "§aFreunde können zu dir springen";
                    }

                    this.setItemName(item, name1);
                    return item;
                }

                return null;

            case 7:
                if (!this.getConfig().getBoolean("General.DisableJump")) {
                    item = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
                    name1 = "";
                    if (getLanguage().equalsIgnoreCase("English")) {
                        name1 = "§cFriends cannot jump to you";
                    } else if (getLanguage().equalsIgnoreCase("own")) {
                        name1 = this.getConfig().getString("Messages.NoJump");
                    } else {
                        name1 = "§cFreunde können nicht zu dir springen";
                    }

                    this.setItemName(item, name1);
                    return item;
                }

                return null;

            case 8:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aYou receive messages";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.YouReceiveMessages");
                } else {
                    name1 = "§aDu erhälst Nachrichten";
                }

                this.setItemName(item, name1);
                return item;

            case 9:
                item = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
                name = item.getItemMeta();
                output = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    output = "§cYou won\'t receive messages";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    output = this.getConfig().getString("Messages.YouWontReceiveMessages");
                } else {
                    output = "§cDu wirst keine nachrichten erhalten";
                }

                name.setDisplayName(output);
                item.setItemMeta(name);
                return item;
            }

        case 7:
            switch (itemNumber) {
            case 0:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.FriendRequestSetting")), 1);
                name = item.getItemMeta();
                output = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    output = "§5Do you want to receive friend requests?";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    output = this.getConfig().getString("Messages.WantReceiveFriendRequests");
                } else {
                    output = "§5Möchtest du Freundschaftsanfragen erhalten?";
                }

                name.setDisplayName(output);
                item.setItemMeta(name);
                return item;

            case 1:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.PartyInvitationsSetting")), 1);
                name = item.getItemMeta();
                output = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    output = "§5Do you want to receive party invitations from everybody?";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    output = this.getConfig().getString("Messages.WantReceivePartyInvitations");
                } else {
                    output = "§5Möchtest du Party Einladungen von jedem Spieler erhalten können?";
                }

                name.setDisplayName(output);
                item.setItemMeta(name);
                return item;

            case 2:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.ShowOnlineSetting")), 1);
                name = item.getItemMeta();
                output = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    output = "§aDo you want that your friends can see, that you are online?";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    output = this.getConfig().getString("Messages.WantToBeShownAsOnline");
                } else {
                    output = "§aMöchtest du, dass deine Freunde sehen, dass du Online bist?";
                }

                name.setDisplayName(output);
                item.setItemMeta(name);
                return item;

            case 3:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.JumpSetting")), 1);
                name = item.getItemMeta();
                output = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    output = "§6Should your friends be allowed to jump to you?";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    output = this.getConfig().getString("Messages.ShouldFriendsBeAbleToJumpToYou");
                } else {
                    output = "§6Sollen Freunde zu dir springen dürfen?";
                }

                name.setDisplayName(output);
                item.setItemMeta(name);
                return item;

            case 4:
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.RecieveMessagesSetting")), 1);
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aDo you want to receive messages?";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.WantReceiveMessages");
                } else {
                    name1 = "§aMöchtest du Nachrichten erhalten?";
                }

                this.setItemName(item, name1);
                return item;
            }

        case 8:
            if (itemNumber == 0) {
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.NextPageButton")));
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aPrevious";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.PreviousPageButtonName");
                } else {
                    name1 = "§aZurück";
                }

                this.setItemName(item, name1);
                return item;
            }

            if (itemNumber == 1) {
                item = new ItemStack(Material.getMaterial(this.getConfig().getString("ItemDataNames.PreviousPageButton")));
                name1 = "";
                if (getLanguage().equalsIgnoreCase("English")) {
                    name1 = "§aNext";
                } else if (getLanguage().equalsIgnoreCase("own")) {
                    name1 = this.getConfig().getString("Messages.NextPageButtonName");
                } else {
                    name1 = "§aVor";
                }

                this.setItemName(item, name1);
                return item;
            }
        }

        return null;
    }


*/
    


}
