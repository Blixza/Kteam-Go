package user

import (
	"context"

	pb "kteam/api/proto"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"gorm.io/gorm"
)

type Service struct {
	pb.UnimplementedUserServiceServer
	repo *Repository
}

func NewUserService(db *gorm.DB) *Service {
	return &Service{repo: NewRepository(db)}
}

func (s *Service) GetUsers(ctx context.Context, req *pb.GetUsersRequest) (*pb.GetUsersResponse, error) {
	users, err := s.repo.FindAll()
	if err != nil {
		return nil, err
	}

	resp := &pb.GetUsersResponse{}
	for _, u := range users {
		resp.Users = append(resp.Users, &pb.User{
			Id:       uint32(u.ID),
			Username: u.Username,
			Nickname: u.Nickname,
			Password: u.Password,
		})
	}
	return resp, nil
}

func (s *Service) CreateUser(ctx context.Context, req *pb.CreateUserRequest) (*pb.CreateUserResponse, error) {
	if req.Nickname == "" || req.Username == "" || req.Password == "" {
		return nil, status.Errorf(codes.InvalidArgument, "all fields are required")
	}

	user := &User{
		Username: req.Username,
		Nickname: req.Nickname,
		Password: req.Password,
	}

	created, err := s.repo.Create(user)
	if err != nil {
		return nil, err
	}

	return &pb.CreateUserResponse{User: created.ToProto()}, nil

	// return &pb.CreateUserResponse{
	// 	User: &pb.User{
	// 		Id: uint32(user.ID),
	// 		Username: user.Username,
	// 		Nickname: user.Nickname,
	// 		Password: user.Password,
	// 	},
	// }, nil
}

func (s *Service) GetUserInfo(ctx context.Context, req *pb.GetUserInfoRequest) (*pb.GetUserInfoResponse, error) {
	user, err := s.repo.FindByID(req.Id)
	if err != nil {
		return nil, err
	}

	return &pb.GetUserInfoResponse{
		UserInfo: &pb.UserInfo{
			Id: uint32(user.ID),
			Username: user.Username,
			Nickname: user.Nickname,
		},
	}, nil
}