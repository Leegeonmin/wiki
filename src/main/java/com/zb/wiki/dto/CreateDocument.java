package com.zb.wiki.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

public class CreateDocument {

  @Getter
  public static class Request{
    @NotBlank
    @Size(min = 1, max = 40)
    private String title;
    @NotBlank
    private String context;
    private List<String> tags;
  }
}
