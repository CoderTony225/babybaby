package com.md.basePlatform.service.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.md.basePlatform.domain.PageResult;
import com.md.basePlatform.domain.Uav;
import com.md.basePlatform.domain.UavForm;
import com.md.basePlatform.exception.DuplicateFrameSnException;
import com.md.basePlatform.repository.mapper.UavMapper;
import com.md.basePlatform.service.UavService;

/**
 * 无人机台账业务实现。
 */
@Service
public class UavServiceImpl implements UavService {

    private final UavMapper uavMapper;

    public UavServiceImpl(UavMapper uavMapper) {
        this.uavMapper = uavMapper;
    }

    @Override
    public PageResult<Uav> findPage(String keyword, int page, int size) {
        int safePage = page < 1 ? 1 : page;
        int safeSize = size < 1 ? 10 : Math.min(size, 100);
        String kw = keyword == null || keyword.trim().isEmpty() ? null : keyword.trim();
        long total = uavMapper.countByKeyword(kw);
        int offset = (safePage - 1) * safeSize;
        PageResult<Uav> result = new PageResult<>();
        result.setPage(safePage);
        result.setSize(safeSize);
        result.setTotal(total);
        result.setItems(uavMapper.selectPage(kw, offset, safeSize));
        return result;
    }

    @Override
    public Uav findById(Long id) {
        if (id == null) {
            return null;
        }
        return uavMapper.selectById(id);
    }

    @Override
    @Transactional
    public Uav create(UavForm form) {
        Uav existing = uavMapper.selectByFrameSn(form.getFrameSn().trim());
        if (existing != null) {
            throw new DuplicateFrameSnException("机身编号已存在：" + form.getFrameSn());
        }
        Uav uav = new Uav();
        copyFormToEntity(form, uav);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        uav.setCreatedAt(now);
        uav.setUpdatedAt(now);
        uavMapper.insert(uav);
        return uavMapper.selectById(uav.getId());
    }

    @Override
    @Transactional
    public Uav update(UavForm form) {
        if (form.getId() == null) {
            throw new IllegalArgumentException("id required");
        }
        Uav current = uavMapper.selectById(form.getId());
        if (current == null) {
            return null;
        }
        Uav other = uavMapper.selectByFrameSn(form.getFrameSn().trim());
        if (other != null && !other.getId().equals(form.getId())) {
            throw new DuplicateFrameSnException("机身编号已存在：" + form.getFrameSn());
        }
        copyFormToEntity(form, current);
        current.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        uavMapper.update(current);
        return uavMapper.selectById(current.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id != null) {
            uavMapper.deleteById(id);
        }
    }

    private void copyFormToEntity(UavForm form, Uav uav) {
        uav.setFrameSn(form.getFrameSn().trim());
        uav.setModelName(form.getModelName().trim());
        uav.setManufacturer(trimToNull(form.getManufacturer()));
        uav.setStatus(trimToNull(form.getStatus()));
        
        // 硬件参数
        uav.setMaxFlightAltitude(form.getMaxFlightAltitude());
        uav.setMaxFlightSpeed(form.getMaxFlightSpeed());
        uav.setFlightDuration(form.getFlightDuration());
        uav.setBatteryCapacity(form.getBatteryCapacity());
        uav.setPayloadCapacity(form.getPayloadCapacity());
        
        uav.setNotes(trimToNull(form.getNotes()));
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String t = value.trim();
        return t.isEmpty() ? null : t;
    }
}