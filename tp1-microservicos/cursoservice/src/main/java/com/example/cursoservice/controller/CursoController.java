package com.example.cursoservice.controller;

import com.example.cursoservice.client.AlunoDTO;
import com.example.cursoservice.model.Curso;
import com.example.cursoservice.model.Matricula;
import com.example.cursoservice.repository.CursoRepository;
import com.example.cursoservice.repository.MatriculaRepository;
import com.example.cursoservice.service.MatriculaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private MatriculaValidationService matriculaValidationService;

    @PostMapping("/cursos")
    public Curso criarCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    @GetMapping("/cursos")
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    @PostMapping("/cursos/{cursoId}/matriculas")
    public ResponseEntity<?> matricular(@PathVariable String cursoId, @RequestParam Long alunoId) {
        AlunoDTO aluno = matriculaValidationService.validarAluno(alunoId);

        if (aluno == null) {
            return ResponseEntity.status(503)
                    .body("Não foi possível validar o aluno no momento. Tente novamente mais tarde.");
        }

        Matricula matricula = new Matricula();
        matricula.setAlunoId(alunoId);
        matricula.setCursoId(cursoId);
        matricula.setDataMatricula(LocalDate.now());

        return ResponseEntity.ok(matriculaRepository.save(matricula));
    }

    @GetMapping("/cursos/{cursoId}/matriculas")
    public List<Matricula> listarMatriculas(@PathVariable String cursoId) {
        return matriculaRepository.findAll().stream()
                .filter(m -> m.getCursoId().equals(cursoId))
                .toList();
    }
}
