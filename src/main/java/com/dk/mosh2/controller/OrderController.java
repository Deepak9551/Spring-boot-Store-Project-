package com.dk.mosh2.controller;

import com.dk.mosh2.exceptions.AccessDeniedException;
import com.dk.mosh2.exceptions.ResourceNotFoundException;
import com.dk.mosh2.dto.OrderDto;
import com.dk.mosh2.mapper.OrderMapper;
import com.dk.mosh2.repo.OrderRepository;
import com.dk.mosh2.service.jwt.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private AuthService authService;
@Autowired
    private OrderRepository orderRepository;
@Autowired
private OrderMapper orderMapper;

@GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        var user = authService.currentUser();
        var orders = orderRepository.findAllByUser(user).orElse(null);

        if (isNull(orders)){
            throw new ResourceNotFoundException("No orders found for the current user");
        }
    System.out.println(orders);
    List<OrderDto> orderDtos = orders.stream()
            .map(orderMapper::toDto)
            .toList();

    return ResponseEntity.ok(orderDtos);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getSingleOrder(@PathVariable(value = "orderId") BigInteger orderId){
        var user = authService.currentUser();
        var order = orderRepository.findById(orderId).orElse(null);

        if (isNull(order)){
            throw new ResourceNotFoundException("Order not found for the current user with id: " + orderId);
        }

        if(!order.getUser().getId().equals(user.getId())){
         throw  new AccessDeniedException("You are not authorized to access this order");
        }

        return ResponseEntity.ok(orderMapper.toDto(order));

    }
}
