package com.cursos.curso_api.repository;

import com.cursos.curso_api.model.Curso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

  List<Curso> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String nameFilter, String categoryFilter);
}