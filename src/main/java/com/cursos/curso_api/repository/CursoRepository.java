package com.cursos.curso_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cursos.curso_api.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category);
}
