package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;


    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(id);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public Restaurant getRestaurantByOwnerId(Long ownerId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findByOwnerId(ownerId);
        RestaurantEntity restaurantEntity = optionalRestaurantEntity.orElse(null);
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public List<Restaurant> getRestaurantsWithPagination(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findAll(pageable);
        List<RestaurantEntity> restaurantEntityList = restaurantPage.getContent();
        if (restaurantEntityList.isEmpty()) {
            throw new NoDataFoundException();
        }
        return restaurantEntityMapper.toRestaurantModelList(restaurantEntityList);
    }
}