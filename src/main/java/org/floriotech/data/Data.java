package org.floriotech.data;

import java.util.List;

public class Data {

    private static Data instance;

    public List<Connexion> connexionList;

    private Data() {
    }

    public static Data getInstance(){
        if(instance == null){
            instance = new Data();
        }
        return instance;
    }

}
