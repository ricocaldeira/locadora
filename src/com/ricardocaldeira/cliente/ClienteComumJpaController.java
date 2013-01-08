/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.cliente;

import locadora.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ricardocaldeira
 */
public class ClienteComumJpaController implements Serializable {

    public ClienteComumJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteComum clienteComum) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(clienteComum);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClienteComum clienteComum) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            clienteComum = em.merge(clienteComum);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = clienteComum.getId();
                if (findClienteComum(id) == null) {
                    throw new NonexistentEntityException("The clienteComum with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClienteComum clienteComum;
            try {
                clienteComum = em.getReference(ClienteComum.class, id);
                clienteComum.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteComum with id " + id + " no longer exists.", enfe);
            }
            em.remove(clienteComum);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClienteComum> findClienteComumEntities() {
        return findClienteComumEntities(true, -1, -1);
    }

    public List<ClienteComum> findClienteComumEntities(int maxResults, int firstResult) {
        return findClienteComumEntities(false, maxResults, firstResult);
    }

    private List<ClienteComum> findClienteComumEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteComum.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ClienteComum findClienteComum(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteComum.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteComumCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteComum> rt = cq.from(ClienteComum.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
