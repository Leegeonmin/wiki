package com.zb.wiki.dto;

import java.util.List;
import lombok.Getter;

public class UpdateDocument {
  @Getter
  public static class Request{
    private String context;
    private List<String> tags;
  }

}
