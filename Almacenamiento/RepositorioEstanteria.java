
package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.*;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEstanteria {
    public Estanteria Add( int division, String nombre, int idtipoEstanteria,
                          List<Nucleo> listanucleos, List<Estanteria> estanteriasAdyacentes, List<Nucleo> listanucleosLibres , int deposito
                            ,int empresa)  throws SQLException, ExcepcionesLogica {
        Estanteria est = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            consulta = con.prepareStatement("INSERT INTO Estanteria (division,nombre,idtipoEstanteria,deposito,empresa) VALUES" +
                    "("+division+" ,'"+nombre+"', "+idtipoEstanteria+","+deposito+","+empresa);
            consulta.executeUpdate() ;
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs != null && rs.next()) {
            for(int i=0; i < listanucleos.size(); i++) {
                PreparedStatement consulta2;
                if (listanucleos.get(i).getEstado() == true){ // si el nucleo esta disponible lo agrego a la lista
                    PreparedStatement consulta3;
                    consulta3 = con.prepareStatement("INSERT INTO Estanteria_NucleosLibres ( estanteria , nucleo , deposito , empresa) " +
                                                         "VALUES" + "(" + rs.getInt(1) + "," + listanucleos.get(i).getIdNucleo() + " ," +
                                                           "" + deposito + ", " + empresa + ")");
                    consulta3.executeUpdate();
                }
                consulta2 = con.prepareStatement("INSERT INTO Nucleo (estanteria) VALUES " + (rs.getInt(1)) + " WHERE " +
                        "deposito =" + deposito + " AND empresa = " + empresa + " idNucleo =" + listanucleos.get(i).getIdNucleo());
                consulta2.executeUpdate();
            }
            for(int i=0; i < estanteriasAdyacentes.size(); i++) {
                PreparedStatement consulta2;
                consulta2 = con.prepareStatement("INSERT INTO Estanteria_EstanteriaAdyacente ( idEstanteria , idEstanteriaAdyacente) " +
                        "VALUES" + "(" + rs.getInt(1) + "," + estanteriasAdyacentes.get(i).getIdEstanteria() + ")");
                consulta2.executeUpdate();
            }
            Division div = new Division(division);
            TipoEstanteria te = new TipoEstanteria(idtipoEstanteria);
             est = new Estanteria(rs.getInt(1),div,nombre,te , listanucleos ,estanteriasAdyacentes, listanucleosLibres );
             }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return est;
    }

    public void Delete(int idEstanteria) throws SQLException, ExcepcionesLogica {
        if(idEstanteria <= 0){throw new ExcepcionesLogica("idEstanteria incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            consulta = con.prepareStatement("DELETE FROM Estanteria where idEstanteria = "+idEstanteria);
            consulta.executeUpdate();
            System.out.println("Se elimino la Estanteria con id: " +idEstanteria);
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
    }

    public Estanteria Search(int idEstanteria ,int idEmpresa , int idDeposito) throws SQLException, ExcepcionesLogica {
        Estanteria eta = null;
        List<Estanteria> listaEstanteriasAdyacentes = new ArrayList<>();
        List<Nucleo> listaNucleos = new ArrayList<>();
        List<Nucleo> listaNucleosLibres = new ArrayList<>();
        if(idEstanteria <= 0){throw new ExcepcionesLogica("idEstanteria incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT e.* , te.nombre FROM Estanteria e\n" +
                " INNER JOIN Division d ON d.idDivision = e.division\n" +
                " INNER JOIN TipoEstanteria te ON te.idTipoEstanteria = e.tipoEstanteria\n" +
                " WHERE e.idEstanteria =  \n" + +idEstanteria;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next() && rs != null) {
                Division div = new Division(rs.getInt(1), rs.getString(8));
                TipoEstanteria te = new TipoEstanteria(rs.getInt(4) , rs.getString(7));

                // Cargo otras listas
                Statement consulta2;
                consulta2 = con.createStatement();
                String query2 = "SELECT * FROM Nucleo n WHERE n.estanteria =" +rs.getInt(1) +
                        " AND n.empresa =" +idEmpresa +
                        " AND n.deposito ="+idDeposito;
                ResultSet rs2 = consulta2.executeQuery(query2);
                while(rs2.next() && rs2 != null) {
                    Nucleo nu = new Nucleo(rs.getInt(1), rs2.getString(2));
                   if(rs2.getInt(6) == 1 ){
                       listaNucleosLibres.add(nu);
                    }
                   listaNucleos.add(nu);
                }
                Statement consulta3;
                consulta3 = con.createStatement();
                String query3 = "SELECT ee.* , e.nombre FROM Estanteria_EstanteriaAdyacente ee \n" +
                        "INNER JOIN Estanteria e  ON e.idEstanteria = ee.idEstanteriaAdyacente" +
                        " WHERE ee.idEstanteria ="+idEstanteria;
                ResultSet rs3 = consulta3.executeQuery(query3);
                while(rs3.next() && rs3 != null){
                     Division div2 = new Division(rs3.getInt(5));
                     Estanteria ea = new Estanteria(rs3.getInt(2), rs3.getString(2), div2);
                    listaEstanteriasAdyacentes.add(ea);
                }
                eta = new Estanteria(rs.getInt(1), div,rs.getString(3),te,listaNucleos,
                            listaEstanteriasAdyacentes, listaNucleosLibres);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return  eta;
    }

    //Busca todas las Estanterias del sistema
    public List<Estanteria> SearchAll(int idEmpresa ) throws SQLException, ExcepcionesLogica {
            Estanteria eta = null;
            List<Estanteria> listaEst = new ArrayList<>();
            List<Estanteria> listaEstanteriasAdyacentes = new ArrayList<>();
            List<Nucleo> listaNucleos = new ArrayList<>();
            List<Nucleo> listaNucleosLibres = new ArrayList<>();
            if(idEmpresa <= 0){throw new ExcepcionesLogica("idEstanteria incorrecto");}
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            Statement consulta;
            String query = "SELECT e.* , te.nombre FROM Estanteria e\n" +
                    " INNER JOIN Division d ON d.idDivision = e.division\n" +
                    " INNER JOIN TipoEstanteria te ON te.idTipoEstanteria = e.tipoEstanteria\n" +
                    " WHERE e.empresa =" +idEmpresa;
            try{
                consulta = con.createStatement();
                ResultSet rs = consulta.executeQuery(query);
                while(rs.next() && rs != null) {
                    Division div = new Division(rs.getInt(1), rs.getString(8));
                    TipoEstanteria te = new TipoEstanteria(rs.getInt(4) , rs.getString(7));

                    // Cargo otras listas
                    Statement consulta2;
                    consulta2 = con.createStatement();
                    String query2 = "SELECT * FROM Nucleo n WHERE n.estanteria =" +rs.getInt(1)+
                            " AND n.empresa =" +idEmpresa;
                    ResultSet rs2 = consulta2.executeQuery(query2);
                    while(rs2.next() && rs2 != null) {
                        Nucleo nu = new Nucleo(rs.getInt(1), rs2.getString(2));
                        if(rs2.getInt(6) == 1 ){
                            listaNucleosLibres.add(nu);
                        }
                        listaNucleos.add(nu);
                    }
                    Statement consulta3;
                    consulta3 = con.createStatement();
                    String query3 = "SELECT ee.* , e.nombre FROM Estanteria_EstanteriaAdyacente ee \n" +
                            "INNER JOIN Estanteria e  ON e.idEstanteria = ee.idEstanteriaAdyacente" +
                            " WHERE ee.idEstanteria ="+rs.getInt(1);
                    ResultSet rs3 = consulta3.executeQuery(query3);
                    while(rs3.next() && rs3 != null){
                        Division div2 = new Division(rs3.getInt(5));
                        Estanteria ea = new Estanteria(rs3.getInt(2), rs3.getString(2), div2);
                        listaEstanteriasAdyacentes.add(ea);
                    }
                    eta = new Estanteria(rs.getInt(1), div,rs.getString(3),te,listaNucleos,
                            listaEstanteriasAdyacentes, listaNucleosLibres);
                    listaEst.add(eta);
                }
            } catch (SQLException ex){
                System.out.println(ex.getMessage());
            } finally{
                cnn.cerrar();
            }
            return  listaEst;
        }
}



