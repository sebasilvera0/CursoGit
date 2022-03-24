package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Articulo;
import logica.MODULO_Almacenamiento.Reserva;
import logica.MODULO_Almacenamiento.TipoReserva;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RepositorioReserva {
    public Reserva Add(int idTipoReserva ,int idArticulo ,double cantidad , int idDeposito , int idEmpresa) throws SQLException, ExcepcionesLogica {
        if (idDeposito<=0) {
            throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
        }
        if (idArticulo <= 0) {
            throw new ExcepcionesLogica("El  idArticulo debe ser positivo");
        }
        if (cantidad <= 0) {
            throw new ExcepcionesLogica("La cantidad  debe ser positiva");
        }
        if (idTipoReserva <= 0) {
            throw new ExcepcionesLogica("El  idTipoReserva debe ser positivo");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("El  idEmpresa debe ser positivo");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        TipoReserva tr = null;
        Articulo ar = null;
        Reserva re = null;
        try{
            consulta = con.prepareStatement("INSERT INTO Reserva(tipoReserva,articulo, cantidad,deposito,empresa) VALUES" +
                            "("+idTipoReserva+","+idArticulo+", "+cantidad+", "+idDeposito+", "+idEmpresa+");", PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs.next() && rs != null) {
                Statement consulta2;
                String query2 = "SELECT * FROM TipoReserva tr WHERE tr.idTipoReserva =" + idTipoReserva;
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                if (rs2.next() && rs2 != null) {
                    tr = new TipoReserva(rs2.getInt(1), rs2.getString(2));
                }
                Statement consulta3;
                String query3 = "SELECT  ar.idArticulo , ar.codigoArticulo , ar.nombre FROM Articulo ar WHERE ar.idArticulo =" + idArticulo;
                consulta3 = con.createStatement();
                ResultSet rs3 = consulta2.executeQuery(query3);
                if (rs2.next() && rs2 != null) {
                    ar = new Articulo(rs3.getInt(1), rs3.getString(2), rs.getString(3));
                }
                 re = new Reserva(rs.getInt(1), tr, ar, cantidad, rs.getDate(5));
                System.out.println("Se creo la Reserva nro" + rs.getInt(1) + "para el articulo: " + idArticulo);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return re;
    }

    public void Delete(int idReserva) throws SQLException, ExcepcionesLogica {
        if(idReserva <= 0){throw new ExcepcionesLogica("idReserva incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            consulta = con.prepareStatement("DELETE FROM Permisos where idReserva = "+idReserva);
            consulta.executeUpdate();
            System.out.println("Se elimino el permiso con id: " + idReserva);
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
    }

    public Reserva Search(int idReserva , int empresa , int idDeposito) throws SQLException, ExcepcionesLogica {
        Reserva reserva = null;
        if(idReserva <= 0){throw new ExcepcionesLogica("idReserva incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT r.* , tr.descripcion , ar.codigoArticulo ,ar.nombre FROM Reseva r\n" +
                " INNER JOIN TipoReserva tr ON  tr.idTipoReserva = r.idReserva \n" +
                " INNER JOIN Articulo ar ON ar.idArticulo = r.articulo\n" +
                " WHERE r.empresa ="  +empresa +
                " AND r.deposito = " +idDeposito +
                " AND r.idReserva =" +idReserva;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null) {
                TipoReserva tipoReserva = new TipoReserva(rs.getInt(2), rs.getString(8));
                Articulo art = new Articulo(rs.getInt(3),rs.getString(9), rs.getString(10) );
                reserva = new Reserva(rs.getInt(1), tipoReserva , art , rs.getDouble(4),
                        rs.getDate(5));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        if(reserva==null ){throw new ExcepcionesLogica("El idReserva no se encuentra en la Base de datos");}
        return reserva;
    }

    //Busca todas las reservas de una empresa
    public List<Reserva> SearchAll(int idEmpresa ) throws SQLException, ExcepcionesLogica {
        List<Reserva> listaReservas = new ArrayList<>();
        Reserva reserva = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT r.* , tr.descripcion , ar.codigoArticulo ,ar.nombre FROM Reseva r\n" +
                " INNER JOIN TipoReserva tr ON  tr.idTipoReserva = r.idReserva \n" +
                " INNER JOIN Articulo ar ON ar.idArticulo = r.articulo\n" +
                " WHERE r.empresa ="  +idEmpresa;

        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next() && rs != null) {
                TipoReserva tipoReserva = new TipoReserva(rs.getInt(2), rs.getString(8));
                Articulo art = new Articulo(rs.getInt(3),rs.getString(9), rs.getString(10) );
                reserva = new Reserva(rs.getInt(1), tipoReserva , art , rs.getDouble(4),
                        rs.getDate(5));
                listaReservas.add(reserva);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return listaReservas;
    }
}
