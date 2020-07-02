package ru.ostrov77.friends;




public enum P_set {
    
    нет(0, false, -1, "", ""), 
    ПОЛУЧАТЬ_ПРИГЛАШЕНИЯ(1, true, 10, "§6Команда", "§7Получать приглашения<br>§7в команды?"),
    ТОЛЬКО_ДРУЗЬЯ(2, false, 12, "§6Только друзья", "§7Получать приглашения<br>§7только от друзей?"),
    ТЕЛЕПОРТ_К_ЛИДЕРУ(3, false, 14, "§eВедOмый", "§7Когда Вы в команде,<br>§7телепортировать Вас к лидеру<br>§7когда он меняет сервер?<br>§7(полезно для командных игр)"),
    УВЕДОМЛЕНИЕ_ТП_ЛИДЕРА(4, false, 16, "§eСледящий", "§7Получать уведомления,<br>§7когда лидер переходит<br>§7на другой сервер?"),

    ;

    public int tag;
    public boolean default_value;
    public int inv_slot;
    public String item_name;
    public String item_lore;
    
    
    private P_set(int tag, boolean default_value, int inv_slot, String item_name, String item_lore){
        this.tag = tag;
        this.default_value = default_value;
        this.inv_slot = inv_slot;
        this.item_name = item_name;
        this.item_lore = item_lore;
    }
    
    public static boolean exist(String set){
        for(P_set s_: P_set.values()){
            if (s_.toString().equals(set)) return true;
        }
        return false;

    }
    
    
    
    public static P_set настройка_по_метке(int tag){
        for(P_set set: P_set.values()){
                if(set.tag==tag){
                        return set;
                }
        }
        return P_set.нет;
    }
    
    public static P_set настройка_по_названию_предмета(String item_name){
        for(P_set set: P_set.values()){
                if(set.item_name.equals(item_name)){
                        return set;
                }
        }
        //for(E_view view: E_view.values()){
        //        if(view.item_name.equals(item_name)){
        //                return ВИДЕТЬ_РЕЖИМ;
        //        }
        //}
        return P_set.нет;
    }
    
    
    
    
}
