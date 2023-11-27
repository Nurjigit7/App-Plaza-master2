package peaksoft.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import peaksoft.model.MailSender;
import peaksoft.model.User;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MailSenderService {
    @PersistenceContext
    private EntityManager entityManager;
    private final JavaMailSender javaMailSender;
    private final UserService userService;

    @Autowired
    public MailSenderService(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    public void save(MailSender mailSender) {
        List<User> userList = userService.findAll();
        for (User user : userList) {
            if (user.isSubscribeToTheNewsletter()) {
                sendMessage(mailSender, user.getEmail());
            }
        }
        mailSender.setCreateDate(LocalDate.now());
        entityManager.persist(mailSender);
    }

    public void sendMessage(MailSender mailSender, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("AppPlaza");
        message.setSubject(mailSender.getSubject());
        message.setText(mailSender.getText());
        javaMailSender.send(message);
    }

    public List<MailSender> findAll() {
        return entityManager.createQuery("from MailSender", MailSender.class).getResultList();
    }

}
