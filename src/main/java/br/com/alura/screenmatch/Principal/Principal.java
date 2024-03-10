package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.dto.DadosEpisodio;
import br.com.alura.screenmatch.dto.DadosSerie;
import br.com.alura.screenmatch.dto.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6fe4b89b";
    private Scanner leitura = new Scanner(System.in);
    private List<DadosTemporada> temporadas = new ArrayList<>();
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    public void exibirMenu() {
        System.out.println("Digite o nome da Série: ");

        var nomeSerie = leitura.nextLine().replace(" ", "+").toLowerCase();
        var json = this.consumoAPI.obterDados(this.ENDERECO+nomeSerie+this.API_KEY);
        var dtoDadosSerie = this.conversor.obterDados(json, DadosSerie.class);

        for (int i = 1; i <= dtoDadosSerie.totalTemporadas() ; i++) {
            json = this.consumoAPI.obterDados(this.ENDERECO + nomeSerie + "&season=" + i + this.API_KEY);
            var dtoDadostemporada = this.conversor.obterDados(json, DadosTemporada.class);
            this.temporadas.add(dtoDadostemporada);
        }
        List<DadosEpisodio> dadosEpisodios = this.temporadas.stream()
                .flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        dadosEpisodios.stream()
                .filter(dadosEpisodio -> !dadosEpisodio.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
