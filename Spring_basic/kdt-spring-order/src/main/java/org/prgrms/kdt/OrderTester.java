package org.prgrms.kdt;

import org.prgrms.kdt.config.AppConfiguration;
import org.prgrms.kdt.order.properties.OrderProperties;
import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {

    // 로거는 하나만 사용함
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) throws IOException {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        // App-context에 등록
        var applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);
//        var applicationContext = new AnnotationConfigApplicationContext();
//        applicationContext.register(AppConfiguration.class);
//        var environment = applicationContext.getEnvironment();
//        environment.setActiveProfiles("dev");
//        applicationContext.refresh();

//        var version = environment.getProperty("kdt.version");
//        var minimumOrderAmount = environment.getProperty("kdt.minimum-order-amount", Integer.class);
//        var supportVendors = environment.getProperty("kdt.support-vendors", List.class);
//        var description = environment.getProperty("kdt.description", List.class);

        // 프로퍼티 바인딩한 클래스 받아오기
        var orderProperties = applicationContext.getBean(OrderProperties.class);
        logger.warn("logger name => {}", logger.getName());
        logger.warn("version -> {}", orderProperties.getVersion());
        logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());
        logger.warn("description -> {}", orderProperties.getDescription());

        // 리소스
        //var resource = applicationContext.getResource("application.yaml");
        var resource2 = applicationContext.getResource("file:sample.txt");
        System.out.println(MessageFormat.format("Resource -> {0}", resource2.getClass().getCanonicalName()));
        var strings = Files.readAllLines(resource2.getFile().toPath());
        System.out.println(strings.stream().reduce("", (a,b)-> a + "\n" + b));

//        var resource3 = applicationContext.getResource("https://stackoverflow.com/");
//        var readableByteChannel = Channels.newChannel(resource3.getURL().openStream());
//        var bufferedReader = new BufferedReader(Channels.newReader(readableByteChannel, StandardCharsets.UTF_8));
//        var contents = bufferedReader.lines().collect(Collectors.joining("\n"));
//        System.out.println(contents);
//        System.out.println(MessageFormat.format("Resource -> {0}", resource3.getClass().getCanonicalName()));



        // 사용자 ID 생성
        var customerId = UUID.randomUUID();

        // Voucher 객체 생성
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
        //var voucherRepository = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class, "memory");
        var voucher = voucherRepository.insert(
                new FixedAmountVoucher(UUID.randomUUID(), 10L)
        );

//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0} ", voucherRepository instanceof JdbcVoucherRepository));
//        System.out.println(MessageFormat.format("is Jdbc Repo -> {0} ", voucherRepository.getClass().getCanonicalName()));

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
