package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CarAdRepositoryImpl implements CarAdRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Long> findAllIdsBySpecification(Specification<CarAd> spec, Pageable pageable) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createTupleQuery();
        var root = query.from(CarAd.class);

        List<Selection<?>> selections = new ArrayList<>();
        selections.add(root.get("id"));

        Predicate finalPredicate = spec != null ? spec.toPredicate(root, query, cb) : null;
        if (finalPredicate != null) query.where(finalPredicate);

        query.distinct(false);

        if (pageable.getSort().isSorted()) {
            List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();
            Join<CarAd, Car> carJoin = (Join<CarAd, Car>) root.getJoins().stream()
                    .filter(j -> j.getAttribute().getName().equals("car"))
                    .findFirst()
                    .orElse(null);
            for (Sort.Order sortOrder : pageable.getSort()) {
                String property = sortOrder.getProperty();
                Path<?> sortPath;
                if (property.contains(".")) {
                    if (carJoin == null) {
                        carJoin = root.join("car", JoinType.LEFT);
                    }
                    sortPath = carJoin.get(property.split("\\.")[1]);
                } else {
                    sortPath = root.get(property);
                }
                selections.add(sortPath);
                orders.add(sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath));
            }

            orders.add(cb.desc(root.get("id")));
            query.orderBy(orders);
        }

        query.multiselect(selections);

        List<Long> contentIds = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList()
                .stream()
                .map(t -> (Long) t.get(0))
                .toList();

        int lookAheadCount = 1200;
        int currentOffset = (int) pageable.getOffset();
        int limitForCount = currentOffset + lookAheadCount;

        long totalFound;
        if (contentIds.size() < pageable.getPageSize() && pageable.getOffset() == 0) {
            totalFound = contentIds.size();
        } else {
            totalFound = getLimitedCount(spec, limitForCount);
        }
        return new PageImpl<>(contentIds, pageable, totalFound);
    }

    private long getLimitedCount(Specification<CarAd> spec, int limit) {
        var cb = em.getCriteriaBuilder();
        var criteriaQuery = cb.createQuery(Long.class);
        var root = criteriaQuery.from(CarAd.class);

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, criteriaQuery, cb);
            if (predicate != null) criteriaQuery.where(predicate);
        }
        var typedQuery = em.createQuery(criteriaQuery.select(cb.literal(1L)));
        List<Long> limitedList = typedQuery
                .setMaxResults(limit)
                .getResultList();
        int foundSize = limitedList.size();
        return Math.min(foundSize, limit);
    }

    @Override
    public List<Object> findDistinctValues(String fieldName, Specification<CarAd> spec) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Object.class);
        var root = query.from(CarAd.class);

        Path<Object> path;

        final List<String> carEnumFields = List.of(
                "bodyType", "engineType", "transmission", "driveType", "steeringSide"
        );

        if (List.of("brand", "model", "yearLow", "enginePower", "generation").contains(fieldName) || carEnumFields.contains(fieldName)) {

            path = root.join("car").get(fieldName);

            if (carEnumFields.contains(fieldName)) {
                query.select(path.as(String.class)).distinct(true);
            } else {
                query.select(path).distinct(true);
            }
        } else {
            path = root.get(fieldName);
            query.select(path).distinct(true);
        }

        if (spec != null) {
            query.where(spec.toPredicate(root, query, cb));
        }

        return em.createQuery(query).getResultList();
    }
}

