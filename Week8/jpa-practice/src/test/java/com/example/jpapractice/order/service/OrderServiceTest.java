package com.example.jpapractice.order.service;

import com.example.jpapractice.domain.order.OrderStatus;
import com.example.jpapractice.dto.*;
import com.example.jpapractice.repository.OrderRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void save_test () {
        // Given
        OrderDto orderDto = OrderDto.builder()
                .uuid(uuid)
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(
                        MemberDto.builder()
                                .name("강홍구")
                                .nickName("guppy.kang")
                                .address("서울시 동작구만 움직이면 쏜다.")
                                .age(33)
                                .description("---")
                                .build()
                )
                .orderItemDtos(List.of(
                        OrderItemDto.builder()
                                .price(1000)
                                .quantity(100)
                                .itemDtos(List.of(
                                        ItemDto.builder()
                                                .type(ItemType.FOOD)
                                                .chef("백종원")
                                                .price(1000)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        // When
        String savedUuid = orderService.save(orderDto);

        // Then
        assertThat(uuid).isEqualTo(savedUuid);
        log.info("UUID:{}", uuid);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void findOneTest() throws NotFoundException {
        // BDD(Behavior Driven Development)로 하자~

        // Given
        String orderUuid = uuid;

        // When
        var one = orderService.findOne(orderUuid);

        // Then
        assertThat(one.getUuid()).isEqualTo(orderUuid);

        // 어떤 uuid가 주어졌을 때 해당 OrderDto를 찾기~
    }

    @Test
    void findAllTest() {
        // Given
        // controller에서 PageRequest를 받고 service로 넘겨준다
        var page = PageRequest.of(0, 10);

        // When
        var all = orderService.findAll(page);

        // Then
        assertThat(all.getTotalElements()).isEqualTo(1);    // 전체 데이터수
    }


//
//    @Test
//    void findAll() {
//        Page<OrderDto> orders = orderService.findOrders(PageRequest.of(0, 10));
//        log.info("{}", orders);
//    }
//


}