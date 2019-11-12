package com.sigma.training.listservice.service.impl;

import com.sigma.training.listservice.model.dto.ItemDTO;
import com.sigma.training.listservice.model.dto.ListDTO;
import com.sigma.training.listservice.model.entity.ListEntity;
import com.sigma.training.listservice.repository.ItemRepository;
import com.sigma.training.listservice.repository.ListRepository;
import com.sigma.training.listservice.service.ListService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListServiceImpl implements ListService {

  private final ItemRepository itemRepository;
  private final ListRepository listRepository;
  private final ListConverter listConverter;
  private final ItemConverter itemConverter;

  @Autowired
  public ListServiceImpl(ItemRepository itemRepository, ListRepository listRepository,
      ListConverter listConverter, ItemConverter itemConverter) {
    this.itemRepository = itemRepository;
    this.listRepository = listRepository;
    this.listConverter = listConverter;
    this.itemConverter = itemConverter;
  }


  @Override
  public List<ListDTO> getLists() {
    List<ListEntity> lists = listRepository.findAll();
    return listConverter.convertAll(lists);
  }

  @Override
  public ListDTO getList(long id) {
    return null;
  }

  @Override
  public ListDTO createList(ListDTO list) {
    return null;
  }

  @Override
  public ListDTO createList(String name, String description) {
    return null;
  }

  @Override
  public ListDTO updateList(long id, String name, String description) {
    return null;
  }

  @Override
  public ListDTO deleteList(long id) {
    return null;
  }

  @Override
  public List<ItemDTO> getItems(long listId) {
    return null;
  }

  @Override
  public ListDTO addItem(long listId, ItemDTO item) {
    return null;
  }

  @Override
  public ListDTO addItems(long listId, List<ItemDTO> items) {
    return null;
  }

  @Override
  public ListDTO updateItem(long listId, long itemId, String name, String description) {
    return null;
  }

  @Override
  public ListDTO removeItem(long listId, long itemId) {
    return null;
  }

  @Override
  public ListDTO removeItems(long listId) {
    return null;
  }
}
