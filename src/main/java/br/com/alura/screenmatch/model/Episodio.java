package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.dto.DadosEpisodio;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
@Getter
public class Episodio {
    private Integer numeroTemporada;
    private String titulo;
    private Integer episodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.numeroTemporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.episodio = dadosEpisodio.numeroEpisodio();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }
    }

    @Override
    public String toString() {
        return "temporada=" + numeroTemporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + episodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
