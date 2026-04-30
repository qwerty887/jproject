package org.jproject.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import org.jproject.domain.AbstractDeleteEntity;
import org.jproject.domain.AbstractDeleteEntity_;
import org.jproject.domain.AbstractHistEntity_;
import org.jproject.domain.AbstractHistEntity;
import org.jproject.utils.TimeUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DaoBase implements IDao {

    private final EntityManager entityManager;

    private final List<Runnable> beforeCommitHandlers;
    private final List<Runnable> afterCommitHandlers;
    private final List<Consumer<RuntimeException>> beforeRollbackHandlers;
    private final List<Consumer<RuntimeException>> afterRollbackHandlers;

    public DaoBase(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.beforeCommitHandlers = new ArrayList<>();
        this.afterCommitHandlers = new ArrayList<>();
        this.beforeRollbackHandlers = new ArrayList<>();
        this.afterRollbackHandlers = new ArrayList<>();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    public <T> T persist(final T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    public <T> T persistAndFlush(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        return entity;
    }

    private <T> TypedQuery<T> getTypedQuery(Specification<T> specification, Class<T> clazz, Consumer<TypedQuery<T>> queryModifier) {
        return getTypedQuery(specification, clazz, clazz, queryModifier);
    }

    private <T> TypedQuery<T> getTypedQuery(Specification<T> specification, Class<T> clazz) {
        return getTypedQuery(specification, clazz, ignored -> {});
    }

    private <T, R> TypedQuery<R> getTypedQuery(
            Specification<T> specification,
            Class<T> entityClass,
            Class<R> resultClass,
            Consumer<TypedQuery<R>> queryModifier) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<R> query = cb.createQuery(resultClass);
        Root<T> root = query.from(entityClass);
        query.where(specification.toPredicate(root, query, cb));
        TypedQuery<R> result = getEntityManager().createQuery(query);
        queryModifier.accept(result);
        return result;
    }

    public <T> Optional<T> findEntity(Specification<T> specification, Class<T> clazz) {
        TypedQuery<T> query = getTypedQuery(specification, clazz);
        return getSingleResult(query);
    }

    public <T> List<T> findEntities(Specification<T> specification, Class<T> clazz) {
        TypedQuery<T> query = getTypedQuery(specification, clazz);
        return query.getResultList();
    }

    public <X> Optional<X> getSingleResult(TypedQuery<X> typedQuery) throws NonUniqueResultException {
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public <T extends AbstractDeleteEntity<?>> void deleteEntity(T entity, Specification<T> specification, Class<T> clazz) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaUpdate<T> update = cb.createCriteriaUpdate(clazz);
        final Root<T> root = update.from(clazz);
        update.set(root.get(AbstractDeleteEntity_.delDate), TimeUtils.getCurrentTime());
        update.where(specification.toPredicate(root, cb.createQuery(), cb));
        getEntityManager().createQuery(update).executeUpdate();
    }

    public <T extends AbstractHistEntity<?>> void closeEntity(T entity, Specification<T> specification, Class<T> clazz) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaUpdate<T> update = cb.createCriteriaUpdate(clazz);
        final Root<T> root = update.from(clazz);
        update.set(root.get(AbstractHistEntity_.endDate), TimeUtils.getCurrentTime());
        update.where(specification.toPredicate(root, cb.createQuery(), cb));
        getEntityManager().createQuery(update).executeUpdate();
    }

    public <X, Y> Join<X, Y> getOrCreateJoin(Root<X> root, SingularAttribute<X, Y> attribute, JoinType joinType) {
        return root.getJoins().stream()
                .filter(j -> j.getAttribute().equals(attribute))
                .findFirst()
                .map(j -> (Join<X, Y>) j)
                .orElseGet(() -> root.join(attribute, joinType));
    }

}
