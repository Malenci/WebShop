package org.example.web.Repo;

import org.example.web.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    Optional<UserModel> findByUsernameAndPassword(String username, String password);

}