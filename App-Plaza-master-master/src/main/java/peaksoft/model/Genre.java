package peaksoft.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "genres")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)


public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    LocalDate createDate;
    @OneToMany(cascade = {DETACH, REFRESH, MERGE, PERSIST},
            mappedBy = "genre")
    List<Application> applications;


}
