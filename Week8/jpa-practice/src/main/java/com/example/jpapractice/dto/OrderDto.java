package com.example.jpapractice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.jpapractice.domain.order.OrderStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String uuid;
    private LocalDateTime orderDatetime;
    private OrderStatus orderStatus;
    private String memo;

    private MemberDto memberDto;
    private List<OrderItemDto> orderItemDtos;

}