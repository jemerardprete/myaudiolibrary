package com.myaudiolibrary.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    // Ajout d'un album à un artiste (gérer les 404 et les 409 en cas d'album déjà existant).
    // Suppression d'un album (gérer les 404)
}
