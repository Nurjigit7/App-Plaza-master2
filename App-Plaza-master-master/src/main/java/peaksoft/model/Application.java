package peaksoft.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import peaksoft.model.enums.AppStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "applications")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    String developer;
    String version;
    @Enumerated(EnumType.STRING)
    AppStatus appStatus;
    String genreName;
    LocalDate createDate;
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL,
            mappedBy = "applications")
    List<User> users;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "genre_id")
    Genre genre;
    @Transient
    Long genreId;


}
