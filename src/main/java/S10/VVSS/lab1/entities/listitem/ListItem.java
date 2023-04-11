package S10.VVSS.lab1.entities.listitem;

import S10.VVSS.lab1.entities.AbstractEntity;
import S10.VVSS.lab1.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "list_items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListItem extends AbstractEntity {
    @Column(name = "message", length = 100, nullable = false)
    private String message;

    @Column(name = "is_done", nullable = false)
    private Boolean isDone;

    @Column(name = "priority", nullable = false)
    private Integer priority = 0;

    @Column(name = "priority_change_number")
    private Integer priorityChangeNumber = 0;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("isDone")
    public Boolean getDone() {
        return isDone;
    }

    @JsonProperty("isDone")
    public void setDone(Boolean done) {
        isDone = done;
    }

    @JsonIgnore
    public Integer getPriority() {
        return priority;
    }

    @JsonIgnore
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonIgnore
    public Integer getPriorityChangeNumber() {
        return priorityChangeNumber;
    }

    @JsonIgnore
    public void setPriorityChangeNumber(Integer priorityChangeNumber) {
        this.priorityChangeNumber = priorityChangeNumber;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    @JsonIgnore
    public void setOwner(User owner) {
        this.owner = owner;
    }
}
