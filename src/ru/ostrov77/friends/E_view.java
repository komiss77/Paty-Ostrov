package ru.ostrov77.friends;




public enum E_view {
    
    ВИДЕТЬ_ВСЕХ(0, 27, "§3Зрячий", "§7Видеть всегда и всех."), 
    ВИДЕТЬ_ДРУЗЕЙ_И_КОМАНДУ(1, 28, "§3Только свои!", "§7Видеть только друзей <br>§7и членов вашей команды.<br>§7(другие игроки Вас видят!)"), 
    ВИДЕТЬ_ДРУЗЕЙ(2, 29, "§3Только друзья!", "§7Видеть только друзей.<br>§7(другие игроки Вас видят!)"), 
    ВИДЕТЬ_КОМАНДУ(3, 30, "§3Только команда!", "§7Видеть только членов команды.<br>§7(другие игроки Вас видят!)"), 
    СКРЫТЬ_ВСЕХ(4, 31, "§3Исчезните все!", "§7Никого не видеть.<br>§7Учтите, что это <br>§7'голову в песок' -<br>§7другие игроки Вас видят!"), 
    ;
    
    public int tag;
    public int inv_slot;
    public String item_name;
    public String item_lore;
    
    
    private E_view(int tag, int inv_slot, String item_name, String item_lore){
        this.tag = tag;
        this.inv_slot = inv_slot;
        this.item_name = item_name;
        this.item_lore = item_lore;
    }
    
    
    
    public static E_view настройка_по_метке(int tag){
        for(E_view set: E_view.values()){
                if(set.tag==tag){
                        return set;
                }
        }
        return ВИДЕТЬ_ВСЕХ;
    }
    
    public static E_view next(E_view current){
        int curr_tag=current.tag;
        curr_tag++;
        if (exist(curr_tag)) return настройка_по_метке(curr_tag);
        else return ВИДЕТЬ_ВСЕХ;
        
    }
    
    public static boolean exist(int tag){
        for(E_view s_: E_view.values()){
            if (s_.tag==tag) return true;
        }
        return false;
    }
    
    public static boolean exist(String type){
        for(E_view s_: E_view.values()){
            if (s_.toString().equals(type)) return true;
        }
        return false;
    }
    
    public static E_view видимость_по_названию_предмета(String item_name){
        for(E_view view: E_view.values()){
                if(view.item_name.equals(item_name)){
                        return view;
                }
        }
        return ВИДЕТЬ_ВСЕХ;
    }
    
    
}
