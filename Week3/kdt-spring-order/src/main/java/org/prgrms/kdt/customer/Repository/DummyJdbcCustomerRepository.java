package org.prgrms.kdt.customer.Repository;


import org.prgrms.kdt.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DummyJdbcCustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(DummyJdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "select * from customers where name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "insert into customers(customer_id, name, email) values (UUID_TO_BIN(?), ?, ?)";
    private final String UPDATE_BY_ID_SQL = "update customers set name = ? where customer_id = UUID_TO_BIN(?)";
    private final String DELETE_ALL_SQL = "delete from customers";


    public List<String> findNames(String name) {
        List<String> names = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        ) {
            statement.setString(1, name);
            logger.info("statement -> {}", statement);

            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {} , name -> {} , createdAt -> {}", customerId, customerName, createdAt);
                    names.add(customerName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("sql 수행중 오류 발생", e);
        }

        return names;
    }


    public List<String> findAllName() {
        List<String> names = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                //logger.info("customer id -> {} , name -> {} , createdAt -> {}", customerId, customerName, createdAt);
                names.add(customerName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("sql 수행중 오류 발생", e);
        }
        return names;
    }


    public List<UUID> findAllId() {
        List<UUID> uuids = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                var customerId = toUUID(resultSet.getBytes("customer_id"));
                uuids.add(customerId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("sql 수행중 오류 발생", e);
        }
        return uuids;
    }


    public int insertCustomer(UUID customerId, String name, String email) {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
                var statement = connection.prepareStatement(INSERT_SQL);
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("sql 수행중 오류 발생", e);
        }
        return 0;
    }


    public int updateCustomerName(UUID customerId, String name) {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
                var statement = connection.prepareStatement(UPDATE_BY_ID_SQL);
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("sql 수행중 오류 발생", e);
        }
        return 0;
    }


    public int deleteAllCustomer() {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
                var statement = connection.prepareStatement(DELETE_ALL_SQL);
        ) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("sql 수행중 오류 발생", e);
        }
        return 0;
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }


    public void transactionTest(Customer customer) {
        String updateNameSql = "update customers set name = ? where customer_id = UUID_TO_BIN(?)";
        String updateEmailSql = "update customers set email = ? where customer_id = UUID_TO_BIN(?)";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234");
            try (
                    var updateNameStatement = connection.prepareStatement(updateNameSql);
                    var updateEmailStatement = connection.prepareStatement(updateEmailSql);
            ) {
                // 오토 커밋을 풀어서 커밋 전까지는 하나의 트랜잭션으로 묶이게 만든다
                connection.setAutoCommit(false);
                updateNameStatement.setString(1, customer.getName());
                updateNameStatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateNameStatement.executeUpdate();

                updateEmailStatement.setString(1, customer.getEmail());
                updateEmailStatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateEmailStatement.executeUpdate();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("{}", ex);
                }
            }
            logger.error("{}", e);
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws SQLException {
        var customerRepository = new DummyJdbcCustomerRepository();

        //797eee6a-2741-4d03-a2e8-9f87fb0d95d1
        customerRepository.transactionTest(
                new Customer(
                        UUID.fromString("797eee6a-2741-4d03-a2e8-9f87fb0d95d1"),
                        "update-user",
                        "new-user2@mail.com",
                        LocalDateTime.now()
                )
        );


//        // 데이터 삭제
//        customerRepository.deleteAllCustomer();
//
//        // 데이터 삽입
//        var newCustomerId = UUID.randomUUID();
//        logger.info("새로 삽입되는 uuid -> {}", newCustomerId);
//        customerRepository.insertCustomer(newCustomerId, "new-user1", "new-user1@mail.com");
//        customerRepository.insertCustomer(newCustomerId, "new-user2", "new-user2@mail.com");

//
//
//        customerRepository.findAllId().forEach(v -> logger.info("uuid -> {}", v));


//        // 데이터 수정
//        customerRepository.updateCustomerName(newCustomerId, "new-user1-updated");
//
//        // 테이블의 모든 데이터 조회
//        customerRepository.findAllName();

        // 입력한 이름 데이터 조회
//        var names = customerRepository.findNames("tester01' or 'a'='a");
//        names.forEach(v -> logger.info("Found name : {}", v));
    }
}
