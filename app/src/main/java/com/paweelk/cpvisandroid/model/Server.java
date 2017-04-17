package com.paweelk.cpvisandroid.model;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class Server {

    private long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private int listPosition = 0;

    public long getId() {
        return id;
    }

    public Server setId(long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Server setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getName() {
        return name;
    }

    public Server setName(String name) {
        this.name = name;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Server setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Server setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getListPosition() {
        return listPosition;
    }

    public Server setListPosition(int listPosition) {
        this.listPosition = listPosition;
        return this;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
