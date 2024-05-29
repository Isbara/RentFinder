package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

import org.hibernate.annotations.Type;
@Entity
@Table(name = "IMAGE_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long imageID;

    @Lob

    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "DATA", nullable = false)
    private byte[] data;

    @ManyToOne()
    @JoinColumn(name = "PROPERTY_ID")
    @ToString.Exclude
    @JsonIgnore
    private Property property;

}
