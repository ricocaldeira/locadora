/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.midia;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.ricardocaldeira.genero.Genero;
import com.ricardocaldeira.locacao.Locacao;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "FILME")
@DiscriminatorValue("FILME")
public class Filme extends Midia {
       
    private int duracao;
    
    @OneToMany(mappedBy = "filme")
    private Collection<Locacao> locacoes = new ArrayList<Locacao>();
    
    @ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

    /**
     * @return the duracao
     */
    public int getDuracao() {
        return duracao;
    }

    /**
     * @param duracao the duracao to set
     */
    public void setDuracao(int duracao) {
        this.duracao = duracao;
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

    /**
     * @return the genero
     */
    public Genero getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    
    
}
