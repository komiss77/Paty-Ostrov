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

import ru.ostrov77.friends.P_set;
import ru.ostrov77.friends.Party;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.PFplayerB;
import ru.ostrov77.friends.bungee.PartyManager;







public class PartyCmd extends Command implements TabExecutor {

    public PartyCmd() {
        super("party");
    }

@Override
    public Iterable<String> onTabComplete(CommandSender cs, String[] arg) {
        if ( arg.length > 2 || arg.length == 0 ) return ImmutableSet.of();

        Set<String> matches = new TreeSet<>();
        
        switch (arg.length) {
            
            case 1:
                matches.add("list"); //0
                matches.add("create"); //0
                matches.add("invite");
                matches.add("accept");
                matches.add("leave"); //0
                matches.add("msg");
                matches.add("leader");
                matches.add("kick");
                matches.add("info");
                matches.add("set");
               break;
                
            case 2:
                final String search = arg[1].toLowerCase();
                switch (arg[0]) {
                    case "invite":
                    case "accept":
                        return BungeePM.getOnlineUserNames(search, true);
                        //BungeePM.oplayers.keySet().stream().forEach((nik) -> {
                        //    if ( nik.toLowerCase().startsWith( search ) )  matches.add( nik );
                        //}); 
                        //break;
                    case "leader":
                    case "kick":
                        if (PartyManager.hasParty(cs.getName())) {
                            matches.addAll(PartyManager.Get_player_party(cs.getName()).getMembers());
                        }
                        break;
                    case "set":
                        for (P_set set:P_set.values()) {
                            matches.add(set.toString());
                        }
                        break;
                        
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
 
        
        final Party party=PartyManager.Get_player_party(sender);

        switch (args[0]) {
            
            case "list":
                if(party==null){
                    sender.sendMessage(new ComponentBuilder(MainB.partyPrefix+"§cВы не в команде! Создайте её либо присоеденитесь к другой §8<создать")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-создать").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party create"))
                    .create()
                    );
                    return;
                }
                sender.sendMessage(new TextComponent(""));
                sender.sendMessage(new TextComponent("       §6==== §eМоя Команда §6===="));
                TextComponent msg;
                if (party.getLeader().equals(sender.getName())) {
                    sender.sendMessage(new TextComponent("§3Лидер: §bВы"));
                    if (party.getMembers().size()==1) {
                        msg=new TextComponent("§3Участники: §7пока никого. Пригласите §5/party invite ник");
                    } else {
                        msg=new TextComponent("§3Участники: ");
                        party.getMembers().stream().forEach((member) -> {
                            if (!member.equals(party.getLeader())) {
                                msg.addExtra(logo_kick(member));
                                msg.addExtra(new TextComponent( "§a"+member+"  " ));
                            }
                        });
                    }
                } else {
                    sender.sendMessage(new TextComponent("§3Лидер: §b"+party.getLeader()));
                    msg=new TextComponent("§3Участники: ");
                    party.getMembers().stream().forEach((member) -> {
                        if (!member.equals(party.getLeader())) msg.addExtra(new TextComponent( "§a"+member+"  " ));
                    });
                }
                sender.sendMessage(msg);
                sender.sendMessage(new TextComponent(""));
                return;
                
            case "create":
                if (party!=null) {
                    if (party.getLeader().equals(sender.getName())) sender.sendMessage(new TextComponent(MainB.partyPrefix+"у вас уже создана команда!"));
                    else sender.sendMessage(new TextComponent(MainB.partyPrefix+"Вы уже в команде! /party leave - выйти."));
                } else {
                    PartyManager.createParty(sender);
                }
                return;

            case "invite":
                if(args.length >=2){
                    if(party==null){
                        sender.sendMessage(new ComponentBuilder(MainB.partyPrefix+"§cУ Вас нет команды! §8<создать")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-создать команду").create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party create"))
                        .create()
                        );
                        return;
                    }
                    if(!party.getLeader().equals(sender.getName())){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cПриглашать в команду может только лидер!"));return; }
                    if(party.getMembers().size()>=8) {sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВ команде не может быть более 8 участников!"));return; }
                    if(!ManagerB.exist(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cИгрока "+args[1]+" нет на сервере!"));return;}
                    if(party.getLeader().equals(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cСебя пригласить нельзя."));return;}
                    final PFplayerB pfp_invited=ManagerB.getPFplayer(args[1]);
                    if(PartyManager.hasParty(pfp_invited.GetProxyPlayer())){
                        sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cИгрок уже в команде!"));
                        pfp_invited.GetProxyPlayer().sendMessage(new TextComponent(MainB.partyPrefix+"§c"+sender.getName()+" приглашает в команду, но вы уже в другой! §5/party leave §7- выйти"));
                        return;
                    }
                    if (!pfp_invited.getPartySettings(P_set.ПОЛУЧАТЬ_ПРИГЛАШЕНИЯ)){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУ "+pfp_invited.nik+" в настройках выключено приглашение в команды!"));return; }
                    if (pfp_invited.getPartySettings(P_set.ТОЛЬКО_ДРУЗЬЯ) && !ManagerB.isFriends(sender.getName(),pfp_invited.nik)){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cПригласить "+pfp_invited.nik+" в команду могут только друзья!"));return; }
                    if(party.invites.contains(pfp_invited.nik)){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы уже пригласили "+pfp_invited.nik+" в команду!"));return; }
                    party.invitePlayerToParty(sender.getName(), pfp_invited.nik);
                } else sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУкажите ник!"));
                return;
                
            case "accept": 
                if(args.length >= 2){
                    if(sender.getName().equals(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cПринять себя в свою команду нельзя!"));return;}
                    if(party!=null){
                        if (party.getLeader().equals(sender.getName())) sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУ Вас есть своя команда, и Вы в ней лидер!"));
                        else sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы уже в команде "+party.getLeader()+"!"));
                        return;
                    }
                    if(!ManagerB.exist(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cИгрока "+args[1]+" нет на сервере!"));return;}
                    Party invite=PartyManager.Get_player_party(args[1]);
                    if (invite==null) {sender.sendMessage(new TextComponent(MainB.partyPrefix+"§c"+args[1]+" не в коменде!"));return;}
                    //if(party==null){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cКоманды с лидером "+args[0]+" не найдено!"));return;}
                    if(!invite.invites.contains(sender.getName())){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cЛидер "+invite.getLeader()+" не приглашал Вас в команду!"));return;}
                    PartyManager.addPlayerToParty(sender, invite);
                } else sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУкажите ник лидера!"));
                return;
                
            case "leave":
                if(party==null){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы не в команде!"));return;}
                sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы вышли из команды. "+party.removePlayer(sender.getName())));
                return;
                
            case "msg":
                if(party==null){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы не в команде!"));return;}
                party.partyMessage(sender.getName(), msgFromArg(args, 1));
                return;
                
            case "leader":
                if(args.length >= 2){
                    if(party==null){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы не в команде!"));return;}
                    if(!ManagerB.exist(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cИгрока "+args[1]+" нет на сервере!"));return;}
                    if(!party.getMembers().contains(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§c"+args[1]+" не в вашей команде!"));return;}
                    if(!party.getLeader().equals(sender.getName())){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cПередать лидерство может только лидер!"));return;}
                    if(party.getLeader().equals(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы и так лидер!"));return;}
                    party.setLeader(args[1]);
                } else sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУкажите ник!"));
                return;
                
            case "kick":
                if(args.length >= 2){
                    if(party==null){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВы не в команде!"));return;}
                    if(!ManagerB.exist(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cИгрока "+args[1]+" нет на сервере!"));return;}
                    if(!party.getMembers().contains(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§c"+args[1]+" не в вашей команде!"));return;}
                    if(!party.getLeader().equals(sender.getName())){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cВыгонять может только лидер!"));return;}
                    if(sender.getName().equals(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cСебя выгнать нельзя!"));return;}
                    party.kickMember(args[1]);
                } else sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУкажите ник!"));
                return;
                
            case "info":
                if(args.length >= 2){
                    if(!ManagerB.exist(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cИгрока "+args[1]+" нет на сервере!"));return;}
                    if(!PartyManager.hasParty(args[1])){sender.sendMessage(new TextComponent(MainB.partyPrefix+"§c"+args[1]+" не состоит в команде."));return;}
                    final Party info = PartyManager.Get_player_party(args[1]);
                    sender.sendMessage(new TextComponent("       §6==== §eКоманда"+args[1]+" §6===="));
                    sender.sendMessage(new TextComponent("§3Лидер: §b"+info.getLeader()));
                    msg=new TextComponent(MainB.friendsPrefix+"§3Участники: ");
                    info.getMembers().stream().forEach((member) -> {
                        if (!member.equals(info.getLeader())) msg.addExtra(new TextComponent( "§a"+member+"  " ));
                    });
                } else sender.sendMessage(new TextComponent(MainB.partyPrefix+"§cУкажите ник!"));
                return;
                
            case "set":
                PSettings.execute(sender, args, false);
                return;
                
            
        }
        
        help(sender);
        
        
        
        
    }

    public static TextComponent logo_kick (final String nik) {
        final TextComponent block=new TextComponent(" §4✕");
        block.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party kick "+nik));
        block.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4Выгнать "+nik+" из команды.").create() ) );
        return block;
    }
    
    
    
    private static void help(final ProxiedPlayer sender) {
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(MainB.partyPrefix+"- §bКоманды §7(кликабельно)"));
        //sender.sendMessage(new TextComponent("§5/party m <ник>  §7- отправить сообщение"));
        sender.sendMessage(
            new ComponentBuilder("§5/party list §7информация о вашей команде")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party list"))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/party create §7создать команду")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party create"))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/party invite <ник>  §7пригласить игрока в команду")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party invite "))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/party accept <ник> §7принять приглашение")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party accept "))
            .create()
        );

        sender.sendMessage(
            new ComponentBuilder("§5/party leave  §7покинуть текущую команду")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party leave"))
            .create()
        );
        sender.sendMessage(
            new ComponentBuilder("§5/party msg <текст>  §7сообщение членам команды")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party msg "))
            .create()
        );

        sender.sendMessage(
            new ComponentBuilder("§5/party leader <ник> §7передать лидерство другому участнику")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party leader "))
            .create()
        );
        
        sender.sendMessage(
            new ComponentBuilder("§5/party kick <ник> §7выгнать участника из команды")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party kick "))
            .create()
        );
        
        sender.sendMessage(
            new ComponentBuilder("§5/party set §7настройки командного режима")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party set"))
            .create()
        );
        
        sender.sendMessage(
            new ComponentBuilder("§5/party info [ник] §7информация о команде игрока [ник]")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-вывод").create()))
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party info "))
            .create()
        );

            
    }
    
    public static String msgFromArg (final String[]arg, final int start_pos) {
        String r="";
    //System.out.print(" GetReason >>>>>>>>>> 1111 "+arg.length);
        for (int i=start_pos; i<arg.length; i++){
    //System.out.print(" GetReason >>>>>>>>>> "+i+ " >> "+arg[i]+" >> "+r);
    //не давать скобки
            r=r+arg[i]+" ";
        }
        return r;
    }
    
    
    
}
