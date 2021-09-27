package com.example.practice.repository.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "customers")  // Table 어노테이션 생략시 클래스명과 매핑
public class CustomerEntity {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    // jpa 객체는 default 생성자만을 사용한다
}
