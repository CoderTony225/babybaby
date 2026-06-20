package com.md.basePlatform.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.md.basePlatform.domain.PageResult;
import com.md.basePlatform.domain.Uav;
import com.md.basePlatform.domain.UavForm;
import com.md.basePlatform.exception.DuplicateFrameSnException;
import com.md.basePlatform.repository.mapper.UavMapper;
import com.md.basePlatform.service.ai.UavAttributeAiClient;

@ExtendWith(MockitoExtension.class)
class UavServiceImplTest {

    @Mock
    private UavMapper uavMapper;

    @Mock
    private UavAttributeAiClient uavAttributeAiClient;

    @InjectMocks
    private UavServiceImpl uavService;

    private UavForm form;

    @BeforeEach
    void setUp() {
        form = new UavForm();
        form.setFrameSn("SN-001");
        form.setModelName("TestModel");
        form.setNotes("n");
    }

    @Test
    void should_create_and_fetch_when_valid() {
        when(uavMapper.selectByFrameSn("SN-001")).thenReturn(null);
        when(uavAttributeAiClient.generateAttributes(anyString(), anyString(), anyString()))
                .thenReturn("{\"k\":\"v\"}");
        when(uavMapper.selectById(5L)).thenAnswer(inv -> {
            Uav u = new Uav();
            u.setId(5L);
            u.setFrameSn("SN-001");
            return u;
        });
        when(uavMapper.insert(any(Uav.class))).thenAnswer(inv -> {
            Uav u = inv.getArgument(0);
            u.setId(5L);
            return 1;
        });

        Uav created = uavService.create(form);

        assertNotNull(created);
        assertEquals(Long.valueOf(5L), created.getId());
        verify(uavMapper).insert(any(Uav.class));
    }

    @Test
    void should_mark_failed_when_ai_throws() {
        when(uavMapper.selectByFrameSn("SN-001")).thenReturn(null);
        when(uavAttributeAiClient.generateAttributes(anyString(), anyString(), anyString()))
                .thenThrow(new IllegalStateException("ai down"));
        when(uavMapper.selectById(7L)).thenAnswer(inv -> {
            Uav u = new Uav();
            u.setId(7L);
            return u;
        });
        when(uavMapper.insert(any(Uav.class))).thenAnswer(inv -> {
            Uav u = inv.getArgument(0);
            u.setId(7L);
            return 1;
        });

        Uav created = uavService.create(form);

        assertNotNull(created);
        verify(uavMapper).insert(any(Uav.class));
    }

    @Test
    void should_throw_when_duplicate_frame_sn_on_create() {
        Uav existing = new Uav();
        existing.setId(1L);
        when(uavMapper.selectByFrameSn("SN-001")).thenReturn(existing);

        assertThrows(DuplicateFrameSnException.class, () -> uavService.create(form));
    }

    @Test
    void should_return_page_result() {
        when(uavMapper.countByKeyword(eq("k"))).thenReturn(2);
        when(uavMapper.selectPage(eq("k"), eq(0), eq(10))).thenReturn(java.util.Collections.emptyList());

        PageResult<Uav> page = uavService.findPage("k", 1, 10);

        assertEquals(2L, page.getTotal());
        assertEquals(1, page.getPage());
    }

    @Test
    void should_delete_by_id() {
        uavService.delete(3L);
        verify(uavMapper).deleteById(3L);
    }

    @Test
    void should_return_null_when_find_by_id_null() {
        assertEquals(null, uavService.findById(null));
    }

    @Test
    void should_return_null_when_update_target_missing() {
        when(uavMapper.selectById(2L)).thenReturn(null);
        UavForm edit = new UavForm();
        edit.setId(2L);
        edit.setFrameSn("SN");
        edit.setModelName("M");

        assertEquals(null, uavService.update(edit));
    }

    @Test
    void should_update_existing() {
        Uav current = new Uav();
        current.setId(2L);
        current.setFrameSn("SN-001");
        current.setModelName("Old");
        when(uavMapper.selectByFrameSn("SN-001")).thenReturn(current);
        when(uavAttributeAiClient.generateAttributes(anyString(), anyString(), anyString()))
                .thenReturn("{}");

        UavForm edit = new UavForm();
        edit.setId(2L);
        edit.setFrameSn("SN-001");
        edit.setModelName("New");
        edit.setNotes(null);

        when(uavMapper.selectById(2L)).thenReturn(current).thenReturn(current);

        Uav updated = uavService.update(edit);

        assertNotNull(updated);
        verify(uavMapper).update(any(Uav.class));
    }
}
