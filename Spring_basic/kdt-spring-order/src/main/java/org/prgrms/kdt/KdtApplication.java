package org.prgrms.kdt;

import org.prgrms.kdt.config.MvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
		basePackages = {"org.prgrms.kdt.customer", "org.prgrms.kdt.config"},
		includeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				value = MvcConfig.class
		),
		useDefaultFilters = false
)
public class KdtApplication {

	public static void main(String[] args){
		SpringApplication.run(KdtApplication.class, args);




//		var springApplication = new SpringApplication(KdtApplication.class);
//		springApplication.setAdditionalProfiles("dev");
//		var applicationContext = springApplication.run(args);
//
//		//var applicationContext = SpringApplication.run(KdtApplication.class, args);
//
//		var orderProperties = applicationContext.getBean(OrderProperties.class);
//		logger.warn("logger name => {}", logger.getName());
//		logger.warn("version -> {}", orderProperties.getVersion());
//		logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
//		logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());
//		logger.warn("description -> {}", orderProperties.getDescription());
//
//		var orderProperties = applicationContext.getBean(OrderProperties.class);
//		System.out.println(MessageFormat.format("version -> {0}", orderProperties.getVersion()));
//		System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", orderProperties.getMinimumOrderAmount()));
//		System.out.println(MessageFormat.format("supportVendors -> {0}", orderProperties.getSupportVendors()));
//		System.out.println(MessageFormat.format("description -> {0}", orderProperties.getDescription()));
//
//		var voucherRepository = applicationContext.getBean(VoucherRepository.class);
//		var voucher = voucherRepository.insert(
//				new FixedAmountVoucher(UUID.randomUUID(), 10L)
//		);
//
//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0} ", voucherRepository instanceof JdbcVoucherRepository));
//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0} ", voucherRepository.getClass().getCanonicalName()));

	}
}
