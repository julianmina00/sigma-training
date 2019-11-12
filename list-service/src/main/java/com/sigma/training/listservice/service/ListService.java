package com.sigma.training.listservice.service;

import com.sigma.training.listservice.model.dto.ItemDTO;
import com.sigma.training.listservice.model.dto.ListDTO;
import java.util.List;

public interface ListService {

  List<ListDTO> getLists();
  ListDTO getList(long id);
  ListDTO createList(ListDTO list);
  ListDTO createList(String name, String description);
  ListDTO updateList(long id, String name, String description);
  ListDTO deleteList(long id);

  List<ItemDTO> getItems(long listId);
  ListDTO addItem(long listId, ItemDTO item);
  ListDTO addItems(long listId, List<ItemDTO> items);
  ListDTO updateItem(long listId, long itemId, String name, String description);
  ListDTO removeItem(long listId, long itemId);
  ListDTO removeItems(long listId);

}
