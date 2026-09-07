package com.devjsmh.icea.content_manager_service.home;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devjsmh.icea.content_manager_service.products.ProductEntity;
import com.devjsmh.icea.content_manager_service.products.ProductService;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // this corresponds to the file name: src/main/resources/templates/index.html
        List<ProductEntity> productLIst = this.productService.getAllV1();

        model.addAttribute("products", productLIst);
        return "index";
    }

    @GetMapping("/product-details")
    public String details(Model model, @RequestParam("id") Long productId) {

        ProductEntity product = this.productService.getByIdV1(productId);

        model.addAttribute("product", product);
        return "views/product-details";
    }
}
