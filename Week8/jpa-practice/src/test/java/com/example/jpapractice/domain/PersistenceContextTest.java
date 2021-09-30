package com.example.jpapractice.domain;

import com.example.jpapractice.domain.customer.Customer;
import com.example.jpapractice.domain.customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
public class PersistenceContextTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void 저장() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");

        // 비영속상태의 customer 객체를 -> PC에 추가하여 영속상태로 만들어준다.
        entityManager.persist(customer);

        // Flush 발생
        transaction.commit();
    }

    @Test
    void 조회_DB() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        entityManager.persist(customer);
        transaction.commit();

        // PC에 있는 Entity를 분리 (영속 -> 준영속)
        entityManager.detach(customer);

        // detach로 기존에 있던 1차캐시가 삭제되었으므로 select 쿼리를 실행하게된다.
        // + 쿼리 결과 entity는 PC에 영속 -> 1차캐시에 등록
        var selected = entityManager.find(Customer.class, 1L);
        log.info("{} {}", selected.getFirstName(), selected.getLastName());
    }

    @Test
    void 조회_1차캐시() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        entityManager.persist(customer);
        transaction.commit();

        // 영속된 entity의 1차 캐시에 저장된 정보를 불러온다 (쿼리 실행 X)
        var selected = entityManager.find(Customer.class, 1L);
        log.info("{} {}", selected.getFirstName(), selected.getLastName());
    }

    @Test
    void 수정() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        entityManager.persist(customer);
        transaction.commit();

        // 다시 트랜잭션 시작
        transaction.begin();

        customer.setFirstName("first2");
        customer.setLastName("last2");

        // 최종은 스냅샷과 비교
        customer.setFirstName("first");
        customer.setLastName("last");


        // Flush 발생시 Entity의 변경 감지 -> update 쿼리를 날려준다
        transaction.commit();
    }

    @Test
    void 삭제() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        entityManager.persist(customer);
        transaction.commit();

        transaction.begin();
        // PC에서 해당 Entity를 삭제
        entityManager.remove(customer);
        // Flush : Entity 삭제를 감지하고 delete 쿼리 날림
        transaction.commit();
    }

    @Test
    void 동일성보장() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        entityManager.persist(customer);
        transaction.commit();

        // 1차캐시에서 가져옴
        var selected1 = entityManager.find(Customer.class, 1L);
        var selected3 = entityManager.find(Customer.class, 1L);
        // 주소 비교
        if(selected1 == selected3){
            log.info("동일성 보장됨");
        }

        entityManager.detach(customer);

        // select 쿼리로 DB에서 가져옴
        var selected2 = entityManager.find(Customer.class, 1L);

        // 주소 비교
        if(selected1 == selected2){
            log.info("동일성 보장됨");
        }
    }

    @Test
    void temp() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        entityManager.persist(customer);
        transaction.commit();


    }


}
