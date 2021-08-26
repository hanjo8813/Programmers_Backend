package org.prgrms.kdt.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.kdt.order.repository.MemoryOrderRepository;
import org.prgrms.kdt.order.repository.OrderRepository;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.VoucherService;
import org.prgrms.kdt.voucher.repository.MemoryVoucherRepository;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class OrderServiceTest {

    // stub용 구현체
    class OrderRepositoryStub implements OrderRepository{
        @Override
        public Order insert(Order order) {
            return null;
        }
    }

    @Test
    @DisplayName("오더가 생성되어야 한다 - Stub")
    void createOrder() {

        // Given - stub을 생성해주자
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        var sut = new OrderService(new VoucherService(voucherRepository), new OrderRepositoryStub());

        // When
        var order = sut.createOrder(
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(),200,1)),
                fixedAmountVoucher.getVoucherId()
        );

        // Then - 상태 관점
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    @DisplayName("오더가 생성되어야 한다 - Mock")
    void createOrderByMock() {

        // Given
        var voucherServiceMock = mock(VoucherService.class);
        var orderRepositoryMock = mock(OrderRepository.class);
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        // voucherService의 getVoucher 메소드의 리턴값을 임시로 정의해준다
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId()))
                .thenReturn(fixedAmountVoucher);
        var sut = new OrderService(voucherServiceMock,orderRepositoryMock);

        // When - 테스트하고 싶은 행위를 실행
        var order = sut.createOrder(
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(),200,1)),
                fixedAmountVoucher.getVoucherId()
        );

        // Then - 목적 행위와 관련된 행위가 잘 실행되었는지 본다
//        assertThat(order.totalAmount(), is(100L));
//        assertThat(order.getVoucher().isEmpty(), is(false));

        // 테스트할 메소드의 순서를 정의 (순서에 사용되는 Mock을 넣어줌)
       var inOrder = inOrder(voucherServiceMock, orderRepositoryMock);

        // 정의한 행위에서 메소드가 호출되었는지 여부를 검사한다
        inOrder.verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());
        inOrder.verify(orderRepositoryMock).insert(order);
        inOrder.verify(voucherServiceMock).useVoucher(fixedAmountVoucher);

        //verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());

    }

}