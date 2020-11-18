package com.myaudiolibrary.web.model;

import javax.persistence.*;

// Annoter la classe Album en tant qu'entité pour qu'elle puisse récupérer les données de la table album
@Entity
public class Album {

    // Annoter champ id de manière a ce qu'il puisse gérer les identifiants générés automatiquement par MySQL.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AlbumId")
    private Integer id;

    @Column(name = "Title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "ArtistId", nullable = false)
    private Artist artist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
