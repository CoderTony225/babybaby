package com.md.basePlatform.repository.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.md.basePlatform.BasePlatformApplication;
import com.md.basePlatform.domain.AiGenerationStatus;
import com.md.basePlatform.domain.Uav;

@SpringBootTest(classes = BasePlatformApplication.class)
@ActiveProfiles("test")
@Transactional
class UavMapperTest {

    @Autowired
    private UavMapper uavMapper;

    @Test
    void should_insert_and_select() {
        Uav u = new Uav();
        u.setFrameSn("MAP-1");
        u.setModelName("Model-A");
        u.setManufacturer("Test Manufacturer");
        u.setStatus("IDLE");
        u.setNotes(null);
        u.setAiAttributes("{}");
        u.setAiGenerationStatus(AiGenerationStatus.SUCCESS);
        u.setAiLastError(null);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        u.setCreatedAt(now);
        u.setUpdatedAt(now);

        int rows = uavMapper.insert(u);
        assertEquals(1, rows);
        assertNotNull(u.getId());

        Uav loaded = uavMapper.selectById(u.getId());
        assertEquals("MAP-1", loaded.getFrameSn());
    }
}