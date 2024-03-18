package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.dto.DadosSerie;

public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private String pontuacao;
    private String categoria;
    private String imagem;
    private String sinopse;

    public Serie(DadosSerie dados) {
        this.titulo = dados.titulo();
        this.totalTemporadas = dados.totalTemporadas();
        this.pontuacao = dados.pontuacao();
        this.categoria = dados.categoria();
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
                "\nsinopse: " + this.sinopse;

    }
}
