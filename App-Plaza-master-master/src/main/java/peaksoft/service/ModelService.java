package peaksoft.service;

import java.util.List;

public interface ModelService<T> {

    void save(T t);
    T findById(Long id);
    List<T> findAll();
    void update(Long id, T t);
    void deleteById(Long id);

}
