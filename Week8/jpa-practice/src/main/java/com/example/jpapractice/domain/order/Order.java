package com.example.jpapractice.domain.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    @Id
    @Column(name = "id")
    private String uuid;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDatetime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Lob
    private String memo;

    // fk : member
    // 해당 필드는 ignore
    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    // 주문 기준 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // order테이블의 member_id라는 컬럼을 생성 -> member 테이블의 id 컬럼을 받아서 FK 지정
    // 생략해도 자동으로 찾아서 설정된다 -> 하지만 가시성을 위해 명시
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();




    // 한쪽이 바뀔때 반대쪽도 바꿔줘야함
    public void setMember(Member member) {
        if (Objects.nonNull(this.member)) {
            this.member.getOrders().remove(this);
        }
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
    }

}