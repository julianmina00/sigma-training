package com.sigma.training.listservice.service.impl;

import com.sigma.training.listservice.dto.ItemDTO;
import com.sigma.training.listservice.dto.ListDTO;
import com.sigma.training.listservice.service.ListService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class ListServiceImpl implements ListService {

  private static long listId = 0;
  private final Map<Long, ListDTO> listDTOMap = new ConcurrentHashMap<>();

  @Override
  public List<ListDTO> getLists() {
    return new ArrayList<>(listDTOMap.values());
  }

  @Override
  public ListDTO getList(long id) {
    return listDTOMap.get(id);
  }

  @Override
  public ListDTO createList(ListDTO list) {
    long id = ++listId;
    list.setId(id);
    listDTOMap.put(id, list);
    return list;
  }

  @Override
  public ListDTO createList(String name, String description) {
    long id = ++listId;
    ListDTO list = new ListDTO();
    list.setId(id);
    list.setName(name);
    list.setDescription(description);
    listDTOMap.put(id, list);
    return list;
  }

  @Override
  public ListDTO updateList(long id, String name, String description) {
    ListDTO list = listDTOMap.get(id);
    if(list != null){
      list.setName(name);
      list.setDescription(description);
    }
    return list;
  }

  @Override
  public ListDTO deleteList(long id) {
    ListDTO list = listDTOMap.get(id);
    if(list != null){
      listDTOMap.remove(id);
    }
    return list;
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
