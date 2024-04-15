package net.javaguides.sms.repository;

import net.javaguides.sms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.sms.entity.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface StudentRepository extends JpaRepository<Student, Long>{

    @Modifying
    @Query("UPDATE Student aa SET aa.firstName=:firstName,aa.lastName=:lastName WHERE aa.id =:id")
    void updatebyId(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName);

//    User finduser(String userEmail);
}
