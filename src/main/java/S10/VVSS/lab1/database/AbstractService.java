package S10.VVSS.lab1.database;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AbstractService<E extends AbstractEntity, R extends JpaRepository<E, UUID>> {
    protected final R repo;
    protected Sort defaultSort;

    public AbstractService(R repo) {
        this.repo = repo;
        defaultSort = Sort.unsorted();
    }

    public List<E> findAll() {
        return repo.findAll(defaultSort);
    }

    public Optional<E> findById(UUID id) {
        return repo.findById(id);
    }

    public E save(E entity) {
        return repo.saveAndFlush(entity);
    }

    public void delete(E entity) {
        repo.delete(entity);
    }

    public Sort getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(Sort defaultSort) {
        this.defaultSort = defaultSort;
    }
}
