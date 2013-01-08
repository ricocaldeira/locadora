/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.midia;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.ricardocaldeira.locacao.Locacao;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "JOGO")
@DiscriminatorValue("JOGO")
public class Jogo extends Midia {
   
    private int diaAniversario;
    
    @OneToMany(mappedBy = "jogo")
    private Collection<Locacao> locacoes = new ArrayList<Locacao>();

    /**
     * @return the diaAniversario
     */
    public int getDiaAniversario() {
        return diaAniversario;
    }

    /**
     * @param diaAniversario the diaAniversario to set
     */
    public void setDiaAniversario(int diaAniversario) {
        this.diaAniversario = diaAniversario;
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
