package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

// Controller permettant de créer du contenu dans des chemins spécifiques
@CrossOrigin
@RestController
@RequestMapping("/artists")
public class ArtistController {

    // Appel du fichier où se trouve les requêtes SQL souhaitées
    @Autowired
    private ArtistRepository artistRepository;

    // http://localhost:5366/artists
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/plain")
    public String hello(){
        return "Hello World!";
    }

    // - 1. Afficher un artiste
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Artist getArtist(@PathVariable(value = "id") Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if(!optionalArtist.isPresent()) {
            // Erreur 404
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé");
        }
        return optionalArtist.get();
    }

    // 2 - Recherche par nom
    @RequestMapping(method = RequestMethod.GET, value = "", params = {"name"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Artist searchByName(@RequestParam String name){
        Artist artist = artistRepository.findByName(name);
        if (artist == null){
            throw new EntityNotFoundException("L'artiste nommé " + name + " n'a pas été trouvé");
        }
        return artist;
    }

    // 3 - Afficher la liste des artistes
    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE, params = {"page", "size", "sortProperty", "sortDirection"})
    public Page<Artist> listeArtists(
            // Paramètres passés dans l'URL
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sortProperty,
            @RequestParam String sortDirection
    ){
        if (page < 0) {
            // Erreur 400
            throw new IllegalArgumentException("Le paramètre page doit être positif ou nul !");
        }
        if (size <= 0 || size > 50) {
            throw new IllegalArgumentException("Le paramètre size doit être compris entre 0 et 50 !");
        }
        if(!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("Le paramètre sortDirection doit valoir ASC ou DESC");
        }
        return artistRepository.findAll(PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty));
    }

    // 4 - Création d'un artiste (gestion de l'erreur 409 s'il existe déjà un artiste de même nom)
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // Retourne 201 au lieu de 200
    @ResponseStatus(HttpStatus.CREATED)
    // @RequestBody = envoi de données complexes (ex : création d'un objet)
    public Artist createArtist(@RequestBody Artist artist){
        if(artistRepository.findByName(artist.getName()) != null){
            throw new EntityExistsException("Il existe déjà un artiste nommé " + artist.getName());
        }
        return artistRepository.save(artist);
    }

    // 5 - Modification d'un artiste
    // équivalent de @RequestMapping(method = RequestMethod.PUT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Artist updateArtist(@PathVariable Integer id, @RequestBody Artist artist){
        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé");
        }
        return artistRepository.save(artist);
    }

    // 7 - Suppression d'un artiste (gérer la suppression en cascade !!!)
    // équivalent de @RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping(value = "/{id}") // Pas de consumes ou de produces pour le delete
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retourne 204
    public void deleteArtist(@PathVariable Integer id){
        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé");
        }
        artistRepository.deleteById(id);
    }

}
