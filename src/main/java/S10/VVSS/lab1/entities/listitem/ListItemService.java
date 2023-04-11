package S10.VVSS.lab1.entities.listitem;

import S10.VVSS.lab1.entities.AbstractService;
import S10.VVSS.lab1.entities.user.User;
import S10.VVSS.lab1.exception.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListItemService extends AbstractService<ListItem, ListItemRepository> {

    @Autowired
    public ListItemService(ListItemRepository repo) {
        super(repo, ListItem.class);
        defaultSort = Sort.by(Sort.Order.asc("owner"),
                Sort.Order.asc("priority"),
                Sort.Order.desc("priorityChangeNumber"));
    }

    @Override
    public void validate(ListItem entity) throws ClientException {
        validateNotNull("message", entity.getMessage());
        validateNotBlank("message", entity.getMessage());
        validateStringLength("message", entity.getMessage(), 100);

        validateNotNull("isDone", entity.getDone());
        validateNotNull("owner", entity.getOwner());
        validateNotNull("priority", entity.getPriority());
        validateNotNull("priorityChangeNumber", entity.getPriorityChangeNumber());
    }

    public List<ListItem> findAllByOwner(User owner) {
        return repo.findAllByOwner(owner, defaultSort);
    }

    @Override
    public ListItem save(ListItem entity) {
        if(entity.getId() == null) {
            Integer maxPriority = repo.findMaxPriorityForOwner(entity.getOwner());
            Integer maxPriorityChangeNumber = repo.findMaxPriorityChangeNumberForOwner(entity.getOwner());
            entity.setPriority(maxPriority + 1);
            entity.setPriorityChangeNumber(maxPriorityChangeNumber + 1);
        }
        return super.save(entity);
    }

    public ListItem saveOrder(ListItem listItem) {
        Integer maxPriorityChangeNumber = repo.findMaxPriorityChangeNumberForOwner(listItem.getOwner());
        listItem.setPriorityChangeNumber(maxPriorityChangeNumber + 1);
        return super.save(listItem);
    }
}
