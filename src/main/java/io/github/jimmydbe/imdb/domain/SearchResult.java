package io.github.jimmydbe.imdb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class SearchResult {

    private final String id;
    private final String name;
    private final int year;
    private final String type;
    private final String thumbnail;

}
