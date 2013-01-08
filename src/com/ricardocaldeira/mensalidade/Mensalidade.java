/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.mensalidade;

import com.ricardocaldeira.cliente.ClienteMensalista;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "MENSALIDADE")
public class Mensalidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private boolean pago;
    private double valor;
    
    @OneToOne
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
        if (!(object instanceof Mensalidade)) {
            return false;
        }
        Mensalidade other = (Mensalidade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "banco.exemplo.Mensalidade[ id=" + id + " ]";
    }

    /**
     * @return the pago
     */
    public boolean isPago() {
        return pago;
    }

    /**
     * @param pago the pago to set
     */
    public void setPago(boolean pago) {
        this.pago = pago;
    }

    /**
     * @return the valor
     */
    public double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(double valor) {
        this.valor = valor;
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
