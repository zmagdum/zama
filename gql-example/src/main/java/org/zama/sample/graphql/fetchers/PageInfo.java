package org.zama.sample.graphql.fetchers;

import graphql.annotations.GraphQLField;

/**
 * PageInfo.
 *
 * @author Zakir Magdum
 */
public class PageInfo {
    @GraphQLField
    private int number;
    @GraphQLField
    private int size;
    @GraphQLField
    private int total;

    public PageInfo() {}
    public PageInfo(int number, int size, int total) {
        this.number = number;
        this.size = size;
        this.total = total;
    }
    public PageInfo(int number) {
        this(number, number, number);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
