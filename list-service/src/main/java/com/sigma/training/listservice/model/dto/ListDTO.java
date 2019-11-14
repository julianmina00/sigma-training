package com.sigma.training.listservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDTO {

  private Long id;
  private String name;
  private String description;
  private Date createdAt;
  private Date updatedAt;
  private List<ItemDTO> items = new ArrayList<>();

  public List<ItemDTO> getItems() {
    if(items == null){
      items = new ArrayList<>();
    }
    return items;
  }

}
