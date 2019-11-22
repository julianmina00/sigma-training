package com.sigma.training.invoiceconsumer.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ListDTO {

  private String name;
  private String description;
  private List<ItemDTO> items;

  public List<ItemDTO> getItems(){
    if(items == null){
      items = new ArrayList<>();
    }
    return items;
  }

}

