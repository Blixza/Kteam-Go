package user

import pb "kteam/api/proto"

type User struct {
	ID       uint   `gorm:"primaryKey"`
	Username string `gorm:"unique;not null"`
	Nickname string `gorm:"not null"`
	Password string `gorm:"not null"`
}

func (u *User) ToProto() *pb.User {
	return &pb.User{
		Id:       uint32(u.ID),
        Username: u.Username,
        Nickname: u.Nickname,
        Password: u.Password,
    }
}