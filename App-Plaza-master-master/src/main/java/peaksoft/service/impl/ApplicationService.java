package peaksoft.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import peaksoft.model.Application;
import peaksoft.model.Genre;
import peaksoft.model.User;
import peaksoft.service.ModelService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ApplicationService implements ModelService<Application> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Application application) {
        Genre genre = entityManager.find(Genre.class, application.getGenreId());
        application.setGenre(genre);
        application.setGenreName(genre.getName());
        application.setCreateDate(LocalDate.now());
        entityManager.persist(application);

    }

    @Override
    public Application findById(Long id) {
        return entityManager.find(Application.class, id);
    }

    @Override
    public List<Application> findAll() {
        return entityManager.createQuery("from Application", Application.class).getResultList();
    }

    @Override
    public void update(Long id, Application application) {
        Application oldApplication = findById(id);
        oldApplication.setName(application.getName());
        oldApplication.setDescription(application.getDescription());
        oldApplication.setDeveloper(application.getDeveloper());
        oldApplication.setVersion(application.getVersion());
        oldApplication.setAppStatus(application.getAppStatus());
        oldApplication.setGenreName(application.getGenre().getName());
        entityManager.persist(oldApplication);

    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }

    public List<Application> getAppByUserId(Long id) {
        User user = entityManager.find(User.class, id);
        List<Application> applications = user.getApplications();
        return applications;
    }

    public List<Application> getApplicationByUser(Long userId) {
        return entityManager.createQuery(
                        "select app from Application app join app.users u where u.id=:id", Application.class)
                .setParameter("id", userId).getResultList();
    }

    public List<Application> findApplicationByName(String name) {
        return entityManager.createQuery("select app from Application app where app.name=:name", Application.class).
                setParameter("name", name).getResultList();
    }
}
