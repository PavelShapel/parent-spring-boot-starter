package com.pavelshapel.web.spring.boot.starter.web;

import com.pavelshapel.core.spring.boot.starter.api.converter.FromDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.converter.ToDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.DaoService;
import com.pavelshapel.jpa.spring.boot.starter.service.SpecificationDaoService;
import com.pavelshapel.jpa.spring.boot.starter.service.search.AbstractSearchSpecification;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import com.pavelshapel.web.spring.boot.starter.html.element.table.TableHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factories;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractSpecificationDaoRestController<ID, E extends Entity<ID>, D extends Dto<ID>> extends AbstractDaoRestController<ID, E, D> {
    private final ObjectFactory<AbstractSearchSpecification<E>> searchSpecificationFactory;

    protected AbstractSpecificationDaoRestController(DaoService<ID, E> daoService, FromDtoConverter<ID, D, E> fromDtoConverter, ToDtoConverter<ID, E, D> toDtoConverter, ObjectFactory<AbstractSearchSpecification<E>> searchSpecificationFactory) {
        super(daoService, fromDtoConverter, toDtoConverter);
        this.searchSpecificationFactory = searchSpecificationFactory;
    }

    @Autowired
    private Factories htmlFactories;

    @GetMapping(path = SEARCH_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<D>> findAll(@RequestBody List<@Valid SearchCriterion> searchCriteria, @PageableDefault Pageable pageable) {
        Page<D> page = getSpecificationDaoService().findAll(composeSpecification(searchCriteria), pageable).map(getToDtoConverter()::convert);
        return ResponseEntity.ok(page);
    }

    @GetMapping(path = TABLE_PATH, produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> getTable(@RequestBody List<@Valid SearchCriterion> searchCriteria, @PageableDefault Pageable pageable) {
        Page<E> page = getSpecificationDaoService().findAll(composeSpecification(searchCriteria), pageable);
        return ResponseEntity.ok(htmlFactories.getFactory(TableHtml.class)
                .create(getDaoService().getEntityClass(), page).toString());
    }

    private SpecificationDaoService<ID, E> getSpecificationDaoService() {
        return (SpecificationDaoService<ID, E>) getDaoService();
    }

    private Specification<E> composeSpecification(List<SearchCriterion> searchCriteria) {
        Specification<E> result = null;
        for (SearchCriterion searchCriterion : searchCriteria) {
            result = and(result, getSearchSpecification(searchCriterion));
        }
        return result;
    }

    private AbstractSearchSpecification<E> getSearchSpecification(SearchCriterion searchCriterion) {
        AbstractSearchSpecification<E> searchSpecification = searchSpecificationFactory.getObject();
        searchSpecification.setSearchCriterion(searchCriterion);
        return searchSpecification;
    }

    private Specification<E> and(Specification<E> first, Specification<E> second) {
        if (allNotNull(first, second)) {
            return first.and(second);
        } else {
            return firstNonNull(first, second);
        }
    }
}