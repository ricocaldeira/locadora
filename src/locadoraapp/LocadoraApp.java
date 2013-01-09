/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locadoraapp;

import com.ricardocaldeira.cliente.ClienteMensalista;
import com.ricardocaldeira.cliente.ClienteMensalistaJpaController;
import com.ricardocaldeira.midia.Filme;
import com.ricardocaldeira.midia.FilmeJpaController;
import com.ricardocaldeira.genero.Genero;
import com.ricardocaldeira.genero.GeneroJpaController;
import com.ricardocaldeira.gui.Formulario;
import com.ricardocaldeira.locacao.Locacao;
import com.ricardocaldeira.locacao.LocacaoJpaController;
import com.ricardocaldeira.mensalidade.Mensalidade;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ricardocaldeira
 */
public class LocadoraApp {
    private static int i;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LocadoraPU");
        
        carregarBancoComGeneros(emf, 10);
        carregarBancoComFilmes(emf, 10);
        carregarBancoComUsuarios(emf, 10);
        
        // Fazendo uma locação para usuário mensalista
        ClienteMensalistaJpaController mensalistaCont = new ClienteMensalistaJpaController(emf);
        FilmeJpaController filmeCont = new FilmeJpaController(emf);
        LocacaoJpaController locacaoCont = new LocacaoJpaController(emf);
        
        ClienteMensalista mensalista = new ClienteMensalista();
        Filme filme = new Filme();
        Locacao locacao = new Locacao();
        
        efetuarLocacaoParaClienteMensalista(filmeCont, mensalistaCont, locacao, locacaoCont);
        
        Formulario form = new Formulario();
        form.setVisible(true);
                
        System.out.println("ok");
        
    }
    
    private static void carregarBancoComUsuarios(EntityManagerFactory emf, int qtdeDeUsuarios) {
        ClienteMensalistaJpaController mensalistaCont = new ClienteMensalistaJpaController(emf);
        for (i=0; i<qtdeDeUsuarios; i++) {
            ClienteMensalista mensalista = new ClienteMensalista();
            Mensalidade mensalidade = new Mensalidade();
            
            mensalidade.setPago(true);
            mensalidade.setValor(29.90);
            mensalista.setNome("Usuario" + i);
            mensalista.setCpf("123" + i);
        
            mensalidade.setMensalista(mensalista);
            mensalista.setMensalidade(mensalidade);
            mensalistaCont.create(mensalista);
            
        }
    }

    private static void carregarBancoComFilmes(EntityManagerFactory emf, int qtdeDeFilmes) {
        FilmeJpaController filmeCont = new FilmeJpaController(emf);
        GeneroJpaController generoCont = new GeneroJpaController(emf);
        for (i=0; i<qtdeDeFilmes; i++) {
            Genero genero = new Genero();
            genero = (Genero) generoCont.findGeneroEntities(1, i).get(0);
            
            Filme filme = new Filme();
            
            filme.setTitulo("Filme" + i);
            filme.setDuracao(110);
            filme.setQtdeDeCopias(i+1);
            filme.setGenero(genero);
            filmeCont.create(filme);
            
        }
    }
    
    private static void carregarBancoComGeneros(EntityManagerFactory emf, int qtdeDeGeneros) {
        GeneroJpaController generoCont = new GeneroJpaController(emf);
        for (i=0; i<qtdeDeGeneros; i++) {
            Genero genero = new Genero();
            genero.setNome("Genero" + i);
            
            generoCont.create(genero);
            
        }
    }

    private static void efetuarLocacaoParaClienteMensalista(FilmeJpaController filmeCont, ClienteMensalistaJpaController mensalistaCont, Locacao locacao, LocacaoJpaController locacaoCont) {
        Filme filme;
        ClienteMensalista mensalista;
        filme = filmeCont.findFilmeEntities(1, 1).get(0);
        mensalista = mensalistaCont.findClienteMensalistaEntities(1, 1).get(0);
        locacao.setFilme(filme);
        locacao.setMensalista(mensalista);
        locacaoCont.create(locacao);
    }
}