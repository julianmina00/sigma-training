package com.sigma.training.listservice.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public interface Converter<T, S> {

  S toDto(@NotNull T t);

  T toEntity(@NotNull S s);

  default List<S> toDtoList(@NotNull List<T> fromList){
    return fromList.stream().map(this::toDto).collect(Collectors.toList());
  }

  default List<T> toEntityList(@NotNull List<S> fromList){
    return fromList.stream().map(this::toEntity).collect(Collectors.toList());
  }

}
