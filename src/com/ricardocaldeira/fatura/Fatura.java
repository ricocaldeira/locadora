/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.fatura;

import com.ricardocaldeira.cliente.ClienteComum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.ricardocaldeira.locacao.Locacao;

/**
 *
 * @author ricardocaldeira
 */
@Entity
public class Fatura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private double precoTotal;
    
    @OneToMany(mappedBy = "fatura")
    private Collection<Locacao> locacoes = new ArrayList<Locacao>();
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteComum cliente_comum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fatura)) {
            return false;
        }
        Fatura other = (Fatura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "banco.exemplo.Fatura[ id=" + id + " ]";
    }

    /**
     * @return the precoTotal
     */
    public double getPrecoTotal() {
        return precoTotal;
    }

    /**
     * @param precoTotal the precoTotal to set
     */
    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }
    
}
