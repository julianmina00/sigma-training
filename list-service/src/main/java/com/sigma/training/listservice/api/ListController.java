package com.sigma.training.listservice.api;

import com.sigma.training.listservice.model.dto.ItemDTO;
import com.sigma.training.listservice.model.dto.ListDTO;
import com.sigma.training.listservice.service.ListService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/list")
public class ListController {

  private static final ResponseEntity<ListDTO> NOT_FOUND_LIST = ResponseEntity.notFound().build();
  private static final ResponseEntity<List<ItemDTO>> NOT_FOUND_LIST_OF_ITEM = ResponseEntity.notFound().build();
  private final ListService listService;

  @Autowired
  public ListController(ListService listService){
    this.listService = listService;
  }

  //(GET) /rest/list: Obtains all lists
  @GetMapping
  @ApiOperation(value = "Obtain all created Lists", produces = "application/json", tags = "List Endpoint")
  public ResponseEntity<List<ListDTO>> getLists(){
    List<ListDTO> lists = listService.getLists();
    return ResponseEntity.ok(lists);
  }

  //(GET) /rest/list/{id}: Obtains the List for the given id
  @GetMapping("/{id}")
  @ApiOperation(value = "Obtains the List for the given id", produces = "application/json", tags = "List Endpoint")
  public ResponseEntity<ListDTO> getLists(
      @ApiParam(value = "Identifier of the List to be recovered", required = true, type = "path") @PathVariable("id") long id
  ){
    ListDTO list = listService.getList(id);
    return ResponseEntity.ok(list);
  }

  //(POST) /rest/list: Creates a new List with items
  @PostMapping
  @ApiOperation(value = "Creates a new List with items", produces = "application/json", tags = "List Endpoint")
  public ResponseEntity<ListDTO> createList(
      @ApiParam(value = "JSON that represents the List to be created", required = true, type = "body") @RequestBody ListDTO listDTO)
  {
    ListDTO list = listService.createList(listDTO);
    return ResponseEntity.ok(list);
  }

  //(POST) /rest/list/{name}/{description}: Creates a new List without items
  @PostMapping("/{name}/{description}")
  @ApiOperation(value = "Creates a new List without items", produces = "application/json", tags = "List Endpoint")
  public ResponseEntity<ListDTO> createList(
      @ApiParam(value = "Name of the List to be created", required = true, type = "path") @PathVariable("name") String name,
      @ApiParam(value = "Description of the List to be created", required = true, type = "path") @PathVariable("description") String description){
    ListDTO list = listService.createList(name, description);
    return ResponseEntity.ok(list);
  }

  //(PUT) /rest/list/{id}/{name}/{description}: Updates the List for the given id
  @PutMapping("/{id}/{name}/{description}")
  @ApiOperation(value = "Updates the List for the given id", produces = "application/json", tags = "List Endpoint")
  public ResponseEntity<ListDTO> updateList(
      @ApiParam(value = "Identifier of the List to be updated", required = true, type = "path") @PathVariable("id") long id,
      @ApiParam(value = "New name of the List", required = true, type = "path") @PathVariable("name") String name,
      @ApiParam(value = "New description of the List", required = true, type = "path") @PathVariable("description") String description){
    ListDTO list = listService.updateList(id, name, description);
    return ResponseEntity.ok(list);
  }

  //(DELETE) /rest/list/{id}: Deletes the List for the given id with all its items
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Deletes the List for the given id with all its items", produces = "application/json", tags = "List Endpoint")
  public ResponseEntity<ListDTO> removeList(
      @ApiParam(value = "Identifier of the List to be removed", required = true, type = "path") @PathVariable("id") long id)
  {
    ListDTO list = listService.deleteList(id);
    return ResponseEntity.ok(list);
  }


  // (GET) /rest/list/{listId}/items: Obtains the items of a List
  @GetMapping("/{listId}/items")
  @ApiOperation(value = "Obtains the Items of a List", produces = "application/json", tags = "Item Endpoint")
  public ResponseEntity<List<ItemDTO>> getItems(
      @ApiParam(value = "Identifier of the List whose Items will be recovered", required = true, type = "path") @PathVariable("listId") long listId)
  {
    List<ItemDTO> items = listService.getItems(listId);
    return ResponseEntity.ok(items);
  }

  // (POST) /rest/list/{listId}/item: Adds a new Item to a List
  @PostMapping("/{listId}/item")
  @ApiOperation(value = "Adds a new item to a List", produces = "application/json", tags = "Item Endpoint")
  public ResponseEntity<ListDTO> createItems(
      @ApiParam(value = "Identifier of the List where the Item will be added", required = true, type = "path") @PathVariable("listId") long listId,
      @ApiParam(value = "JSON that represents the new Item", required = true, type = "body")@RequestBody ItemDTO item){
    ListDTO list = listService.addItem(listId, item);
    return ResponseEntity.ok(list);
  }

  // (POST) /rest/list/{listId}/items: Adds a set of items to a List
  @PostMapping("/{listId}/items")
  @ApiOperation(value = "Adds a set of items to a List", produces = "application/json", tags = "Item Endpoint")
  public ResponseEntity<ListDTO> createItems(
      @ApiParam(value = "Identifier of the List where the Items will be added", required = true, type = "path") @PathVariable("listId") long listId,
      @ApiParam(value = "JSON that represents the collection of the new Items", required = true, type = "body")@RequestBody List<ItemDTO> items){
    ListDTO list = listService.addItems(listId, items);
    return ResponseEntity.ok(list);
  }

  // (PUT) /rest/list/{listId}/item/{itemId}/{name}/{description}: Updates an Item of a List
  @PutMapping("/{listId}/item/{itemId}/{name}/{description}")
  @ApiOperation(value = "Updates an Item of a List", produces = "application/json", tags = "Item Endpoint")
  public ResponseEntity<ListDTO> updateItem(
      @ApiParam(value = "Identifier of the List whose Item will be updated", required = true, type = "path") @PathVariable("listId") long listId,
      @ApiParam(value = "Identifier of the Item to be updated", required = true, type = "path") @PathVariable("itemId") long itemId,
      @ApiParam(value = "New name of the Item", required = true, type = "path") @PathVariable("name") String name,
      @ApiParam(value = "New description of the Item", required = true, type = "path") @PathVariable("description") String description){
    ListDTO list = listService.updateItem(listId, itemId, name, description);
    return ResponseEntity.ok(list);
  }

  // (DELETE) /rest/list/{listId}/item/{itemId}: Removes an Item of a List
  @DeleteMapping("/{listId}/item/{itemId}")
  @ApiOperation(value = "Removes an Item of a List", produces = "application/json", tags = "Item Endpoint")
  public ResponseEntity<ListDTO> deleteItem(
      @ApiParam(value = "Identifier of the List whose Item will be removed", required = true, type = "path") @PathVariable("listId") long listId,
      @ApiParam(value = "Identifier of the item to be recovered", required = true, type = "path") @PathVariable("itemId") long itemId){
    ListDTO list = listService.removeItem(listId, itemId);
    if(list == null){
      return NOT_FOUND_LIST;
    }
    return ResponseEntity.ok(list);

  }

  // (DELETE) /rest/list/{listId}/items: Removes all items from a List
  @DeleteMapping("{listId}/items")
  @ApiOperation(value = "Removes all items from a List", produces = "application/json", tags = "Item Endpoint")
  public ResponseEntity<ListDTO> deleteItems(
      @ApiParam(value = "Identifier of the List whose Items will be removed", required = true, type = "path") @PathVariable("listId") long listId)
  {
    ListDTO list = listService.removeItems(listId);
    if(list == null){
      return NOT_FOUND_LIST;
    }
    return ResponseEntity.ok(list);
  }

}
