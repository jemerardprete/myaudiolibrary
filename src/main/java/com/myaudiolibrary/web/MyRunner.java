package com.myaudiolibrary.web;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyRunner implements CommandLineRunner {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public void run(String... strings) throws Exception {
        // Faire des tests
        Optional<Artist> artist = artistRepository.findById(1);
        if(artist.isEmpty()){
            System.out.println("Artiste inexistant");
        } else {
            Artist e = artist.get();
            System.out.println(e.toString());
        }
    }
}
