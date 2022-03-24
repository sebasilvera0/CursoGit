package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.TipoReserva;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTipoReserva {
    public TipoReserva Add(String descripcion, int empresa) throws SQLException, ExcepcionesLogica {
        TipoReserva tr = null;
        if (descripcion.length() > 50) {
            throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try {
            consulta = con.prepareStatement( "INSERT INTO TipoReserva (descripcion,empresa) " +
                    "VALUES ('"+descripcion + "' , "+empresa+");", PreparedStatement.RETURN_GENERATED_KEYS);

            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs.next()) {
                tr = new TipoReserva(rs.getInt(1), descripcion);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return  tr;
    }





        public boolean Delete(int idTipoReserva  , int idEmpresa) throws SQLException, ExcepcionesLogica {
            if(idTipoReserva <= 0){throw new ExcepcionesLogica("idTipoEstanteria incorrecto");}
            boolean borrado = false;
            if (idTipoReserva <= 0) {
                throw new ExcepcionesLogica("idTipoReserva incorrecto");
            }
            if (idEmpresa <= 0) {
                throw new ExcepcionesLogica("empresa incorrecto");
            }
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            Statement consulta;
            String query = "DELETE FROM TipoReserva where idTipoReserva = "+idTipoReserva+";";
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
    public TipoReserva Search(int idTipoReserva) throws SQLException, ExcepcionesLogica {
        TipoReserva tipoReserva = null;
        if(idTipoReserva <= 0){throw new ExcepcionesLogica("idTipoReserva incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoReserva WHERE idTipoReserva = "+idTipoReserva+"";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                tipoReserva = new TipoReserva(rs.getInt(1),rs.getString(2));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return tipoReserva;
    }

    public List<TipoReserva> SearchAll() throws SQLException, ExcepcionesLogica {
        List<TipoReserva> listaTipoReserva = new ArrayList<>();
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoReserva ";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                TipoReserva  tipoReserva = new TipoReserva(rs.getInt(1),rs.getString(2));
                listaTipoReserva.add(tipoReserva);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return listaTipoReserva;
    }


    public TipoReserva Update(int TipoReserva ,String descripcion, int empresa)
            throws SQLException, ExcepcionesLogica {
        if(empresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(descripcion.length() > 50 ){throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        TipoReserva te = null;
        try{
            String query = "UPDATE TipoReserva SET descripcion = '"+descripcion+"' " +
                    " WHERE empresa ="+empresa+" AND " +
                    " idTipoReserva ="+TipoReserva+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
            te = new TipoReserva(TipoReserva,descripcion);
            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return te;
    }
}
