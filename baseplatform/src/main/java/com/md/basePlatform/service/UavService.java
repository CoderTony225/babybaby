package com.md.basePlatform.service;

import com.md.basePlatform.domain.PageResult;
import com.md.basePlatform.domain.Uav;
import com.md.basePlatform.domain.UavForm;

/**
 * 无人机台账业务接口。
 */
public interface UavService {

    /**
     * 分页查询。
     *
     * @param keyword 关键字，可为空
     * @param page    页码，从 1 开始
     * @param size    每页条数
     * @return 分页结果
     */
    PageResult<Uav> findPage(String keyword, int page, int size);

    /**
     * 详情。
     *
     * @param id 主键
     * @return 实体或 null
     */
    Uav findById(Long id);

    /**
     * 新建。
     *
     * @param form 表单
     * @return 新建实体
     */
    Uav create(UavForm form);

    /**
     * 更新。
     *
     * @param form 表单（含 id）
     * @return 更新后实体
     */
    Uav update(UavForm form);

    /**
     * 删除。
     *
     * @param id 主键
     */
    void delete(Long id);
}
