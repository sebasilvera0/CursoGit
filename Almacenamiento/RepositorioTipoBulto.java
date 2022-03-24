package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.TipoBulto;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTipoBulto {

    public TipoBulto Add(String nombre, int empresa , double profundidad , double altura , double largo) throws SQLException, ExcepcionesLogica {
        if (nombre.length() > 50) {
            throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");
        }
        if (empresa <= 0) {
            throw new ExcepcionesLogica("El numero de empresa debe ser positivo");
        }
        TipoBulto tb = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try {
            consulta = con.prepareStatement("INSERT INTO TipoBulto (nombre, profundidad, altura, largo, empresa) " +
                    "VALUES ('"+nombre + "',"+profundidad+" , "+altura+" ,"+largo+", "+empresa+");", PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs.next()){
               tb = new TipoBulto(rs.getInt(1),nombre,profundidad,altura,largo);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return tb;
    }

    public boolean Delete(int idTipoBulto  , int empresa) throws SQLException, ExcepcionesLogica {
        if(idTipoBulto <= 0){throw new ExcepcionesLogica("idTipoEstanteria incorrecto");}
        boolean borrado = false;
        if (idTipoBulto <= 0) {
            throw new ExcepcionesLogica("idTipoBulto incorrecto");
        }
        if (empresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM TipoBulto where idTipoBulto = "+idTipoBulto+";";
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




    public TipoBulto Search(int idTipoBulto, int empresa) throws SQLException, ExcepcionesLogica {
        TipoBulto TipoBultoBuscado = null;
        if(empresa <= 0){throw new ExcepcionesLogica("empresa incorrecto");}
        if(idTipoBulto <= 0){throw new ExcepcionesLogica("idTipoBulto incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoBulto WHERE idTipoBulto = "+idTipoBulto;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                TipoBultoBuscado = new TipoBulto(rs.getInt(1),rs.getString(2),rs.getDouble(3),
                        rs.getDouble(4) , rs.getDouble(5));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return TipoBultoBuscado;
    }

    public List<TipoBulto> SearchAll(int empresa) throws SQLException, ExcepcionesLogica {
        List<TipoBulto> listaTipoBultos = new ArrayList<>();
        if(empresa <= 0){throw new ExcepcionesLogica("empresa incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoBulto WHERE empresa = "+empresa;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                TipoBulto TipoBultoBuscado = new TipoBulto(rs.getInt(1),rs.getString(2),rs.getDouble(3),
                        rs.getDouble(4) , rs.getDouble(5));
                listaTipoBultos.add(TipoBultoBuscado);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return listaTipoBultos;
    }


    public TipoBulto Update(int TipoBulto , String nombre , double profundiad , double altura ,double largo, int empresa)
            throws SQLException, ExcepcionesLogica {
        if(empresa <= 0){throw new ExcepcionesLogica("empresa incorrecto");}
        if(nombre.length() > 50 ){throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        TipoBulto te = null;
        try{
            String query = "UPDATE TipoBulto SET nombre = '"+nombre+"' ," +
                    " profundidad = "+profundiad+" , altura= "+altura+" ,largo = "+largo+"" +
                    " WHERE empresa ="+empresa+" AND " +
                    " idTipoBulto ="+TipoBulto+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
           // te = new TipoBulto(TipoBulto,descripcion);
            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return te;
    }
}
