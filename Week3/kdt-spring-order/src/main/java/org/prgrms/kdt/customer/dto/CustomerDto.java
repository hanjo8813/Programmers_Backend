package org.prgrms.kdt.customer.dto;

import org.prgrms.kdt.customer.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerDto(
        UUID customerId,
        String name,
        String email,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt
) {

    // customer -> dto
    static CustomerDto of(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt(),
                customer.getCreatedAt()
        );
    }

    // dto -> customer
    // 생성의 책임이 service냐 dto냐?
    static Customer to(CustomerDto dto) {
        return new Customer(
                dto.customerId(),
                dto.name(),
                dto.email(),
                dto.lastLoginAt(),
                dto.createdAt()
        );
    }
}
