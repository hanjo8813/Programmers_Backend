package org.prgrms.kdt.customer.Repository;

import org.prgrms.kdt.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class JdbcCustomerRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    // dataSource는 Auto wired로 의존성 주입됨
    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerName = resultSet.getString("name");
        var customerEmail = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null
                ? resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        //logger.info("customer id -> {} , name -> {} , createdAt -> {}", customerId, customerName, createdAt);
        return new Customer(customerId, customerName, customerEmail, lastLoginAt, createdAt);
    };

    public JdbcCustomerRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public Customer insert(Customer customer) {
        var update = jdbcTemplate.update(
                "insert into customers(customer_id, name, email, created_at) values (UUID_TO_BIN(?), ?, ?, ?)",
                customer.getCustomerId().toString().getBytes(),
                customer.getName(),
                customer.getEmail(),
                Timestamp.valueOf(customer.getCreatedAt())
        );
        if (update != 1)
            throw new RuntimeException("insert 실패");
        return customer;

//        try (
//                var connection = dataSource.getConnection();
//                var statement = connection.prepareStatement("insert into customers(customer_id, name, email, created_at) values (UUID_TO_BIN(?), ?, ?, ?)");
//        ) {
//            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
//            statement.setString(2, customer.getName());
//            statement.setString(3, customer.getEmail());
//            // 애초에 도메인에서 Timestamp 타입으로 저장하면 되는거 아닌가?
//            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));
//            var excutedUpdate = statement.executeUpdate();
//            if (excutedUpdate != 1)
//                throw new RuntimeException("추가 안됐음");
//            return customer;
//        } catch (SQLException e) {
//            logger.error("insert sql 수행중 오류 발생", e);
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update(
                "update customers set name = ?, email = ?, last_login_at = ? where customer_id = UUID_TO_BIN(?)",
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null,
                customer.getCustomerId().toString().getBytes()
        );
        if (update != 1)
            throw new RuntimeException("update 실패");
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);

//        List<Customer> allCustomers = new ArrayList<>();
//        try (
//                var connection = dataSource.getConnection();
//                var statement = connection.prepareStatement("select * from customers");
//                var resultSet = statement.executeQuery()
//        ) {
//            while (resultSet.next()) {
//                mapToCustomer(allCustomers, resultSet);
//            }
//        } catch (SQLException e) {
//            logger.error("findAll sql 수행중 오류 발생", e);
//            throw new RuntimeException(e);
//        }
//        return allCustomers;
    }


    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            // null인지 아닌지 확신할 수 없는 객체를 담을 때
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from customers where customer_id = UUID_TO_BIN(?)",
                            customerRowMapper,
                            customerId.toString().getBytes()
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty Result", e);
            return Optional.empty();
        }

//        List<Customer> foundCustomers = new ArrayList<>();
//        try (
//                var connection = dataSource.getConnection();
//                var statement = connection.prepareStatement("select * from customers where customer_id = UUID_TO_BIN(?)");
//        ) {
//            statement.setBytes(1, customerId.toString().getBytes());
//            try (var resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    mapToCustomer(foundCustomers, resultSet);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("findById sql 수행중 오류 발생", e);
//            throw new RuntimeException(e);
//        }
//        return foundCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try {
            // null인지 아닌지 확신할 수 없는 객체를 담을 때
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from customers where name = ?",
                            customerRowMapper,
                            name
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty Result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            // null인지 아닌지 확신할 수 없는 객체를 담을 때
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from customers where email = ?",
                            customerRowMapper,
                            email
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty Result", e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from customers");
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject(
                "select count(*) from customers",
                Integer.class
        );
    }


    // inner method
    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
