package com.example.jpapractice.repository;

import com.example.jpapractice.domain.order.Order;
import com.example.jpapractice.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    // OrderStatus와 일치하는 값을 모두 찾는다
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    // OrderStatus와 일치하는 값을 찾고 + OrderDatetime 기준으로 order by
    List<Order> findAllByOrderStatusOrderByOrderDatetime(OrderStatus orderStatus);

    // JPQL
    @Query("SELECT o FROM Order AS o WHERE o.memo LIKE %?1%")
    Optional<Order> findByMemo(String memo);
}
