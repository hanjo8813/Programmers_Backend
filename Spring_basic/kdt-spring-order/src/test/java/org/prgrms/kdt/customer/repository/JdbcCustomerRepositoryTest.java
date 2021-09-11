package org.prgrms.kdt.customer.repository;

import static org.hamcrest.MatcherAssert.assertThat;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.prgrms.kdt.customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.hamcrest.Matchers.*;


import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;


import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcCustomerRepositoryTest {

    @Configuration
    @ComponentScan(basePackages = {"org.prgrms.kdt.customer"})
    static class Config {
        @Bean
        public DataSource dataSource() {
//            // H2
//            return new EmbeddedDatabaseBuilder()
//                    .generateUniqueName(true)
//                    .setType(H2)
//                    .setScriptEncoding("UTF-8")
//                    .ignoreFailedDrops(true)
//                    .addScript("schema.sql")
//                    .build();

            // MySql
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("root1234")
                    .type(HikariDataSource.class)
                    .build();
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100);

            // Embedded MySql
//            var dataSource = DataSourceBuilder.create()
//                    .url("jdbc:mysql://localhost:2215/test-order_mgmt")
//                    .username("test")
//                    .password("test1234")
//                    .type(HikariDataSource.class)
//                    .build();
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }
    }

    @Autowired
    JdbcCustomerRepository jdbcCustomerRepository;

    @Autowired
    DataSource dataSource;

    Customer newCustomer;

    // EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setup() {
//        var mysqlConfig = aMysqldConfig(v8_0_11)
//                .withCharset(UTF8)
//                .withPort(2215)
//                .withUser("test", "test1234")
//                .withTimeZone("Asia/Seoul")
//                .build();
//
//        embeddedMysql = anEmbeddedMysql(mysqlConfig)
//                .addSchema("test-order_mgmt", classPathScript("schema.sql"))
//                .start();

        newCustomer = new Customer(UUID.randomUUID(), "test-user", "test-user.mail.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)) ;
        jdbcCustomerRepository.deleteAll();
    }

//    @AfterAll
//    void cleanup(){
//        embeddedMysql.stop();
//    }

    @Test
    @Order(2)
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @Order(3)
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert(){
        jdbcCustomerRepository.insert(newCustomer);
        var retrievedCustomer = jdbcCustomerRepository.findById(newCustomer.getCustomerId());
        // 잘 들어갔니?
        assertThat(retrievedCustomer.isEmpty(), is(false));
        // customer 클래스 안에 인자 다 똑같니?
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }


    @Test
    @Order(4)
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll() throws InterruptedException {
        var customers = jdbcCustomerRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
        //Thread.sleep(5000);
    }

    @Test
    @Order(5)
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    public void testFindByName(){
        var customer = jdbcCustomerRepository.findByName(newCustomer.getName());
        assertThat(customer.isEmpty(), is(false));

        var unknown = jdbcCustomerRepository.findByName("unknown_user");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(6)
    @DisplayName("이메일으로 고객을 조회할 수 있다.")
    public void testFindByEmail() {
        var customer = jdbcCustomerRepository.findByEmail(newCustomer.getEmail());
        assertThat(customer.isEmpty(), is(false));

        var unknown = jdbcCustomerRepository.findByEmail("unknownr@mail.com");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(7)
    @DisplayName("고객을 수정할 수 있다.")
    public void testUpdate() {
        newCustomer.changeName("updated-user");
        jdbcCustomerRepository.update(newCustomer);

        var allCustomers = jdbcCustomerRepository.findAll();
        assertThat(allCustomers, hasSize(1));
        assertThat(allCustomers, everyItem(samePropertyValuesAs(newCustomer)));

        var retrievedCustomer = jdbcCustomerRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }


}