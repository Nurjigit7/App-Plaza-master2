package peaksoft.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.model.Application;
import peaksoft.model.Role;
import peaksoft.model.User;
import peaksoft.service.ModelService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService implements ModelService<User> {
    @PersistenceContext
    private EntityManager entityManager;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserService(RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        List<User> users = findAll();
        if (users.isEmpty()) {
            for (User user1 : users) {
                if (user1.getEmail().equals(user.getEmail())) {
                    throw new EntityExistsException("The user from such mail already exists"+user.getEmail());
                }
            }
        }
        if (roleService.findAll().isEmpty()) {
            roleService.create("ADMIN");
            roleService.create("USER");
        }
        if (findAll().isEmpty()) {
            Role adminRole = roleService.findByName("ADMIN");
            adminRole.setUsers(Collections.singletonList(user));
            user.setRoles(Collections.singletonList(adminRole));
        } else {
            Role userRole = roleService.findByName("USER");
            userRole.setUsers(Collections.singletonList(user));
            user.setRoles(Collections.singletonList(userRole));
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateDate(LocalDate.now());
        entityManager.persist(user);
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public void update(Long id, User user) {
        User oldUser = findById(id);
        oldUser.setName(user.getName());
        oldUser.setAge(user.getAge());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setSubscriptionStatus(user.getSubscriptionStatus());
        oldUser.setSubscribeToTheNewsletter(user.isSubscribeToTheNewsletter());
        entityManager.persist(oldUser);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    public User findByEmail(String email) {
        return entityManager.createQuery("select u from  User u where u.email=:emaill", User.class)
                .setParameter("emaill", email).getSingleResult();
    }

//    public User getByEmailAndPassword(String email, String password) {
//        return (User) entityManager.createQuery(
//                "select u from User u where u.email=:email and u.password=:password").setParameter(
//                "email", email).setParameter(
//                "password", password).getSingleResult();
//    }

    public void addApplicationByUser(Long userId, Long appId) {
        User user = findById(userId);
        Application application = entityManager.find(Application.class, appId);
        if (user != null && application != null) {
            List<Application> myApplications = user.getApplications();
            if (!myApplications.contains(application)) {
                myApplications.add(application);
                entityManager.persist(user);
            }
        }
    }


}
