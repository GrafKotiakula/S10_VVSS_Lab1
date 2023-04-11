package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.dto.ListItemOrderRequestDto;
import S10.VVSS.lab1.entities.listitem.ListItem;
import S10.VVSS.lab1.entities.listitem.ListItemService;
import S10.VVSS.lab1.entities.user.User;
import S10.VVSS.lab1.exception.BadRequestException;
import S10.VVSS.lab1.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/list-item")
public class ListItemRestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ListItemService listItemService;

    @Autowired
    public ListItemRestController(ListItemService listItemService) {
        this.listItemService = listItemService;
    }

    private UUID castToUUID(String value) {
        return castToUUID("id", value);
    }

    private UUID castToUUID(String parameter, String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw BadRequestException.wrongFormat(parameter, "UUID", ex);
        }
    }

    private ListItem findListItemByUserAndId(UUID id, User user) {
        return listItemService.findById(id).map(item -> {
            if(item.getOwner().equals(user)){
                return item;
            } else {
                logger.info("{} try to access {}", user, item);
                return null;
            }
        }).orElseThrow(() -> {
            logger.debug("ListItem[{}] not found", id);
            return NotFoundException.entityNotFound(id, ListItem.class);
        });
    }

    @GetMapping("/find-by/user")
    public List<ListItem> getAllByUser(@AuthenticationPrincipal User user){
        return user.getList();
    }

    @GetMapping("/{id}")
    public ListItem getById(@AuthenticationPrincipal User user, @PathVariable("id") String strId) {
        UUID id = castToUUID(strId);
        return findListItemByUserAndId(id, user);
    }

    @PostMapping
    public ListItem create(@AuthenticationPrincipal User user, @RequestBody ListItem item) {
        item.setOwner(user);
        listItemService.validate(item);
        return listItemService.save(item);
    }

    @PutMapping("/{id}")
    public ListItem update(@AuthenticationPrincipal User user, @PathVariable("id") String strId,
                           @RequestBody ListItem request) {
        UUID id = castToUUID(strId);
        ListItem item = findListItemByUserAndId(id, user);
        item.setMessage(request.getMessage());
        item.setDone(request.getDone());
        listItemService.validate(item);
        return listItemService.save(item);
    }

    @PutMapping("/{id}/order")
    public List<ListItem> updateOrder(@AuthenticationPrincipal User user, @PathVariable("id") String strId,
                                      @RequestBody ListItemOrderRequestDto request) {
        UUID id = castToUUID(strId);
        ListItem item = findListItemByUserAndId(id, user);
        item.setPriority(request.getOrder());
        listItemService.saveOrder(item);
        return listItemService.findAllByOwner(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal User user, @PathVariable("id") String strId) {
        UUID id = castToUUID(strId);
        ListItem listItem = findListItemByUserAndId(id, user);
        listItemService.delete(listItem);
    }
}
