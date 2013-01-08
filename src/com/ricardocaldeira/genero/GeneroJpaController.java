/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricardocaldeira.genero;

import com.ricardocaldeira.midia.Filme;
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

/**
 *
 * @author ricardocaldeira
 */
public class GeneroJpaController implements Serializable {

    public GeneroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genero genero) {
        if (genero.getFilmes() == null) {
            genero.setFilmes(new ArrayList<Filme>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Filme> attachedFilmes = new ArrayList<Filme>();
            for (Filme filmesFilmeToAttach : genero.getFilmes()) {
                filmesFilmeToAttach = em.getReference(filmesFilmeToAttach.getClass(), filmesFilmeToAttach.getId());
                attachedFilmes.add(filmesFilmeToAttach);
            }
            genero.setFilmes(attachedFilmes);
            em.persist(genero);
            for (Filme filmesFilme : genero.getFilmes()) {
                Genero oldGeneroOfFilmesFilme = filmesFilme.getGenero();
                filmesFilme.setGenero(genero);
                filmesFilme = em.merge(filmesFilme);
                if (oldGeneroOfFilmesFilme != null) {
                    oldGeneroOfFilmesFilme.getFilmes().remove(filmesFilme);
                    oldGeneroOfFilmesFilme = em.merge(oldGeneroOfFilmesFilme);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Genero genero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero persistentGenero = em.find(Genero.class, genero.getId());
            Collection<Filme> filmesOld = persistentGenero.getFilmes();
            Collection<Filme> filmesNew = genero.getFilmes();
            Collection<Filme> attachedFilmesNew = new ArrayList<Filme>();
            for (Filme filmesNewFilmeToAttach : filmesNew) {
                filmesNewFilmeToAttach = em.getReference(filmesNewFilmeToAttach.getClass(), filmesNewFilmeToAttach.getId());
                attachedFilmesNew.add(filmesNewFilmeToAttach);
            }
            filmesNew = attachedFilmesNew;
            genero.setFilmes(filmesNew);
            genero = em.merge(genero);
            for (Filme filmesOldFilme : filmesOld) {
                if (!filmesNew.contains(filmesOldFilme)) {
                    filmesOldFilme.setGenero(null);
                    filmesOldFilme = em.merge(filmesOldFilme);
                }
            }
            for (Filme filmesNewFilme : filmesNew) {
                if (!filmesOld.contains(filmesNewFilme)) {
                    Genero oldGeneroOfFilmesNewFilme = filmesNewFilme.getGenero();
                    filmesNewFilme.setGenero(genero);
                    filmesNewFilme = em.merge(filmesNewFilme);
                    if (oldGeneroOfFilmesNewFilme != null && !oldGeneroOfFilmesNewFilme.equals(genero)) {
                        oldGeneroOfFilmesNewFilme.getFilmes().remove(filmesNewFilme);
                        oldGeneroOfFilmesNewFilme = em.merge(oldGeneroOfFilmesNewFilme);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = genero.getId();
                if (findGenero(id) == null) {
                    throw new NonexistentEntityException("The genero with id " + id + " no longer exists.");
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
            Genero genero;
            try {
                genero = em.getReference(Genero.class, id);
                genero.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genero with id " + id + " no longer exists.", enfe);
            }
            Collection<Filme> filmes = genero.getFilmes();
            for (Filme filmesFilme : filmes) {
                filmesFilme.setGenero(null);
                filmesFilme = em.merge(filmesFilme);
            }
            em.remove(genero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genero> findGeneroEntities() {
        return findGeneroEntities(true, -1, -1);
    }

    public List<Genero> findGeneroEntities(int maxResults, int firstResult) {
        return findGeneroEntities(false, maxResults, firstResult);
    }

    private List<Genero> findGeneroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genero.class));
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

    public Genero findGenero(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genero.class, id);
        } finally {
            em.close();
        }
    }

    public int getGeneroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genero> rt = cq.from(Genero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
