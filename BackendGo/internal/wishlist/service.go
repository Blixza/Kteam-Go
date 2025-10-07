package wishlist

import (
	"context"
	pb "kteam/api/proto"

	"gorm.io/gorm"
)

type Service struct {
	pb.UnimplementedWishlistServiceServer
	repo *Repository
}

func NewWishlistService(db *gorm.DB) *Service {
	return &Service{repo: NewRepository(db)}
}

func (s *Service) GetUserWishlist(ctx context.Context, req *pb.GetUserWishlistRequest) (*pb.GetUserWishlistResponse, error) {
	wishlist, err := s.repo.FindByUserId(req.Id)
	if err != nil {
		return nil, err
	}

	return &pb.GetUserWishlistResponse{GameId: wishlist}, nil
}

func (s *Service) AddToWishlist(ctx context.Context, req *pb.AddToWishlistRequest) (*pb.AddToWishlistResponse, error) {
	wishlist, err := s.repo.Create(req.UserId, req.GameId)
	if err != nil {
		return nil, err
	}

	return &pb.AddToWishlistResponse{
		Wishlist: &pb.Wishlist{
			Id: uint32(wishlist.ID),
			UserId: uint32(wishlist.UserID),
			GameId: uint32(wishlist.GameID),
		},
	}, nil
}

func (s *Service) RemoveFromWishlist(ctx context.Context, req *pb.RemoveFromWishlistRequest) (*pb.RemoveFromWishlistResponse, error) {
	wishlist, err := s.repo.Delete(req.UserId, req.GameId)
	if err != nil {
		return nil, err
	}

	return &pb.RemoveFromWishlistResponse{
		Wishlist: &pb.Wishlist{
			Id:     uint32(wishlist.ID),
			UserId: uint32(wishlist.UserID),
			GameId: uint32(wishlist.GameID),
		},
	}, nil
}