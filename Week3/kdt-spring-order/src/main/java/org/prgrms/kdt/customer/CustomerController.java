package org.prgrms.kdt.customer;

import org.prgrms.kdt.customer.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CustomerController {

    private final CustomerService customerService;

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
    public String addNewCustomer(CreateCustomerRequest createCustomerRequest){
        // Request를 받고 변수에 저장해주는 record -> DTO
        customerService.createCustomer(createCustomerRequest.email(), createCustomerRequest.name());
        return "redirect:/customers";
    }
    // post request -> dto -> controller -> service -> repository


    // url에 데이터 담은거 빼려면 @PathVariable로 매핑해야됨됨
    @GetMapping("/customers/{customerId}")
    public String findCustomer(@PathVariable("customerId") UUID customerId, Model model){
        var maybeCustomer = customerService.getCustomer(customerId);
        if(maybeCustomer.isPresent()){
            model.addAttribute("customer", maybeCustomer.get());
            return "views/customer-details";
        }
        else{
            return "views/404";
        }
    }

//    --------------------------------------------------------------------------------------------


    @GetMapping("/api/v1/customers")
    @ResponseBody
    public List<Customer> findCustomers() {
        return customerService.getAllCustomers();
    }



}
