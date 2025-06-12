package com.cursos.curso_api.controller;

import com.cursos.curso_api.model.Curso;
import com.cursos.curso_api.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "cursos";
    }

    @GetMapping("/novo")
    public String novoCurso(Model model) {
        model.addAttribute("curso", new Curso());
        return "curso-novo";
    }

    @PostMapping
    public String salvarCurso(@ModelAttribute Curso curso) {
        cursoRepository.save(curso);
        return "redirect:/web/cursos";
    }

    @GetMapping("/{id}")
    public String detalharCurso(@PathVariable Long id, Model model) {
        Curso curso = cursoRepository.findById(id).orElseThrow();
        model.addAttribute("curso", curso);
        return "curso-detalhe";
    }

    @PostMapping("/{id}/editar")
    public String editarCurso(@PathVariable Long id, @ModelAttribute Curso curso) {
        Curso existingCurso = cursoRepository.findById(id).orElseThrow();
        existingCurso.setName(curso.getName());
        existingCurso.setCategory(curso.getCategory());
        existingCurso.setProfessor(curso.getProfessor());
        cursoRepository.save(existingCurso);
        return "redirect:/web/cursos";
    }

    @PostMapping("/{id}/excluir")
    public String excluirCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return "redirect:/web/cursos";
    }
}