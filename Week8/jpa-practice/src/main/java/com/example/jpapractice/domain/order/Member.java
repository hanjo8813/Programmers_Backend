package com.example.jpapractice.domain.order;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "nick_name", nullable = false, length = 30, unique = true)
    private String nickName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    // 연관관계의 주인은 Order 엔티티이다. -> FK의 주인은 Order이므로 (Order에서만 member 객체를 가짐)
    // 따라서 Member 엔티티에는 Order 엔티티가 가지는 member라는 객체를 매핑시켜줘야한다
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();



    //
    public void addOrder(Order order) {
        order.setMember(this);
    }

}