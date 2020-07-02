package ru.ostrov77.friends;




public enum F_set {
    
    нет(0, 0, -1, "", ""), 
    ОПОВЕЩ_ВХОД_ПОЛУЧАТЬ(1, 1, 19, "§6Получение оповещений о входе", "§7Получать оповещения,<br>§7когда друзья заходят на остров?"),
    ОПОВЕЩ_ВЫХОД_ПОЛУЧАТЬ(2, 1, 20, "§6Получение оповещений о выходе", "§7Получать оповещения,<br>§7когда друзья отключаются?"),
    ОПОВЕЩ_ВХОД_ОТПРАВЛЯТЬ(3, 1, 23, "§6Отправление оповещений о входе", "§7Отправлять друзьям оповещение,<br>§7когда вы заходите на остров?"),
    ОПОВЕЩ_ВЫХОД_ОТПРАВЛЯТЬ(4, 1, 24, "§6Отправление оповещений о выходе", "§7Отправлять друзьям оповещение,<br>§7когда вы отключетесь?"),
    ОПОВЕЩ_ПЕРЕХОД_ПОЛУЧАТЬ(5, 1, 21, "§6Получение оповещений о переходе", "§7Получать оповещения,<br>§7когда друзья меняют сервер?"),
    ОПОВЕЩ_ПЕРЕХОД_ОТПРАВЛЯТЬ(6, 1, 25, "§6Отправление оповещений о переходе", "§7Отправлять друзьям оповещение,<br>§7когда вы меняете серер?"),
    
    СКРЫВАТЬ_ПРИСУТСТВИЕ(7, 0, 1, "§3Режим 'невидимка'", "§7Скрывать от друзей<br>§7ваше присутствие на сервере?"),
    СООБЩЕНИЯ_ПОЛУЧАТЬ(8, 1, 4, "§3Личные сообщения", "§7Получать личные сообщения<br>§7от друзей?<br>§5комада: §d/fr m ник сообщение"),
    СООБЩЕНИЯ_ОФФЛАЙН(9, 1, 5, "§3Офф-лайн сообщения", "§7Разрешить друзьям <br>§7оставлять Вам сообщения,<br>§7когда Вы не на сервере?<br>§7(Вы их сможете прочитать<br>§7при следующем входе)"),// - отделььная графа
    
    //ТЕЛЕПОРТ_К_ЛИДЕРУ(10, 0, 9, "§eВедOмый", "§7Когда Вы в команде,<br>§7телепортировать Вас к лидеру<br>§7когда он меняет сервер?<br>§7(полезно для командных игр)"),
    ТЕЛЕПОРТ_КО_МНЕ(11, 1, 2, "§eМаячёк", "§7Разрешить друзьям<br>§7отправлять Вам запросы<br>§7на телепорт?"),
    
    ПРИНИМАТЬ_ДРУЖБУ(12, 1, 0, "§eОткрытость", "§7Получать предложения дружить<br>§7от других игроков?"),
    ВИДЕТЬ_РЕЖИМ(13, 0, 11, "§6Режим видимости игроков", ""),
    //ВИДЕТЬ_ВСЕХ(13, 0),
    //ВИДЕТЬ_КОМАНДУ(14, 0),
    //ВИДЕТЬ_ДРУЗЕЙ(15, 0),
    ;
    
    public int tag;
    public int default_value;
    public int inv_slot;
    public String item_name;
    public String item_lore;
    
    
    private F_set(int tag, int default_value, int inv_slot, String item_name, String item_lore){
        this.tag = tag;
        this.default_value = default_value;
        this.inv_slot = inv_slot;
        this.item_name = item_name;
        this.item_lore = item_lore;
    }
    
    public static boolean exist(String set){
        for(F_set s_: F_set.values()){
            if (s_.toString().equals(set)) return true;
        }
        return false;

    }
    public static int nextValue(F_set set, int current){
        if (set==ВИДЕТЬ_РЕЖИМ) {
            current++;
            if (E_view.exist(current)) return current;
            else return 0;
        } else {
            if (current==0) return 1;
            else return 0;
        }
        //int curr_tag=current.tag;
        //curr_tag++;
        //if (exist(curr_tag)) return настройка_по_метке(curr_tag);
        //else return ВИДЕТЬ_ВСЕХ;
        
    }
    
    
    
    public static F_set настройка_по_метке(int tag){
        for(F_set set: F_set.values()){
                if(set.tag==tag){
                        return set;
                }
        }
        return F_set.нет;
    }
    
    public static F_set настройка_по_названию_предмета(String item_name){
        for(F_set set: F_set.values()){
                if(set.item_name.equals(item_name)){
                        return set;
                }
        }
        //for(E_view view: E_view.values()){
        //        if(view.item_name.equals(item_name)){
        //                return ВИДЕТЬ_РЕЖИМ;
        //        }
        //}
        return F_set.нет;
    }
    
    
    
    
}
