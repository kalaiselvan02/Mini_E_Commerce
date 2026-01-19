package com.example.mini_E_Commerce.Controller;

import com.example.mini_E_Commerce.Model.Product;
import com.example.mini_E_Commerce.Repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepo productRepo;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product addProduct(
            @RequestParam String name,
            @RequestParam MultipartFile image
    ) throws IOException {

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();


        File file = new File(UPLOAD_DIR + fileName);
        image.transferTo(file);


        Product product = new Product();
        product.setName(name);
        product.setImageUrl("/uploads/" + fileName);

        return productRepo.save(product);
    }
}
