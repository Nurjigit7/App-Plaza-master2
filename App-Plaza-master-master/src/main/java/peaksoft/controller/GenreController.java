package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peaksoft.model.Genre;
import peaksoft.service.impl.ApplicationService;
import peaksoft.service.impl.GenreService;

import java.util.List;

@Controller
@RequestMapping("/genre")
public class GenreController {

    private final GenreService genreService;
    private final ApplicationService applicationService;

    @Autowired
    public GenreController(GenreService genreService, ApplicationService applicationService) {
        this.genreService = genreService;
        this.applicationService = applicationService;
    }

    @GetMapping("/add")
    public String addGenre(Model model) {
        model.addAttribute("genre", new Genre());
        return "genre/save";
    }

    @PostMapping("/save")
    public String saveGenre(@ModelAttribute("genre") Genre genre) {
        genreService.save(genre);
        return "redirect:find-all";
    }

    @GetMapping("/find-all")
    public String findAll(Model model) {
        model.addAttribute("genreList", genreService.findAll());
        return "genre/find-all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Genre genre = genreService.findById(id);
        model.addAttribute("genre", genre);
        return "genre/update";
    }

    @PostMapping("{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("genre") Genre genre) {
        genreService.update(id, genre);
        return "redirect:find-all";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        genreService.deleteById(id);
        return "redirect:find-all";
    }
}
