package com.example.TransportCompany.repository;

import com.example.TransportCompany.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseRepository  extends JpaRepository<Course, Integer>
{
    List<Course> findByType(String status);
}
