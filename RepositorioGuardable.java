
package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.*;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositorioGuardable {
    public Guardable Add( int nucleo , int articulo, int bultoRecepcion,
                 double cantidadContenido, int prioridadOrdenamiento,
                int usuario ,int deposito , int empresa )  throws SQLException, ExcepcionesLogica {
        Guardable ge = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            consulta = con.prepareStatement("INSERT INTO Guardable ( nucleo ,  articulo,  bultoRecepcion,\n" +
                    "  cantidadContenido, prioridadOrdenamiento, \n" +
                    "  usuario , deposito , empresa) VALUES" +
                    "("+nucleo+" ,"+articulo+", "+bultoRecepcion+", "+cantidadContenido+"" +
                    ", "+prioridadOrdenamiento+", "+usuario+","+deposito+","+empresa+");", PreparedStatement.RETURN_GENERATED_KEYS);

            consulta.executeUpdate();
            Date date = new Date();
            ResultSet rs = consulta.getGeneratedKeys();
            Nucleo no = new Nucleo(nucleo);
            Articulo art = new Articulo(articulo);
            Usuario usu = new Usuario(usuario);
           //BultoRecepcion  br= new BultoRecepcion(bultoRecepcion);
            if(rs != null && rs.next()) {
                System.out.println("estoy aca");
            //ge = new Guardable(rs.getInt(1),no,art,br,cantidadContenido,prioridadOrdenamiento,date,usu);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return ge;
    }

    public boolean Delete(int idGuardable  , int idEmpresa) throws SQLException, ExcepcionesLogica {
        if(idGuardable <= 0){throw new ExcepcionesLogica("idGuardable incorrecto");}
        boolean borrado = false;
        if (idGuardable <= 0) {
            throw new ExcepcionesLogica("idGuardable incorrecto");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM Guardable where idGuardable = "+idGuardable +  " and empresa =  "+ idEmpresa+";";
        try {
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            if (rs > 0) {
                borrado = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return borrado;
    }

    public Guardable Search(int idGuardable ,int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
        Guardable Guardable = null;
        List<Guardable> listaContenido = new ArrayList<>();
        if(idGuardable <= 0){throw new ExcepcionesLogica("idGuardable incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT g.* ,a.codigoArticulo , a.nombre , n.idNucleo , n.codigoNucleo FROM Guardable g " +
                " INNER JOIN Articulo  a ON g.articulo = a.idArticulo" +
                " INNER JOIN Nucleo n ON g.nucleo = n.idNucleo" +
                " WHERE g.idGuardable =" + idGuardable+
                " AND g.empresa = "+idEmpresa +
                " AND g.deposito = " +idDeposito;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null) {
                Nucleo no = new Nucleo(rs.getInt(13), rs.getString(14));

                Articulo art = new Articulo(rs.getInt(3), rs.getString(11),rs.getString(12));
                Usuario usu = new Usuario(rs.getInt(8));
                //BultoRecepcion br = new BultoRecepcion(rs.getInt(4));
                //Guardable = new Guardable(rs.getInt(1), no, art, br, rs.getDouble(5),
                  //          rs.getInt(6), rs.getDate(7), usu);

            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  Guardable;
    }

    //Busca todas las Guardables de un deposito con 0 busca todo los guardables
    public List<Guardable> SearchAll(int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
        Guardable guardable = null;
        List<Guardable> listaGuardables = new ArrayList<>();
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idGuardable incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT g.* ,a.codigoArticulo , a.nombre , n.idNucleo , n.codigoNucleo FROM Guardable g " +
                " INNER JOIN Articulo  a ON g.articulo = a.idArticulo" +
                " INNER JOIN Nucleo n ON g.nucleo = n.idNucleo" +
                " AND g.empresa = "+idEmpresa +
                " AND g.deposito = " +idDeposito;

        if(idDeposito==0){
            query = "SELECT g.* ,a.codigoArticulo , a.nombre , n.idNucleo , n.codigoNucleo FROM Guardable g " +
                    " INNER JOIN Articulo  a ON g.articulo = a.idArticulo" +
                    " INNER JOIN Nucleo n ON g.nucleo = n.idNucleo" +
                    " AND g.empresa = "+idEmpresa;
        }
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next() && rs != null) {
                Nucleo no = new Nucleo(rs.getInt(13), rs.getString(14));
                Articulo art = new Articulo(rs.getInt(3), rs.getString(11),rs.getString(12));
                Usuario usu = new Usuario(rs.getInt(8));
                BultoRecepcion br = new BultoRecepcion(rs.getInt(4));
                guardable = new Guardable(rs.getInt(1), no, art, br, rs.getDouble(5),
                        rs.getInt(6), rs.getDate(7), usu);
                listaGuardables.add(guardable);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  listaGuardables;
}


    public Guardable Update(int idGuardable, int nucleo , int articulo, int bultoRecepcion,
                                 double cantidadContenido, int prioridadOrdenamiento,
                                 int usuario ,int deposito , int empresa ) throws SQLException, ExcepcionesLogica {
        if(empresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idGuardable <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        Guardable ge = null;
        try{
            String query = "UPDATE Guardable SET nucleo = "+nucleo+", " +
                    "articulo = "+articulo+", bultoRecepcion ="+bultoRecepcion+",  cantidadContenido="+cantidadContenido+"" +
                    " ,  prioridadOrdenamiento = "+prioridadOrdenamiento+", usuario = "+usuario+"  WHERE empresa ="+empresa+" AND " +
                    "idGuardable ="+idGuardable+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
            Nucleo no = new Nucleo(nucleo);
            Articulo art = new Articulo(articulo);
            Usuario usu = new Usuario(usuario);
            BultoRecepcion  br= new BultoRecepcion(bultoRecepcion);
            Date date = new Date();
            ge = new Guardable(idGuardable,no,art,br,cantidadContenido,prioridadOrdenamiento,date,usu);
            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return ge;
    }

}



