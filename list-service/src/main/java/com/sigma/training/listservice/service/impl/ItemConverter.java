package com.sigma.training.listservice.service.impl;

import com.sigma.training.listservice.model.dto.ItemDTO;
import com.sigma.training.listservice.model.entity.ItemEntity;
import com.sigma.training.listservice.service.Converter;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ItemConverter implements Converter<ItemEntity, ItemDTO> {

  @Override
  public ItemDTO toDto(@NotNull ItemEntity entity) {
    ItemDTO dto = new ItemDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    return dto;
  }

  @Override
  public ItemEntity toEntity(@NotNull ItemDTO dto) {
    ItemEntity entity = new ItemEntity();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    return entity;
  }

}
