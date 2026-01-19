package com.example.mini_E_Commerce.Controller;

import com.example.mini_E_Commerce.Model.*;
import com.example.mini_E_Commerce.Repo.CartRepo;
import com.example.mini_E_Commerce.Repo.OrderRepo;
import com.example.mini_E_Commerce.Repo.ProductRepo;
import com.example.mini_E_Commerce.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartRepo cartRepo;
    private final OrderRepo orderRepo;

    @GetMapping("/products")
    public List<Product> products() {
        return productRepo.findAll();
    }

    @PostMapping("/cart/{productId}")
    public Cart addToCart(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable Long productId) {

        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return c;
                });

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);

        cart.getItems().add(item);
        return cartRepo.save(cart);
    }

    @PostMapping("/order")
    public Order placeOrder(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow();
        Cart cart = cartRepo.findByUser(user).orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setItems(cart.getItems());
        order.setTotalAmount(
                cart.getItems().stream()
                        .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                        .sum()
        );
        order.setStatus("PLACED");

        cart.getItems().clear();
        cartRepo.save(cart);

        return orderRepo.save(order);
    }
}

