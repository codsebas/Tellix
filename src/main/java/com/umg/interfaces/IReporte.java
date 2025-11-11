package com.umg.interfaces;

import javax.swing.table.DefaultTableModel;

public interface IReporte {
    DefaultTableModel ventasDelDia();
    DefaultTableModel ventasRangoFechas(String fechaInicio, String fechaFin);
}
