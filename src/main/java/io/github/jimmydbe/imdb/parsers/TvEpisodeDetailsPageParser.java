package io.github.jimmydbe.imdb.parsers;

import io.github.jimmydbe.imdb.domain.TvEpisodeDetails;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class TvEpisodeDetailsPageParser implements Parser<TvEpisodeDetails> {

    private static final String AIR_DATE = "TvEpisodeDetailsPageParser.airDate";
    private static final String SHOW_NAME = "TvEpisodeDetailsPageParser.showName";
    private static final String GENRES = "TvEpisodeDetailsPageParser.genres";
    private static final String EPISODE_NUMBER = "TvEpisodeDetailsPageParser.episodeNumber";
    private static final String SEASON_NUMBER = "TvEpisodeDetailsPageParser.seasonNumber";
    private static final String EPISODE_NAME = "TvEpisodeDetailsPageParser.episodeName";

    private final Properties properties;

    public TvEpisodeDetailsPageParser(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TvEpisodeDetails parse(Element document, Optional<Element> detailsDocument) {
        String showName = getShowName(document);
        String episodeName = getEpisodeName(document);
        Long seasonNumber = getSeasonNumber(document);
        Long episodeNumber = getEpisodeNumber(document);
        List<String> genres = getGenres(document);
        LocalDate airDate = getAirDate(document);

        return new TvEpisodeDetails(showName, episodeName, seasonNumber, episodeNumber, genres, airDate);
    }

    private LocalDate getAirDate(Element document) {
        String[] dateStringWithBrackets = document.select(properties.get(AIR_DATE).toString()).text().split(Pattern.quote("|"));

        String dateString = Arrays.stream(dateStringWithBrackets)
                .filter(n -> n.contains("aired"))
                .findFirst()
                .get()
                .replace("Episode aired ", "")
                .trim();

        if (dateString.length() == 4) {
            return LocalDate.of(Integer.parseInt(dateString), 1, 1);
        }
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    private List<String> getGenres(Element document) {
        String genreString = document.select(properties.get(GENRES).toString()).text().trim().replace(" Add a plot", "");
        List<String> answer = new ArrayList<>();
        for (String genre : genreString.split(", ")) {
            answer.add(genre.trim());
        }
        return answer;
    }

    private Long getEpisodeNumber(Element document) {
        String episodeInfo = document.select(properties.get(EPISODE_NUMBER).toString()).text();
        return Long.parseLong(episodeInfo.replace("E", "").trim());
    }

    private Long getSeasonNumber(Element document) {
        String seasonInfo = document.select(properties.get(SEASON_NUMBER).toString()).text();
        return Long.parseLong(seasonInfo.replace("S", "").trim());
    }

    private String getEpisodeName(Element document) {
        return document.select(properties.get(EPISODE_NAME).toString()).text();
    }

    private String getShowName(Element document) {
        return document.select(properties.get(SHOW_NAME).toString()).text();
    }

}
