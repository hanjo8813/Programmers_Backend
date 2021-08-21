package org.prgrms.kdt.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// 베이스가 되는 클래스 이름으로도 지정 가능
//@ComponentScan(basePackageClasses = {Order.class, Voucher.class})

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
// @PropertySource("application.properties")

@Configuration
@ComponentScan(basePackages = {"org.prgrms.kdt.order", "org.prgrms.kdt.voucher", "org.prgrms.kdt.config"})
@PropertySource(value = {"application.yaml"}, factory = YamlPropertiesFactory.class)
@EnableConfigurationProperties
public class AppConfiguration {
}
    /*
    @Bean(initMethod = "init")
    public BeanOne beanOne() {
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
    */

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

