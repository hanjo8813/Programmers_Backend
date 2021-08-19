package org.prgrms.kdt;

import org.prgrms.kdt.order.Order;
import org.prgrms.kdt.voucher.Voucher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// 서비스 간 의존관계 맺기를 담당

@Configuration
//@ComponentScan(basePackages = {"org.prgrms.kdt.order", "org.prgrms.kdt.voucher"})
// 베이스가 되는 클래스 이름으로도 지정 가능
 @ComponentScan(basePackageClasses = {Order.class, Voucher.class})
/* 스캔시 필터링 대상 지정
@ComponentScan(
        basePackages = {"org.prgrms.kdt.order", "org.prgrms.kdt.voucher"},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = MemoryVoucherRepository.class
                )
        }
)
 */
public class AppConfiguration {

 @Bean(initMethod = "init")
 public BeanOne beanOne(){
  return new BeanOne();
 }


 class BeanOne implements InitializingBean {
  public void init() {
   System.out.println("init Called");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
   System.out.println("afterPropertiesSet Called");
  }
 }







    /*
    @Bean
    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }

            @Override
            public Voucher insert(Voucher voucher) {
                return null;
            }
        };
    }

    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository() {
            @Override
            public void insert(Order order) {

            }
        };
    }

    @Bean
    public VoucherService voucherService(VoucherRepository voucherRepository) {
        return new VoucherService(voucherRepository);
    }

    @Bean
    public OrderService orderService(VoucherService voucherService, OrderRepository orderRepository) {
        return new OrderService(voucherService, orderRepository);
    }*/
}
