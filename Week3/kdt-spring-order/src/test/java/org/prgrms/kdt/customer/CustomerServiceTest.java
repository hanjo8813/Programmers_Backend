package org.prgrms.kdt.customer;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.prgrms.kdt.customer.model.Customer;
import org.prgrms.kdt.customer.repository.CustomerRepository;
import org.prgrms.kdt.customer.repository.JdbcCustomerNamedRepository;
import org.prgrms.kdt.customer.service.CustomerService;
import org.prgrms.kdt.customer.service.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.hamcrest.Matchers.*;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Configuration
    @ComponentScan(basePackages = {"org.prgrms.kdt.customer"})
    @EnableTransactionManagement    // App Context내 Bean에서 트랜잭션을 사용할 수 있게 하줌
    static class Config {
        @Bean
        public DataSource dataSource() {
            // MySql
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("root1234")
                    .type(HikariDataSource.class)
                    .build();
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100);
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

//        // dataSource를 주입받고 -> DataSourceTransactionManager생성하기 -> JdbcCustomerNamedRepository에서 사용가능
//        @Bean
//        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
//            return new DataSourceTransactionManager(dataSource);
//        }
//
//        @Bean
//        public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
//            return new TransactionTemplate(platformTransactionManager);
//        }

        @Bean
        public CustomerRepository customerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
            return new JdbcCustomerNamedRepository(jdbcTemplate);
        }

        @Bean
        public CustomerService customerService(CustomerRepository customerRepository) {
            return new CustomerServiceImpl(customerRepository);
        }
    }

    @Autowired
    CustomerRepository jdbcCustomerRepository;

    @Autowired
    CustomerService customerService;

    @BeforeAll
    void setup() {
        jdbcCustomerRepository.deleteAll();
    }

    @AfterEach
    void dataCleanup(){
        jdbcCustomerRepository.deleteAll();
    }

    @Test
    @DisplayName("다건 추가 테스트")
    void multiInsertTest() {
        // 고객을 서비스에 삽입 -> 레포에 삽입 -> DB에 삽입
        var customers = List.of(
                new Customer(UUID.randomUUID(), "a", "a@mail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)),
                new Customer(UUID.randomUUID(), "b", "b@mail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        );
        customerService.createCustomers(customers);

        // 레포에서 모두 찾기 -> 잘 드갔는지 확인
        var allCustomersRetrived = jdbcCustomerRepository.findAll();
        assertThat(allCustomersRetrived.size(), is(2));
        assertThat(allCustomersRetrived, containsInAnyOrder(samePropertyValuesAs(customers.get(0)), samePropertyValuesAs(customers.get(1))));
    }

    @Test
    @DisplayName("다건 추가 실패시 전체 트랜잭션이 롤백되어야 한다.")
    void multiInsertRollbackTest() {
        var customers = List.of(
                new Customer(UUID.randomUUID(), "c", "c@mail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)),
                new Customer(UUID.randomUUID(), "d", "c@mail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        );

        try{
            customerService.createCustomers(customers);
        } catch (DataAccessException e){

        }

        var allCustomersRetrived = jdbcCustomerRepository.findAll();
        assertThat(allCustomersRetrived.size(), is(0));
        assertThat(allCustomersRetrived.isEmpty(), is(true));
        assertThat(allCustomersRetrived, not(containsInAnyOrder(samePropertyValuesAs(customers.get(0)), samePropertyValuesAs(customers.get(1)))));
    }


}