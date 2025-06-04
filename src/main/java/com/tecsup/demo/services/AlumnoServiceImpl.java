package com.tecsup.demo.services;

import com.tecsup.demo.domain.entities.Alumno;
import com.tecsup.demo.domain.persistence.AlumnoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoDao alumnoDao;

    @Override
    @Transactional
    public void guardar(Alumno alumno) {
        if (alumno.getId() == null) {
            // Si el ID es null, crear un nuevo alumno
            alumnoDao.save(alumno);
        } else {
            // Si el ID no es null, actualizar el alumno
            alumnoDao.save(alumno);  // Este método maneja la actualización si el ID ya existe
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Alumno buscar(Integer id) {
        return alumnoDao.findById(id).orElse(null);  // Buscar alumno por ID
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> listar() {
        return (List<Alumno>) alumnoDao.findAll();  // Listar todos los alumnos
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        alumnoDao.deleteById(id);  // Eliminar alumno por ID
    }
}
