package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.CarAd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarAdRepositoryImpl implements CarAdRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final int MAX_SEARCH_LIMIT = 990;

    @Override
    public Page<Long> findAllIdsBySpecification(Specification<CarAd> spec, Pageable pageable) {

        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Long.class);
        var root = query.from(CarAd.class);
        query.select(root.get("id"));

        if (spec != null) {
            query.where(spec.toPredicate(root, query, cb));
        }

        List<Long> ids = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        var countQuery = cb.createQuery(Long.class);
        var countRoot = countQuery.from(CarAd.class);
        countQuery.select(countRoot.get("id")); // Выбираем просто ID

        if (spec != null) {
            countQuery.where(spec.toPredicate(countRoot, countQuery, cb));
        }

        List<Long> limitedResult = em.createQuery(countQuery)
                .setMaxResults(MAX_SEARCH_LIMIT)
                .getResultList();

        long total = limitedResult.size();

        return new PageImpl<>(ids, pageable, total);
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

