package com.example.fitpack.Model;

public class User {
    private String id;
    private String username;
    private String imageUrl;
    private String kategori;

    public User() {
    }

    public User(String id, String username, String imageUrl, String kategori) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.kategori = kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
