package org.floriotech.data;

public class Connexion {

    private String name;
    private String adress;
    private String port;

    public Connexion(String name, String adress, String port) {
        this.name = name;
        this.adress = adress;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public String getPort() {
        return port;
    }

    public String getConnexionString(){
        return "mongodb://" + adress + ":" + name;
    }
}
