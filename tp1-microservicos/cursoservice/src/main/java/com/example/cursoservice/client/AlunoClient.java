package com.example.cursoservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "alunoservice")
public interface AlunoClient {

    @GetMapping("/alunos/{id}")
    AlunoDTO buscarAluno(@PathVariable("id") Long id);
}
