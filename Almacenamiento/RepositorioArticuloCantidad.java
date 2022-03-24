package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Articulo;
import logica.MODULO_Almacenamiento.Movimiento;
import logica.MODULO_Almacenamiento.TipoMovimiento;
import logica.MODULO_Almacenamiento.Usuario;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioArticuloCantidad {
    public Movimiento Add(String idArticulo , double cantidad , int idUsuario , int idTipoMovimiento , int idDeposito , int idEmpresa) throws SQLException, ExcepcionesLogica {
        Movimiento mov = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            consulta = con.prepareStatement("INSERT INTO ArticuloCantidad (articulo, cantidad,usuario,tipoMovimiento,deposito,empresa) VALUES" +
                    "('"+idArticulo+"', "+cantidad+", "+idUsuario+", "+idTipoMovimiento+", "+idDeposito+", "+idEmpresa+") ");
            consulta.executeUpdate();
            Articulo art = null;
            Usuario usu = new Usuario(idUsuario);
            ResultSet rs = consulta.getGeneratedKeys();
            TipoMovimiento tm = new TipoMovimiento(idTipoMovimiento);
            if(rs != null && rs.next()) {
                mov = new Movimiento(rs.getInt(1), art, cantidad, rs.getDate(4), usu, tm);
                System.out.println("Se creo el Articulo nro" + idArticulo + "para el articulo: " + idArticulo);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return mov;
    }

    public void Delete(int idMovimiento) throws SQLException, ExcepcionesLogica {
        if(idMovimiento <= 0){throw new ExcepcionesLogica("idMovimiento incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            consulta = con.prepareStatement("DELETE FROM Movimiento where idMovimiento = "+idMovimiento);
            consulta.executeUpdate();
            System.out.println("Se elimino el permiso con id: " +idMovimiento);
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
    }

    public Movimiento Search(int idMovimiento) throws SQLException, ExcepcionesLogica {
        Movimiento mov = null;
        if(idMovimiento <= 0){throw new ExcepcionesLogica("idReserva incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Movimiento WHERE idMovimiento ="+idMovimiento;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null) {
                Articulo art = null;
                Usuario usu = new Usuario(rs.getInt(5));
                TipoMovimiento tm = new TipoMovimiento(rs.getInt(6));
                mov = new Movimiento(rs.getInt(1), art, rs.getInt(3), rs.getDate(4), usu, tm);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  mov;
    }


    //Traigo objetos mas completos
    public Movimiento Search2(int idMovimiento) throws SQLException, ExcepcionesLogica {
        Movimiento mov = null;
        if(idMovimiento <= 0){throw new ExcepcionesLogica("idReserva incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT m.* , ar.codigoArticulo , ar.nombre , u.idUsuario , u.nombre , u.apellido  , tm.idTipoMovimiento, tm.descripcion FROM Movimiento m\n" +
                " INNER JOIN Articulo ar ON ar.codigoArticulo = m.articulo\n" +
                " INNER JOIN Usuario u ON u.idUsuario = m.usuario\n" +
                " INNER JOIN TipoMovimiento tm ON m.tipoMovimiento = tm.idTipoMovimiento \n" +
                " WHERE m.idMovmiento ="+idMovimiento;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null) {
                Articulo art = null;
                Usuario usu = new Usuario(rs.getInt(10),rs.getString(11),rs.getString(12));
                TipoMovimiento tm = new TipoMovimiento(rs.getInt(13),rs.getString(14));
                mov = new Movimiento(rs.getInt(1), art, rs.getInt(3), rs.getDate(4), usu, tm);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  mov;
    }

    //Busca todas las reservas de una empresa
    public List<Movimiento> SearchAll(int idEmpresa ) throws SQLException, ExcepcionesLogica {
        List<Movimiento> listaMov = new ArrayList<>();
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "\"SELECT * FROM Movimiento where idEmpresa ="+idEmpresa;

        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                Articulo art = null;
                Usuario usu = new Usuario(rs.getInt(5));
                TipoMovimiento tm = new TipoMovimiento(rs.getInt(6));
                Movimiento mov = new Movimiento(rs.getInt(1), art, rs.getInt(3), rs.getDate(4), usu, tm);
                listaMov.add(mov);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return listaMov;
    }


    public List<Movimiento> SearchAll2(int idEmpresa) throws SQLException, ExcepcionesLogica {

        if(idEmpresa <= 0){throw new ExcepcionesLogica("idReserva incorrecto");}
        List<Movimiento> listaMovi = new ArrayList<>();
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT m.* , ar.codigoArticulo , ar.nombre , u.idUsuario , u.nombre , u.apellido  , tm.idTipoMovimiento, tm.descripcion FROM Movimiento m\n" +
                " INNER JOIN Articulo ar ON ar.codigoArticulo = m.articulo\n" +
                " INNER JOIN Usuario u ON u.idUsuario = m.usuario\n" +
                " INNER JOIN TipoMovimiento tm ON m.tipoMovimiento = tm.idTipoMovimiento \n" +
                " WHERE m.idEmpresa ="+idEmpresa;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next() && rs != null) {
                Movimiento mov = null;
                Articulo art = null;
                Usuario usu = new Usuario(rs.getInt(10),rs.getString(11),rs.getString(12));
                TipoMovimiento tm = new TipoMovimiento(rs.getInt(13),rs.getString(14));
                mov = new Movimiento(rs.getInt(1), art, rs.getInt(3), rs.getDate(4), usu, tm);
                listaMovi.add(mov);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  listaMovi;
    }
}

