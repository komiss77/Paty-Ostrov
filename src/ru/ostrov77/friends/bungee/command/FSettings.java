package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.E_view;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.PFplayerB;



public class FSettings {
    
    
    public static void execute(final ProxiedPlayer sender, final String[] args, final boolean silent) {
        if (!(sender instanceof ProxiedPlayer)) return;
        
        final PFplayerB pf=ManagerB.getPFplayer(sender.getName());
        
        if (args.length==2) {
            if (F_set.exist(args[1])) {
                F_set set=F_set.valueOf(args[1]);
//System.out.println("!!!! FSettings set="+set);

                if (set==F_set.ВИДЕТЬ_РЕЖИМ) {
                    E_view view=E_view.настройка_по_метке(pf.getIntSettings(set));
                    view = E_view.next(view);
                    pf.settings.put(set, view.tag);
                    if(!silent) sender.sendMessage(new TextComponent("§eВы изменили настройку "+set.item_name+" на "+view.item_name));
                } else {
                    boolean current=pf.getSettings(set);
                    if (current) pf.settings.put(set, 0);
                    else pf.settings.put(set, 1);
                    if(!silent) sender.sendMessage(new TextComponent("§eВы изменили настройку "+set.item_name+" на "+(!current?"§aда":"§4нет")));
                }
            }
            return;
        } else if (args.length==3) {
            if (F_set.exist(args[1])) {
                F_set set=F_set.valueOf(args[1]);
//System.out.println("!!!! FSettings set="+set);
                if (set==F_set.ВИДЕТЬ_РЕЖИМ) {
                    if (E_view.exist(args[2])) {
                        E_view view=E_view.valueOf(args[2]);
//System.out.println("!!!! FSettings view="+view);
                        pf.settings.put(set, view.tag);
                        if(!silent) sender.sendMessage(new TextComponent("§eВы изменили настройку "+set.item_name+" на "+view.item_name));
                    }
                }
            }
            return;
        }


        
        
        
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+" - НАСТРОЙКИ    §6[§2вкл. §4выкл. §7(значение)§6]"));
        
        
        TextComponent msg=new TextComponent("");
        TextComponent add;
        
        for (F_set set_:F_set.values()) {
//System.out.println("FSettings 222 set="+set_);
            
            if (set_==F_set.нет) continue;
            if (set_==F_set.ВИДЕТЬ_РЕЖИМ) {
                add=new TextComponent("§7"+set_.toString()+"(§l"+E_view.настройка_по_метке(pf.getIntSettings(set_)).item_name+"§7)");
            } else {
                add=new TextComponent((pf.getSettings(set_)?"§2":"§4")+set_.toString());
            }
            add.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fr set "+set_ ) );
            add.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§oКлик-поменять значение").create() ) );
            msg.addExtra(add);
            msg.addExtra("  ");
        }
        sender.sendMessage(msg);

    }
    
    


}

