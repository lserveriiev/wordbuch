package de.wordbuch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryDeRepository extends JpaRepository<DictionaryDe, String> {

    Page<DictionaryDe> findAllByNameStartingWith(String word, Pageable pageable);

    Page<DictionaryDe> findAllByNameEndingWith(String word, Pageable pageable);

    Page<DictionaryDe> findAllByNameContains(String word, Pageable pageable);

}
