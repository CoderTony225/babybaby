package com.md.basePlatform.domain;

import java.util.Collections;
import java.util.List;

/**
 * 简单分页结果。
 *
 * @param <T> 元素类型
 */
public class PageResult<T> {

    private List<T> items = Collections.emptyList();
    private int page = 1;
    private int size = 10;
    private long total;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 总页数。
     *
     * @return 页数，至少为 1（无数据时为 1）
     */
    public int getTotalPages() {
        if (size <= 0) {
            return 1;
        }
        return (int) Math.ceil((double) total / (double) size);
    }
}
