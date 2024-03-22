package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.dto.DadosSerie;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Getter
@Entity
@Table(name = "series")
@EqualsAndHashCode(of = "id")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double pontuacao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private String imagem;
    private String sinopse;
    @OneToMany
    private List<Episodio> episodios = new ArrayList<>();

    public Serie(){}
    public Serie(DadosSerie dados) {
        this.titulo = dados.titulo();
        this.totalTemporadas = dados.totalTemporadas();
        this.pontuacao = OptionalDouble.of(Double.valueOf(dados.pontuacao())).orElse(0);
        this.categoria = Categoria.fromString(dados.categoria().split(",")[0].trim());
        this.imagem = dados.imagem();
        this.sinopse = dados.snipse();
    }

    @Override
    public String toString() {
        return "Título: " + this.titulo +
                "\nTotal de Temporadas: " + this.totalTemporadas +
                "\nPontuação: " + this.pontuacao +
                "\nCategoria: " + this.categoria +
                "\nImagem: " + this.imagem +
                "\nsinopse: " + this.sinopse + "\n";

    }
}
