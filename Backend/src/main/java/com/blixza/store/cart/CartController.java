package com.blixza.store.cart;

import com.blixza.store.repo.CartRepo;
import com.blixza.store.repo.WishlistRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepo cRepo;

    @GetMapping
    public List<CartStorage> getCart() throws SQLException {
        return cRepo.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartStorage request) throws SQLException {
        cRepo.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/remove")
    @Transactional
    public ResponseEntity<Void> removeFromCart(@RequestBody Map<String, Integer> request) throws SQLException {
        cRepo.deleteByUserIdAndGameId(request.get("userId"), request.get("gameId"));
        return ResponseEntity.noContent().build();
    }
}
