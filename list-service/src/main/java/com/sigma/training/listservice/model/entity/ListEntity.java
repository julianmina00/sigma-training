package com.sigma.training.listservice.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "lists")
public class ListEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank
  @Size(min = 3, max = 100)
  private String name;

  @Column
  @NotBlank
  @Size(max = 500)
  private String description;

  @Column(name = "creation_date", nullable = false, updatable = false)
  private Date createdAt;

  @Column(name = "updated_date")
  private Date updatedAt;

  @PrePersist
  protected void onCreate(){
    this.createdAt = new Date();
  }

  @PreUpdate
  protected void onUpdate(){
    this.updatedAt = new Date();
  }

}
