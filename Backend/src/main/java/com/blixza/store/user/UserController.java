package com.blixza.store.user;

import com.blixza.store.game.GameStorage;
import com.blixza.store.repo.GameRepo;
import com.blixza.store.repo.UserRepo;
import com.blixza.store.repo.WishlistRepo;
import com.blixza.store.wishlist.WishlistStorage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepo uRepo;

    @Autowired
    private WishlistRepo wRepo;

    @Autowired
    private GameRepo gRepo;

    @GetMapping
    public List<UserStorage> getUsers() throws SQLException {
        return uRepo.findAll();
    }

    @GetMapping("/me/wishlist")
    public List<Integer> getMyWishlist() throws SQLException {
        List<WishlistStorage> wishlist = wRepo.findByUserId(1);

        return wishlist.stream()
                .map(w -> w.getGame().getId())
                .toList();
    }

    @PostMapping("/me/wishlist/add")
    public ResponseEntity<WishlistStorage> addToWishlist(@RequestBody Map<String, Integer> body) throws SQLException {
        UserStorage user = uRepo.findById(body.get("userId"))
                .orElseThrow(() -> new SQLException("User not found"));

        GameStorage game = gRepo.findById(body.get("gameId"))
                .orElseThrow(() -> new SQLException("Game not found"));

        WishlistStorage wishlist = new WishlistStorage();
        wishlist.setUser(user);
        wishlist.setGame(game);

        WishlistStorage saved = wRepo.save(wishlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/me/wishlist/remove")
    @Transactional
    public ResponseEntity<Void> removeFromWishlist(@RequestBody Map<String, Integer> body) throws SQLException {
        wRepo.deleteByUserIdAndGameId(body.get("userId"), body.get("gameId"));
        return ResponseEntity.noContent().build();
    }

}
