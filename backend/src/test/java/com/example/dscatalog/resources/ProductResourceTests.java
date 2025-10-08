package com.example.dscatalog.resources;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.services.ProductService;
import com.example.dscatalog.services.exceptions.DatabaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import com.example.dscatalog.tests.factory.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(value = ProductResource.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private long existingId;
    private long notExistingId;
    private long dependentId;
    private Product product;


    @BeforeEach
    void setUp() {
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProduct();

        Mockito.when(productService.findAllByPage(ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
        Mockito.when(productService.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(productService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).delete(notExistingId);
        Mockito.doThrow(DatabaseException.class).when(productService).delete(dependentId);

        Mockito.when(productService.update(Mockito.eq(existingId), Mockito.any())).thenReturn(productDTO);
        Mockito.when(productService.update(Mockito.eq(notExistingId), Mockito.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productService.insert(Mockito.any())).thenReturn(productDTO);
    }

    @Test
    public void findAllByPageShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", notExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", notExistingId));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }
}

