package app.repository.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.views.DishView;

@Repository
public interface DishViewRepository extends JpaRepository<DishView, Integer> {

}
