package com.example.mini_E_Commerce.Repo;

import com.example.mini_E_Commerce.Model.Cart;
import com.example.mini_E_Commerce.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);
}
