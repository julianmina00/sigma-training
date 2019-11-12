package com.sigma.training.listservice.repository;

import com.sigma.training.listservice.model.entity.ItemEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

  List<ItemEntity> findByListId(Long listId);

}
