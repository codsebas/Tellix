/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.interfaces;

import com.umg.modelo.ModeloEmpleados;
import java.util.List;

/**
 *
 * @author javie
 */
public interface IEmpleado {
 
    boolean insertar(ModeloEmpleados e) throws Exception;

    boolean actualizar(ModeloEmpleados e) throws Exception;

    boolean inactivar(Integer codigoEmpleado) throws Exception;

    ModeloEmpleados obtenerPorCodigo(Integer codigoEmpleado) throws Exception;

    List<ModeloEmpleados> obtenerTodos() throws Exception;

    List<ModeloEmpleados> buscar(String filtro) throws Exception;

    List<ModeloEmpleados> listarJefes() throws Exception;
}