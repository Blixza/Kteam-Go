package game

import (
	pb "kteam/api/proto"

	"gorm.io/gorm"
)

type Repository struct {
	pb.UnimplementedGameServiceServer
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) FindAll() ([]Game, error) {
	var games []Game
	if err := r.db.Unscoped().Find(&games).Error; err != nil {
		return nil, err
	}
	return games, nil
}

func (r *Repository) Create(game *Game) (*Game, error) {
	if err := r.db.Create(game).Error; err != nil {
		return nil, err
	}
	return game, nil
}

func (r *Repository) FindByID(id uint32) (*Game, error) {
    var game Game
    if err := r.db.First(&game, id).Error; err != nil {
        return nil, err
    }
    return &game, nil
}

func (r *Repository) Delete(id uint32) (*Game, error) {
	game := &Game{ID: id}
	if err := r.db.Where("id = ?", id).Delete(game).Error; err != nil {
		return nil, err
	}
	return game, nil
}