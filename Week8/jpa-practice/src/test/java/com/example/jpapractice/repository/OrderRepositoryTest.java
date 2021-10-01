package com.example.jpapractice.repository;

import com.example.jpapractice.domain.order.Order;
import com.example.jpapractice.domain.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void test() {
        String uuid = UUID.randomUUID().toString();

        Order order = new Order();
        order.setUuid(uuid);
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo("메모메모");
        order.setCreatedBy("나나나");
        order.setCratedAt(LocalDateTime.now());

        orderRepository.save(order);
        var order1 = orderRepository.findById(uuid).get();

        orderRepository.findAllByOrderStatus(OrderStatus.OPENED);
        orderRepository.findAllByOrderStatusOrderByOrderDatetime(OrderStatus.OPENED);

        orderRepository.findByMemo("메모메모");
    }

}