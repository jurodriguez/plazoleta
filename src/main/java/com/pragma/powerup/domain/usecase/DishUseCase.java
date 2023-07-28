package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.infrastructure.exception.DishNotExistException;
import com.pragma.powerup.infrastructure.exception.OwnerInvalidException;
import com.pragma.powerup.infrastructure.exception.OwnerNotAuthenticatedException;
import com.pragma.powerup.infrastructure.exception.PriceInvalidException;
import com.pragma.powerup.infrastructure.exception.RestaurantIdInvalidException;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    private final IToken token;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IToken token) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.token = token;
    }

    @Override
    public void saveDish(Dish dish) {
        dish.setActive(true);
        dishValidations(dish);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDish(Long id, Dish dishModel) {
        Dish previusDish = getPreviusDish(id);

        previusDish.setPrice(dishModel.getPrice());
        previusDish.setDescription(dishModel.getDescription());

        dishValidations(previusDish);

        dishPersistencePort.saveDish(previusDish);
    }

    @Override
    public void updateEnableDisableDish(Long dishId, Long flag) {
        Dish dish = getPreviusDish(dishId);
        ownerValidation(dish.getRestaurantId());

        boolean isEnableOrDisable = (flag == 1);
        dish.setActive(isEnableOrDisable);

        dishPersistencePort.saveDish(dish);
    }

    private Dish getPreviusDish(Long id) {
        Dish previusDish = dishPersistencePort.getDishById(id);
        if (previusDish == null) throw new DishNotExistException();
        return previusDish;
    }

    private void dishValidations(Dish dish) {
        ownerValidation(dish.getRestaurantId());
        priceValidation(dish.getPrice());
    }

    private void ownerValidation(Long restaurantId) {
        Long idOwnerRestaurant = getOwnerRestaurant(restaurantId);
        Long idOwnerAuth = getOwnerAuth();

        if (idOwnerRestaurant.longValue() != idOwnerAuth.longValue()) throw new OwnerInvalidException();
    }

    private Long getOwnerRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantId);
        if (restaurant == null) throw new RestaurantIdInvalidException();
        return restaurant.getOwnerId();
    }

    private Long getOwnerAuth() {
        String bearerToken = token.getBearerToken();
        if (bearerToken == null) throw new OwnerNotAuthenticatedException();
        return token.getUserAuthenticatedId(bearerToken);
    }

    private void priceValidation(Integer price) {
        if (price <= 0) {
            throw new PriceInvalidException();
        }
    }
}
