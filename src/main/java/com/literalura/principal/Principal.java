package com.literalura.principal;


import com.literalura.model.Autor;
import com.literalura.model.DadosAPI;
import com.literalura.model.DadosLivro;
import com.literalura.model.Livro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LivroRepository;
import com.literalura.service.ConsumoAPI;
import com.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI api = new ConsumoAPI();
    private final ConverteDados conversorDeDados = new ConverteDados();
    private static final String ENDERECO = "https://gutendex.com/books/?search=";

    @Autowired
    private LivroRepository livroRepositorio;

    @Autowired
    private AutorRepository autorRepositorio;

    public void menu() {
        int opcao;
        do {
            String menu = """
                    \n******************* MENU ********************
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - Sair
                    **********************************************
                    
                    Digite oque deseja realizar:
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarPorTitulo();
                    break;
                case 2:
                    buscarLivrosRegistrados();
                    break;
                case 3:
                    buscarLivroPorAutores();
                    break;
                case 4:
                    pesquisarAutoresVivosPorAno();
                    break;
                case 5:
                    buscarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    private DadosLivro getDadosLivro() {
        System.out.println("Digite o livro que deseja encontrar: ");
        String nomeLivro = scanner.nextLine().trim();
        String endereco = ENDERECO + nomeLivro.replace(" ", "%20");
        String json = api.obterDados(endereco);

        if (json == null || json.isEmpty()) {
            System.out.println("Nenhum dado encontrado");
            return null;
        }

        DadosAPI dadosAPI = conversorDeDados.obterDados(json, DadosAPI.class);
        if (dadosAPI == null || dadosAPI.results().isEmpty()) {
            System.out.println("Nenhum dado encontrado");
            return null;
        }

        return dadosAPI.results().get(0);
    }

    private void buscarPorTitulo() {
        DadosLivro dadosLivro = getDadosLivro();
        if (dadosLivro == null) return;

        Livro livroJaBuscado = livroRepositorio.findByTituloIgnoreCase(dadosLivro.titulo());
        if (livroJaBuscado != null) {
            System.out.println("O livro \"" + livroJaBuscado.getTitulo() + "\" já está cadastrado.");
            return;
        }

        List<Autor> autores = dadosLivro.autores().stream()
                .map(autorDados -> {
                    Autor autorExistente = autorRepositorio.findByNomeIgnoreCase(autorDados.nomeAutor());
                    return autorExistente != null ? autorExistente : autorRepositorio.save(new Autor(autorDados));
                })
                .collect(Collectors.toList());

        Livro livro = new Livro(dadosLivro);
        livro.setAutores(autores);
        livroRepositorio.save(livro);

        System.out.println("Livro \"" + livro.getTitulo() + "\" salvo com sucesso!");
        System.out.println(dadosLivro.toString());
    }

    private void buscarLivrosRegistrados() {
        List<Livro> livros = livroRepositorio.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.forEach(livro -> {
                System.out.println("--------------- Livro Pesquisado -----------------");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autores: " + livro.getAutores().stream()
                        .map(Autor::getNomeDoAutor)
                        .collect(Collectors.joining(", ", "", "")));
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Número de Downloads: " + livro.getQuantidadeDownload());
                System.out.println("--------------------------------------------------");
            });
        }
    }

    @Transactional
    private void buscarLivroPorAutores() {

        List<Autor> autores = autorRepositorio.findAll();
                autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado.");
        } else {
            autores.forEach(autor -> {
                System.out.println("--------------- Autor -----------------");
                System.out.println("Nome: " + autor.getNomeDoAutor());
                System.out.println("Ano de nascimento: " + autor.getAnoDeNascimento());
                System.out.println("Ano de falecimento: " + autor.getAnoDeFalecimento());
                System.out.println("Livros: " + (autor.getLivros().isEmpty() ? "Nenhum" :
                        autor.getLivros().stream()
                                .map(Livro::getTitulo)
                                .collect(Collectors.joining(", "))));
                System.out.println("--------------------------------------");
            });
        }
    }

    private void buscarLivrosPorIdioma() {
        System.out.println("""
                Digite a sigla do idioma:
                es- espanhol 
                en- inglês
                fr- francês
                pt- português 
                """);
        String idioma = scanner.nextLine().trim();
        List<Livro> livros = livroRepositorio.findByIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado neste idioma.");
        } else {
            livros.forEach(livro -> {
                System.out.println("--------------- Livro -----------------");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autores: " + livro.getAutores().stream()
                        .map(Autor::getNomeDoAutor)
                        .collect(Collectors.joining(", ")));
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Número de Downloads: " + livro.getQuantidadeDownload());
                System.out.println("--------------------------------------");
            });
        }
    }

    private void pesquisarAutoresVivosPorAno() {
        System.out.println("Digite o ano: ");
        int anoPesquisa = scanner.nextInt();
        scanner.nextLine();

        try {
            List<Autor> autores = autorRepositorio.findAll();
            List<Autor> autoresVivos = autores.stream()
                        .filter(autor -> {
                            Integer anoNascimento = autor.getAnoDeNascimento();
                            Integer anoFalecimento = autor.getAnoDeFalecimento();
                            return anoNascimento != null &&
                                    anoNascimento <= anoPesquisa &&
                                    (anoFalecimento == null || anoFalecimento > anoPesquisa);
                    })
                    .collect(Collectors.toList());

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado no ano " + anoPesquisa);
            } else {
                System.out.println("Autores vivos no ano " + anoPesquisa + ":");
                autoresVivos.forEach(autor -> {
                    System.out.println("Nome: " + autor.getNomeDoAutor());
                    System.out.println("Ano de nascimento: " + autor.getAnoDeNascimento());
                    System.out.println("Ano de falecimento: " + (autor.getAnoDeFalecimento() != null ? autor.getAnoDeFalecimento() : "Ainda vivo"));
                });
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar autores: " + e.getMessage());
        }
    }
}
