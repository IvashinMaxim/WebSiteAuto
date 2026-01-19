package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.CarAd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CarAdRepositoryImpl implements CarAdRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final int MAX_SEARCH_LIMIT = 1183;

    @Override
    public Page<Long> findAllIdsBySpecification(Specification<CarAd> spec, Pageable pageable) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createTupleQuery();
        var root = query.from(CarAd.class);

        List<Selection<?>> selections = new ArrayList<>();
        selections.add(root.get("id"));

        if (spec != null) {
            var predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) query.where(predicate);
        }

        List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(sortOrder -> {
                String property = sortOrder.getProperty();
                Path<?> sortPath = property.contains(".")
                        ? root.join("car").get(property.split("\\.")[1])
                        : root.get(property);

                selections.add(sortPath);

                if (sortOrder.isAscending()) {
                    orders.add(cb.asc(sortPath));
                } else {
                    orders.add(cb.desc(sortPath));
                }
            });
            orders.add(cb.desc(root.get("id")));
            query.orderBy(orders);
        }

        query.multiselect(selections);

        List<Long> ids = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList()
                .stream()
                .map(tuple -> (Long) tuple.get(0))
                .toList();

        var countQuery = cb.createQuery(Long.class);
        var countRoot = countQuery.from(CarAd.class);
        countQuery.select(cb.countDistinct(countRoot.get("id")));

        if (spec != null) {
            countQuery.where(spec.toPredicate(countRoot, countQuery, cb));
        }

        Long actualCount = em.createQuery(countQuery).getSingleResult();
        long total = (actualCount != null) ? actualCount : 0L;

        return new PageImpl<>(ids, pageable, Math.min(total, MAX_SEARCH_LIMIT));
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

