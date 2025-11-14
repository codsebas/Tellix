package com.umg.implementacion;

import com.umg.interfaces.ICuentaPorCobrar;
import com.umg.seguridad.Sesion;
import sql.Conector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CuentaPorCobrarImp implements ICuentaPorCobrar.Servicio {

    private final Conector con = Sesion.getConexion();

    // Ahora hacemos JOIN con metodo_liquidacion para obtener la descripción
    private static final String LISTAR_TODO =
            "SELECT cxc.correlativo, " +
                    "       cxc.secuencia, " +
                    "       cxc.metodo_pago, " +
                    "       ml.descripcion AS metodo_pago_desc, " +  // <--- descripción
                    "       cxc.valor_total, " +
                    "       cxc.valor_pagado, " +
                    "       cxc.fecha_limite, " +
                    "       cxc.numero_cuenta, " +
                    "       cxc.cliente_nit " +
                    "FROM tellix.cuenta_por_cobrar cxc " +
                    "JOIN metodo_liquidacion ml ON ml.codigo = cxc.metodo_pago " +
                    "ORDER BY cxc.correlativo";

    @Override
    public List<ICuentaPorCobrar.RowCxc> listarTodo() {
        List<ICuentaPorCobrar.RowCxc> lista = new ArrayList<>();

        try (PreparedStatement ps = con.preparar(LISTAR_TODO);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ICuentaPorCobrar.RowCxc r = new ICuentaPorCobrar.RowCxc();
                r.correlativo    = rs.getInt("correlativo");
                r.secuencia      = rs.getInt("secuencia");
                r.metodoPago     = rs.getInt("metodo_pago");         // código numérico
                r.metodoPagoDesc = rs.getString("metodo_pago_desc"); // <--- texto
                r.valorTotal     = rs.getDouble("valor_total");
                r.valorPagado    = rs.getDouble("valor_pagado");
                r.fechaLimite    = rs.getDate("fecha_limite");
                r.numeroCuenta   = rs.getString("numero_cuenta");
                r.clienteNit     = rs.getString("cliente_nit");
                lista.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error listarTodo CuentasPorCobrar: " + e.getMessage());
        }

        return lista;
    }
}
