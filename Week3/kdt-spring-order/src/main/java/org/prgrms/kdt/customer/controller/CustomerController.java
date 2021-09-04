package org.prgrms.kdt.customer.controller;

import org.prgrms.kdt.customer.model.Customer;
import org.prgrms.kdt.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
//@CrossOrigin(origins="*", methods = RequestMethod.GET)
public class CustomerController {

    private final CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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


    @GetMapping("/customers")
    public String viewCustomersPage(Model model) {
        var allCustomers = customerService.getAllCustomers();
        model.addAttribute("testString", "dddddd");
        model.addAttribute("customers", allCustomers);
        return "views/customers";
    }


    @GetMapping("/customers/new")
    public String viewNewCustomerPage() {
        return "views/new-customers";
    }


    @PostMapping("/customers/new")
    public String addNewCustomer(CreateCustomerRequest createCustomerRequest) {
        // Request를 받고 변수에 저장해주는 record -> DTO
        customerService.createCustomer(createCustomerRequest.email(), createCustomerRequest.name());
        return "redirect:/customers";
    }
    // post request -> dto -> controller -> service -> repository


    // url에 데이터 담은거 빼려면 @PathVariable로 매핑해야됨됨
    @GetMapping("/customers/{customerId}")
    public String findCustomer(@PathVariable("customerId") UUID customerId, Model model) {
        var maybeCustomer = customerService.getCustomer(customerId);
        if (maybeCustomer.isPresent()) {
            model.addAttribute("customer", maybeCustomer.get());
            return "views/customer-details";
        } else {
            return "views/404";
        }
    }

    //--------------------------------------------------------------------------------------------

    @PostMapping("/api/test")
    @ResponseBody
    public String test() {
        logger.info("POST 요청 잘 받았다 z");
        return "Hello React";
    }

    //--------------------------------------------------------------------------------------------


    @GetMapping("/api/v1/customers")
    @ResponseBody
    public List<Customer> findCustomers() {
        return customerService.getAllCustomers();
    }

    @CrossOrigin(origins="*")
    @GetMapping("/api/v1/customers/{customerId}")
    @ResponseBody
    public ResponseEntity<Customer> findCustomer(@PathVariable("customerId") UUID customerId) {
        var customer = customerService.getCustomer(customerId);
        // Optional -> 값이 존재할때 ok를 보냄 / 없다면 404
        return customer.map(v -> ResponseEntity.ok(v))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/v1/customers/{customerId}")
    @ResponseBody
    public CustomerDto saveCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDto customerDto) {
        logger.info("Got customer save requset {}", customerDto);
        return customerDto;
    }


}
