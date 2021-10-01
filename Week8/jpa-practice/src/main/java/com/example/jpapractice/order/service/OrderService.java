package com.example.jpapractice.order.service;

import com.example.jpapractice.domain.order.Order;
import com.example.jpapractice.dto.OrderDto;
import com.example.jpapractice.order.converter.OrderConverter;
import com.example.jpapractice.repository.OrderRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Transactional
    public String save(OrderDto dto) {
        // 1. dto -> entity (준영속)
        var order = orderConverter.convertOrder(dto);
        // 2. orderRepository.save(entity) -> 영속화
        var entity = orderRepository.save(order);
        // 3. 결과 반환
        return entity.getUuid();
    }

    @Transactional
    public OrderDto findOne(String uuid) throws NotFoundException {
        return orderRepository.findById(uuid)
                .map(order -> orderConverter.convertOrderDto(order))   // entity -> dto
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));
    }

    @Transactional
    public Page<OrderDto> findAll(Pageable pageable){
        return orderRepository.findAll(pageable)    // 리턴값 -> Page<Order>
                .map(page -> orderConverter.convertOrderDto(page)); //?
    }

}
