package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Integer> {

}
