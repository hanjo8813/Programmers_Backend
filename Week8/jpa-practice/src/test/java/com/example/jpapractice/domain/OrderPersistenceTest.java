package com.example.jpapractice.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.jpapractice.domain.OrderStatus.OPENED;

@Slf4j
@SpringBootTest
public class OrderPersistenceTest {

    @Autowired
    EntityManagerFactory emf;

    @Test
    void member_insert() {
        Member member = new Member();
        member.setName("name");
        member.setAddress("address");
        member.setAge(1);
        member.setNickName("nickname");
        member.setDescription("description");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.persist(member);
        transaction.commit();
    }

    @Test
    void 잘못된_설계() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Member member = new Member();
        member.setName("kanghonggu");
        member.setAddress("서울시 동작구(만) 움직이면 쏜다.");
        member.setAge(33);
        member.setNickName("guppy.kang");
        member.setDescription("백앤드 개발자에요.");

        transaction.begin();

        entityManager.persist(member);
        Member memberEntity = entityManager.find(Member.class, 1L);

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        order.setMemo("부재시 전화주세요.");
        order.setMemberId(memberEntity.getId()); // 외래키를 직접 지정

        entityManager.persist(order);
        transaction.commit();

        // order 엔티티를 찾고
        Order orderEntity = entityManager.find(Order.class, order.getUuid());

        // 해당 order 엔티티의 키값을 통해 -> FK 를 이용해 회원 다시 조회
        // FK를 이용한 조회를 할때마다 서로의 객체를 참조해야함
        Member orderMemberEntity = entityManager.find(Member.class, orderEntity.getMemberId());

        // orderEntity.getMember() // 객체중심 설계라면 객체그래프 탐색을 해야하지 않을까?

        log.info("nick : {}", orderMemberEntity.getNickName());
    }
}
