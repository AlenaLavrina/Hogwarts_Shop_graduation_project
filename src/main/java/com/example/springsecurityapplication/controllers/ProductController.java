package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping("")
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }

    @GetMapping("/info/{id}")
    public String infoUser(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "product/infoProduct";
    }

    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(value="price", required = false, defaultValue = "") String price, @RequestParam(value="contact", required = false, defaultValue = "") String contact, Model model){
        if(!from.isEmpty() & !to.isEmpty()){
            if(!price.isEmpty()){
                if(price.equals("sorted_by_ascending_price")){
                    if(!contact.isEmpty()){
                        if(contact.equals("furniture")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 1));
                        }
                        else if(contact.equals("appliances")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 2));
                        }
                        else if(contact.equals("clothes")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 3));
                        }
                    }
                }
                else if(price.equals("sorted_by_descending_price")){
                    if(!contact.isEmpty()){
                        if(contact.equals("furniture")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 1));
                        }
                        else if(contact.equals("appliances")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 2));
                        }
                        else if(contact.equals("clothes")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 3));
                        }
                    }
                }
            }
            else{
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search, Float.parseFloat(from), Float.parseFloat(to)));
            }
        }
        else {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
        }
        model.addAttribute("value_search", search);
        model.addAttribute("value_price_from", from);
        model.addAttribute("value_price_to", to);
        model.addAttribute("products",productService.getAllProduct());
        return "/product/product";
    }
}
