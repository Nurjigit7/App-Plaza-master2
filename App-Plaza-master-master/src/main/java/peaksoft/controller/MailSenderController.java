package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peaksoft.model.MailSender;
import peaksoft.service.impl.MailSenderService;

import java.util.List;

@Controller
@RequestMapping("/mailing")
public class MailSenderController {
    private final MailSenderService mailSenderService;
    @Autowired
    public MailSenderController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @GetMapping("/add")
    public String addMailSender(Model model) {
        model.addAttribute("mailing", new MailSender());
        return "mailSender/save";
    }

    @PostMapping("/save")
    public String saveMailing(@ModelAttribute("mailSender") MailSender mailSender) {
        mailSenderService.save(mailSender);
        return "redirect:/mailing/find-all";
    }
    @GetMapping("/find-all")
    public String findAll(Model model) {
        List<MailSender> mailSenderList = mailSenderService.findAll();
        model.addAttribute("mailingList", mailSenderList);
        return "mailSender/get-all";
    }

}
