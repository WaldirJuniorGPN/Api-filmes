package br.com.alura.screenmatch.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String pontuacao,
        @JsonAlias("Genre") String categoria,
        @JsonAlias("Poster") String imagem,
        @JsonAlias("Plot") String snipse
) {
}
