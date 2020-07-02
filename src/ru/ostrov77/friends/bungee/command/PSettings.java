package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.P_set;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.PFplayerB;



public class PSettings {
    
    
    public static void execute(final ProxiedPlayer sender, final String[] args, final boolean silent) {
        if (!(sender instanceof ProxiedPlayer)) return;
        
        final PFplayerB pf=ManagerB.getPFplayer(sender.getName());
        
        if (args.length==2) {
            if (P_set.exist(args[1])) {
                P_set set=P_set.valueOf(args[1]);
//System.out.println("!!!! FSettings set="+set);
                boolean current=pf.getPartySettings(set);
                if (current)pf.party_settings.put(set, false);
                else pf.party_settings.put(set, true);
                if(!silent) sender.sendMessage(new TextComponent("§eВы изменили настройку "+set.item_name+" на "+(!current?"§aда":"§4нет")));
            }
            return;
        }         
        
        
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+" - НАСТРОЙКИ    §6[§2вкл. §4выкл. §7(значение)§6]"));
        
        
        TextComponent msg=new TextComponent("");
        TextComponent add;
        
        for (P_set set_:P_set.values()) {
//System.out.println("FSettings 222 set="+set_);
            if (set_==P_set.нет) continue;
            add=new TextComponent((pf.getPartySettings(set_)?"§2":"§4")+set_.toString());
            add.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/party set "+set_ ) );
            add.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик-поменять значение").create() ) );
            msg.addExtra(add);
            msg.addExtra("  ");
        }
        sender.sendMessage(msg);

    }
    
    


}

