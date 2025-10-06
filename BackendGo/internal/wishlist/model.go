package wishlist

type Wishlist struct {
	ID     uint32 `gorm:"primaryKey"`
	UserID uint32
	GameID uint32
}