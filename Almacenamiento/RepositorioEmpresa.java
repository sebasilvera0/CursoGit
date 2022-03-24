package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Empresa;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEmpresa {

    public Empresa Add(String nombre) throws SQLException, ExcepcionesLogica {
        Empresa empresa = null;
        if(nombre.length() > 50){ throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            String query = "INSERT INTO Empresa(nombre) VALUES ('"+nombre+"')";
            consulta = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            System.out.println(query);
            if(rs.next()){
                int idEmpresa = rs.getInt(1);
                empresa = new Empresa(idEmpresa, nombre);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return empresa;
    }

    public boolean Delete(int idEmpresa) throws SQLException, ExcepcionesLogica {
        boolean borrado = false;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try{
            String query = "DELETE FROM Empresa where idEmpresa = "+idEmpresa;
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            System.out.println(query);
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

     public Empresa Search(int idEmpresa) throws SQLException, ExcepcionesLogica {
        Empresa empresaBuscada = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Empresa WHERE idEmpresa = "+idEmpresa;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){

                empresaBuscada = new Empresa(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return empresaBuscada;
    }

 public List<Empresa> SearchAll() throws SQLException, ExcepcionesLogica {
        List<Empresa> empresasBuscadas = new ArrayList<>();

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Empresa";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while (rs.next()){
                Empresa empresaBuscada = new Empresa(rs.getInt(1), rs.getString(2));
                empresasBuscadas.add(empresaBuscada);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return empresasBuscadas;
    }

        /*  public Empresa Search(int idEmpresa) throws SQLException, ExcepcionesLogica {
        Empresa empresaBuscada = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Empresas WHERE idEmpresa = "+idEmpresa;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                Statement consulta2;
                String query2 = "SELECT * FROM Depositos WHERE idEmpresa = "+idEmpresa;
                System.out.println(query2);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                List<Deposito> depositos = new ArrayList<>();
                while ( rs2.next()) {
                    Statement consulta3;
                    String query3 = "SELECT * FROM ModosDeposito WHERE idModoDeposito = "+rs2.getInt(7);
                    System.out.println(query3);
                    consulta3 = con.createStatement();
                    ResultSet rs3 = consulta3.executeQuery(query3);
                    if(rs3.next()){
                        ModoDeposito md = new ModoDeposito(rs2.getInt(1), rs2.getString(2));
                        Deposito d = new Deposito(rs.getInt(1), rs.getString(2),
                                rs.getBoolean(3), rs.getString(4), rs.getString(5), md,rs.getBoolean(7));
                        depositos.add(d);
                    }
                }
                empresaBuscada = new Empresa(rs.getInt(1), rs.getString(2), depositos);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return empresaBuscada;
    }
*/

    /*public List<Empresa> SearchAll() throws SQLException, ExcepcionesLogica {
        List<Empresa> empresasBuscadas = new ArrayList<>();

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Empresas ";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while (rs.next()){
                Statement consulta2;
                String query2 = "SELECT * FROM Depositos WHERE idEmpresa = "+ rs.getInt(1);
                System.out.println(query2);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                List<Deposito> depositos = new ArrayList<>();
                while ( rs2.next()) {
                    Statement consulta3;
                    String query3 = "SELECT * FROM ModosDeposito WHERE idModoDeposito = "+rs2.getInt(7);
                    System.out.println(query3);
                    consulta3 = con.createStatement();
                    ResultSet rs3 = consulta3.executeQuery(query3);
                    if(rs3.next()){
                        ModoDeposito md = new ModoDeposito(rs3.getInt(1),rs3.getInt(3), rs3.getString(2) );
                        Deposito d = new Deposito(rs2.getInt(1), rs2.getInt(4),rs2.getString(6),rs2.getBoolean(2), rs2.getString(3),rs2.getString(5) , md);
                        depositos.add(d);
                    }
                }
                Empresa empresaBuscada = new Empresa(rs.getInt(1), rs.getString(2), depositos);
                empresasBuscadas.add(empresaBuscada);
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return empresasBuscadas;
    }*/

    public Empresa Update(String nombre, int idEmpresa) throws SQLException, ExcepcionesLogica {
        Empresa empresa = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(nombre.length() > 50 ){throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try{
            String query = "UPDATE Empresa SET nombre ='"+nombre+"' WHERE idEmpresa ="+idEmpresa;
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            System.out.println(query);
            if(rs > 0){
                empresa = new Empresa(idEmpresa, nombre);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return empresa;
    }
}
