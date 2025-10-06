package cart

import "gorm.io/gorm"

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{db: db}
} 

func (r *Repository) FindByUserId(id uint32) ([]Cart, error) {
	var cart []Cart
	if err := r.db.Where("user_id = ?", id).Find(&cart).Error; err != nil {
		return nil, err
	}

	return cart, nil
}

func (r *Repository) Create(userId, gameId uint32) (*Cart, error) {
	item := &Cart{UserID: userId, GameID: gameId}
	if err := r.db.Create(item).Error; err != nil {
		return nil, err
	}
	return item, nil
}

func (r *Repository) Delete(userId, gameId uint32) (*Cart, error) {
	item := &Cart{UserID: userId, GameID: gameId}
	if err := r.db.
		Where("user_id = ?", userId).
		Where("game_id = ?", gameId).
		Delete(item).Error;
	err != nil {
		return nil, err
	}
	return item, nil
}