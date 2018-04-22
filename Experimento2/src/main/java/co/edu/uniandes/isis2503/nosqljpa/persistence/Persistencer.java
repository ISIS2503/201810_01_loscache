/*
 * The MIT License
 *
 * Copyright 2016 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.edu.uniandes.isis2503.nosqljpa.persistence;

import co.edu.uniandes.isis2503.nosqljpa.model.entity.AlarmaEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.HubEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.InmuebleEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.UnidadResidencialEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import org.hibernate.Criteria;

public class Persistencer<T, PK> {

    private static final Logger LOG = Logger.getLogger(Persistencer.class.getName());
    protected Class<T> entityClass;
    protected EntityManager entityManager;

    public Persistencer() {

        this.entityManager = JPAConnection.CONNECTION.getEntityManager();

    }

    public T add(T entity) {
        try {

            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;

    }

    public T update(T entity) {
        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public T find(PK id) {
        T entity;
        try {
            entity = entityManager.find(entityClass, id);
        } catch (NoResultException | NonUniqueResultException e) {
            entity = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public List<T> all() {
        List<T> entities;
        String queryString = "Select c FROM " + entityClass.getSimpleName() + " c";
        Query query = entityManager.createQuery(queryString);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities;
    }

    public Boolean delete(PK id) {
        try {
            T entity = entityManager.find(entityClass, id);
            entityManager.getTransaction().begin();
            this.entityManager.remove(entity);
            entityManager.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public T findCode(String code) {
        T entity;
        String queryString = "Select c FROM " + entityClass.getSimpleName() + " c where c.code = :code1";
        Query query = entityManager.createQuery(queryString).setParameter("code1", code);
        try {
            entity = (T) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            entity = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public List<AlarmaEntity> findBySensorId(String id) {
        List<AlarmaEntity> entities;
        String queryString = "Select c FROM ALARMA" + " c where c.idSensor = :id1 and c.activado=1";
        Query query = entityManager.createQuery(queryString).setParameter("id1", id);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities;
    }
    
       
   
    public List<AlarmaEntity> findAllAlarmas() {
        List<AlarmaEntity> entities;
        String queryString = "Select c FROM ALARMA c";
        Query query = entityManager.createQuery(queryString);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities;
    }
    
    public HubEntity findHubById(String n) {
        List<HubEntity> entities;
        String queryString = "Select c FROM Hub" + " c where c.id = :id1 and c.activado=1";
        Query query = entityManager.createQuery(queryString).setParameter("id1", n);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities.get(0);
    }
    
    public List<UnidadResidencialEntity> findUnidadByIdHub(String n) {
        List<UnidadResidencialEntity> entities;
        String queryString = "Select c FROM UnidadResidencial" + " c where c.idHub = :id1 and c.activado=1";
        Query query = entityManager.createQuery(queryString).setParameter("id1", n);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities;
    }
    
    public List<InmuebleEntity> findInmuebleByUnidad(String n) {
        List<InmuebleEntity> entities;
        String queryString = "Select c FROM INMUEBLE" + " c where c.idUnidad = :id1 and c.activado=1";
        Query query = entityManager.createQuery(queryString).setParameter("id1", n);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities;
    }
    
    
}
