package com.pragma.powerup.factory;

import com.pragma.powerup.domain.model.Dish;

public class FactoryDishesDataTest {

    public static Dish getDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Homemade salad");
        dish.setDescription("small salad");
        dish.setPrice(10000);
        dish.setCategoryId(3L);
        dish.setImageUrl("https://weekend-bucket.s3.amazonaws.com/Mercagan_Parrilla_Bucaramanga_Santander_4_1814dcd866.jpg");
        dish.setRestaurantId(1L);

        return dish;
    }

    public static Dish getNewDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Homemade salad");
        dish.setDescription("big salad");
        dish.setPrice(95000);
        dish.setCategoryId(3L);
        dish.setImageUrl("https://weekend-bucket.s3.amazonaws.com/Mercagan_Parrilla_Bucaramanga_Santander_4_1814dcd866.jpg");
        dish.setRestaurantId(1L);

        return dish;
    }
}
