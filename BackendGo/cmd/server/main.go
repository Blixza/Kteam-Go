package main

import (
	"log"

	pb "kteam/api/proto"
	"kteam/internal/cart"
	"kteam/internal/db"
	"kteam/internal/game"
	"kteam/internal/server"
	"kteam/internal/user"
	"kteam/internal/wishlist"
)

func main() {
	database := db.Connect()

	s := server.NewGRPCServer()

	userService := user.NewUserService(database)
	gameService := game.NewGameService(database)
	wishlistService := wishlist.NewWishlistService(database)
	cartService := cart.NewCartService(database)
	
	pb.RegisterUserServiceServer(s.Server, userService)
	pb.RegisterGameServiceServer(s.Server, gameService)
	pb.RegisterWishlistServiceServer(s.Server, wishlistService)
	pb.RegisterCartServiceServer(s.Server, cartService)

	log.Println("Server started on :50051")
	go s.Start(":50051")

	log.Println("Gateway started on :8080")
	if err := server.RunGateway(":50051", ":8080"); err != nil {
		log.Fatal(err)
	}
}
