package com.app.end2end.product;

import com.app.product.models.Product;
import com.app.product.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductEnd2EndTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void shouldReturnProductWhenRepositorySavesProductInH2() throws Exception {
        Product product = new Product(1L, "Dorflex", 15., 12);

        mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Product> product1 = productRepository.findById(1L);

        Assert.assertEquals(product1, Optional.of(product));
    }

    @Test
    public void shouldReturnProductWhenRepositoryFindsProductById() throws Exception {
        Product product = new Product(1L, "Dorflex", 15., 12);

        productRepository.save(product);

        mockMvc.perform(get("/products/{id}", 1L)
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Product> product1 = productRepository.findById(1L);

        Assert.assertEquals(product1, Optional.of(product));
    }

    @Test
    public void shouldDeleteProductWhenRepositoryFindsProductById() throws Exception {
        Product product = new Product(1L, "Azitromicina", 24., 21);

        productRepository.save(product);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isOk());
    }
}
