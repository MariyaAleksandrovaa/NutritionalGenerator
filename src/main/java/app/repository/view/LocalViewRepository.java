package app.repository.view;

import org.springframework.data.jpa.repository.JpaRepository;

import app.views.LocalView;

public interface LocalViewRepository extends JpaRepository<LocalView, String> {

}
