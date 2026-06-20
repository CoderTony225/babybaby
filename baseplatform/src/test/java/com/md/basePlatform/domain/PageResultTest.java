package com.md.basePlatform.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;

class PageResultTest {

    @Test
    void should_compute_total_pages() {
        PageResult<String> p = new PageResult<>();
        p.setTotal(21);
        p.setSize(10);
        p.setPage(1);
        p.setItems(Collections.singletonList("a"));
        assertEquals(3, p.getTotalPages());
    }

    @Test
    void should_return_one_page_when_size_invalid() {
        PageResult<String> p = new PageResult<>();
        p.setTotal(5);
        p.setSize(0);
        assertEquals(1, p.getTotalPages());
    }
}
