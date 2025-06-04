package com.tecsup.demo.aop;

import com.tecsup.demo.domain.entities.Auditoria;
import com.tecsup.demo.domain.entities.Alumno;
import com.tecsup.demo.domain.entities.Curso;
import com.tecsup.demo.domain.persistence.AuditoriaDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Calendar;

@Component
@Aspect
public class LogginAspecto {

    private Long tx;

    @Autowired
    private AuditoriaDao auditoriaDao;

    // Log para capturar la ejecución de los servicios
    @Around("execution(* com.tecsup.demo.services.*ServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Long currTime = System.currentTimeMillis();
        tx = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = "tx[" + tx + "] - " + joinPoint.getSignature().getName();

        if (joinPoint.getArgs().length > 0)
            logger.info(metodo + "() INPUT:" + Arrays.toString(joinPoint.getArgs()));

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }

        logger.info(metodo + "(): tiempo transcurrido " + (System.currentTimeMillis() - currTime) + " ms.");
        return result;
    }

    // Auditoría después de ejecutar operaciones sobre Curso o Alumno
    @After("execution(* com.tecsup.demo.controllers.*Controller.guardar*(..)) ||" +
            "execution(* com.tecsup.demo.controllers.*Controller.editar*(..)) ||" +
            "execution(* com.tecsup.demo.controllers.*Controller.eliminar*(..))")
    public void auditoria(JoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = joinPoint.getSignature().getName();
        Integer id = null;
        String tabla = null;

        // Si es el método de guardar, editar o eliminar
        if (metodo.startsWith("guardar") || metodo.startsWith("editar") || metodo.startsWith("eliminar")) {
            Object[] parametros = joinPoint.getArgs();

            // Si es un Curso, manejarlo como tal
            if (parametros[0] instanceof Curso) {
                Curso curso = (Curso) parametros[0];  // Cast a Curso
                id = curso.getId();
                tabla = "cursos";  // Establecer la tabla como "cursos"
                logger.info("Auditoría - Curso: " + curso.getNombre());
            }
            // Si es un Alumno, manejarlo como tal
            else if (parametros[0] instanceof Alumno) {
                Alumno alumno = (Alumno) parametros[0];  // Cast a Alumno
                id = alumno.getId();
                tabla = "alumnos";  // Establecer la tabla como "alumnos"
                logger.info("Auditoría - Alumno: " + alumno.getNombre());
            }

            // Registrar la auditoría en la base de datos
            String traza = "tx[" + tx + "] - " + metodo;
            logger.info(traza + "(): registrando auditoría...");
            auditoriaDao.save(new Auditoria(tabla, id, Calendar.getInstance().getTime(), "usuario", metodo));
        }
    }
}
