package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public Dish saveDish(Dish dish) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dish));
        return dishEntityMapper.toDishModel(dishEntity);
    }

    @Override
    public Dish getDishById(Long id) {
        Optional<DishEntity> optionalDishEntity = dishRepository.findById(id);
        DishEntity dishEntity = optionalDishEntity.orElse(null);
        return dishEntityMapper.toDishModel(dishEntity);
    }

    @Override
    public List<Dish> findDishPaginationByRestaurantId(Long restaurantId, Long categoryId, Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("categoryId"));
        return dishRepository.findAllByRestaurantIdAndCategoryIdAndActive(restaurantId, categoryId, true, pageable)
                .stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());
    }
}
