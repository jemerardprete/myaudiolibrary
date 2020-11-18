package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

// Controller permettant de créer du contenu dans des chemins spécifiques
@RestController
@RequestMapping("/artistes")
public class ArtistController {

    // Appel du fichier où se trouve les requêtes SQL souhaitées
    @Autowired
    private ArtistRepository artistRepository;

    // http://localhost:5367/artistes
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/plain")
    public String hello(){
        return "Hello World!";
    }

    // - Afficher un artiste
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Artist getArtist(@PathVariable(value = "id") Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if(optionalArtist.isEmpty()) {
            // Erreur 404
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé");
        }
        return optionalArtist.get();
    }

    // - Recherche par nom (exemple lorsqu'on recherche le nom "rosmi" dans la barre de recherche, on obtient bien une liste paginée de résultats avec "Aerosmith" ainsi que sur "Aerosmith & Sierra Leone's Refugee Allstars". Lorsqu'on recherche un artiste inexistant commme "ABCDEF", on obtient une liste vide).
    // - Liste des artistes avec pagination
    // - Création d'un artiste (gestion de l'erreur 409 s'il existe déjà un artiste de même nom)
    // Modification d'un artiste (Méthode PUT avec gestion des 404)
    // Suppression d'un artiste (gérer les 404 et le bon code HTTP de retour, gérer de manière cohérente le cascading...)

}
