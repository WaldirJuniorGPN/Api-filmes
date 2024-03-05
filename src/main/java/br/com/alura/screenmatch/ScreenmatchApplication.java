package br.com.alura.screenmatch;

import br.com.alura.screenmatch.dto.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=the+flash&apikey=6fe4b89b");
		System.out.println(json);

		var conversor = new ConverteDados();
		var dtoDadosSerie = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dtoDadosSerie);
	}
}
