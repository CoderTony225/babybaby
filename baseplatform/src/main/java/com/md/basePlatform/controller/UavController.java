package com.md.basePlatform.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.md.basePlatform.domain.Uav;
import com.md.basePlatform.domain.UavForm;
import com.md.basePlatform.exception.DuplicateFrameSnException;
import com.md.basePlatform.service.UavService;

/**
 * 无人机台账页面控制器。
 */
@Controller
@RequestMapping("/uavs")
public class UavController {

    private final UavService uavService;

    public UavController(UavService uavService) {
        this.uavService = uavService;
    }

    /**
     * 分页列表。
     *
     * @param keyword 关键字
     * @param page    页码
     * @param size    每页条数
     * @param model   模型
     * @return 视图名
     */
    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        model.addAttribute("pageResult", uavService.findPage(keyword, page, size));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "uav/list";
    }

    /**
     * 新建表单。
     *
     * @param model 模型
     * @return 视图名
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("uavForm", new UavForm());
        model.addAttribute("editMode", Boolean.FALSE);
        return "uav/form";
    }

    /**
     * 提交新建。
     *
     * @param form    表单
     * @param errors  校验结果
     * @param ra      flash
     * @return 视图或重定向
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("uavForm") UavForm form, BindingResult errors,
            Model model,
            RedirectAttributes ra) {
        if (errors.hasErrors()) {
            model.addAttribute("editMode", Boolean.FALSE);
            return "uav/form";
        }
        try {
            Uav created = uavService.create(form);
            ra.addFlashAttribute("successMessage", "已保存，编号 " + created.getFrameSn());
            return "redirect:/uavs/" + created.getId();
        } catch (DuplicateFrameSnException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/uavs/new";
        }
    }

    /**
     * 详情。
     *
     * @param id    主键
     * @param model 模型
     * @return 视图名或重定向
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        Uav uav = uavService.findById(id);
        if (uav == null) {
            ra.addFlashAttribute("errorMessage", "记录不存在");
            return "redirect:/uavs";
        }
        model.addAttribute("uav", uav);
        return "uav/detail";
    }

    /**
     * 编辑表单。
     *
     * @param id    主键
     * @param model 模型
     * @param ra    flash
     * @return 视图名或重定向
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        Uav uav = uavService.findById(id);
        if (uav == null) {
            ra.addFlashAttribute("errorMessage", "记录不存在");
            return "redirect:/uavs";
        }
        UavForm form = new UavForm();
        form.setId(uav.getId());
        form.setFrameSn(uav.getFrameSn());
        form.setModelName(uav.getModelName());
        form.setManufacturer(uav.getManufacturer());
        form.setStatus(uav.getStatus());
        
        // 硬件参数
        form.setMaxFlightAltitude(uav.getMaxFlightAltitude());
        form.setMaxFlightSpeed(uav.getMaxFlightSpeed());
        form.setFlightDuration(uav.getFlightDuration());
        form.setBatteryCapacity(uav.getBatteryCapacity());
        form.setPayloadCapacity(uav.getPayloadCapacity());
        
        form.setNotes(uav.getNotes());
        model.addAttribute("uavForm", form);
        model.addAttribute("editMode", Boolean.TRUE);
        return "uav/form";
    }

    /**
     * 提交更新。
     *
     * @param id      主键
     * @param form    表单
     * @param errors  校验结果
     * @param ra      flash
     * @return 视图或重定向
     */
    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
            @Valid @ModelAttribute("uavForm") UavForm form,
            BindingResult errors,
            Model model,
            RedirectAttributes ra) {
        form.setId(id);
        if (errors.hasErrors()) {
            model.addAttribute("editMode", Boolean.TRUE);
            return "uav/form";
        }
        try {
            Uav updated = uavService.update(form);
            if (updated == null) {
                ra.addFlashAttribute("errorMessage", "记录不存在");
                return "redirect:/uavs";
            }
            ra.addFlashAttribute("successMessage", "已更新");
            return "redirect:/uavs/" + id;
        } catch (DuplicateFrameSnException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/uavs/" + id + "/edit";
        }
    }

    /**
     * 删除。
     *
     * @param id 主键
     * @param ra flash
     * @return 重定向
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        uavService.delete(id);
        ra.addFlashAttribute("successMessage", "已删除");
        return "redirect:/uavs";
    }
}