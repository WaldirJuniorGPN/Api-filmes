package br.com.alura.screenmatch.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.List;

@Table(name = "temporadas")
@Entity
@EqualsAndHashCode(of = "id")
public class Temporada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroTemporada;
    @Transient
    private List<Episodio> episodios;
}
