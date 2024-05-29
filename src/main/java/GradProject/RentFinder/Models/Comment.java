package GradProject.RentFinder.Models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class Comment {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long commentID;
    @Column(name = "DESCRIPTION", length=1024)
    private String description;
    @Column(name = "DATE")
    private Date date;
}
