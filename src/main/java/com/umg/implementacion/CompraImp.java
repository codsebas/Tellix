package com.umg.implementacion;

import com.umg.interfaces.ICompra;
import com.umg.modelo.*;
import com.umg.seguridad.Sesion;
import sql.Conector;
import sql.QuerysCompras;
import sql.query_vistas_db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompraImp implements ICompra {

    private final Conector con = Sesion.getConexion();
    private final QuerysCompras querys = new QuerysCompras();
    private final query_vistas_db vistas = new query_vistas_db();

    @Override
    public boolean insertarCompra(ModeloComprasDB compra, List<ModeloDetalleCompraDB> detalle) {
        boolean resultado = false;

        try{
            PreparedStatement ps = con.prepararCompra(querys.getInsertCompra(), true);
            ps.setString(1,compra.getProveedor());
            ps.setString(2, compra.getRepresentante());
            ps.setDate(3, compra.getFecha_operacion());
            ps.setTimestamp(4, compra.getHora_operacion());
            ps.setString(5, Sesion.getUsuario());
            ps.setInt(6, compra.getMetodo_pago());
            ps.setInt(7, compra.getPlazo_credito());
            ps.setString(8, compra.getTipo_plazo());
            ps.setString(9, "E");

            int filas = ps.executeUpdate();

            if(filas == 0){
                resultado = false;
            } else {
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        int no_doc = rs.getInt(1);

                        for(ModeloDetalleCompraDB det : detalle){
                            det.setNo_documento(no_doc);
                        }

                        resultado = insertarDetalleCompra(detalle);
                    } else {
                        resultado = false;
                    }
                } catch (Exception e){
                    System.out.println("Eror al obtener la llave generada " + e.getMessage());
                    resultado = false;
                }
            }

        } catch (Exception e){
            System.out.println("No se pudo insertar venta " +e.getMessage());
            resultado = false;
        }
        return resultado;
    }

    @Override
    public boolean insertarDetalleCompra(List<ModeloDetalleCompraDB> detalle) {
        if (detalle == null || detalle.isEmpty()){
            return false;
        }

        boolean resultado = true;
        String sql = querys.getInsertDetalleCompra();

        try{
            PreparedStatement ps = con.preparar(sql);

            for(ModeloDetalleCompraDB det : detalle){
                ps.setInt(1, det.getNo_documento());
                ps.setInt(2, det.getCod_producto());
                ps.setInt(3, det.getCantidad());
                ps.setFloat(4, det.getPrecio_bruto());
                ps.setFloat(5, det.getDescuentos());
                ps.setFloat(6, det.getImpuestos());

                ps.addBatch();
            }

            int[] filas = ps.executeBatch();

            for (int f : filas) {
                if (f == PreparedStatement.EXECUTE_FAILED || f == 0) {
                    resultado = false;
                    break;
                }
            }

            if(!resultado){
                return false;
            }

            for(ModeloDetalleCompraDB det : detalle){
                int codProducto = det.getCod_producto();
                int cantidad = det.getCantidad();

                boolean invOk = insertarInventario(codProducto, cantidad);
                boolean stockOk = actualizarStock(codProducto, cantidad);

                if(!invOk || !stockOk){
                    resultado =  false;
                    break;
                }
            }

        } catch (Exception e){
            System.out.println("Error al insertar detalle compra");
        }

        return resultado;
    }

    @Override
    public ModeloResumProd seleccionarProducto(int codigo) {
        try {
            PreparedStatement ps = con.preparar(vistas.getSELECT_APLICACION());
            ps.setInt(1, codigo);
            ps.setString(2, "C");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ModeloResumProd modelo = new ModeloResumProd();

                modelo.setCodigo(rs.getInt("codigo"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setStockDisponible(rs.getInt("stock_disponible"));
                modelo.setStockMinimo(rs.getInt("stock_minimo"));
                modelo.setStockBajoMinimo(rs.getString("stock_bajo_minimo"));
                modelo.setAplicacion(rs.getString("aplicacion"));
                modelo.setPrecioBase(rs.getFloat("precio_base"));
                modelo.setTotalImpuestos(rs.getFloat("total_impuestos"));
                modelo.setTotalDescuentos(rs.getFloat("total_descuentos"));
                modelo.setPrecioFinal(rs.getFloat("precio_final"));

                return modelo;
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar producto con código " + codigo, e);
        }
    }

    @Override
    public boolean insertarInventario(int codigo, int cantidad) {
        boolean resultado = false;
        try{
            PreparedStatement ps = con.preparar(querys.getINSERTAR_INVENTARIO());
            ps.setInt(1, codigo);
            ps.setInt(2, cantidad);
            ps.setString(3, "Compra de productos");
            ps.setString(4, "C");
            ps.setString(5, Sesion.getUsuario());
            int filas = ps.executeUpdate();
            resultado = (filas > 0);
        } catch(Exception e){
        }
        return resultado;
    }

    @Override
    public boolean actualizarStock(int codigo, int stock) {
        boolean resultado = false;
        try{
            ModeloResumProd producto = seleccionarProducto(codigo);
            int stockNuevo = producto.getStockDisponible() + stock;

            PreparedStatement ps = con.preparar(querys.getACTUALIZAR_STOCK_PRODUCTO());
            ps.setInt(1, stockNuevo);
            ps.setInt(2, codigo);
            int files = ps.executeUpdate();
            resultado = (files > 0);
        } catch(Exception e){
            throw new RuntimeException("Error al actualizar stock", e);
        }
        return resultado;
    }

    @Override
    public List<ModeloProveedoresDB> seleccionarProveedores() {
        List<ModeloProveedoresDB> lista = new ArrayList<>();

        try {
            PreparedStatement ps = con.preparar(querys.getSelectAllProveedores());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModeloProveedoresDB modelo = new ModeloProveedoresDB();

                modelo.setNit(rs.getString("nit_proveedor"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setDireccion(rs.getString("direccion_fiscal"));
                modelo.setTelefono(rs.getString("telefono"));

                lista.add(modelo);
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar proveedores", e);
        }
    }


    @Override
    public List<ModeloRepresentanteDB> seleccionarRepresentantes(String proveedor) {
        List<ModeloRepresentanteDB> lista = new ArrayList<>();

        try {
            PreparedStatement ps = con.preparar(querys.getSelectAllRepresentantes());
            ps.setString(1, proveedor);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModeloRepresentanteDB modelo = new ModeloRepresentanteDB();

                modelo.setNit(rs.getString("nit_representante"));   // NIT del representante
                modelo.setNit_proveedor(rs.getString("nit_proveedor"));
                modelo.setNombre(rs.getString("nombre_completo"));  // nombre concatenado de la vista

                lista.add(modelo);
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar representantes", e);
        }
    }


    @Override
    public List<ModeloMetodoPagoDB> seleccionarMetodosPago() {
        List<ModeloMetodoPagoDB> lista = new ArrayList<>();

        try {
            PreparedStatement ps = con.preparar(querys.getSelectAllMetodosPago());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModeloMetodoPagoDB modelo = new ModeloMetodoPagoDB();

                modelo.setCodigo(rs.getInt("codigo"));
                modelo.setMetodo_pago(rs.getString("descripcion"));

                lista.add(modelo);
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Error al seleccionar métodos de pago", e);
        }
    }



}