package com.example.demo;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
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
  void apiDocsErrorResponses() throws Exception {
    String errorDto = "#/components/schemas/ErrorDto";

    mockMvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.components.schemas.ErrorDto.properties.type").exists())
        .andExpect(jsonPath("$.components.schemas.ErrorDto.properties.description").exists())
        .andExpect(jsonPath("$.components.schemas.ErrorDto.properties.detail").exists())
        .andExpect(jsonPath("$.paths['/persons'].post.responses.keys()")
            .value(containsInAnyOrder("201", "400", "500")))
        .andExpect(jsonPath("$.paths['/persons'].get.responses.keys()")
            .value(containsInAnyOrder("200", "400", "500")))
        .andExpect(jsonPath("$.paths['/persons/{id}'].get.responses.keys()")
            .value(containsInAnyOrder("200", "400", "404", "500")))
        .andExpect(jsonPath("$.paths['/persons/{id}'].put.responses.keys()")
            .value(containsInAnyOrder("200", "400", "404", "409", "500")))
        .andExpect(jsonPath("$.paths['/persons/{id}'].delete.responses.keys()")
            .value(containsInAnyOrder("204", "400", "500")))
        .andExpect(jsonPath("$.paths['/persons'].get.responses['200'].content.*.schema.$ref")
            .value(everyItem(is("#/components/schemas/PagedModelPersonDto"))))
        .andExpect(jsonPath("$.paths['/persons/{id}'].get.responses['200'].content.*.schema.$ref")
            .value(everyItem(is("#/components/schemas/PersonDto"))))
        .andExpect(jsonPath("$.paths['/persons/{id}'].put.responses['409'].content.*.schema.$ref")
            .value(everyItem(is(errorDto))))
        .andExpect(jsonPath("$.paths['/persons/{id}'].get.responses['404'].content.*.schema.$ref")
            .value(everyItem(is(errorDto))))
        .andExpect(jsonPath("$.paths['/persons'].post.responses['500'].content.*.schema.$ref")
            .value(everyItem(is(errorDto))));
  }

  @Test
  void swaggerUi() throws Exception {
    mockMvc.perform(get("/swagger-ui/index.html"))
        .andExpect(status().isOk());
  }

}
