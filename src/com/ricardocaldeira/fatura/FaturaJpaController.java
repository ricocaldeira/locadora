/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.fatura;

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
public class FaturaJpaController implements Serializable {

    public FaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fatura fatura) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(fatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fatura fatura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            fatura = em.merge(fatura);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fatura.getId();
                if (findFatura(id) == null) {
                    throw new NonexistentEntityException("The fatura with id " + id + " no longer exists.");
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
            Fatura fatura;
            try {
                fatura = em.getReference(Fatura.class, id);
                fatura.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fatura with id " + id + " no longer exists.", enfe);
            }
            em.remove(fatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fatura> findFaturaEntities() {
        return findFaturaEntities(true, -1, -1);
    }

    public List<Fatura> findFaturaEntities(int maxResults, int firstResult) {
        return findFaturaEntities(false, maxResults, firstResult);
    }

    private List<Fatura> findFaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fatura.class));
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

    public Fatura findFatura(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fatura> rt = cq.from(Fatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
