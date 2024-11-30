package de.wordbuch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "dictionary_de")
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryDe {

    @Id
    private String name;

}
