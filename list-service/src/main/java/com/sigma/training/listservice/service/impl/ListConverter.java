package com.sigma.training.listservice.service.impl;

import com.sigma.training.listservice.model.dto.ListDTO;
import com.sigma.training.listservice.model.entity.ItemEntity;
import com.sigma.training.listservice.model.entity.ListEntity;
import com.sigma.training.listservice.repository.ItemRepository;
import com.sigma.training.listservice.service.Converter;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListConverter implements Converter<ListEntity, ListDTO> {

  private final ItemRepository itemRepository;
  private final ItemConverter itemConverter;

  @Autowired
  public ListConverter(ItemRepository itemRepository, ItemConverter itemConverter) {
    this.itemRepository = itemRepository;
    this.itemConverter = itemConverter;
  }

  @Override
  public ListDTO toDto(@NotNull ListEntity entity) {
    ListDTO dto = new ListDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());

    List<ItemEntity> items = itemRepository.findByListId(entity.getId());
    dto.setItems(itemConverter.toDtoList(items));
    return dto;
  }

  @Override
  public ListEntity toEntity(@NotNull ListDTO dto) {
    ListEntity entity = new ListEntity();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    return entity;
  }
}
