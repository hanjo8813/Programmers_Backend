package com.example.practice;

import com.example.practice.repository.CustomerMapper;
import com.example.practice.repository.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@SpringBootTest
public class MybatisTest {

    static final String DROP_TABLE_SQL = "DROP TABLE customers IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void save_test(){
        jdbcTemplate.update(DROP_TABLE_SQL);
        jdbcTemplate.update(CREATE_TABLE_SQL);

        // 인터페이스인데 구현체 없이 메소드사용 (xml과 매핑됐기 때문)
        customerMapper.save(new Customer(1L, "first", "last"));
        Customer customer = customerMapper.findById(1L);
        log.info("fullName : {} {}", customer.getFirstName(), customer.getLastName());
    }
}
