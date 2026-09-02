package com.devjsmh.icea.content_manager_service.home;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devjsmh.icea.content_manager_service.products.Product;
import com.devjsmh.icea.content_manager_service.products.ProductImage;

@Controller
public class HomeController {

    private List<Product> products = new ArrayList<>();

    public HomeController() {

        /** Image list for arduino * */
        List<ProductImage> p1Images = new ArrayList<>();

        ProductImage p1Image1 = new ProductImage();
        p1Image1.setUrl("./../assets/images/arduino-r4-wifi-2.webp");

        ProductImage p1Image2 = new ProductImage();
        p1Image2.setUrl("./../assets/images/arduino-r4-wifi-3.webp");

        ProductImage p1Image3 = new ProductImage();
        p1Image3.setUrl("./../assets/images/arduino-r4-wifi-4.webp");

        ProductImage p1Image4 = new ProductImage();
        p1Image4.setUrl("./../assets/images/arduino-r4-wifi-1.webp");

        p1Images.add(p1Image1);
        p1Images.add(p1Image2);
        p1Images.add(p1Image3);
        p1Images.add(p1Image4);

        /** Image list for arduino * */
        Product p1 = new Product("Arduboard Uno R4 WiFi (compatible)", 114.000, "001326");
        p1.setId(1L);
        p1.setImage_url("./../assets/images/arduino-r4-wifi-1.webp");
        p1.setImage_alt("arduino-r4-wifi");
        p1.setImages(p1Images);

        /** --------------- Image list for bicicle --------------- */
        List<ProductImage> p2Images = new ArrayList<>();

        ProductImage p2Image1 = new ProductImage();
        p2Image1.setUrl("./../assets/images/bicicleta-roadmaster-1.webp");

        ProductImage p2Image2 = new ProductImage();
        p2Image2.setUrl("./../assets/images/bicicleta-roadmaster-2.webp");

        ProductImage p2Image3 = new ProductImage();
        p2Image3.setUrl("./../assets/images/bicicleta-roadmaster-3.webp");

        ProductImage p2Image4 = new ProductImage();
        p2Image3.setUrl("./../assets/images/bicicleta-roadmaster-4.webp");

        p2Images.add(p2Image1);
        p2Images.add(p2Image2);
        p2Images.add(p2Image3);
        p2Images.add(p2Image4);

        /** --------------- Image list for bicicle --------------- */
        Product p2 = new Product("Bicicleta Roadmaster", 156.990, "0032449");
        p2.setId(2L);
        p2.setImage_url("./../assets/images/bicicleta-roadmaster-1.webp");
        p2.setImage_alt("bicicleta-roadmaster");
        p2.setImages(p2Images);

        /** --------------- Image list for cable jumpers --------------- */
        List<ProductImage> p3Images = new ArrayList<>();

        ProductImage p3Image1 = new ProductImage();
        p3Image1.setUrl("./../assets/images/cables-jumper-1.webp");
        p3Image1.setAltText("Cables Jumper X 20");

        ProductImage p3Image2 = new ProductImage();
        p3Image2.setUrl("./../assets/images/cables-jumper-2.webp");
        p3Image2.setAltText("Cables Jumper X 20");

        p3Images.add(p3Image1);
        p3Images.add(p3Image2);

        /** --------------- Image list for cable jumpers --------------- */
        Product p3 = new Product("Cables Jumper X 20", 25.000, "001036");
        p3.setId(3L);
        p3.setImage_url("./../assets/images/cables-jumper-1.webp");
        p3.setImage_alt("cables-jumper");
        p3.setImages(p3Images);

        /** --------------- Image list for multimeter --------------- */
        List<ProductImage> p4Images = new ArrayList<>();

        ProductImage p4Image1 = new ProductImage();
        p4Image1.setUrl("./../assets/images/multimetro-1.webp");
        p4Image1.setAltText("Multimetro Digital");

        ProductImage p4Image2 = new ProductImage();
        p4Image2.setUrl("./../assets/images/multimetro-2.webp");
        p4Image2.setAltText("Multimetro Digital");

        ProductImage p4Image3 = new ProductImage();
        p4Image3.setUrl("./../assets/images/multimetro-3.webp");
        p4Image3.setAltText("Multimetro Digital");

        ProductImage p4Image4 = new ProductImage();
        p4Image4.setUrl("./../assets/images/multimetro-4.webp");
        p4Image4.setAltText("Multimetro Digital");

        p4Images.add(p4Image1);
        p4Images.add(p4Image2);
        p4Images.add(p4Image3);
        p4Images.add(p4Image4);

        Product p4 = new Product("Multimetro Digital", 45.000, "0934838");
        p4.setId(4L);
        p4.setImage_url("./../assets/images/multimetro-1.webp");
        p4.setImage_alt("multimetro-digital");
        p4.setImages(p4Images);

        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);
    }

    @GetMapping("/")
    public String home(Model model) {
        // this corresponds to the file name: src/main/resources/templates/index.html

        model.addAttribute("products", this.products);
        return "index";
    }

    @GetMapping("/product-details")
    public String details(Model model, @RequestParam("id") Long productId) {

        Product product = this.products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .get();

        model.addAttribute("product", product);
        return "views/product-details";
    }
}
