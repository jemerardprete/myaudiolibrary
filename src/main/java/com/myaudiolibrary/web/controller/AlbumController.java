package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/albums")
public class AlbumController {

    // Appel du fichier où se trouve les requêtes SQL souhaitées
    @Autowired
    private AlbumRepository albumRepository;

    // 7- Ajout d'un album à un artiste
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Album addAlbum(@RequestBody Album album){
        if(albumRepository.findByTitle(album.getTitle()) != null){
            throw new EntityExistsException("Il existe déjà un album nommé " + album.getTitle());
        }
        return albumRepository.save(album);
    }

    // 8- Suppression d'un album
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable Integer id){
        if(!albumRepository.existsById(id)){
            throw new EntityNotFoundException("L'album d'identifiant " + id + " n'a pas été trouvé");
        }
        albumRepository.deleteById(id);
    }
}
