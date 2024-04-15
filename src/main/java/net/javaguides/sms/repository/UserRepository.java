package net.javaguides.sms.repository;

import net.javaguides.sms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT ud FROM User ud WHERE ud.email=:userEmail")
    User findUser(@Param("userEmail") String userEmail);


}
