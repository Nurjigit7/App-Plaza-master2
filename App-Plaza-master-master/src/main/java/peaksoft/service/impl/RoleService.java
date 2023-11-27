package peaksoft.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import peaksoft.model.Role;

import java.util.List;

@Service
@Transactional
public class RoleService {
    @PersistenceContext
    private EntityManager entityManager;
    public void create(String roleName){
        Role role=new Role();
        role.setRoleName(roleName);
        entityManager.persist(role);
    }

    public Role findByName(String roleName){
        return entityManager.createQuery("select r from Role r where r.roleName =:roleName",Role.class)
                .setParameter("roleName",roleName).getSingleResult();
    }

    public List<Role> findAll(){
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

}
