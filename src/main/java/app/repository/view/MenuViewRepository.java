package app.repository.view;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.views.MenuView;

@Repository
public interface MenuViewRepository extends JpaRepository<MenuView, Integer> {

}
