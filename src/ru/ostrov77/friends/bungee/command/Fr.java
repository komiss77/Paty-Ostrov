package ru.ostrov77.friends.bungee.command;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.TreeSet;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import ru.ostrov77.auth.bungee.Managers.BungeePM;
import ru.ostrov77.friends.E_view;
import ru.ostrov77.friends.F_set;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;







public class Fr extends Command implements TabExecutor {

    public Fr() {
        super("fr");
    }

@Override
    public Iterable<String> onTabComplete(CommandSender cs, String[] arg) {
        if ( arg.length > 3 || arg.length == 0 ) return ImmutableSet.of();

        Set<String> matches = new TreeSet<>();
        
        switch (arg.length) {
            
            case 1:
                matches.add("add");
                matches.add("reject");
                matches.add("del");
                matches.add("list");
                matches.add("block");
                matches.add("unblock");
                matches.add("jump");
                matches.add("jaccept");
                matches.add("m");
                matches.add("r");
                matches.add("mread");
                matches.add("set");
                break;
                
            case 2:
                final String search = arg[1].toLowerCase();
                switch (arg[0]) {
                    case "add":
                    case "reject":
                        return BungeePM.getOnlineUserNames(search, true);
                        //BungeePM.oplayers.keySet().stream().forEach((nik) -> {
                        //    if ( nik.toLowerCase().startsWith( search ) )  matches.add( nik );
                        //}); 
                        //break;
                    case "del":
                    case "block":
                    case "jump":
                    case "jaccept":
                    case "m":
                    case "r":
                        if (ManagerB.exist(cs.getName())) {
                            matches.addAll(ManagerB.getPFplayer(cs.getName()).online_friends);
                            matches.addAll(ManagerB.getPFplayer(cs.getName()).offline_friends);
                        }
                        break;
                    case "unblock":
                        if (ManagerB.exist(cs.getName())) {
                            matches.addAll(ManagerB.getPFplayer(cs.getName()).blocked);
                        }
                        break;
                    case "set":
                        for (F_set set:F_set.values()) {
                            matches.add(set.toString());
                        }
                        break;
                        
                }
                break;
                
            case 3:
                if (arg[1].equals(F_set.ВИДЕТЬ_РЕЖИМ.toString())) {
                    for (E_view w:E_view.values()) {
                        matches.add(w.toString());
                    }
                } 
                break;
                
        }
        return matches;

    }
    
    
    
    @Override
    public void execute(CommandSender cs, String[] args) {
        
        if (!(cs instanceof ProxiedPlayer)) return;
        final ProxiedPlayer sender=(ProxiedPlayer) cs;
        
        if (args.length==0) {
            help (sender);
            return;
        } 
        
        
     /*   String cmd_as_string="";
        
        for (String arg_:args) {
            cmd_as_string=cmd_as_string+"<:>"+arg_;
        }
        cmd_as_string=cmd_as_string.replaceFirst("<:>", "");
        
        proccesFrCommand (sender, cmd_as_string);
    }

    
    
    
    public static void proccesFrCommand(final ProxiedPlayer sender, String raw) {
//System.out.println("1111 sender="+sender.getName()+ " raw="+raw);

        if (raw.isEmpty()) return;
        
        String sub_command="";
        if (raw.contains("<:>")) {
            sub_command=raw.split("<:>")[0].toLowerCase();
            raw=raw.replace(sub_command+"<:>", "");
        } else {
            sub_command=raw.toLowerCase();
            raw="";
        }
        
//System.out.println("2222 sender="+sender.getName()+ " sub_cmd="+sub_command+" arg="+raw);
        
        if (sub_command.isEmpty()) return;
        
        String args[];
        if (raw.contains("<:>")) args=raw.split("<:>");
        else if (!raw.isEmpty()) args=new String[]{raw};
        else args=new String[]{};*/
        
        switch (args[0]) {
            
            case "add": AddFriend.execute(sender,args); break;
            case "reject": Reject.execute(sender,args); break;
            case "del": DelFriend.execute(sender,args); break;
            case "list": ListFriend.execute(sender,args); break;
            case "block": BlockFriend.execute(sender,args); break;
            case "unblock": UnblockFriend.execute(sender,args); break;
            
            case "jump": Jump.execute(sender,args); break;
            case "jaccept": JumpAccept.execute(sender,args); break;

            case "m": MsgSend.execute(sender,args); break;
            case "r": MsgReply.execute(sender,args); break;
            case "mread": MsgReadOffline.execute(sender,args); break;
            
            case "set": FSettings.execute(sender,args,false); break;
            
            default: help(sender);
            
        }
        
        
        
        
        
        
    }

    
    
    
    private static void help(final ProxiedPlayer sender) {
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(MainB.friendsPrefix+"- §bКоманды §7(кликабельно)"));
        //sender.sendMessage(new TextComponent("§5/fr m <ник>  §7- отправить сообщение"));
        sender.sendMessage(
            new ComponentBuilder("§5/fr list  §7список друзей онлайн")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr list"))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/fr add <ник>  §7подружиться")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr add "))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/fr reject <ник>  §7отказать")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr reject "))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/fr del <ник>  §7разругаться")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr del "))
            .create()
        );

        sender.sendMessage(
            new ComponentBuilder("§5/fr block <ник>  §7временно заблокировать")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr block "))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/fr unblock <ник>  §7снять блокировку")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr unblock "))
            .create()
        );


        
        sender.sendMessage(
            new ComponentBuilder("§5/fr jump <ник>  §7отправить запрос на ТП")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr jump "))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/fr jaccept  §7принять запрос на ТП")
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr jaccept"))
            .create()
        );
        
        
        
        
        sender.sendMessage(
            new ComponentBuilder("§5/fr m <ник> <сообщение>  §7отправить сообщение ")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr m "))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/fr r <сообщение>  §7ответить на последнее сообщение ")
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fr r "))
            .create()
        );
        
        
        sender.sendMessage(
            new ComponentBuilder("§5/fr set §7настройки")
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fr set"))
            .create()
        );

            
    }
    
    
    
    
}
