package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peaksoft.model.User;
import peaksoft.service.impl.ApplicationService;
import peaksoft.service.impl.UserService;

import java.security.Principal;


@Controller
@RequestMapping()
public class UserController {
    private final UserService userService;
    private final ApplicationService applicationService;

    @Autowired
    public UserController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }
    @GetMapping()
    public String mainPage(){
        return "user/main-page";
    }

    @GetMapping("/add")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "user/save";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        userService.save(user);
        User user1 = userService.findById(user.getId());
        model.addAttribute("user", user1);
        model.addAttribute("app", applicationService.findAll());
        return "redirect:/";
    }

    @GetMapping("/find-all")
    public String getAllUser(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/find-all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/save-update/{id}")
    public String saveUpdate(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        userService.update(id, user);
        return "user/main-page";
    }

    @GetMapping("/profile")
    public String getUserProfile(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/";
    }


    @GetMapping("/download/{appId}")
    public String addApplicationByUser( @PathVariable("appId") Long appId,Principal principal) {
        User user = userService.findByEmail(principal.getName());
         userService.addApplicationByUser(user.getId(),appId);
        return "redirect:/applications/my-application";
    }
}
