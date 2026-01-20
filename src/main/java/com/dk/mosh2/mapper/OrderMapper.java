package com.dk.mosh2.mapper;

import com.dk.mosh2.dto.OrderDto;
import com.dk.mosh2.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderItem", target = "items")
    OrderDto toDto(Order order);

}
