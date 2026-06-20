package com.md.basePlatform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.md.basePlatform.domain.PageResult;
import com.md.basePlatform.domain.Uav;
import com.md.basePlatform.service.UavService;

@WebMvcTest(UavController.class)
@AutoConfigureMockMvc(addFilters = false)
class UavControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UavService uavService;

    @Test
    void should_show_list() throws Exception {
        PageResult<Uav> pr = new PageResult<>();
        pr.setItems(Collections.emptyList());
        when(uavService.findPage(any(), anyInt(), anyInt())).thenReturn(pr);

        mockMvc.perform(get("/uavs"))
                .andExpect(status().isOk())
                .andExpect(view().name("uav/list"));
    }

    @Test
    void should_show_create_form() throws Exception {
        mockMvc.perform(get("/uavs/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("uav/form"));
    }

    @Test
    void should_redirect_after_create() throws Exception {
        Uav created = new Uav();
        created.setId(9L);
        created.setFrameSn("SN");
        when(uavService.create(any())).thenReturn(created);

        mockMvc.perform(post("/uavs")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("frameSn", "SN-99")
                        .param("modelName", "M")
                        .param("notes", ""))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void should_show_detail() throws Exception {
        Uav u = new Uav();
        u.setId(1L);
        when(uavService.findById(1L)).thenReturn(u);

        mockMvc.perform(get("/uavs/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("uav/detail"));
    }

    @Test
    void should_redirect_when_detail_missing() throws Exception {
        when(uavService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/uavs/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/uavs"));
    }

    @Test
    void should_redirect_when_edit_missing() throws Exception {
        when(uavService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/uavs/99/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/uavs"));
    }

    @Test
    void should_post_delete() throws Exception {
        mockMvc.perform(post("/uavs/5/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/uavs"));

        verify(uavService).delete(5L);
    }
}
