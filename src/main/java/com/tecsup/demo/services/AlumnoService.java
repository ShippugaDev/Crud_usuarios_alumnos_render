package com.tecsup.demo.services;

import com.tecsup.demo.domain.entities.Alumno;

import java.util.List;

public interface AlumnoService {
    void guardar(Alumno alumno);
    Alumno buscar(Integer id);
    List<Alumno> listar();
    void eliminar(Integer id);
}
