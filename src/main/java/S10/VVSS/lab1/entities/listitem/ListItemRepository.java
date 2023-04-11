package S10.VVSS.lab1.entities.listitem;

import S10.VVSS.lab1.entities.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ListItemRepository extends JpaRepository<ListItem, UUID> {
    List<ListItem> findAllByOwner(User owner, Sort sort);

    @Query("SELECT COALESCE(MAX(li.priority), 0) FROM ListItem li WHERE li.owner = :owner")
    Integer findMaxPriorityForOwner(@Param("owner") User owner);

    @Query("SELECT COALESCE(MAX(li.priorityChangeNumber), 0) FROM ListItem li WHERE li.owner = :owner")
    Integer findMaxPriorityChangeNumberForOwner(@Param("owner") User owner);
}
