package com.example.jpapractice.domain;

import com.example.jpapractice.domain.order.Order;
import com.example.jpapractice.domain.order.item.Food;
import com.example.jpapractice.domain.parent.Parent;
import com.example.jpapractice.domain.parent.ParentId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.jpapractice.domain.order.OrderStatus.OPENED;

@Slf4j
@SpringBootTest
public class ImproveMappingTest {

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void inheritance_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Food food = new Food();
        food.setPrice(1000);
        food.setStockQuantity(100);
        food.setChef("백종원");
        entityManager.persist(food);

        transaction.commit();
    }

    @Test
    void mapped_super_class_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        order.setMemo("부재시 전화주세요.");

        // Basic 클래스로 상속받은 필드값도 잘 생성된다~
        order.setCreatedBy("CreatedBy");
        order.setCratedAt(LocalDateTime.now());

        entityManager.persist(order);

        transaction.commit();
    }

    @Test
    void id_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Parent parent = new Parent();
//        parent.setId1("id1");
//        parent.setId2("id2");
        ParentId parentId = new ParentId("id1","id2");
        parent.setId(parentId);
        entityManager.persist(parent);

        transaction.commit();

        entityManager.clear();
        Parent selected = entityManager.find(Parent.class, parentId);
        log.info("{} {}", selected.getId().getId1(), selected.getId().getId2());
    }
}
