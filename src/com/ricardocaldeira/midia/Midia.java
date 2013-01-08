/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.midia;

import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "MIDIA")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "MIDIA_TIPO")
public abstract class Midia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  
    
    private String titulo;
    private int qtdeDeCopias;
    private int qtdeDeCopiasLocadas;

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
        if (!(object instanceof Midia)) {
            return false;
        }
        Midia other = (Midia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "banco.exemplo.Conta[ id=" + id + " ]";
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the qtdeDeCopias
     */
    public int getQtdeDeCopias() {
        return qtdeDeCopias;
    }

    /**
     * @param qtdeDeCopias the qtdeDeCopias to set
     */
    public void setQtdeDeCopias(int qtdeDeCopias) {
        this.qtdeDeCopias = qtdeDeCopias;
    }

    /**
     * @return the qtdeDeCopiasLocadas
     */
    public int getQtdeDeCopiasLocadas() {
        return qtdeDeCopiasLocadas;
    }

    /**
     * @param qtdeDeCopiasLocadas the qtdeDeCopiasLocadas to set
     */
    public void setQtdeDeCopiasLocadas(int qtdeDeCopiasLocadas) {
        this.qtdeDeCopiasLocadas = qtdeDeCopiasLocadas;
    }
    
}
