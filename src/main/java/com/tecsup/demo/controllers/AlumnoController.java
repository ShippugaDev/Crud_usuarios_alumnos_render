package com.tecsup.demo.controllers;

import com.tecsup.demo.domain.entities.Alumno;
import com.tecsup.demo.services.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import java.util.Map;

@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    // Mostrar la lista de alumnos
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listarAlumno(Model model) {
        model.addAttribute("alumnos", alumnoService.listar());
        return "listarAlumnos";  // Vista de lista de alumnos
    }

    // Formulario para crear un nuevo alumno
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String crear(Map<String, Object> model) {
        Alumno alumno = new Alumno();  // Crear un nuevo alumno
        model.put("alumno", alumno);  // Agregar el alumno al modelo
        return "formAlumno";  // Vista del formulario de creación
    }

    // Guardar o actualizar el alumno (POST)
    @PostMapping("/form")
    public String guardar(@Valid Alumno alumno, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            return "formAlumno";  // Si hay errores de validación, vuelve al formulario
        }

        try {
            alumnoService.guardar(alumno);  // Guarda o actualiza el alumno
            status.setComplete();  // Limpiar la sesión
            return "redirect:/alumnos/listar";  // Redirigir a la lista de alumnos
        } catch (Exception e) {
            model.addAttribute("error", "Hubo un error al guardar el alumno: " + e.getMessage());
            return "formAlumno";  // Volver al formulario si ocurre un error
        }
    }


    // Formulario para editar un alumno
    @GetMapping("/form/{id}")
    public String editar(@PathVariable(value = "id") Integer id, Map<String, Object> model) {
        Alumno alumno = alumnoService.buscar(id);  // Buscar alumno por ID
        if (alumno == null) {
            return "redirect:/alumnos/listar";  // Si no se encuentra el alumno, redirigir a la lista
        }
        model.put("alumno", alumno);  // Poner el alumno en el modelo para el formulario
        return "formAlumno";  // Muestra el formulario de edición
    }
    // Eliminar un alumno
    @RequestMapping(value = "/eliminar/{id}", method = RequestMethod.GET)
    public String eliminar(@PathVariable(value = "id") Integer id) {
        if (id > 0) {
            alumnoService.eliminar(id);  // Eliminar el alumno por ID
        }
        return "redirect:/alumnos/listar";  // Redirigir a la lista de alumnos
    }

    // Exportar a PDF
    @RequestMapping(value = "/ver", method = RequestMethod.GET)
    public String ver(Model model) {
        model.addAttribute("alumnos", alumnoService.listar());
        return "alumno/ver";  // Vista de alumnos (PDF)
    }

    // Exportar a XLS
    @RequestMapping(value = "/ver.xlsx", method = RequestMethod.GET)
    public String verXls(Model model) {
        model.addAttribute("alumnos", alumnoService.listar());
        return "alumno/ver.xlsx";  // Vista de alumnos (XLS)
    }
}


