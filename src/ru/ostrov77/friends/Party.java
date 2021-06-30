package ru.ostrov77.friends;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.komiss77.enums.Data;
import ru.komiss77.enums.Operation;
import ru.ostrov77.auth.bungee.Listener.BungeeMsgHandler;
import ru.ostrov77.auth.bungee.Managers.BungeePM;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.PartyManager;




public class Party{
    
    private final UUID party_uuid;
    private final List<String> members;
    public Set<String> invites;
    //public String leader;

    public Party(final String leader){
            //this.leader = leader;
            this.members = new ArrayList<>();
            this.invites = new HashSet<>();
            this.members.add(leader);
            this.party_uuid = UUID.randomUUID();
    }

    public UUID get_party_uuid(){
        return this.party_uuid;
    }

    public boolean equals(final Party party){
        return this.party_uuid.toString().equals(party.party_uuid.toString());
    }

    public boolean Has_player(final String nik){
        return this.members.contains(nik);
    }

    public void Add_player(final String nik){
        if (!this.members.contains(nik)) this.members.add(nik);
        if (this.invites.contains(nik)) this.invites.remove(nik);
        
        ProxyServer.getInstance().getPlayer(nik).sendMessage(new TextComponent(MainB.partyPrefix+"Вы вошли в команду "+members.get(0)));
        partyMessage("оповещение", nik+" присоединился к команде!");
        syncSpigotMembers();
    }

    public void disband(){
        members.stream().forEach((nik) -> {
            if (ProxyServer.getInstance().getPlayer(nik)!=null) PartyManager.removePlayerFromAllParty(ProxyServer.getInstance().getPlayer(nik));
        });
        PartyManager.parties.remove(this.party_uuid);
    }

    public boolean isEmpty(){
            return members.size() <= 0;
    }

    public void partyMessage(final String sender, final String message){
        members.stream().forEach((nik) -> {
            if(ProxyServer.getInstance().getPlayer(nik) != null) ProxyServer.getInstance().getPlayer(nik).sendMessage(new TextComponent( MainB.partyPrefix+" §3"+sender+"§7: §f"+message));
        });
    }

    public String removePlayer(final String nik){
        if (!members.contains(nik)) return "";
        final boolean leader_exit=members.get(0).equals(nik);
        members.remove(nik);
        if(members.isEmpty()) {
            PartyManager.parties.remove(party_uuid);
            return "§4Команда распалась.";
        }
        if (leader_exit) partyMessage("оповещение", "лидер "+nik+" покинул команду. Новый лидер: "+members.get(0));
        else partyMessage("оповещение","§c"+nik+" покинул команду.");
        syncSpigotMembers();
        return "Лидером стал "+members.get(0)+".";
    }
    
    public void invitePlayerToParty(final String who, final String invited){
        invites.add(invited);
        partyMessage("оповещение", who+" пригласил "+invited+" в команду!");
        ManagerB.GetProxyPlayer(invited).sendMessage(new ComponentBuilder(MainB.partyPrefix+"§a"+who+" пригласил вас в команду! Клик на это сообщение-принять.")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept "+who))
                .create()
        );
        
        ProxyServer.getInstance().getScheduler().schedule(MainB.getInstance(), () -> {
            if (ManagerB.exist(invited) && PartyManager.parties.containsKey(party_uuid) && invites.contains(invited)) {
                invites.remove(invited);
                if (PartyManager.hasParty(invited)) {
                    if (!members.get(0).isEmpty()) ManagerB.GetProxyPlayer(members.get(0)).sendMessage(new TextComponent(MainB.partyPrefix+"§c"+ invited+" §cв итоге вступил в другую команду!" ));
                } else {
                    ManagerB.GetProxyPlayer(invited).sendMessage(new TextComponent(MainB.partyPrefix+"§4Приглашение от §d"+ members.get(0)+" §4утратило актуальность!" ));
                    if (!members.get(0).isEmpty()) ManagerB.GetProxyPlayer(members.get(0)).sendMessage(new ComponentBuilder(MainB.partyPrefix+"§4"+invited+" §dне успел принять приглашение! §8<повторить")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aКлик-повторить приглашение").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party invite "+invited))
                    .create()
                    );
                }
            }
        }, 60L, TimeUnit.SECONDS);

    }

    public void setLeader(final String new_leader) {
        if (new_leader.equals(members.get(0))||!members.contains(new_leader)) return;
        final String old_leader=members.get(0);
        members.remove(new_leader);
        members.set(0, new_leader);
        members.add(old_leader);
        //leader = name;
        partyMessage("оповещение", "лидер "+old_leader+" сложил полномочия. Новый лидер: "+new_leader);
        syncSpigotMembers();
    }

    public void kickMember(final String name) {
        if (!members.contains(name)) return;
        members.remove(name);
        partyMessage("оповещение", members.get(0)+" выгнал "+name+" из команды.");
        if(ProxyServer.getInstance().getPlayer(name) != null) ProxyServer.getInstance().getPlayer(name).sendMessage(new TextComponent( MainB.partyPrefix+"§c"+members.get(0)+" выгнал Вас из команды /:("));
        syncSpigotMembers();
    }

    public String getLeader() {
        return members.get(0);
    }

    public void syncSpigotMembers() {
        final String membersAndServerList=memberAndServerToString();
        final String membersList=memberToString();
        members.stream().forEach((member) -> {
            if (BungeePM.IsAuth(member)) BungeePM.getBplayer(member).setData(Data.PARTY_MEBRERS, membersList, false);
            if (ManagerB.exist(member)) BungeeMsgHandler.sendBungeeMessage(ManagerB.GetProxyPlayer(member), Operation.PF_PARTY_MEMBER, member, membersAndServerList);
        });
    }

    private String memberToString() {
//System.out.println("--memberAsString members="+members);        
        String res="";
        for (String nik:members) {
            if (ManagerB.exist(nik)) res=res+","+nik;
        }
        res=res.replaceFirst(",", "");
//System.out.println("----memberAsString res="+res);        
        return res;
    }
    public String memberAndServerToString() {
//System.out.println("--memberAsString members="+members);        
        String res="";
        for (String nik:members) {
            if (ManagerB.getPFplayer(nik).GetProxyPlayer()!=null) res=res+","+nik+"<>"+ManagerB.getPFplayer(nik).GetProxyPlayer().getServer().getInfo().getName();
        }
        res=res.replaceFirst(",", "");
//System.out.println("----memberAsString res="+res);        
        return res;
    }

    public List<String> getMembers() {
        return members;
    }

    
    
}
