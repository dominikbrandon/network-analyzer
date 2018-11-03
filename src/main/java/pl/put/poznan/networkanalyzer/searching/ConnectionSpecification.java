package pl.put.poznan.networkanalyzer.searching;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import pl.put.poznan.networkanalyzer.model.Connection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class ConnectionSpecification implements Specification<Connection> {
    private ConnectionSearchParameters searchParameters;

    @Override
    public Predicate toPredicate(Root<Connection> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new LinkedList<>();
        for (Field field : searchParameters.getClass().getDeclaredFields()) {
            Object value = null;
            try {
                value = field.get(searchParameters);
            } catch (IllegalAccessException e) {
                log.error("Could not read field value", e);
            }
            if (value == null) {
                continue;
            }
            predicates.add(criteriaBuilder.equal(root.get("id").get(field.getName()), value));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
