package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder({"type", "description", "detail"})
@Data
public class ErrorDto {

  private String type;
  private String description;
  private String detail;

}
