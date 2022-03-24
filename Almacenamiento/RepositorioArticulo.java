package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.*;
import persistencia.Conexion;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositorioArticulo {
    public Articulo Add(String codigoArticulo , String nombre , String descripcion , int prioridadSalida, double stockReal ,
                       double  stockDisponible , double stockMinimo , String lote , Date fechavenc ,int idEmpresa , List<Reserva> listaReservas) throws SQLException, ExcepcionesLogica {
        Articulo art = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechavenc);
            System.out.println(fecha);
            consulta = con.prepareStatement("INSERT INTO Articulo (codigoArticulo, nombre,descripcion,prioridadSalida" +
                    ",stockReal,stockDisponible, stockMinimo,lote,fechaVencimiento,empresa) VALUES" +
                    "('"+codigoArticulo+"',  '"+nombre+"' ,'"+descripcion+"',"+prioridadSalida+", "+stockReal+", "+stockDisponible+"" +
                    " ,"+stockMinimo+", '"+lote+"', '"+fecha+"',"+idEmpresa+");", PreparedStatement.RETURN_GENERATED_KEYS);

            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs != null && rs.next()) {
                art = new Articulo(rs.getInt(1),codigoArticulo , nombre , descripcion , prioridadSalida,  stockReal ,
                  stockDisponible ,  stockMinimo ,  lote ,  fechavenc  , listaReservas);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return art;

    }


    public boolean Delete(int idArticulo  , int idEmpresa) throws SQLException, ExcepcionesLogica {
        if(idArticulo <= 0){throw new ExcepcionesLogica("idArticulo incorrecto");}
        boolean borrado = false;
        if (idArticulo <= 0) {
            throw new ExcepcionesLogica("idArticulo incorrecto");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM Articulo where idArticulo = "+idArticulo +  " and empresa =  "+ idEmpresa+";";
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

    public Articulo Search(int idArticulo , int idEmpresa) throws SQLException, ExcepcionesLogica {
        Articulo art = null;
        List<Reserva> listaR = new ArrayList<>();
        if(idArticulo <= 0){throw new ExcepcionesLogica("idReserva incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Articulo WHERE idArticulo =" +idArticulo+ " AND empresa="+idEmpresa;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null){
                Statement consulta2;
                String query2 = "Select * FROM Reserva WHERE articulo= "+idArticulo+ " AND empresa="+ idEmpresa;
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                while(rs2.next() && rs != null){
                    TipoReserva tr = new TipoReserva(rs2.getInt(2));
                    Reserva re = new Reserva(rs2.getInt(1),tr,rs2.getInt(2));
                    listaR.add(re);
                }
               art = new Articulo( rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
                       rs.getInt(5), rs.getDouble(6) , rs.getDouble(7), rs.getDouble(8),
                        rs.getString(9),rs.getDate(10),listaR);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  art;
    }



    //Busca todas las reservas de una empresa
    public List<Articulo> SearchAll(int idEmpresa ) throws SQLException, ExcepcionesLogica {
        Articulo art = null;
        List<Articulo> listaArticulos = new ArrayList<>();
        List<Reserva> listaR = new ArrayList<>();
            if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto NO puede ser negativo");}
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            Statement consulta;
            String query = "SELECT * FROM Articulo WHERE empresa="+idEmpresa;
            try{
                consulta = con.createStatement();
                ResultSet rs = consulta.executeQuery(query);
                while(rs.next() && rs != null){
                    Statement consulta2;
                    String query2 = "Select * FROM Reserva WHERE articulo= "+rs.getInt(1)+ " AND empresa="+ idEmpresa;
                    consulta2 = con.createStatement();
                    ResultSet rs2 = consulta2.executeQuery(query2);
                    while(rs2.next() && rs != null){
                        TipoReserva tr = new TipoReserva(rs2.getInt(2));
                        Reserva re = new Reserva(rs2.getInt(1),tr,rs2.getInt(2));
                        listaR.add(re);
                    }
                    art = new Articulo( rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
                            rs.getInt(5), rs.getDouble(6) , rs.getDouble(7), rs.getDouble(8),
                            rs.getString(9),rs.getDate(10),listaR);
                    listaArticulos.add(art);
                }
            } catch (SQLException ex){
                System.out.println(ex.getMessage());
            } finally{
                cnn.cerrar();
            }
            return  listaArticulos;
        }



        public Articulo Update(int idArticulo, String codigoArticulo , String nombre , String descripcion , int prioridadSalida, double stockReal ,
                               double  stockDisponible , double stockMinimo , String lote , Date fechavenc ,int idEmpresa , List<Reserva> listaReservas)
            throws SQLException, ExcepcionesLogica {
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idArticulo <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        if(nombre.length() > 50 ){throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        Articulo art = null;
        try{
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechavenc);

            String query = "UPDATE Articulo SET codigoArticulo = '"+codigoArticulo+"', nombre = '"+nombre+"' ,descripcion = '"+descripcion+"'" +
                    ",prioridadSalida = "+prioridadSalida+", stockReal ="+stockReal+",  stockReal="+stockReal+" , stockMinimo="+stockMinimo+"" +
                            ", lote = '"+lote+"', fechaVencimiento= '"+fecha+"' WHERE empresa ="+idEmpresa+" AND " +
                         "idArticulo ="+idArticulo+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
            art = new Articulo(idArticulo,codigoArticulo , nombre , descripcion , prioridadSalida,  stockReal ,
                    stockDisponible ,  stockMinimo ,  lote ,  fechavenc  , listaReservas);

            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return art;
    }



}
