package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.*;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RepositorioMovimiento {
        public Movimiento Add(int idArticulo , double cantidad , int idUsuario , int idTipoMovimiento , int idDeposito , int idEmpresa) throws SQLException, ExcepcionesLogica {
            if (idDeposito<=0) {
                throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
            }
            if (idArticulo <= 0) {
                throw new ExcepcionesLogica("El  idMovimiento debe ser positivo");
            }
            if (idUsuario <= 0) {
                throw new ExcepcionesLogica("El  idUsuario debe ser positivo");
            }
            if (idTipoMovimiento <= 0) {
                throw new ExcepcionesLogica("El  idTipoMovimiento debe ser positivo");
            }
            if (idEmpresa <= 0) {
                throw new ExcepcionesLogica("El  idEmpresa debe ser positivo");
            }
            Movimiento mov = null;
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            PreparedStatement consulta;
            try{
                consulta = con.prepareStatement("INSERT INTO Movimiento (articulo, cantidad,usuario,tipoMovimiento,deposito,empresa) VALUES" +
                        "("+idArticulo+", "+cantidad+", "+idUsuario+", "+idTipoMovimiento+", "+idDeposito+", "+idEmpresa+");", PreparedStatement.RETURN_GENERATED_KEYS);
                consulta.executeUpdate();
                Date date = new Date();
                Articulo art = new Articulo(idArticulo);
                Usuario usu = new Usuario(idUsuario);
                TipoMovimiento tm = new TipoMovimiento(idTipoMovimiento);
                ResultSet rs = consulta.getGeneratedKeys();
                if(rs != null && rs.next()) {
                     mov = new Movimiento(rs.getInt(1), art, cantidad, date, usu, tm);
                    System.out.println("Se creo el Movimiento nro" + rs.getInt(1) + "para el articulo: " + idArticulo);
                }
            } catch( SQLException ex){
                System.out.println(ex.getMessage());
            } finally {
                cnn.cerrar();
            }
            return mov;
        }

    public boolean Delete(int idMovimiento  , int idEmpresa) throws SQLException, ExcepcionesLogica {
        if(idMovimiento <= 0){throw new ExcepcionesLogica("idMovimiento incorrecto");}
        boolean borrado = false;
        if (idMovimiento <= 0) {
            throw new ExcepcionesLogica("idMovimiento incorrecto");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM Movimiento WHERE idMovmiento = "+idMovimiento +  " and empresa =  "+ idEmpresa+";";
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
        


        //Traigo objetos mas completos
    public Movimiento Search(int idMovimiento , int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
        Movimiento mov = null;
        if(idMovimiento <= 0){throw new ExcepcionesLogica("idMovimiento incorrecto");}
        if (idDeposito<=0) {
            throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT m.* , ar.codigoArticulo , ar.nombre , u.idUsuario , u.nombre , u.apellido  , tm.idTipoMovimiento, tm.descripcion FROM Movimiento m\n" +
                " INNER JOIN Articulo ar ON  ar.idArticulo = m.articulo\n" +
                " INNER JOIN Usuario u ON u.idUsuario = m.usuario\n" +
                " INNER JOIN TipoMovimiento tm ON m.tipoMovimiento = tm.idTipoMovimiento \n" +
                " WHERE m.idMovmiento ="+idMovimiento +"" +
                " AND m.deposito ="+idDeposito+ "" +
                " AND m.empresa ="+idEmpresa;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null) {
                Articulo art = new Articulo(rs.getInt(2),rs.getString(9), rs.getString(10));
                Usuario usu = new Usuario(rs.getInt(11),rs.getString(12),rs.getString(13));
                TipoMovimiento tm = new TipoMovimiento(rs.getInt(14),rs.getString(15));
                mov = new Movimiento(rs.getInt(1), art, rs.getInt(3), rs.getDate(4), usu, tm);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  mov;
    }

 // Si quiero buscar en todo los depositos agrego un 0

    public List<Movimiento> SearchAll(int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        List<Movimiento> listaMovi = new ArrayList<>();
        Movimiento mov = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idMovimiento incorrecto");}
        if (idDeposito<=0) {
            throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT m.* , ar.codigoArticulo , ar.nombre , u.idUsuario , u.nombre , u.apellido  , tm.idTipoMovimiento, tm.descripcion FROM Movimiento m\n" +
                " INNER JOIN Articulo ar ON  ar.idArticulo = m.articulo\n" +
                " INNER JOIN Usuario u ON u.idUsuario = m.usuario\n" +
                " INNER JOIN TipoMovimiento tm ON m.tipoMovimiento = tm.idTipoMovimiento \n" +
                " WHERE m.empresa ="+idEmpresa;

        if(idDeposito ==0){  query  = "SELECT m.* , ar.codigoArticulo , ar.nombre , u.idUsuario , u.nombre , u.apellido  , tm.idTipoMovimiento, tm.descripcion FROM Movimiento m\n" +
                       " INNER JOIN Articulo ar ON  ar.idArticulo = m.articulo\n" +
                       " INNER JOIN Usuario u ON u.idUsuario = m.usuario\n" +
                       " INNER JOIN TipoMovimiento tm ON m.tipoMovimiento = tm.idTipoMovimiento \n" +
                       " WHERE m.empresa ="+idEmpresa+" AND "+
                       "m.deposito ="+idDeposito+";";

               }
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next() && rs != null) {
                Articulo art = new Articulo(rs.getInt(2),rs.getString(9), rs.getString(10));
                Usuario usu = new Usuario(rs.getInt(11),rs.getString(12),rs.getString(13));
                TipoMovimiento tm = new TipoMovimiento(rs.getInt(14),rs.getString(15));
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


    public Movimiento Update(int idMovimiento,int idArticulo , double cantidad , int idUsuario , int idTipoMovimiento , int idDeposito , int idEmpresa)
            throws SQLException, ExcepcionesLogica {
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idArticulo <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        Movimiento mov = null;
        try{
            // String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechavenc);
            Date date = new Date();
            Articulo art = new Articulo(idArticulo);
            Usuario usu = new Usuario(idUsuario);
            TipoMovimiento tm = new TipoMovimiento(idTipoMovimiento);
            String query = "UPDATE Movimiento SET  articulo = "+idArticulo+", cantidad = "+cantidad+",usuario = "+idUsuario+"," +
                                                            "tipoMovimiento = "+idTipoMovimiento+" ,deposito ="+idDeposito+" "+
                                                            " WHERE idMovmiento="+idMovimiento+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
            mov = new Movimiento(idMovimiento, art, cantidad, date, usu, tm);

            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return mov;
    }

}

