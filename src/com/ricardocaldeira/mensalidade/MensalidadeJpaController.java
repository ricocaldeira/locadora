/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.mensalidade;

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
public class MensalidadeJpaController implements Serializable {

    public MensalidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mensalidade mensalidade) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(mensalidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mensalidade mensalidade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            mensalidade = em.merge(mensalidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mensalidade.getId();
                if (findMensalidade(id) == null) {
                    throw new NonexistentEntityException("The mensalidade with id " + id + " no longer exists.");
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
            Mensalidade mensalidade;
            try {
                mensalidade = em.getReference(Mensalidade.class, id);
                mensalidade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mensalidade with id " + id + " no longer exists.", enfe);
            }
            em.remove(mensalidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mensalidade> findMensalidadeEntities() {
        return findMensalidadeEntities(true, -1, -1);
    }

    public List<Mensalidade> findMensalidadeEntities(int maxResults, int firstResult) {
        return findMensalidadeEntities(false, maxResults, firstResult);
    }

    private List<Mensalidade> findMensalidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mensalidade.class));
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

    public Mensalidade findMensalidade(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mensalidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getMensalidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mensalidade> rt = cq.from(Mensalidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
