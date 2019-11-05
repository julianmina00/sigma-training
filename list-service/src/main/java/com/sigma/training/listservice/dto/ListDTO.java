package com.sigma.training.listservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDTO {

  private Long id;
  private String name;
  private String description;
  private List<ItemDTO> items = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ItemDTO> getItems() {
    if(items == null){
      items = new ArrayList<>();
    }
    return items;
  }

  public void setItems(List<ItemDTO> items) {
    this.items = items;
  }

}
