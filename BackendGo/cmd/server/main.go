package main

import (
	"log"

	pb "kteam/api/proto"
	"kteam/internal/db"
	"kteam/internal/server"
	"kteam/internal/user"
)

func main() {
	database := db.Connect()

	s := server.NewGRPCServer()

	userService := user.NewUserService(database)
	pb.RegisterUserServiceServer(s.Server, userService)

	log.Println("Server started on :50051")
	go s.Start(":50051")

	log.Println("Gateway started on :8080")
	if err := server.RunGateway(":50051", ":8080"); err != nil {
		log.Fatal(err)
	}
}
