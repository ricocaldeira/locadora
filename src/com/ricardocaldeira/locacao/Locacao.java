/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.locacao;

import com.ricardocaldeira.midia.Filme;
import com.ricardocaldeira.midia.Jogo;
import com.ricardocaldeira.fatura.Fatura;
import com.ricardocaldeira.cliente.ClienteMensalista;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "LOCACAO")
public class Locacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "filme_id")
    private Filme filme;
    
    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;
    
    @ManyToOne
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;
    
    @ManyToOne
    @JoinColumn(name = "mensalista_id")
    private ClienteMensalista mensalista;

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
        if (!(object instanceof Locacao)) {
            return false;
        }
        Locacao other = (Locacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "banco.exemplo.Agencia[ id=" + id + " ]";
    }

    /**
     * @return the fatura
     */
    public Fatura getFatura() {
        return fatura;
    }

    /**
     * @param fatura the fatura to set
     */
    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    /**
     * @return the filme
     */
    public Filme getFilme() {
        return filme;
    }

    /**
     * @param filme the filme to set
     */
    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    /**
     * @return the jogo
     */
    public Jogo getJogo() {
        return jogo;
    }

    /**
     * @param jogo the jogo to set
     */
    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    /**
     * @return the mensalista
     */
    public ClienteMensalista getMensalista() {
        return mensalista;
    }

    /**
     * @param mensalista the mensalista to set
     */
    public void setMensalista(ClienteMensalista mensalista) {
        this.mensalista = mensalista;
    }
    
}
