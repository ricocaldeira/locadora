/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.midia;

import locadora.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import com.ricardocaldeira.locacao.Locacao;

/**
 *
 * @author ricardocaldeira
 */
public class FilmeJpaController implements Serializable {

    public FilmeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Filme filme) {
        if (filme.getLocacoes() == null) {
            filme.setLocacoes(new ArrayList<Locacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Locacao> attachedLocacoes = new ArrayList<Locacao>();
            for (Locacao locacoesLocacaoToAttach : filme.getLocacoes()) {
                locacoesLocacaoToAttach = em.getReference(locacoesLocacaoToAttach.getClass(), locacoesLocacaoToAttach.getId());
                attachedLocacoes.add(locacoesLocacaoToAttach);
            }
            filme.setLocacoes(attachedLocacoes);
            em.persist(filme);
            for (Locacao locacoesLocacao : filme.getLocacoes()) {
                Filme oldFilmeOfLocacoesLocacao = locacoesLocacao.getFilme();
                locacoesLocacao.setFilme(filme);
                locacoesLocacao = em.merge(locacoesLocacao);
                if (oldFilmeOfLocacoesLocacao != null) {
                    oldFilmeOfLocacoesLocacao.getLocacoes().remove(locacoesLocacao);
                    oldFilmeOfLocacoesLocacao = em.merge(oldFilmeOfLocacoesLocacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Filme filme) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Filme persistentFilme = em.find(Filme.class, filme.getId());
            Collection<Locacao> locacoesOld = persistentFilme.getLocacoes();
            Collection<Locacao> locacoesNew = filme.getLocacoes();
            Collection<Locacao> attachedLocacoesNew = new ArrayList<Locacao>();
            for (Locacao locacoesNewLocacaoToAttach : locacoesNew) {
                locacoesNewLocacaoToAttach = em.getReference(locacoesNewLocacaoToAttach.getClass(), locacoesNewLocacaoToAttach.getId());
                attachedLocacoesNew.add(locacoesNewLocacaoToAttach);
            }
            locacoesNew = attachedLocacoesNew;
            filme.setLocacoes(locacoesNew);
            filme = em.merge(filme);
            for (Locacao locacoesOldLocacao : locacoesOld) {
                if (!locacoesNew.contains(locacoesOldLocacao)) {
                    locacoesOldLocacao.setFilme(null);
                    locacoesOldLocacao = em.merge(locacoesOldLocacao);
                }
            }
            for (Locacao locacoesNewLocacao : locacoesNew) {
                if (!locacoesOld.contains(locacoesNewLocacao)) {
                    Filme oldFilmeOfLocacoesNewLocacao = locacoesNewLocacao.getFilme();
                    locacoesNewLocacao.setFilme(filme);
                    locacoesNewLocacao = em.merge(locacoesNewLocacao);
                    if (oldFilmeOfLocacoesNewLocacao != null && !oldFilmeOfLocacoesNewLocacao.equals(filme)) {
                        oldFilmeOfLocacoesNewLocacao.getLocacoes().remove(locacoesNewLocacao);
                        oldFilmeOfLocacoesNewLocacao = em.merge(oldFilmeOfLocacoesNewLocacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = filme.getId();
                if (findFilme(id) == null) {
                    throw new NonexistentEntityException("The filme with id " + id + " no longer exists.");
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
            Filme filme;
            try {
                filme = em.getReference(Filme.class, id);
                filme.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filme with id " + id + " no longer exists.", enfe);
            }
            Collection<Locacao> locacoes = filme.getLocacoes();
            for (Locacao locacoesLocacao : locacoes) {
                locacoesLocacao.setFilme(null);
                locacoesLocacao = em.merge(locacoesLocacao);
            }
            em.remove(filme);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Filme> findFilmeEntities() {
        return findFilmeEntities(true, -1, -1);
    }

    public List<Filme> findFilmeEntities(int maxResults, int firstResult) {
        return findFilmeEntities(false, maxResults, firstResult);
    }

    private List<Filme> findFilmeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Filme.class));
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

    public Filme findFilme(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Filme.class, id);
        } finally {
            em.close();
        }
    }

    public int getFilmeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Filme> rt = cq.from(Filme.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
