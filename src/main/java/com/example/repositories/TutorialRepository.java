package com.example.repositories;

import com.example.entities.Tutorial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);
    Page<Tutorial> findByPublished(boolean published, Pageable pageable);
    List<Tutorial> findByTitleContaining(String title);
    List<Tutorial> findByTitleContaining(String title, Sort sort);
    Page<Tutorial> findByTitleContaining(String title, Pageable pageable);
    List<Tutorial> findByDescriptionContaining(String title);
    List<Tutorial> findByDescriptionContaining(String title, Sort sort);
    Page<Tutorial> findByDescriptionContaining(String title, Pageable pageable);
}
