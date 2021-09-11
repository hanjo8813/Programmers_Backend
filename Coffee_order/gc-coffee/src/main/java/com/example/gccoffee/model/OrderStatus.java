package com.example.gccoffee.model;

public enum OrderStatus {
    // 주문완료 -> 결제확인 -> 배달준비 -> 배달중 -> 배달완료
    ACCEPTED,
    PAYMENT_CONFIRMED,
    READY_FOR_DELIVERY,
    SHIPPED,
    SETTLED,
    CANCELED
}
