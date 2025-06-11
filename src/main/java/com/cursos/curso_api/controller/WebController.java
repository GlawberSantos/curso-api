package com.cursos.curso_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import com.cursos.curso_api.model.Curso;
import com.cursos.curso_api.repository.CursoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/cursos")
public class WebController {

    @Autowired
    private CursoRepository repository;

    // Listar cursos
    @GetMapping
    public String listarCursos(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String category,
                              Model model) {
        List<Curso> cursos;
        if ((name != null && !name.isEmpty()) || (category != null && !category.isEmpty())) {
            String nameFilter = name != null ? name : "";
            String categoryFilter = category != null ? category : "";
            cursos = repository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(nameFilter, categoryFilter);
        } else {
            cursos = repository.findAll();
        }
        model.addAttribute("cursos", cursos);
        return "cursos";
    }

    // Página para criar novo curso
    @GetMapping("/novo")
    public String novoCursoForm(Model model) {
        model.addAttribute("curso", new Curso());
        return "curso-novo";
    }

    // Salvar novo curso
    @PostMapping("/novo")
    public String salvarCurso(@Valid @ModelAttribute("curso") Curso curso,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "curso-novo";
        }
        repository.save(curso);
        return "redirect:/web/cursos";
    }

    // Detalhes do curso
    @GetMapping("/{id}")
    public String detalhesCurso(@PathVariable Long id, Model model) {
        Optional<Curso> curso = repository.findById(id);
        if (curso.isEmpty()) {
            return "redirect:/web/cursos";
        }
        model.addAttribute("curso", curso.get());
        return "curso-detalhe";
    }

    // Atualizar curso (edição)
    @PostMapping("/{id}/editar")
    public String editarCurso(@PathVariable Long id,
                              @Valid @ModelAttribute("curso") Curso cursoAtualizado,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curso", cursoAtualizado);
            return "curso-detalhe";
        }
        return repository.findById(id).map(curso -> {
            curso.setName(cursoAtualizado.getName());
            curso.setCategory(cursoAtualizado.getCategory());
            curso.setProfessor(cursoAtualizado.getProfessor());
            curso.setActive(cursoAtualizado.getActive());
            repository.save(curso);
            return "redirect:/web/cursos/" + id;
        }).orElse("redirect:/web/cursos");
    }

    // Excluir curso
    @PostMapping("/{id}/deletar")
    public String deletarCurso(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
        return "redirect:/web/cursos";
    }
}
