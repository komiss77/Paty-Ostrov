package ru.ostrov77.friends.bungee;


import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.Party;





public class PartyManager {

    
    public static HashMap<UUID, Party> parties;

    
    public PartyManager(MainB main){
        parties = new HashMap<>();
    }

    public static void createParty(final ProxiedPlayer p){
        Party party = new Party(p.getName());
        parties.put(party.get_party_uuid(), party);
        p.sendMessage(new ComponentBuilder(MainB.partyPrefix+"§aКоманда создана! §6/party invite [ник] §a- пригласить друзей! §8<клик")
        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party invite "))
        .create()
        );
        cleanInvites(p.getName());
    }

    public static Party getPartyFromUuid(final UUID uuid){
        for(UUID id:parties.keySet()){
            if(id.equals(uuid)){
                return parties.get(id);
            }
        }
        return null;
    }


    public static boolean hasParty(final ProxiedPlayer p){
        return hasParty(p.getName());
    }
    public static boolean hasParty(final String nik){
        return parties.values().stream().anyMatch((party) -> (party.Has_player(nik)));
    }

    public static Party Get_player_party(final ProxiedPlayer p){
        return Get_player_party(p.getName());
    }
    public static Party Get_player_party(final String nik){
        for (Party party:parties.values()) {
            if (party.Has_player(nik)) return party;
        }
        return null;
    }

    public static void addPlayerToParty(final ProxiedPlayer p, final Party party){
        removePlayerFromAllParty(p);
        party.Add_player(p.getName());
        //party.partyMessage("", "§a"+p.getName()+" присоединился к команде!");
        cleanInvites(p.getName());
    }

    
    public static void removePlayerFromAllParty(final ProxiedPlayer p){
        if(hasParty(p)) Get_player_party(p).removePlayer(p.getName());
    }
    
    public static void cleanInvites(final String nik){
        parties.values().stream().forEach((party)-> {
            if (party.invites.contains(nik)) party.invites.remove(nik);
        });
    }




    
    /*

    public boolean isInvited(final ProxiedPlayer p){
        return getInvitedParty(p)!= null;
    }

    public Party getInvitedParty(final ProxiedPlayer p){
        for (Party party:parties.values()) {
            if (party.invites.contains(p.getName())) return party;
        }
        return null;
    }
    */



        
        
}
