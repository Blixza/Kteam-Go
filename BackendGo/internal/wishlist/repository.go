package wishlist

import "gorm.io/gorm"

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) FindByUserId(id uint32) ([]uint32, error) {
	var wishlist []Wishlist
	if err := r.db.Where("user_id = ?", id).Find(&wishlist).Error; err != nil {
		return nil, err
	}

	var pbWishlist []uint32
	for _, v := range wishlist {
		pbWishlist = append(pbWishlist, v.GameID)
	}

	return pbWishlist, nil
}

func (r *Repository) Create(userId, gameId uint32) (*Wishlist, error) {
	item := &Wishlist{UserID: userId, GameID: gameId}
	if err := r.db.Create(item).Error; err != nil {
		return nil, err
	}
	return item, nil
}

func (r *Repository) Delete(userId, gameId uint32) (*Wishlist, error) {
	item := &Wishlist{UserID: userId, GameID: gameId}
	if err := r.db.
		Where("user_id = ?", userId).
		Where("game_id = ?", gameId).
		Delete(item).Error;
	err != nil {
		return nil, err
	}
	return item, nil
}