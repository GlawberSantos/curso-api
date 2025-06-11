package com.cursos.curso_api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cursos.curso_api.model.Curso;
import com.cursos.curso_api.repository.CursoRepository;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
@CrossOrigin
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping
    public ResponseEntity<Curso> criarCurso(@RequestBody @Valid Curso curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(curso));
    }

    @GetMapping
    public List<Curso> listar(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String category) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty();

        if (hasName || hasCategory) {
            String nameFilter = hasName ? name : "";
            String categoryFilter = hasCategory ? category : "";
            return repository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(nameFilter, categoryFilter);
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscar(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody @Valid Curso atualizado) {
        return repository.findById(id).map(curso -> {
            // Atualização total: substitui todos os campos (exceto id e timestamps)
            curso.setName(atualizado.getName());
            curso.setCategory(atualizado.getCategory());
            curso.setProfessor(atualizado.getProfessor());
            curso.setActive(atualizado.getActive() != null ? atualizado.getActive() : true);
            // Não precisa setar updatedAt manualmente, Hibernate cuida disso
            return ResponseEntity.ok(repository.save(curso));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Curso> toggleAtivo(@PathVariable Long id) {
        return repository.findById(id).map(curso -> {
            curso.setActive(!curso.getActive());
            return ResponseEntity.ok(repository.save(curso));
        }).orElse(ResponseEntity.notFound().build());
    }
}
