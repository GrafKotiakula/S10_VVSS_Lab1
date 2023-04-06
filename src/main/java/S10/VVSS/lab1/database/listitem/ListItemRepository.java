package S10.VVSS.lab1.database.listitem;

import S10.VVSS.lab1.database.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ListItemRepository extends JpaRepository<ListItem, UUID> {
    @Query("SELECT MAX(li.priorityChangeNumber) FROM ListItem.li WHERE li.owner = :owner")
    Integer findMaxPriorityChangeNumberForOwner(@Param("owner") User owner);
}
