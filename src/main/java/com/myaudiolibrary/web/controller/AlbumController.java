package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping("/albums")
public class AlbumController {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    // Création d'un album et redirection vers l'artiste sur lequel il a été ajouté
    @RequestMapping(method = RequestMethod.POST, value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createAlbum(Album album, @PathVariable Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        album.setArtist(optionalArtist.get());
        albumRepository.save(album);
        return new RedirectView("/artists/" + optionalArtist.get().getId());

    }

    // Suppression d'un album
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/delete")
    public RedirectView deleteAlbum(@PathVariable Integer id){
        albumRepository.deleteById(id);
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }

}