  package com.md.basePlatform.repository.mapper;//路径

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.md.basePlatform.domain.Uav;//导入无人机实体

/**
 * 无人机数据访问 Mapper 接口。
 */
public interface UavMapper {

    /**
     * 按主键查询。
     *
     * @param id 主键
     * @return 实体或 null
     */
    Uav selectById(@Param("id") Long id);

    /**
     * 按机身编号查询。
     *
     * @param frameSn 机身编号
     * @return 实体或 null
     */
    Uav selectByFrameSn(@Param("frameSn") String frameSn);

    /**
     * 关键字条件计数。
     *
     * @param keyword 关键字，可为空表示全部
     * @return 记录数
     */
    int countByKeyword(@Param("keyword") String keyword);

    /**
     * 分页查询。
     *
     * @param keyword 关键字
     * @param offset 偏移
     * @param limit 条数
     * @return 列表
     */
    List<Uav> selectPage(@Param("keyword") String keyword, @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 插入。
     *
     * @param entity 实体（回填 id）
     * @return 影响行数
     */
    int insert(Uav entity);

    /**
     * 更新。
     *
     * @param entity 实体
     * @return 影响行数
     */
    int update(Uav entity);

    /**
     * 按主键删除。
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
