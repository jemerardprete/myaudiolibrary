package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
    @GetMapping(params = "name")
    public String searchByName(final ModelMap model, @RequestParam("name") String name,
                                                     @RequestParam(value="page", defaultValue = "0") Integer page,
                                                     @RequestParam(value="size", defaultValue = "10") Integer size,
                                                     @RequestParam(value="sortDirection", defaultValue = "ASC") String sortDirection,
                                                     @RequestParam(value="sortProperty", defaultValue = "name") String sortProperty) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtist = artistRepository.findAllByNameContaining(name, pageRequest);
        model.put("artists", pageArtist);
        model.put("sortDirection", sortDirection);
        model.put("sortProperty", sortProperty);
        model.put("size", size);
        model.put("name", name);
        model.put("pageNumber", page);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put ("end", (page) * size + pageArtist.getNumberOfElements());
        return "listeArtists";
    }

    // Liste des artistes
    @GetMapping()
    public String listArtists(final ModelMap model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "ASC") String sortDirection,
                               @RequestParam(defaultValue = "name") String sortProperty) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtist = artistRepository.findAll(pageRequest);
        model.put("artists", pageArtist);
        model.put("sortDirection", sortDirection);
        model.put("sortProperty", sortProperty);
        model.put("size", size);
        model.put("pageNumber", page);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put ("end", (page) * size + pageArtist.getNumberOfElements());
        return "listeArtists";
    }

    // CREATION, MODIFICATION & SUPPRESSION
    // VOIR GESTION DES ERREURS

    @RequestMapping(method= RequestMethod.GET, value = "/new")
    public String newArtist(final ModelMap model){
        model.put("artist", new Artist());
        return "detailArtist";
    }

    // Création et modification d'un artiste
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveArtist(Artist artist){
        return saveArtist(artist);
    }

    // Redirection vers l'artiste venant d'être crée
    private RedirectView saveArtist(Artist artist){
        artist = artistRepository.save(artist);
        return new RedirectView("/artists/" + artist.getId());
    }

    // Suppression => Rediriger vers la liste des artistes
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/delete")
    public RedirectView deleteArtist(@PathVariable Integer id){
        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé !");
        }
        artistRepository.deleteById(id);
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }

}
