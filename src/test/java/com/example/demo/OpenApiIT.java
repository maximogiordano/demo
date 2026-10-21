package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

// Same in-memory H2 setup as PersonControllerIT, so the full context starts without Postgres.
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:demo;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password="
})
@AutoConfigureMockMvc
class OpenApiIT {

  @Autowired
  MockMvc mockMvc;

  @Test
  void apiDocs() throws Exception {
    mockMvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.openapi").exists())
        .andExpect(jsonPath("$.paths['/persons']").exists())
        .andExpect(jsonPath("$.paths['/persons/{id}']").exists());
  }

  @Test
  void swaggerUi() throws Exception {
    mockMvc.perform(get("/swagger-ui/index.html"))
        .andExpect(status().isOk());
  }

}
