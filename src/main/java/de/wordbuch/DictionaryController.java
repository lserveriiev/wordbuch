package de.wordbuch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionaries")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryDeRepository repository;

    @GetMapping
    public Slice<DictionaryDe> dictionaries(
            @RequestParam SearchCondition searchCondition,
            @RequestParam String searchWord,
            @PageableDefault Pageable pageable
    ) {
        return switch (searchCondition) {
            case START_WITH -> repository.findAllByNameStartingWith(searchWord, pageable);
            case END_WITH -> repository.findAllByNameEndingWith(searchWord, pageable);
            default -> repository.findAllByNameContains(searchWord, pageable);
        };
    }

}
