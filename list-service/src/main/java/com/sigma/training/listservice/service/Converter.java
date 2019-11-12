package com.sigma.training.listservice.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public interface Converter<T, S> {

  S convert(@NotNull T t);

  T reverseConvert(@NotNull S s);

  default List<S> convertAll(@NotNull List<T> fromList){
    return fromList.stream().map(this::convert).collect(Collectors.toList());
  }

  default List<T> revertConvertAll(@NotNull List<S> fromList){
    return fromList.stream().map(this::reverseConvert).collect(Collectors.toList());
  }

}
