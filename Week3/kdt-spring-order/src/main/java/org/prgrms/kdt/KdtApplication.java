package org.prgrms.kdt;

import org.prgrms.kdt.order.properties.OrderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgrms.kdt.order", "org.prgrms.kdt.voucher"})
public class KdtApplication {

	private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

	public static void main(String[] args) {

//		var springApplication = new SpringApplication(KdtApplication.class);
//		springApplication.setAdditionalProfiles("local");
//		var applicationContext = springApplication.run(args);

		var applicationContext = SpringApplication.run(KdtApplication.class, args);

		var orderProperties = applicationContext.getBean(OrderProperties.class);
		logger.warn("logger name => {}", logger.getName());
		logger.warn("version -> {}", orderProperties.getVersion());
		logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
		logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());
		logger.warn("description -> {}", orderProperties.getDescription());
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
