package peaksoft.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "mailsenders")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String subject;

    String text;

    LocalDate createDate;
}
