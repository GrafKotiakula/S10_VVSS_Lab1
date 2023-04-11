package S10.VVSS.lab1.entities;

import S10.VVSS.lab1.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractService<E extends AbstractEntity, R extends JpaRepository<E, UUID>>
        extends AbstractValidator<E> {
    protected final R repo;
    protected Sort defaultSort;

    public AbstractService(R repo, Class<E> entityClass) {
        super(entityClass);
        this.repo = repo;
        defaultSort = Sort.unsorted();
    }

    public List<E> findAll() {
        return repo.findAll(defaultSort);
    }

    public Optional<E> findById(UUID id) {
        return repo.findById(id);
    }

    public E findByIdOrThrow(UUID id) throws NotFoundException {
        return findById(id).orElseThrow(() -> NotFoundException.entityNotFound(id, entityClass));
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
