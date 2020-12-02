package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping("/artists")
public class ArtistController {
    @Autowired
    private ArtistRepository artistRepository;

    // Affichage d'un artiste
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getArtistById(@PathVariable Integer id, final ModelMap model) {
        Optional<Artist> artistOptional = artistRepository.findById(id);
        // Gérer erreur 404
        if(artistOptional.isEmpty()){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé !");
        }
        model.put("artist", artistOptional.get());
        return "detailArtist";
    }

    // Recherche par nom
    @RequestMapping(value = "", params = "name", method = RequestMethod.GET)
    public String searchByName(@RequestParam String name, final ModelMap model) {
        Artist artist = artistRepository.findByName(name);
        // Gérer erreur 404
        model.put("artist", artist);
        return "detailArtist";
    }

    // Liste des artistes
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listArtists(final ModelMap model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "ASC") String sortDirection,
                               @RequestParam(defaultValue = "name") String sortProperty) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtist = artistRepository.findAll(pageRequest);
        model.put("artistes", pageArtist);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put ("end", (page) * size + pageArtist.getNumberOfElements());
        return "listeArtists";
    }

}
