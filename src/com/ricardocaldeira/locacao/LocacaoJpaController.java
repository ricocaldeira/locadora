/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.locacao;

import com.ricardocaldeira.midia.Filme;
import com.ricardocaldeira.midia.Jogo;
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
public class LocacaoJpaController implements Serializable {

    public LocacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Locacao locacao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Filme filme = locacao.getFilme();
            if (filme != null) {
                filme = em.getReference(filme.getClass(), filme.getId());
                locacao.setFilme(filme);
            }
            Jogo jogo = locacao.getJogo();
            if (jogo != null) {
                jogo = em.getReference(jogo.getClass(), jogo.getId());
                locacao.setJogo(jogo);
            }
            em.persist(locacao);
            if (filme != null) {
                filme.getLocacoes().add(locacao);
                filme = em.merge(filme);
            }
            if (jogo != null) {
                jogo.getLocacoes().add(locacao);
                jogo = em.merge(jogo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Locacao locacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Locacao persistentLocacao = em.find(Locacao.class, locacao.getId());
            Filme filmeOld = persistentLocacao.getFilme();
            Filme filmeNew = locacao.getFilme();
            Jogo jogoOld = persistentLocacao.getJogo();
            Jogo jogoNew = locacao.getJogo();
            if (filmeNew != null) {
                filmeNew = em.getReference(filmeNew.getClass(), filmeNew.getId());
                locacao.setFilme(filmeNew);
            }
            if (jogoNew != null) {
                jogoNew = em.getReference(jogoNew.getClass(), jogoNew.getId());
                locacao.setJogo(jogoNew);
            }
            locacao = em.merge(locacao);
            if (filmeOld != null && !filmeOld.equals(filmeNew)) {
                filmeOld.getLocacoes().remove(locacao);
                filmeOld = em.merge(filmeOld);
            }
            if (filmeNew != null && !filmeNew.equals(filmeOld)) {
                filmeNew.getLocacoes().add(locacao);
                filmeNew = em.merge(filmeNew);
            }
            if (jogoOld != null && !jogoOld.equals(jogoNew)) {
                jogoOld.getLocacoes().remove(locacao);
                jogoOld = em.merge(jogoOld);
            }
            if (jogoNew != null && !jogoNew.equals(jogoOld)) {
                jogoNew.getLocacoes().add(locacao);
                jogoNew = em.merge(jogoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = locacao.getId();
                if (findLocacao(id) == null) {
                    throw new NonexistentEntityException("The locacao with id " + id + " no longer exists.");
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
            Locacao locacao;
            try {
                locacao = em.getReference(Locacao.class, id);
                locacao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The locacao with id " + id + " no longer exists.", enfe);
            }
            Filme filme = locacao.getFilme();
            if (filme != null) {
                filme.getLocacoes().remove(locacao);
                filme = em.merge(filme);
            }
            Jogo jogo = locacao.getJogo();
            if (jogo != null) {
                jogo.getLocacoes().remove(locacao);
                jogo = em.merge(jogo);
            }
            em.remove(locacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Locacao> findLocacaoEntities() {
        return findLocacaoEntities(true, -1, -1);
    }

    public List<Locacao> findLocacaoEntities(int maxResults, int firstResult) {
        return findLocacaoEntities(false, maxResults, firstResult);
    }

    private List<Locacao> findLocacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Locacao.class));
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

    public Locacao findLocacao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Locacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Locacao> rt = cq.from(Locacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
