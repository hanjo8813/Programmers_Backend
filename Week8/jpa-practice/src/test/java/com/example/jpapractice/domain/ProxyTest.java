package com.example.jpapractice.domain;

import com.example.jpapractice.domain.order.Member;
import com.example.jpapractice.domain.order.Order;
import com.example.jpapractice.domain.order.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.jpapractice.domain.order.OrderStatus.OPENED;


@Slf4j
@SpringBootTest
public class ProxyTest {

    @Autowired
    EntityManagerFactory emf;

    private String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        // 주문 엔티티
        Order order = new Order();
        order.setUuid(uuid);
        order.setMemo("부재시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        entityManager.persist(order);

        // 회원 엔티티
        Member member = new Member();
        member.setName("kanghonggu");
        member.setNickName("guppy.kang");
        member.setAge(33);
        member.setAddress("서울시 동작구만 움직이면쏜다.");
        member.setDescription("KDT 화이팅");
        member.addOrder(order); // 연관관계 편의 메소드 사용
        entityManager.persist(member);

        transaction.commit();
    }

    @Test
    void proxy() {
        EntityManager entityManager = emf.createEntityManager();

        // order 엔티티만 select로 가져오고 -> 영속화
        var order = entityManager.find(Order.class, uuid);
        // member는 그냥 객체만 받아보기 (아직 비영속 객체임)
        var member = order.getMember();

        // member는 영속화 됐니?
        log.info("{}", emf.getPersistenceUnitUtil().isLoaded(member));
        // fetch = LAZY일 경우 이때까지 member는 프록시 객체임

        // 이제 member를 사용 -> 영속화
        var nickName = member.getNickName();
        // 다시 영속화 여부 파악
        log.info("{}", emf.getPersistenceUnitUtil().isLoaded(member));
    }

    @Test
    void move_persist() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        var order = entityManager.find(Order.class, uuid);

        transaction.begin();

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(10);
        orderItem.setPrice(1000);
        order.addOrderItem(orderItem);  // 영속성 전이 : 준영속 -> 영속

        transaction.commit();

        // ---------------------------------------------------------------

        entityManager.clear();
        var order2 = entityManager.find(Order.class, uuid);

        transaction.begin();

        // 고아상태
        order2.getOrderItems().remove(0);

        transaction.commit();
    }


//    @Test
//    void orphan() {
//        EntityManager entityManager = emf.createEntityManager();
//
//        // 회원 조회 -> 회원의 주문 까지 조회
//        Member findMember = entityManager.find(Member.class, 1L);
//        findMember.getOrders().remove(0);
//
//        EntityTransaction transaction = entityManager.getTransaction();
//
//        transaction.begin();
//        transaction.commit();
//    }

}