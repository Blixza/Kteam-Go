package cart

import (
	"context"
	pb "kteam/api/proto"

	"gorm.io/gorm"
)

type Service struct {
	pb.UnimplementedCartServiceServer
	repo *Repository
}

func NewCartService(db *gorm.DB) *Service {
	return &Service{repo: NewRepository(db)}
}

func (s *Service) GetUserCart(ctx context.Context, req *pb.GetUserCartRequest) (*pb.GetUserCartResponse, error) {
	cart, err := s.repo.FindByUserId(req.Id)
	if err != nil {
		return nil, err
	}

	var pbCart []*pb.Cart
	for _, w := range cart {
		pbCart = append(pbCart, &pb.Cart{
			Id:     w.ID,
			UserId: w.UserID,
			GameId: w.GameID,
		})
	}

	return &pb.GetUserCartResponse{Cart: pbCart}, nil
}

func (s *Service) AddToCart(ctx context.Context, req *pb.AddToCartRequest) (*pb.AddToCartResponse, error) {
	cart, err := s.repo.Create(req.UserId, req.GameId)
	if err != nil {
		return nil, err
	}

	return &pb.AddToCartResponse{
		Cart: &pb.Cart{
			Id: uint32(cart.ID),
			UserId: uint32(cart.UserID),
			GameId: uint32(cart.GameID),
		},
	}, nil
}

func (s *Service) RemoveFromCart(ctx context.Context, req *pb.RemoveFromCartRequest) (*pb.RemoveFromCartResponse, error) {
	cart, err := s.repo.Delete(req.UserId, req.GameId)
	if err != nil {
		return nil, err
	}

	return &pb.RemoveFromCartResponse{
		Cart: &pb.Cart{
			Id:     cart.ID,
			UserId: cart.UserID,
			GameId: cart.GameID,
		},
	}, nil
}