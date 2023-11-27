package peaksoft.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peaksoft.model.Application;
import peaksoft.model.Genre;
import peaksoft.model.User;
import peaksoft.service.impl.ApplicationService;
import peaksoft.service.impl.GenreService;
import peaksoft.service.impl.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/applications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationController {

    final ApplicationService applicationService;
    final GenreService genreService;
    final UserService userService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, GenreService genreService, UserService userService) {
        this.applicationService = applicationService;
        this.genreService = genreService;
        this.userService = userService;
    }


    @GetMapping("/add")
    public String addApplication(Model model) {
        model.addAttribute("applications", new Application());
        return "applications/save";
    }

    @PostMapping("/save")
    public String saveApplication(@ModelAttribute("applications") Application application) {
        applicationService.save(application);
        return "redirect:find-all";
    }

    @GetMapping("/find-all")
    public String findAll(Model model) {
        model.addAttribute("applicationList", applicationService.findAll());
        return "applications/find-all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Application application = applicationService.findById(id);
        model.addAttribute("application1", application);
        return "applications/update";
    }

    @PostMapping("/save-update/{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("application1") Application application) {
        applicationService.update(id, application);
        return "redirect:find-all";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        applicationService.deleteById(id);
        return "redirect:find-all";
    }

    @GetMapping("/get-app-by-id/{id}")
    public String getAppByUserId(@PathVariable("id") Long id, Model model) {
        List<Application> myApplications = applicationService.getAppByUserId(id);
        model.addAttribute("myApplications", myApplications);
        return "user/my-applications";
    }

    @ModelAttribute("genreList")
    public List<Genre> getGenre() {
        return genreService.findAll();
    }

    @GetMapping("/my-application")
    public String getApplicationByUser(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Application> myApplication = applicationService.getApplicationByUser((user.getId()));
        model.addAttribute("myApplications", myApplication);
        return "applications/userApplication";
    }
    @GetMapping("/search")
    public String findApplicationByName(Model model,String name){
        if(name==null) {
            model.addAttribute("appList", applicationService.findAll());
        }else {
            List<Application> applicationList = applicationService.findApplicationByName(name);
            model.addAttribute("appList",applicationList);
        }
        return "applications/search";
    }
}
