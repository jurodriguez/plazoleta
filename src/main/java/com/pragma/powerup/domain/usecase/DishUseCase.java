package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.exception.DishNotExistException;
import com.pragma.powerup.infrastructure.exception.OwnerInvalidException;
import com.pragma.powerup.infrastructure.exception.PriceInvalidException;
import com.pragma.powerup.infrastructure.exception.RestaurantIdInvalidException;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveDish(Dish dish) {
        dish.setActive(true);
        saveValidations(dish);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDish(Long id, Dish dishModel) {
        Dish previusDish = getPreviusDish(id);
        priceValidation(dishModel.getPrice());
        previusDish.setPrice(dishModel.getPrice());
        previusDish.setDescription(dishModel.getDescription());

        dishPersistencePort.saveDish(previusDish);
    }

    private Dish getPreviusDish(Long id) {
        Dish previusDish = dishPersistencePort.getDishById(id);
        if (previusDish == null) throw new DishNotExistException();
        return previusDish;
    }

    private void saveValidations(Dish dish) {
        ownerValidation(dish.getRestaurantId());
        priceValidation(dish.getPrice());
    }

    private void ownerValidation(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantId);
        Long idOwnerRestaurant = restaurant != null ? restaurant.getOwnerId() : null;
        if (idOwnerRestaurant == null) {
            throw new RestaurantIdInvalidException();
        } else if (false) {
            throw new OwnerInvalidException();
        }
    }

    private void priceValidation(Integer price) {
        if (price <= 0) {
            throw new PriceInvalidException();
        }
    }
}
