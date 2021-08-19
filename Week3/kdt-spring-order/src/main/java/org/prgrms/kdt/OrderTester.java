package org.prgrms.kdt;

import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        // 사용자 ID 생성
        var customerId = UUID.randomUUID();

        // App-context에 등록
        var applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);

        // Voucher 객체 생성
        //var voucherRepository = applicationContext.getBean(VoucherRepository.class);
        var voucherRepository = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class, "memory");


        var voucher = voucherRepository.insert(
                new FixedAmountVoucher(UUID.randomUUID(), 10L)
        );

        // Order 객체 생성
        var orderService = applicationContext.getBean(OrderService.class);
        var order = orderService.createOrder(
                customerId,
                new ArrayList<OrderItem>(){{
                    add(new OrderItem(UUID.randomUUID(), 100L, 1));
                }},
                voucher.getVoucherId()
        );

        // 거짓이라면 오류메시지 출력
        Assert.isTrue(
                order.totalAmount() == 90L,
                MessageFormat.format("totalAmount{0} is not 90L", order.totalAmount())
        );

        applicationContext.close();
    }
}
