package org.prgrms.kdt.customer;

import org.prgrms.kdt.customer.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String findCustomers(Model model) {
        var allCustomers = customerService.getAllCustomers();
        model.addAttribute("testString", "dddddd");
        model.addAttribute("customers", allCustomers);
        return "views/customers";
    }


    // '/customers'로 'GET' 요청오면 데이터(모델)를 반환하는데..
    // 데이터에 view 이름을 붙여서 반환해줌 -> 해당 view 파일에 데이터 바인딩? 하는건가
//    @RequestMapping(value = "/customers", method = RequestMethod.GET)
//    public ModelAndView findCustomers() {
//        var allCustomers = customerService.getAllCustomers();
//        return new ModelAndView(
//                "customers",
//                Map.of("testString", "dddddd",
//                        "customers", allCustomers
//                )
//        );
//    }
}
