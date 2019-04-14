package org.loadx.application.db.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.loadx.application.db.entity.LoadxEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic GenericDao class for all entities.
 * <p>
 * The class provides with the CRUD operations in a generic way.
 *
 * @see LoadxEntity
 * @see Dao for detailed JavaDoc per method.
 */
@Component
@SuppressWarnings("unchecked")
public class GenericDao implements Dao {

    private SessionFactory sessionFactory;

    @Autowired
    public GenericDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private static final Logger LOG = LoggerFactory.getLogger(GenericDao.class);

    public LoadxEntity getById(int id, Class type) {
        Transaction transaction = getSession().beginTransaction();
        LoadxEntity item = (LoadxEntity) getSession().get(type, id);
        transaction.commit();
        LOG.info("Fetched the record: item={}", item);
        return item;
    }

    public List<LoadxEntity> getAll(Class type) {
        Transaction transaction = getSession().beginTransaction();
        Query<LoadxEntity> query = getSession().createQuery(createCriteriaQuery(type));
        List<LoadxEntity> items = query.getResultList();
        transaction.commit();
        LOG.info("Fetched the records: items.size={}", items.size());
        return items;
    }

    @Override
    public int save(LoadxEntity item) {
        Transaction transaction = getSession().beginTransaction();
        int id = (Integer) getSession().save(item);
        transaction.commit();
        LOG.info("The item is saved and id generated: id={}", id);
        return id;
    }

    @Override
    public List<Integer> save(List<LoadxEntity> items) {
        Transaction transaction = getSession().beginTransaction();
        List<Integer> savedIds = items.stream()
                .map(item -> (Integer) getSession().save(item))
                .collect(Collectors.toList());
        transaction.commit();
        LOG.info("The items were saved and ids generated: savedIds.size={}", savedIds.size());
        return savedIds;
    }

    @Override
    public void update(LoadxEntity item) {
        Transaction transaction = getSession().beginTransaction();
        getSession().update(item);
        transaction.commit();
        LOG.info("The item updated: id={}, entityClass={}", item.getId(), item.getClass().getSimpleName());
    }

    @Override
    public void remove(LoadxEntity item) {
        Transaction transaction = getSession().beginTransaction();
        getSession().delete(item);
        transaction.commit();
        LOG.info("Item from DB removed: id={}, entityClass={}", item.getId(), item.getClass().getSimpleName());
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private CriteriaQuery<LoadxEntity> createCriteriaQuery(Class<LoadxEntity> type) {
        CriteriaQuery<LoadxEntity> criteriaQuery = getSession()
                .getCriteriaBuilder()
                .createQuery(type);
        Root<LoadxEntity> rootItem = criteriaQuery.from(type);
        criteriaQuery.select(rootItem);
        return criteriaQuery;
    }

}
