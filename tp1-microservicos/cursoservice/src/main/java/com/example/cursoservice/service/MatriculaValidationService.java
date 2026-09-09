package com.example.cursoservice.service;

import com.example.cursoservice.client.AlunoClient;
import com.example.cursoservice.client.AlunoDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatriculaValidationService {

    @Autowired
    private AlunoClient alunoClient;

    @CircuitBreaker(name = "alunoService", fallbackMethod = "fallbackValidarAluno")
    public AlunoDTO validarAluno(Long alunoId) {
        return alunoClient.buscarAluno(alunoId);
    }

    // Metodo de fallback: mesma assinatura do metodo original + um parametro Throwable no final
    public AlunoDTO fallbackValidarAluno(Long alunoId, Throwable t) {
        System.out.println("Fallback acionado! aluno-service indisponivel: " + t.getMessage());
        return null; // sinaliza que nao foi possivel validar
    }
}