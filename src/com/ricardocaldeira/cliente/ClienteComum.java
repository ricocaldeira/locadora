/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.cliente;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.ricardocaldeira.fatura.Fatura;

/**
 *
 * @author ricardocaldeira
 */
@Entity
@Table(name = "CLIENTE_COMUM")
public class ClienteComum extends Cliente {
    private boolean inadimplente;
    
    @OneToMany(mappedBy = "cliente_comum")
    private Collection<Fatura> faturas = new ArrayList<Fatura>();

    /**
     * @return the inadimplente
     */
    public boolean isInadimplente() {
        return inadimplente;
    }

    /**
     * @param inadimplente the inadimplente to set
     */
    public void setInadimplente(boolean inadimplente) {
        this.inadimplente = inadimplente;
    }

    /**
     * @return the faturas
     */
    public Collection<Fatura> getFaturas() {
        return faturas;
    }

    /**
     * @param faturas the faturas to set
     */
    public void setFaturas(Collection<Fatura> faturas) {
        this.faturas = faturas;
    }
}
