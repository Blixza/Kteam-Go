package server

import (
	"context"
	"net/http"

	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	"github.com/rs/cors"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"

	pb "kteam/api/proto"
)

func RunGateway(grpcAddr, httpAddr string) error {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithTransportCredentials(insecure.NewCredentials())}

	if err := pb.RegisterUserServiceHandlerFromEndpoint(ctx, mux, grpcAddr, opts); err != nil {
		return err
	}

	if err := pb.RegisterGameServiceHandlerFromEndpoint(ctx, mux, grpcAddr, opts); err != nil {
		return err
	}

	if err := pb.RegisterWishlistServiceHandlerFromEndpoint(ctx, mux, grpcAddr, opts); err != nil {
		return err
	}

	if err := pb.RegisterCartServiceHandlerFromEndpoint(ctx, mux, grpcAddr, opts); err != nil {
		return err
	}

	handler := cors.New(cors.Options{
		AllowedOrigins: []string{"http://localhost:5173"},
		AllowedMethods: []string{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"},
		AllowedHeaders: []string{"*"},
		AllowCredentials: true,
	}).Handler(mux)

	return http.ListenAndServe(httpAddr, handler)
}