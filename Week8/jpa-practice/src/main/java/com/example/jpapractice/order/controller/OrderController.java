package com.example.jpapractice.order.controller;

import com.example.jpapractice.dto.OrderDto;
import com.example.jpapractice.order.service.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/orders")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Exception 처리

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundHandler (NotFoundException e){
        return ApiResponse.fail(404, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> internalServerErrorHandler (Exception e){
        return ApiResponse.fail(500, e.getMessage());
    }

    // 매핑

    @PostMapping
    public ApiResponse<String> save(
            @RequestBody OrderDto orderDto
    ) {
        String uuid = orderService.save(orderDto);
        return ApiResponse.ok(uuid);
    }

    @GetMapping("/{uuid}")
    public ApiResponse<OrderDto> getOne(
            @PathVariable String uuid
    ) throws NotFoundException {
        OrderDto one = orderService.findOne(uuid);
        return ApiResponse.ok(one);
    }

    @GetMapping
    public ApiResponse<Page<OrderDto>> getAll(
            Pageable pageable
    ) {
        var all = orderService.findAll(pageable);
        return ApiResponse.ok(all);
    }

}
