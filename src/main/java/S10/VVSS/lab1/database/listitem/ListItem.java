package S10.VVSS.lab1.database.listitem;

import S10.VVSS.lab1.database.AbstractEntity;
import S10.VVSS.lab1.database.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "list_items")
public class ListItem extends AbstractEntity {
    @Column(name = "message", length = 100, nullable = false)
    private String message;

    @Column(name = "is_done", nullable = false)
    private Boolean isDone;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "priority_change_number")
    private Integer priorityChangeNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @JsonProperty
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("isDone")
    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    @JsonIgnore
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonIgnore
    public Integer getPriorityChangeNumber() {
        return priorityChangeNumber;
    }

    public void setPriorityChangeNumber(Integer priorityChangeNumber) {
        this.priorityChangeNumber = priorityChangeNumber;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
