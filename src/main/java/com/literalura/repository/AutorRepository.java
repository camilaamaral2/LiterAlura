package com.literalura.repository;


import com.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a JOIN FETCH a.livros WHERE LOWER(a.nome) = LOWER(:nome)")
    Autor findByNomeIgnoreCase(@Param("nome") String nome);

}
