package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.TipoMovimiento;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTipoMovimiento {
    public TipoMovimiento Add(String descripcion, int empresa) throws SQLException, ExcepcionesLogica {
        if (descripcion.length() > 50) {
            throw new ExcepcionesLogica("La descripcion debe tener 50 caracteres o menos");
        }
        if (empresa <= 0) {
            throw new ExcepcionesLogica("El idEmpresa debe ser positivo");
        }
            TipoMovimiento tm = null;
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            PreparedStatement consulta;
            try {
                consulta = con.prepareStatement("INSERT INTO TipoMovimiento (descripcion, empresa) " +
                        "VALUES ('"+descripcion + "',"+ empresa +");", PreparedStatement.RETURN_GENERATED_KEYS);
                consulta.executeUpdate();
                ResultSet rs = consulta.getGeneratedKeys();
                if(rs.next()) {
                    tm = new TipoMovimiento(rs.getInt(1), descripcion);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                cnn.cerrar();
            }
            return tm;
        }


    public boolean Delete(int idTipoMovimiento  , int idEmpresa) throws SQLException, ExcepcionesLogica {
        if(idTipoMovimiento <= 0){throw new ExcepcionesLogica("idTipoMovimiento incorrecto");}
        boolean borrado = false;
        if (idTipoMovimiento <= 0) {
            throw new ExcepcionesLogica("idTipoMovimiento incorrecto");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM TipoMovimiento where idTipoMovimiento = "+idTipoMovimiento +  " and empresa =  "+ idEmpresa+";";
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



    public TipoMovimiento Search(int idTipoMovimiento, int idEmpresa) throws SQLException, ExcepcionesLogica {
        TipoMovimiento tipoMovimientoBuscado = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idTipoMovimiento <= 0){throw new ExcepcionesLogica("idTipoMovimiento incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoMovimiento WHERE idTipoMovimiento = "+idTipoMovimiento+" AND empresa = "+idEmpresa;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                tipoMovimientoBuscado = new TipoMovimiento(rs.getInt(1),rs.getString(2));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return tipoMovimientoBuscado;
    }

    public List<TipoMovimiento> SearchAll( int idEmpresa) throws SQLException, ExcepcionesLogica {
        List<TipoMovimiento> listatipoMovimientos = new ArrayList<>();
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM TipoMovimiento WHERE empresa = "+idEmpresa;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
              TipoMovimiento  tipoMovimientoBuscado = new TipoMovimiento(rs.getInt(1),rs.getString(2));
              listatipoMovimientos.add(tipoMovimientoBuscado);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return listatipoMovimientos;
    }



    public TipoMovimiento Update(int idTipoMovimiento, int idEmpresa, String descripcion)
            throws SQLException, ExcepcionesLogica {
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idTipoMovimiento <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        if(descripcion.length() > 50 ){throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        TipoMovimiento te = null;
        try{
            String query = "UPDATE TipoMovimiento SET descripcion = '"+descripcion+"' " +
                    " WHERE empresa ="+idEmpresa+" AND " +
                    "idTipoMovimiento ="+idTipoMovimiento+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
          te = new TipoMovimiento(idTipoMovimiento,descripcion);
            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return te;
    }
}

