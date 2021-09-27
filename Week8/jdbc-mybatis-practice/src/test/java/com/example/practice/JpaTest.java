package com.example.practice;

import com.example.practice.repository.CustomerRepository;
import com.example.practice.repository.domain.CustomerEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JpaTest {

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void insert_test() {
        // Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        customer.setAge(1);

        // When
        customerRepository.save(customer);

        // Then
        var entity = customerRepository.findById(1L).get();
        log.info("{} {}", entity.getFirstName(), entity.getLastName());
        log.info("{}", entity.getAge());
    }

    @Test
    @Transactional
    void update_test(){
        // Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("first");
        customer.setLastName("last");
        customerRepository.save(customer);

        // When
        // Transactional이 붙어 있으면 쿼리 결과 객체는 '영속성 컨텍스트'에서 관리된다
        // 트랜잭션 안에서 쿼리 결과 객체의 변화가 감지되면 자동으로 업데이트 쿼리가 날라간다
        // --> 자바 객체와 DB 테이블이 완벽히 연동되어 동작한다
        var entity = customerRepository.findById(1L).get();
        entity.setFirstName("first2");
        entity.setLastName("last2");

        // Then
        var updated = customerRepository.findById(1L).get();
        log.info("{} {}", updated.getFirstName(), updated.getLastName());
    }
}