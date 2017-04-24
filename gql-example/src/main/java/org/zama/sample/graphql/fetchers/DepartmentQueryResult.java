package org.zama.sample.graphql.fetchers;

import graphql.annotations.GraphQLField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zama.sample.graphql.domain.Department;

import java.util.List;

/**
 * DepartmentQueryResult.
 *
 * @author Zakir Magdum
 */
public class DepartmentQueryResult {
    @GraphQLField
    private List<Department> results;

    @GraphQLField
    private PageInfo pageInfo;

    public DepartmentQueryResult() {}

    public DepartmentQueryResult(List<Department> results, PageInfo pageInfo) {
        this.results = results;
        this.pageInfo = pageInfo;
    }

    public DepartmentQueryResult(Page<Department> page, Pageable req) {
        this.results = page.getContent();
        this.pageInfo = new PageInfo(req.getPageNumber(), req.getPageSize(), page.getNumberOfElements());
    }

    public List<Department> getResults() {
        return results;
    }

    public void setResults(List<Department> results) {
        this.results = results;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
