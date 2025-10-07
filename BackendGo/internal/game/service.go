package game

import (
	"context"
	pb "kteam/api/proto"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"gorm.io/gorm"
)


type Service struct {
	pb.UnimplementedGameServiceServer
	repo *Repository
}

func NewGameService(db *gorm.DB) *Service {
	return &Service{repo: NewRepository(db)}
}

func (s *Service) GetGames(ctx context.Context, req *pb.GetGamesRequest) (*pb.GetGamesResponse, error) {
	games, err := s.repo.FindAll()
	if err != nil {
		return nil, err
	}

	resp := &pb.GetGamesResponse{}
	for _, g := range games {
		resp.Games = append(resp.Games, &pb.Game{
			Id: uint32(g.ID),
			Name: g.Name,
			Description: g.Description,
			Price: g.Price,
			Creator: g.Creator,
			Icon: g.Icon,
		})
	}
	return resp, nil
}

func (s *Service) CreateGame(ctx context.Context, req *pb.CreateGameRequest) (*pb.CreateGameResponse, error) {
	if req.Creator == "" || req.Name == "" {
		return nil, status.Errorf(codes.InvalidArgument, "all fields are required")
	}

	game := &Game{
		Name: req.Name,
		Creator: req.Creator,
		Price: req.Price,
	}

	if req.Description != nil {
		game.Description = *req.Description
	}

	if req.Icon != nil {
		game.Icon = *req.Icon
	}

	created, err := s.repo.Create(game)
	if err != nil {
		return nil, err
	}

	return &pb.CreateGameResponse{Game: created.ToProto()}, nil
}

func (s *Service) DeleteGame(ctx context.Context, req *pb.DeleteGameRequest) (*pb.DeleteGameResponse, error) {
	deleted, err := s.repo.Delete(req.GameId)
	if err != nil {
		return nil, err
	}

	return &pb.DeleteGameResponse{Game: deleted.ToProto()}, nil
}