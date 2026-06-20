package com.md.basePlatform.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.md.basePlatform.domain.PageResult;
import com.md.basePlatform.domain.Uav;
import com.md.basePlatform.domain.UavForm;
import com.md.basePlatform.exception.DuplicateFrameSnException;
import com.md.basePlatform.service.UavService;

/**
 * 无人机台账 REST API 控制器。
 */
@RestController
@RequestMapping("/api/uavs")
public class UavApiController {

    private final UavService uavService;

    public UavApiController(UavService uavService) {
        this.uavService = uavService;
    }

    /**
     * 分页查询。
     *
     * @param keyword 关键字，可为空
     * @param page    页码，从 1 开始
     * @param size    每页条数
     * @return 分页结果
     */
    @GetMapping
    public ResponseEntity<PageResult<Uav>> findPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResult<Uav> result = uavService.findPage(keyword, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据 ID 查询单个无人机。
     *
     * @param id 主键
     * @return 无人机实体或 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Uav> findById(@PathVariable("id") Long id) {
        Uav uav = uavService.findById(id);
        if (uav == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(uav);
    }

    /**
     * 创建新无人机。
     *
     * @param form 表单数据
     * @return 创建的无人机实体
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UavForm form) {
        try {
            Uav created = uavService.create(form);
            return ResponseEntity.ok(created);
        } catch (DuplicateFrameSnException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * 更新无人机信息。
     *
     * @param id   主键
     * @param form 表单数据
     * @return 更新后的无人机实体
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UavForm form) {
        form.setId(id);
        try {
            Uav updated = uavService.update(form);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (DuplicateFrameSnException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * 删除无人机。
     *
     * @param id 主键
     * @return 无内容
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        uavService.delete(id);
        return ResponseEntity.noContent().build();
    }
}