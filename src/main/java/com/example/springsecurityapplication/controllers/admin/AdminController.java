package com.example.springsecurityapplication.controllers.admin;

import com.example.springsecurityapplication.models.Image;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN') and /*or*/ hasRole('ROLE_USER')\")
public class AdminController {

    @Value("${upload.path}")
    private String uploadPath;

    private final PersonService personService;

    private final PersonRepository personRepository;
    private final OrderRepository orderRepository;

    private final OrderService orderService;

    private final ProductService productService;

    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminController(PersonService personService, PersonRepository personRepository, OrderRepository orderRepository, OrderService orderService, ProductService productService, CategoryRepository categoryRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN') and /*or*/ hasRole('ROLE_USER')\")
    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "admin/admin";
    }

    //http://localhost:8080/admin/product/add
    //Метод по отображению страницы с возможностью добавления товаров
    @GetMapping("product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    //Метод по добавлению продукта в БД через сервис -> репозиторий
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three) throws IOException {
        if(bindingResult.hasErrors())
        {
            return "product/addProduct";
        }

        if(file_one != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);;
        }

        if(file_two != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);;
        }
        if(file_three != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);;
        }
        productService.saveProduct(product);
        return "redirect:/admin/admin";
    }

    //Метод по удалению товаров
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin/admin";
    }

    //Метод по отображению страницы с возможностью редактирования товаров
    @GetMapping("/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors())
        {
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin/admin";
    }

    @GetMapping("ordersAdmin")
    public String ordersUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        return "/orders/ordersAdmin";
    }

    @GetMapping("/orders/editOrder/{id}")
    public String editOrder(Model model, @PathVariable("id") int id){
        model.addAttribute("order", orderService.getOrderId(id));
        return "orders/editOrder";
    }
    @PostMapping("/orders/editOrder/{id}")
    public String editOrder(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors())
        {
            return "orders/editOrder";
        }
        orderService.updateOrderStatus(id, order);
        return "redirect:/admin/admin";
    }

    @PostMapping("ordersAdmin/search")
    public String productSearch(@RequestParam("search") String search, Model model){
        model.addAttribute("search_order", orderRepository.findByLastFourSymbols(search));
        model.addAttribute("orders",orderService.getAllOrder());
        return "/orders/ordersAdmin";
    }

    @GetMapping("/users")
    public String getAllPerson(Model model){
        model.addAttribute("person", personService.getAllPerson());
        return "admin/users";
    }

    //Метод по отображению страницы с возможностью редактирования товаров
    @GetMapping("/editUser/{id}")
    public String editPerson(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.getPersonId(id));
        model.addAttribute("role", personRepository.findAll());
        return "admin/editUser";
    }

    @PostMapping("/editUser/{id}")
    public String editPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors())
        {
            return "admin/users";
        }
        personService.updatePerson(id, person);
        return "redirect:/admin/admin";
    }

}
