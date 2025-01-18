package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DadosAutor> autores,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Integer quantidadeDownload) {

    @Override
    public String toString() {
        String autores = this.autores != null && !this.autores.isEmpty()
                ? this.autores.stream()
                .map(DadosAutor::nomeAutor).reduce((a, b) -> a + ", " + b)
                .orElse("Desconhecido")
                : "Desconhecido";

        String idiomas = this.idioma != null && !this.idioma.isEmpty()
                ? String.join(", ", this.idioma)
                : "Desconhecido";

        return """
                -------------- Livro ----------------
                Título= %s
                Autores= %s
                Idioma= %s
                Número de Downloads= %s
                -------------------------------------
                """.formatted(titulo, autores, idiomas, quantidadeDownload);
    }
}
