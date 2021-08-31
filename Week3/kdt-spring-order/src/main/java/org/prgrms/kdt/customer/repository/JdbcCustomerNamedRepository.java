package org.prgrms.kdt.customer.repository;

import org.prgrms.kdt.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;


@Repository
@Primary
public class JdbcCustomerNamedRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerNamedRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    //private final TransactionTemplate transactionTemplate;

    public JdbcCustomerNamedRepository(NamedParameterJdbcTemplate jdbcTemplate /*, TransactionTemplate transactionTemplate*/) {
        this.jdbcTemplate = jdbcTemplate;
        //this.transactionTemplate = transactionTemplate;
    }


    private Map<String, Object> toParamMap(Customer customer) {
        return new HashMap<>() {{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
            put("createdAt", Timestamp.valueOf(customer.getCreatedAt()));
        }};
    }

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


    @Override
    public Customer insert(Customer customer) {

        var update = jdbcTemplate.update(
                "insert into customers(customer_id, name, email, created_at) values (UUID_TO_BIN(:customerId), :name, :email, :createdAt)",
                toParamMap(customer)
        );
        if (update != 1)
            throw new RuntimeException("insert 실패");
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update(
                "update customers set name = :name, email = :email, last_login_at = :lastLoginAt where customer_id = UUID_TO_BIN(:customerId)",
                toParamMap(customer)
        );
        if (update != 1)
            throw new RuntimeException("update 실패");
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);
    }


    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from customers where customer_id = UUID_TO_BIN(:customerId)",
                            Collections.singletonMap("customerId", customerId.toString().getBytes()),
                            customerRowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty Result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from customers where name = :name",
                            Collections.singletonMap("name", name),
                            customerRowMapper
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
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from customers where email = :email",
                            Collections.singletonMap("email", email),
                            customerRowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty Result", e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(
                "delete from customers",
                Collections.emptyMap()
        );
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject(
                "select count(*) from customers",
                Collections.emptyMap(),
                Integer.class
        );
    }


    // inner method
    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }


//    // 트랜잭션 테스트트
//   public void testTransaction(Customer customer){
//        // dd
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
//                jdbcTemplate.update(
//                        "update customers set name = :name where customer_id = UUID_TO_BIN(:customer_id)",
//                        toParamMap(customer));
//                jdbcTemplate.update(
//                        "update customers set email = :email where customer_id = UUID_TO_BIN(:customer_id)",
//                        toParamMap(customer));
//            }
//        });
//    }

}
