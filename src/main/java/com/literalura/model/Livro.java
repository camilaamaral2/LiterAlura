package com.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Integer quantidadeDownload;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores = new ArrayList<>();

    public Livro () {}

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.quantidadeDownload = dadosLivro.quantidadeDownload();
        this.autores = getAutores();

        if (dadosLivro.autores() != null) {
            this.autores = dadosLivro.autores().stream()
                    .map(autor -> new Autor(new DadosAutor(autor.nomeAutor(), autor.anoDeNascimento(), autor.anoDeFalecimento())))
                    .toList();
        }
        this.idioma = dadosLivro.idioma() != null ? String.join(", ", dadosLivro.idioma()) : "Idioma Desconhecido";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getQuantidadeDownload() {
        return quantidadeDownload;
    }

    public void setQuantidadeDownload(Integer numeroDownload) {
        this.quantidadeDownload = numeroDownload;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores != null ? autores : new ArrayList<>();
    }
}
