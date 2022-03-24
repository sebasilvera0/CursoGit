package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.ModoDeposito;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioModoDeposito {
    public ModoDeposito Add(String descripcion) throws SQLException, ExcepcionesLogica {
        ModoDeposito modoDeposito = null;
        if(descripcion.length() > 50){ throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            String query = "INSERT INTO ModoDeposito(descripcion) VALUES ('"+descripcion+"')";
            System.out.println(query);
            consulta = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs.next()){
                int idModoDeposito = rs.getInt(1);
                modoDeposito = new ModoDeposito(idModoDeposito, descripcion);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return modoDeposito;
    }

    public boolean Delete(int idModoDeposito) throws SQLException, ExcepcionesLogica {
        boolean borrado = false;
        if(idModoDeposito <= 0){throw new ExcepcionesLogica("idModoDeposito incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try{
            String query = "DELETE FROM ModoDeposito where idModoDeposito = "+idModoDeposito;
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            if(rs>0){
                borrado = true;
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return borrado;
    }

    public ModoDeposito Search(int idModoDeposito) throws SQLException, ExcepcionesLogica {
        ModoDeposito modoBuscado = null;

        if(idModoDeposito <= 0){throw new ExcepcionesLogica("idModoDeposito incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM ModoDeposito WHERE idModoDeposito = "+idModoDeposito;
        try{
            System.out.println(query);
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                modoBuscado = new ModoDeposito(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return modoBuscado;
    }

    //ModoDep Viejo
    /*public List<ModoDeposito> SearchAll(int idEmpresa) throws SQLException, ExcepcionesLogica {
        List<ModoDeposito> modosBuscados = new ArrayList<>();

        if(idEmpresa <= 0){ throw new ExcepcionesLogica("El idEmpresa no puede ser menor o igual a 0");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM ModoDeposito WHERE idEmpresa = " + idEmpresa;
        try{
            System.out.println(query);
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                ModoDeposito modoBuscado = new ModoDeposito(rs.getInt(1), rs.getString(2));
                modosBuscados.add(modoBuscado);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return modosBuscados;
    }*/


    //Modo Deposito es general para todo no hay un modo Dep para una empresa particular
    public List<ModoDeposito> SearchAll() throws SQLException, ExcepcionesLogica {
        List<ModoDeposito> modosBuscados = new ArrayList<>();
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM ModoDeposito ";
        try{
            System.out.println(query);
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                ModoDeposito modoBuscado = new ModoDeposito(rs.getInt(1), rs.getString(2));
                modosBuscados.add(modoBuscado);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return modosBuscados;
    }

    public ModoDeposito Update(int idModoDeposito, String descripcion) throws SQLException, ExcepcionesLogica {
        ModoDeposito modoDeposito = null;
        if(descripcion.length() > 50 ){throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "UPDATE ModoDeposito SET descripcion ='"+descripcion+"' where idModoDeposito = "+idModoDeposito;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            if(rs > 0){
                modoDeposito = new ModoDeposito(idModoDeposito, descripcion);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return modoDeposito;
    }
}
