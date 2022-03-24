package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Permiso;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPermiso{
    public Permiso Add(String descripcion,String nombre) throws SQLException, ExcepcionesLogica {
        Permiso permiso = null;
        if(nombre.length() > 50){ throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        if(descripcion.length() > 50){ throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        String query = "INSERT INTO Permiso(descripcion,nombre) VALUES ('"+descripcion+"','"+nombre+"')";
        try{
            consulta = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs.next()){
                int idPermiso = rs.getInt(1);
                permiso = new Permiso(idPermiso, descripcion, nombre);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return permiso;
    }

    public boolean Delete(int idPermiso) throws SQLException, ExcepcionesLogica {
        boolean borrado = false;
        if(idPermiso <= 0){throw new ExcepcionesLogica("idPermiso incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM Permiso where idPermiso = "+idPermiso;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            if(rs > 0){
                borrado = true;
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return borrado;
    }

    public Permiso Search(int idPermiso) throws SQLException, ExcepcionesLogica {
        Permiso PermisoBuscado = null;
        if(idPermiso <= 0){throw new ExcepcionesLogica("idPermiso incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Permiso WHERE idPermiso = "+idPermiso;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null ){
                PermisoBuscado = new Permiso(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        if(PermisoBuscado==null ){throw new ExcepcionesLogica("El idPermiso no se encuentra en la Base de datos");}
        return PermisoBuscado;
    }

    public List<Permiso> SearchAll() throws SQLException, ExcepcionesLogica {
        List<Permiso> listapersmisos = new ArrayList<Permiso>();
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Permiso";
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                    Permiso permiso = new Permiso(rs.getInt(1),rs.getString(2), rs.getNString(3));
                    listapersmisos.add(permiso);
                }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return listapersmisos;
    }



    public Permiso Update( int idPermiso ,String nombre, String descripcion) throws SQLException, ExcepcionesLogica {
        Permiso permiso = null;
        if(idPermiso <= 0){throw new ExcepcionesLogica("idPermiso incorrecto");}
        if(nombre.length() > 50){ throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        if(descripcion.length() > 50){ throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "UPDATE Permiso SET descripcion = '"+descripcion+"', nombre ='"+nombre+"' WHERE idPermiso ="+idPermiso;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            if(rs > 0){
                permiso = new Permiso(idPermiso, descripcion, nombre);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return permiso;
    }
}

