package S10.VVSS.lab1.database.listitem;

import S10.VVSS.lab1.database.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public ListItem save(ListItem entity) {
        Integer max = repo.findMaxPriorityChangeNumberForOwner(entity.getOwner());
        entity.setPriorityChangeNumber(max + 1);
        return super.save(entity);
    }
}
