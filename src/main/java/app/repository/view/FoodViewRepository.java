package app.repository.view;


import org.springframework.data.jpa.repository.JpaRepository;

import app.views.*;

public interface FoodViewRepository extends JpaRepository<FoodView, String> {

}
