package com.example.cursoservice.repository;

import com.example.cursoservice.model.Matricula;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatriculaRepository extends MongoRepository<Matricula, String> {
}