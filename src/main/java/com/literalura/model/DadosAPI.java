package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAPI(
        Integer count,
        String next,
        String previous,
        List<DadosLivro> results
) {
}