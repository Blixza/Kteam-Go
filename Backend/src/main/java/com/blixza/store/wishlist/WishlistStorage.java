package com.blixza.store.wishlist;

import com.blixza.store.game.GameStorage;
import com.blixza.store.user.UserStorage;
import jakarta.persistence.*;

@Entity
@Table(name = "wishlist")
public class WishlistStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserStorage user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameStorage game;

    public WishlistStorage() {}

    public WishlistStorage(Integer id, UserStorage user, GameStorage game) {
        this.id = id;
        this.user = user;
        this.game = game;
    }

    public GameStorage getGame() {
        return game;
    }

    public void setGame(GameStorage game) {
        this.game = game;
    }

    public UserStorage getUser() {
        return user;
    }

    public void setUser(UserStorage user) {
        this.user = user;
    }
}
