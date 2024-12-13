package com.sumativafs3.demo.controllers;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sumativafs3.demo.models.Compra;
import com.sumativafs3.demo.services.CompraService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CompraControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CompraService compraService;

    @InjectMocks
    private CompraController compraController;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Registra el módulo para manejo de fechas
        mockMvc = MockMvcBuilders.standaloneSetup(compraController).build();
    }

    @Test
    public void getAllComprasTest() throws Exception {
        List<Compra> compras = new ArrayList<>();
        when(compraService.getAllCompras()).thenReturn(compras);

        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getCompraByIdTest() throws Exception {
        Compra compra = new Compra();
        compra.setId(1L);
        when(compraService.getCompraById(1L)).thenReturn(compra);

        mockMvc.perform(get("/api/compras/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void createCompraTest() throws Exception {
        Compra compra = new Compra();
        when(compraService.createCompra(compra)).thenReturn(compra);

        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compra)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCompraTest() throws Exception {
        Compra existingCompra = new Compra();
        existingCompra.setId(1L);
        Compra updatedDetails = new Compra();

        when(compraService.updateCompra(1L, updatedDetails)).thenReturn(updatedDetails);

        mockMvc.perform(put("/api/compras/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCompraTest() throws Exception {
        doNothing().when(compraService).deleteCompra(1L);

        mockMvc.perform(delete("/api/compras/{id}", 1))
                .andExpect(status().isOk());
    }
}
