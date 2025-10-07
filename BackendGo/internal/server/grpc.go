package server

import (
	"log"
	"net"

	pb "kteam/api/proto"

	"google.golang.org/grpc"
)

type GRPCServer struct {
	Server *grpc.Server
}

func NewGRPCServer() *GRPCServer {
	return &GRPCServer{Server: grpc.NewServer()}
}

func (s *GRPCServer) Start(addr string) {
	lis, err := net.Listen("tcp", addr)
	if err != nil {
		log.Fatal("failed to listen:", err)
	}
	if err := s.Server.Serve(lis); err != nil {
		log.Fatal("server failed:", err)
	}

}

func (s *GRPCServer) RegisterUserService(svc pb.UserServiceServer) {
	pb.RegisterUserServiceServer(s.Server, svc)
}
