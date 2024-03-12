package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.dto.DadosEpisodio;
import br.com.alura.screenmatch.dto.DadosSerie;
import br.com.alura.screenmatch.dto.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        var json = this.consumoAPI.obterDados(this.ENDERECO + nomeSerie + this.API_KEY);
        var dtoDadosSerie = this.conversor.obterDados(json, DadosSerie.class);

        for (int i = 1; i <= dtoDadosSerie.totalTemporadas(); i++) {
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

        List<Episodio> episodios = this.temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(dadosEpisodio -> new Episodio(t.numeroTemporada(), dadosEpisodio))).collect(Collectors.toList());

        System.out.println("\n");

        episodios.stream()
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        System.out.println("Digite o trecho de um título para buscá-lo: ");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> resultado = episodios.stream()
                .filter(episodio -> episodio.getTitulo().matches("(?i).*" + trechoTitulo + ".*")).findFirst();

        if (resultado.isPresent()) {
            System.out.println("Episódio encontrado!");
            System.out.println("\nTemporada: " + resultado.get().getNumeroTemporada() +
                " Título: " + resultado.get().getTitulo());
        }

//        System.out.println("\nInsira o ano para filtrar episódios a partir dele: ");
//
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate data = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(episodio -> episodio.getDataLancamento() != null && episodio.getDataLancamento().isAfter(data))
//                .forEach(episodio -> System.out.println(
//                        "Temporada: " + episodio.getNumeroTemporada() +
//                        " Título: " + episodio.getTitulo() +
//                        " Avaliacao: " + episodio.getAvaliacao() +
//                        " Data de Lançamento: " + episodio.getDataLancamento().format(formatador)
//
//                ));


    }
}
