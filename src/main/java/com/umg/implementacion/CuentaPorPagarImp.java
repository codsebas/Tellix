package com.umg.implementacion;

import com.umg.interfaces.ICuentaPorPagar;
import com.umg.seguridad.Sesion;
import sql.Conector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CuentaPorPagarImp implements ICuentaPorPagar.Servicio {

    private final Conector con = Sesion.getConexion();

    private static final String LISTAR_TODAS =
            "SELECT c.correlativo, " +
                    "       c.no_documento, " +
                    "       c.metodo_pago, " +
                    "       ml.descripcion AS metodo_pago_desc, " +
                    "       c.valor_total, " +
                    "       c.valor_pagado, " +
                    "       c.fecha_limite, " +
                    "       c.numero_cuenta, " +
                    "       c.banco " +
                    "FROM tellix.cuenta_por_pagar c " +
                    "JOIN metodo_liquidacion ml ON ml.codigo = c.metodo_pago " +
                    "ORDER BY c.correlativo";

    @Override
    public List<ICuentaPorPagar.RowCxp> listarTodas() {
        List<ICuentaPorPagar.RowCxp> lista = new ArrayList<>();

        try (PreparedStatement ps = con.preparar(LISTAR_TODAS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ICuentaPorPagar.RowCxp r = new ICuentaPorPagar.RowCxp();
                r.correlativo    = rs.getInt("correlativo");
                r.noDocumento    = rs.getInt("no_documento");
                r.metodoPago     = rs.getInt("metodo_pago");
                r.metodoPagoDesc = rs.getString("metodo_pago_desc");
                r.valorTotal     = rs.getDouble("valor_total");
                r.valorPagado    = rs.getDouble("valor_pagado");
                r.fechaLimite    = rs.getDate("fecha_limite");
                r.numeroCuenta   = rs.getString("numero_cuenta");
                r.banco          = rs.getString("banco");
                lista.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error listarTodas CuentasPorPagar: " + e.getMessage());
        }

        return lista;
    }
}
