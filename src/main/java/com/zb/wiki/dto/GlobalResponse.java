package com.zb.wiki.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GlobalResponse<T> {
  private String status;
  private String message;
  private ArrayList<T> data;
  private String fieldError;
}
