package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.Repository.SerieRepository;
import br.com.alura.screenmatch.dto.DadosSerie;
import br.com.alura.screenmatch.dto.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6fe4b89b";
    private Scanner leitura = new Scanner(System.in);
    private List<DadosTemporada> temporadas = new ArrayList<>();
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private List<Serie> seriesBuscadas;
    private SerieRepository serieRepository;

    public Principal(SerieRepository repository) {
        this.serieRepository = repository;
    }

    public void exibirMenu() {

        var opcao = -1;

        while (opcao != 0) {

            var menu = """
                    Digite uma das Opções:
                                    
                    1 - Buscar Série
                    2 - Buscar Episódio
                    3 - Listar Sérias buscadas
                                    
                    0 - Sair...
                    """;

            System.out.println(menu);
            try {
                opcao = this.leitura.nextInt();
            } catch (InputMismatchException e) {
                System.out.println();
            }
            leitura.nextLine();

            switch (opcao) {
                case 1 -> buscaSerie();
                case 2 -> buscaEpisodio();
                case 3 -> listarSeriesBuscadas();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção Inválida\n");
            }

        }
    }

    private void listarSeriesBuscadas() {
        this.seriesBuscadas = this.serieRepository.findAll();
        this.seriesBuscadas.stream().sorted(Comparator.comparing(Serie::getCategoria)).forEach(System.out::println);
    }

    private void buscaEpisodio() {

        System.out.println("Digite o nome da Série cujo episódio deseja pesquisar");
        var nomeSerie = this.leitura.nextLine().replace(" ", "+").toLowerCase();
        var json = this.consumoAPI.obterDados(this.ENDERECO + nomeSerie + this.API_KEY);
        var dtoDadosSerie = this.conversor.obterDados(json, DadosSerie.class);

        for (int i = 1; i <= dtoDadosSerie.totalTemporadas(); i++) {
            json = this.consumoAPI.obterDados(this.ENDERECO + nomeSerie + "&season=" + i + this.API_KEY);
            var dtoDadostemporada = this.conversor.obterDados(json, DadosTemporada.class);
            this.temporadas.add(dtoDadostemporada);
        }


        List<Episodio> episodios = this.temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(dadosEpisodio -> new Episodio(t.numeroTemporada(), dadosEpisodio))).collect(Collectors.toList());

        episodios.stream().sorted(Comparator.comparing(Episodio::getEpisodio))
                .sorted(Comparator.comparing(Episodio::getNumeroTemporada))
                .forEach(System.out::println);

        System.out.println("Digite o título ou o trecho do título do epsódio que deseja buscar");
        var nomeEpisodio = this.leitura.nextLine();

        Optional<Episodio> resultado = episodios.stream()
                .filter(episodio -> episodio.getTitulo().matches("(?i).*" + nomeEpisodio + ".*")).findFirst();

        if (resultado.isPresent()) {
            System.out.println("Episódio encontrado!");
            System.out.println("\nTemporada: " + resultado.get().getNumeroTemporada() +
                    " Título: " + resultado.get().getTitulo() +
                    " Avaliação " + resultado.get().getAvaliacao());
        } else {
            System.out.println("Episódio não encontrado");
        }

        System.out.println();
    }

    private void buscaSerie() {
        System.out.println("Digite o nome da Série: ");

        var nomeSerie = leitura.nextLine().replace(" ", "+").toLowerCase();
        var json = this.consumoAPI.obterDados(this.ENDERECO + nomeSerie + this.API_KEY);
        var dtoDadosSerie = this.conversor.obterDados(json, DadosSerie.class);
        var serie = new Serie(dtoDadosSerie);
        this.serieRepository.save(serie);
        System.out.println(serie);
        System.out.println();
    }
}
