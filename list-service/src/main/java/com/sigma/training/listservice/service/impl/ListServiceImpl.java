package com.sigma.training.listservice.service.impl;

import static java.text.MessageFormat.format;

import com.sigma.training.listservice.exception.NotFoundException;
import com.sigma.training.listservice.model.dto.ItemDTO;
import com.sigma.training.listservice.model.dto.ListDTO;
import com.sigma.training.listservice.model.entity.ItemEntity;
import com.sigma.training.listservice.model.entity.ListEntity;
import com.sigma.training.listservice.repository.ItemRepository;
import com.sigma.training.listservice.repository.ListRepository;
import com.sigma.training.listservice.service.ListService;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListServiceImpl implements ListService {

  private static final String NOT_POSSIBLE_TO_UPDATE_ITEM = "It was not possible to update the Item since there is not a List with Id: {0} or the Item Id: {1} doesn't exist";
  private static final String NOT_POSSIBLE_TO_DELETE_ITEM = "It was not possible to delete the Item since there is not a List with Id: {0} or the Item Id: {1} doesn't exist";
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
    return listConverter.toDtoList(listRepository.findAll());
  }

  @Override
  public ListDTO getList(long id) {
    Optional<ListEntity> optional = listRepository.findById(id);
    return optional.map(listConverter::toDto).orElseThrow(() -> new NotFoundException("There is not a List with Id: "+id));
  }

  @Override
  public ListDTO createList(ListDTO list) {
    ListEntity createdList = listRepository.save(listConverter.toEntity(list));
    List<ItemEntity> itemEntities = itemConverter.toEntityList(list.getItems());
    itemEntities.forEach(itemEntity -> itemEntity.setListId(createdList.getId()));
    List<ItemEntity> createdItems = itemRepository.saveAll(itemEntities);
    ListDTO listDTO = listConverter.toDto(createdList);
    listDTO.setItems(itemConverter.toDtoList(createdItems));
    return listDTO;
  }

  @Override
  public ListDTO createList(String name, String description) {
    ListEntity listEntity = new ListEntity();
    listEntity.setName(name);
    listEntity.setDescription(description);
    ListEntity createdList = listRepository.save(listEntity);
    return listConverter.toDto(createdList);
  }

  @Override
  public ListDTO updateList(long id, String name, String description) {
    Optional<ListEntity> optional = listRepository.findById(id);
    return optional.map(entity -> {
      entity.setName(name);
      entity.setDescription(description);
      ListEntity updatedList = listRepository.save(entity);
      return listConverter.toDto(updatedList);
    }).orElseThrow(() -> new NotFoundException("It was not possible to update since there is not a List with Id: "+id));
  }

  @Override
  public ListDTO deleteList(long id) {
    Optional<ListEntity> optional = listRepository.findById(id);
    return optional.map(entity -> {
      removeItems(entity.getId());
      listRepository.delete(entity);
      return listConverter.toDto(entity);
    }).orElseThrow(() -> new NotFoundException("It was not possible to update since there is not a List with Id: "+id));
  }

  @Override
  public List<ItemDTO> getItems(long listId) {
    List<ItemEntity> itemEntityList = itemRepository.findByListId(listId);
    return itemConverter.toDtoList(itemEntityList);
  }

  @Override
  public ListDTO addItem(long listId, ItemDTO item) {
    Optional<ListEntity> optional = listRepository.findById(listId);
    return optional.map(listEntity -> {
      ItemEntity itemEntity = itemConverter.toEntity(item);
      itemEntity.setListId(listEntity.getId());
      itemRepository.save(itemEntity);
      return listConverter.toDto(listEntity);
    }).orElseThrow(() -> new NotFoundException("It was not possible to add the Item since there is not a List with Id: "+listId));
  }

  @Override
  public ListDTO addItems(long listId, List<ItemDTO> items) {
    Optional<ListEntity> optional = listRepository.findById(listId);
    return optional.map(listEntity -> {
      List<ItemEntity> itemEntities = itemConverter.toEntityList(items);
      itemEntities.forEach(entity -> entity.setListId(listEntity.getId()));
      itemRepository.saveAll(itemEntities);
      return listConverter.toDto(listEntity);
    }).orElseThrow(() -> new NotFoundException("It was not possible to add the Items since there is not a List with Id: "+listId));
  }

  @Override
  public ListDTO updateItem(long listId, long itemId, String name, String description) {
    Optional<ItemEntity> optionalItem = itemRepository.findByListIdAndId(listId, itemId);
    return optionalItem.map(entity -> {
      entity.setName(name);
      entity.setDescription(description);
      itemRepository.save(entity);


      Optional<ListEntity> optionalList = listRepository.findById(listId);
      ListDTO dto = optionalList.map(listConverter::toDto).orElseThrow(() -> new NotFoundException(
          "It was not possible to update the Items since there is not a List with Id: " + listId));
      List<ItemEntity> itemEntities = itemRepository.findByListId(listId);
      dto.setItems(itemConverter.toDtoList(itemEntities));
      return dto;
    }).orElseThrow(() -> new NotFoundException(format(NOT_POSSIBLE_TO_UPDATE_ITEM, listId, itemId)));
  }

  @Override
  public ListDTO removeItem(long listId, long itemId) {
    Optional<ItemEntity> optionalItem = itemRepository.findByListIdAndId(listId, itemId);
    return optionalItem.map(entity -> {
      itemRepository.deleteById(itemId);
      Optional<ListEntity> optionalList = listRepository.findById(listId);
      ListDTO dto = optionalList.map(listConverter::toDto).orElseThrow(() -> {
        String message = format("It was not possible to remove the Items since there is not a List with Id: {0}", listId);
        return new NotFoundException(message);
      });
      List<ItemEntity> itemEntities = itemRepository.findByListId(listId);
      dto.setItems(itemConverter.toDtoList(itemEntities));
      return dto;
    }).orElseThrow(() -> new NotFoundException(format(NOT_POSSIBLE_TO_DELETE_ITEM, listId, itemId)));
  }

  @Override
  public ListDTO removeItems(long listId) {
    Optional<ListEntity> optionalList = listRepository.findById(listId);
    return optionalList.map(entity -> {
      List<ItemEntity> items = itemRepository.findByListId(listId);
      items.forEach(itemEntity -> itemRepository.deleteById(itemEntity.getId()));
      return listConverter.toDto(entity);
    }).orElseThrow(() -> new NotFoundException(format("It was impossible to remove the items due to List id: {0} does not exist", listId)));
  }
}
