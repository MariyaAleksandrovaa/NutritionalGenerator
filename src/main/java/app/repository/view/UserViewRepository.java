package app.repository.view;

import org.springframework.data.jpa.repository.JpaRepository;

import app.views.UserView;

public interface UserViewRepository extends JpaRepository<UserView, Integer> {

}
