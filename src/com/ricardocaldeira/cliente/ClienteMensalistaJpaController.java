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
public class ClienteMensalistaJpaController implements Serializable {

    public ClienteMensalistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteMensalista clienteMensalista) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(clienteMensalista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClienteMensalista clienteMensalista) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            clienteMensalista = em.merge(clienteMensalista);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = clienteMensalista.getId();
                if (findClienteMensalista(id) == null) {
                    throw new NonexistentEntityException("The clienteMensalista with id " + id + " no longer exists.");
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
            ClienteMensalista clienteMensalista;
            try {
                clienteMensalista = em.getReference(ClienteMensalista.class, id);
                clienteMensalista.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteMensalista with id " + id + " no longer exists.", enfe);
            }
            em.remove(clienteMensalista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClienteMensalista> findClienteMensalistaEntities() {
        return findClienteMensalistaEntities(true, -1, -1);
    }

    public List<ClienteMensalista> findClienteMensalistaEntities(int maxResults, int firstResult) {
        return findClienteMensalistaEntities(false, maxResults, firstResult);
    }

    private List<ClienteMensalista> findClienteMensalistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteMensalista.class));
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

    public ClienteMensalista findClienteMensalista(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteMensalista.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteMensalistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteMensalista> rt = cq.from(ClienteMensalista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
