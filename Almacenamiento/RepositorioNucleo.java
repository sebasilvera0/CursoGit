
package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.*;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

    public class RepositorioNucleo {
        public Nucleo Add(String codigoNucleo, int estanteria, int accesibilidad , double espacioDispoible,
                          boolean estado , boolean automatizacion
                         , List<Guardable> listacontenido , int deposito, int empresa)  throws SQLException, ExcepcionesLogica {
            Nucleo no = null;
            Conexion cnn = new Conexion();
            Estanteria est = null;
            Connection con = cnn.getConection();
            PreparedStatement consulta;
            try{
                consulta = con.prepareStatement("INSERT INTO Nucleo ( codigoNucleo, estanteria, accesibilidad ,espacioDisponible" +
                        ",estado,automatizacion  ,deposito,empresa) VALUES" +
                        "('"+codigoNucleo+"',"+estanteria+" ,"+accesibilidad+", "+espacioDispoible+","+estado+", "+automatizacion+"" +
                        ","+deposito+","+empresa+");", PreparedStatement.RETURN_GENERATED_KEYS);
                consulta.executeUpdate() ;
                Date fecha = new Date();
                ResultSet rs = consulta.getGeneratedKeys();
                if(rs != null && rs.next()) {
                    if(estado) {
                        PreparedStatement consulta4;
                        consulta4 = con.prepareStatement("Insert INTO Estanteria_NucleosLibres (estanteria,nucleo,deposito,empresa)" +
                                        " VALUES ("+estanteria +" , "+rs.getInt(1)+", "+deposito +", "+empresa+")");
                        consulta4.executeUpdate();
                    }

                    for(int i=0; i < listacontenido.size(); i++) {
                        PreparedStatement consulta2;
                        System.out.println(listacontenido.get(i).getidGuardable());
                        consulta2 = con.prepareStatement("UPDATE Guardable SET  nucleo =" + rs.getInt(1)+ " WHERE idGuardable ="
                                                            +listacontenido.get(i).getidGuardable() + " AND deposito="+deposito+  " AND empresa =" +empresa);
                        /*consulta2 = con.prepareStatement( "INSERT INTO Guardable (nucleo) VALUES "+(rs.getInt(1))+" WHERE " +
                                "idGuardable =" +listacontenido.get(i).getidGuardable()+ "AND deposito="+deposito+  "AND empresa =" +empresa);*/
                        consulta2.executeUpdate();
                    }
                    Statement consulta3;
                    String query3 = "SELECT e.idEstanteria ,e.division , e.nombre , d.nombre " +
                            " FROM Estanteria e" +
                            " INNER JOIN Division d ON d.idDivision = e.division"+
                            " WHERE e.idEstanteria =" +estanteria;
                    consulta3 = con.createStatement();
                    ResultSet rs3 = consulta3.executeQuery(query3);
                    if(rs3.next()){
                        Division div = new Division(rs3.getInt(2), rs3.getString(4));
                        est = new Estanteria(rs3.getInt(1), rs3.getString(3), div);
                    }

                    no = new Nucleo(rs.getInt(1), codigoNucleo, est, accesibilidad ,  espacioDispoible
                            ,estado ,automatizacion , fecha ,listacontenido);
                }
            } catch( SQLException ex){
                System.out.println(ex.getMessage());
            } finally {
                cnn.cerrar();
            }
            return no;
        }

        public void Delete(int idNucleo) throws SQLException, ExcepcionesLogica {
            if(idNucleo <= 0){throw new ExcepcionesLogica("idNucleo incorrecto");}
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            PreparedStatement consulta;
            try{
                consulta = con.prepareStatement("DELETE FROM Nucleo where idNucleo = "+idNucleo);
                consulta.executeUpdate();
                System.out.println("Se elimino la Nucleo con id: " +idNucleo);
            } catch (SQLException ex){
                System.out.println(ex.getMessage());
            } finally{
                cnn.cerrar();
            }
        }


        public Nucleo Search(int idNucleo ,int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
            Nucleo Nucleo = null;
            List<Guardable> listaContenido = new ArrayList<>();
            if(idNucleo <= 0){throw new ExcepcionesLogica("idNucleo incorrecto");}
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            Statement consulta;
            String query = "SELECT n.*, e.nombre , e.division , d.nombre  FROM Nucleo  n " +
                            " INNER JOIN Estanteria e ON e.idEstanteria = n.estanteria"+
                            " INNER JOIN Division d ON d.idDivision = e.division"+
                            " WHERE n.idNucleo = " +idNucleo +
                            " AND n.empresa = " +idEmpresa+
                            " AND n.deposito = " +idDeposito;
            try{
                consulta = con.createStatement();
                ResultSet rs = consulta.executeQuery(query);
                if(rs.next() && rs != null) {
                    Division div = new Division(rs.getInt(12), rs.getString(13));
                    Estanteria est = new Estanteria(rs.getInt(3), rs.getString(11) , div);

                    Statement consulta2;
                    String query2 = "SELECT * FROM Guardable g WHERE g.nucleo = "+idNucleo +
                                    " AND g.deposito ="+ idDeposito+ " AND  g.empresa=" +idEmpresa;
                    consulta2 = con.createStatement();
                    ResultSet rs2 = consulta2.executeQuery(query2);
                    while(rs2.next() && rs2 != null) {
                        Guardable guar= new Guardable(rs2.getInt(1));
                        listaContenido.add(guar);
                    }
                    Nucleo = new Nucleo(rs.getInt(1),rs.getString(2),est , rs.getInt(4),
                                        rs.getDouble(5), rs.getBoolean(6), rs.getBoolean(7),
                                        rs.getDate(8) , listaContenido);

                }
            } catch (SQLException ex){
                System.out.println(ex.getMessage());
            } finally{
                cnn.cerrar();
            }
            return  Nucleo;
        }


        public List<Nucleo> SearchAll(int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
            Nucleo nuc = null;
            List<Guardable> listaContenido = new ArrayList<>();
            List<Nucleo> listaNucleos = new ArrayList<>();

                if(idEmpresa <= 0){throw new ExcepcionesLogica("idNucleo incorrecto");}
                Conexion cnn = new Conexion();
                Connection con = cnn.getConection();
                Statement consulta;
                String query = "SELECT n.*, e.nombre , e.division , d.nombre  FROM Nucleo  n " +
                        " INNER JOIN Estanteria e ON e.idEstanteria = n.estanteria"+
                        " INNER JOIN Division d ON d.idDivision = e.division"+
                        " WHERE n.empresa = " +idEmpresa;
                if(idDeposito !=0){
                    query = "SELECT n.*, e.nombre , e.division , d.nombre  FROM Nucleo  n " +
                            " INNER JOIN Estanteria e ON e.idEstanteria = n.estanteria"+
                            " INNER JOIN Division d ON d.idDivision = e.division"+
                            " WHERE n.empresa = " +idEmpresa+
                            " AND n.deposito = " +idDeposito;
                }
                try{
                    consulta = con.createStatement();
                    ResultSet rs = consulta.executeQuery(query);
                    while(rs.next() && rs != null) {
                        Division div = new Division(rs.getInt(12), rs.getString(13));
                        Estanteria est = new Estanteria(rs.getInt(3), rs.getString(11) , div);
                        Statement consulta2;
                        String query2 = "SELECT * FROM Guardable g WHERE g.nucleo = "+rs.getInt(1)+
                                " AND g.deposito ="+ idDeposito+ " AND  g.empresa=" +idEmpresa;
                        consulta2 = con.createStatement();
                        ResultSet rs2 = consulta2.executeQuery(query2);
                        while(rs2.next() && rs2 != null) {
                            Guardable guar= new Guardable(rs2.getInt(1));
                            listaContenido.add(guar);
                        }
                        nuc = new Nucleo(rs.getInt(1),rs.getString(2),est , rs.getInt(4),
                                rs.getDouble(5), rs.getBoolean(6), rs.getBoolean(7),
                                rs.getDate(8) , listaContenido);
                        listaNucleos.add(nuc);
                    }
                } catch (SQLException ex){
                    System.out.println(ex.getMessage());
                } finally{
                    cnn.cerrar();
                }
                return  listaNucleos;
            }

        public Nucleo Update( int idNucleo,String codigoNucleo, int estanteria, int accesibilidad , double espacioDisponible,
                             boolean estado , boolean automatizacion, List<Guardable> listacontenido , int deposito, int empresa) throws SQLException, ExcepcionesLogica {
            Nucleo no = null;
            if (codigoNucleo.length() > 50) {
                throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");
            }
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            Statement consulta;
            con.setAutoCommit(false);
            try {
                String query = "UPDATE Nucleo SET codigoNucleo = '" + codigoNucleo + "', estanteria = "+estanteria+",accesibilidad = "+accesibilidad+"," +
                        "espacioDisponible = "+espacioDisponible+" ,automatizacion ="+automatizacion+" "+
                        " ,deposito = "+deposito+ " WHERE idNucleo="+idNucleo+" AND empresa = "+empresa;
                consulta = con.createStatement();
                Date fecha = new Date();
                int rs = consulta.executeUpdate(query);
                System.out.println(query);
                if (rs > 0) {
                    Estanteria est = new Estanteria(estanteria);
                     no = new Nucleo(idNucleo, codigoNucleo,  est, accesibilidad , espacioDisponible, estado ,
                             automatizacion  ,fecha, listacontenido);
                    }
                    con.commit();

            } catch (SQLException ex) {
                con.rollback();
                System.out.println(ex.getMessage());
            } finally {
                cnn.cerrar();
            }
            return no;
        }


        }





