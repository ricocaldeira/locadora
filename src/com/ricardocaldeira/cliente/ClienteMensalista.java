/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.cliente;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.ricardocaldeira.locacao.Locacao;
import com.ricardocaldeira.mensalidade.Mensalidade;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "CLIENTE_MENSALISTA")
public class ClienteMensalista extends Cliente {
    
    @OneToOne(mappedBy = "mensalista", cascade = CascadeType.PERSIST)
    private Mensalidade mensalidade;
    
    @OneToMany(mappedBy = "mensalista")
    private Collection<Locacao> locacoes = new ArrayList<Locacao>();

    /**
     * @return the mensalidade
     */
    public Mensalidade getMensalidade() {
        return mensalidade;
    }

    /**
     * @param mensalidade the mensalidade to set
     */
    public void setMensalidade(Mensalidade mensalidade) {
        this.mensalidade = mensalidade;
    }

    /**
     * @return the locacoes
     */
    public Collection<Locacao> getLocacoes() {
        return locacoes;
    }

    /**
     * @param locacoes the locacoes to set
     */
    public void setLocacoes(Collection<Locacao> locacoes) {
        this.locacoes = locacoes;
    }
    
}
