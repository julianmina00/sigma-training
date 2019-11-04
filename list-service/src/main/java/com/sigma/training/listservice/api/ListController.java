package com.sigma.training.listservice.api;

import com.sigma.training.listservice.dto.ListDTO;
import com.sigma.training.listservice.service.ListService;
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

  private final ListService listService;

  @Autowired
  public ListController(ListService listService){
    this.listService = listService;
  }

  //(GET) /rest/list: Obtains all lists
  @GetMapping
  public ResponseEntity<List<ListDTO>> getLists(){
    List<ListDTO> lists = listService.getLists();
    if(lists == null || lists.isEmpty()){
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(lists);
  }

  //(GET) /rest/list/{id}: Obtains the List for the given id
  @GetMapping("/{id}")
  public ResponseEntity<ListDTO> getLists(@PathVariable("id") long id){
    ListDTO list = listService.getList(id);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  //(POST) /rest/list: Creates a new List with items
  @PostMapping
  public ResponseEntity<ListDTO> createList(@RequestBody ListDTO listDTO){
    ListDTO list = listService.createList(listDTO);
    if(list == null){
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(list);
  }

  //(POST) /rest/list/{name}/{description}: Creates a new List without items
  @PostMapping("/{name}/{description}")
  public ResponseEntity<ListDTO> createList(@PathVariable("name") String name,
      @PathVariable("description") String description){
    ListDTO list = listService.createList(name, description);
    if(list == null){
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(list);
  }

  //(PUT) /rest/list/{id}/{name}/{description}: Updates the List for the given id
  @PutMapping("/{id}/{name}/{description}")
  public ResponseEntity<ListDTO> createList(
      @PathVariable("id") long id,
      @PathVariable("name") String name,
      @PathVariable("description") String description){
    ListDTO list = listService.updateList(id, name, description);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  //(DELETE) /rest/list/{id}: Deletes the List for the given id with all its items
  @DeleteMapping("/{id}")
  public ResponseEntity<ListDTO> removeList(@PathVariable("id") long id){
    ListDTO list = listService.deleteList(id);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

}
