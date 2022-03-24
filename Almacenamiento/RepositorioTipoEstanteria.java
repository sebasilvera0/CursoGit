package persistencia.Almacenamiento;
import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.TipoEstanteria;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTipoEstanteria {

    public TipoEstanteria Add(int idEmpresa,String nombre ,double profundidad, double altura, double largo) throws SQLException, ExcepcionesLogica {
        if (nombre.length() > 10) {
            throw new ExcepcionesLogica("El nombre debe tener 1 caracter o mas"); }
        TipoEstanteria TipoEstanteriaBuscada = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try {
            consulta = con.prepareStatement( "INSERT INTO TipoEstanteria (nombre,profundidad, altura, largo,empresa)" +
                            "VALUES ('"+nombre+"',"+profundidad+", "+altura+", "+largo+", "+idEmpresa+");",
                                        PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            System.out.println("Se agrego el TipoEstanteria: " + nombre);
            if(rs.next()) {
              TipoEstanteriaBuscada = new TipoEstanteria(rs.getInt(1) , nombre , profundidad , altura , largo);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return TipoEstanteriaBuscada;
    }



    public boolean Delete(int idTipoEstanteria  , int idEmpresa) throws SQLException, ExcepcionesLogica {
        if(idTipoEstanteria <= 0){throw new ExcepcionesLogica("idTipoEstanteria incorrecto");}
        boolean borrado = false;
        if (idTipoEstanteria <= 0) {
            throw new ExcepcionesLogica("idTipoEstanteria incorrecto");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM TipoEstanteria where idTipoEstanteria = "+idTipoEstanteria +  " and empresa =  "+ idEmpresa+";";
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

    public TipoEstanteria Search(int idTipoEstanteria , int idEmpresa) throws SQLException, ExcepcionesLogica {
        TipoEstanteria TipoEstanteriaBuscada = null;

        if(idTipoEstanteria <= 0){throw new ExcepcionesLogica("idTipoEstanteria incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoEstanteria WHERE idTipoEstanteria = "+idTipoEstanteria+" AND empresa = "+idEmpresa;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()) {
                TipoEstanteriaBuscada = new TipoEstanteria(rs.getInt(1) , rs.getString(2),
                        rs.getDouble(3), rs.getDouble(4), rs.getDouble(5));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return TipoEstanteriaBuscada;
    }

    public List<TipoEstanteria> SearchAll( int idEmpresa) throws SQLException, ExcepcionesLogica {
        List<TipoEstanteria> lista = new ArrayList<>();
        TipoEstanteria tipoEstanteriaBuscada = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoEstanteria WHERE empresa = "+idEmpresa;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()) {
                tipoEstanteriaBuscada = new TipoEstanteria(rs.getInt(1) , rs.getString(2),
                        rs.getDouble(3), rs.getDouble(4), rs.getDouble(5));
                lista.add(tipoEstanteriaBuscada);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return lista;
    }

    public TipoEstanteria Update(int idTipoEstanteria, int idEmpresa,String nombre , double profundidad , double altura , double largo)
                        throws SQLException, ExcepcionesLogica {
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idTipoEstanteria <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        if(nombre.length() > 50 ){throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        TipoEstanteria te = null;
        try{
            String query = "UPDATE TipoEstanteria SET nombre = '"+nombre+"', " +
                    "profundidad = "+profundidad+", altura ="+altura+",  largo="+largo+"   WHERE empresa ="+idEmpresa+" AND " +
                    "idTipoEstanteria ="+idTipoEstanteria+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
             te = new TipoEstanteria(idTipoEstanteria,nombre,profundidad,altura,largo);
            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return te;
    }


}