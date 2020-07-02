package ru.ostrov77.friends.bungee.command;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import ru.ostrov77.friends.bungee.ManagerB;
import ru.ostrov77.friends.bungee.MainB;
import ru.ostrov77.friends.bungee.PFplayerB;
import ru.ostrov77.auth.bungee.Auth;







public class JumpAccept {

    public static void execute(final ProxiedPlayer to, final String[] args) {

        if (args.length < 2) {
            to.sendMessage(new TextComponent(MainB.friendsPrefix+ "не указан целевой игрок!"));
            to.sendMessage(new TextComponent("§5/fr jaccept <ник> §7- принять запрос на ТП от игрока"));
            return;
        } 
        
        final PFplayerB pf_to=ManagerB.getPFplayer(to.getName());
        final String from_ = args[1];
        
        if (!ManagerB.exist(from_)) {
            to.sendMessage(new TextComponent(MainB.friendsPrefix+" §c"+from_+ " §5нет на сервере!"));
            return;
        }
        
        //final PFplayerB pf_from=ManagerB.getPFplayer(from_);
        
        if (!ManagerB.getPFplayer(from_).jump_reqiest_to.contains(pf_to.nik)) {
            to.sendMessage(new TextComponent(MainB.friendsPrefix+ "§cзапроса на ТП от "+from_+" не было, либо запрос устарел!"));
            return;
        }
        
        ManagerB.getPFplayer(from_).jump_reqiest_to.clear();
        
        //final ProxiedPlayer from=ManagerB.getPFplayer(from_).GetProxyPlayer();
//System.out.println("jump 11111111111");        
        Auth.teleport(ManagerB.getPFplayer(from_).GetProxyPlayer(), to);
        //jump (to, ManagerB.getPFplayer(from_).GetProxyPlayer());

    }
 
    
    
    
    
    
  /*  public static void jump(final ProxiedPlayer to, final ProxiedPlayer from ) {

        if (to.getServer().getInfo().getName().equals(from.getServer().getInfo().getName())) {
            
        } else {
            from.connect(to.getServer().getInfo(),new Callback<Boolean>() {
                @Override
                public void done(Boolean ok, Throwable thrwbl) {
                    if (ok) {
//System.out.println("jump.done() !!!!!!!!!!!!!!!!!");    
                       // SpigotMessage.sendMSGto(message, server);
                    }
                }
            });
        }
        
     
    }*/
   
    
    
    
    
    
    
    
    
    
    
}
