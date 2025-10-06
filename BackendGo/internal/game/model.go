package game

import pb "kteam/api/proto"

type Game struct {
	ID          uint32 `gorm:"primaryKey"`
	Name        string `gorm:"not null"`
	Description string `gorm:"type:text"`
	Price       uint32 `gorm:"not null"`
	Creator     string `gorm:"not null"`
	Icon        string `gorm:"not null"`
}

func (u *Game) ToProto() *pb.Game {
	return &pb.Game{
		Id: uint32(u.ID),
		Name: u.Name,
		Description: u.Description,
		Price: u.Price,
		Creator: u.Creator,
		Icon: u.Icon,
	}
}