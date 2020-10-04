package org.floriotech.data;

public class ConnexionWithAuthentification extends Connexion{

    private String login;
    private String password;

    public ConnexionWithAuthentification(String name, String adress, String port, String login, String password) {
        super(name, adress, port);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

}
