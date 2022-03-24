package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Deposito;
import logica.MODULO_Almacenamiento.ModoDeposito;
import persistencia.Conexion;

import java.sql.*;

public class RepositorioDeposito {
    public Deposito Add(boolean esPrincipal, String direccion, int idEmpresaPertenece,
                    String nombre, int idModoDeposito, boolean activo , String nombreDepositoCliente) throws SQLException, ExcepcionesLogica {
        Deposito deposito = null;
        if(direccion.length() > 50){ throw new ExcepcionesLogica("La direccion debe tener 50 caracteres o menos");}
        if(nombre.length() > 50){ throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        String query = "INSERT INTO Deposito(esPrincipal, direccion, nombre, modoDeposito, activo, depositoCliente, empresa) " +
                "VALUES ("+esPrincipal+", '"+direccion+"', '"+nombre+"', "+idModoDeposito+", "+activo+", " +
                "'"+nombreDepositoCliente+"', "+idEmpresaPertenece+");";
        System.out.println(query);
        try{
            consulta = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if(rs != null && rs.next()){
                int idDeposito = rs.getInt(1);
                RepositorioModoDeposito rmd = new RepositorioModoDeposito();                ModoDeposito md = rmd.Search(idModoDeposito);//new ModoDeposito(idModoDeposito);
                deposito = new Deposito(idDeposito, nombreDepositoCliente, esPrincipal, direccion,
                        nombre, md, activo);
                return deposito;
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return null;
    }

    public boolean Delete(int idEmpresa, int idDeposito) throws SQLException, ExcepcionesLogica {
        boolean borrado = false;
        if(idDeposito <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}


        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try{

            String query = "DELETE FROM Deposito where empresa = "+idEmpresa+" AND " +
                        "idDeposito = "+idDeposito+";";
            System.out.println(query);
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
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

    public Deposito SearchCodigoCliente(int idEmpresa, String idDeposito) throws SQLException, ExcepcionesLogica {
        Deposito depositoBuscado = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idDeposito == ""){throw new ExcepcionesLogica("idDeposito incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM depositoCliente WHERE idDeposito = '"+idDeposito+"' AND empresa = "+idEmpresa+" AND activo = 1";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                Statement consulta2;
                String query2 = "SELECT * FROM ModoDeposito WHERE idModoDeposito = "+rs.getInt(6);
                System.out.println(query);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                if(rs2.next()){
                    ModoDeposito md = new ModoDeposito(rs2.getInt(1), rs2.getString(2));
                    depositoBuscado = new Deposito(rs.getInt(1), rs.getString(2),
                            rs.getBoolean(3), rs.getString(4), rs.getString(5), md,rs.getBoolean(7));
                }
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return depositoBuscado;
    }

    public Deposito Search(int idEmpresa, int idDeposito) throws SQLException, ExcepcionesLogica {
        Deposito depositoBuscado = null;
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idDeposito <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Deposito WHERE idDeposito = "+idDeposito+" AND empresa = "+idEmpresa+" AND activo = 1";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                Statement consulta2;
                String query2 = "SELECT * FROM ModoDeposito WHERE idModoDeposito = "+rs.getInt(6);
                System.out.println(query);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                if(rs2.next()){
                    ModoDeposito md = new ModoDeposito(rs2.getInt(1), rs2.getString(2));
                    depositoBuscado = new Deposito(rs.getInt(1), rs.getString(2),
                            rs.getBoolean(3), rs.getString(4), rs.getString(5), md,rs.getBoolean(7));
                }
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return depositoBuscado;
    }

    public Deposito SearchAll_Activos(int idEmpresa) throws SQLException, ExcepcionesLogica {
        Deposito depositoBuscado = null;

        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Deposito WHERE empresa = "+idEmpresa+" AND activo = 1";
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs != null && rs.next()){
                Statement consulta2;
                String query2 = "SELECT * FROM ModoDeposito WHERE idModoDeposito = "+rs.getInt(6);
                System.out.println(query);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                if(rs2.next()){
                    ModoDeposito md = new ModoDeposito(rs2.getInt(1), rs2.getString(2));
                    depositoBuscado = new Deposito(rs.getInt(1), rs.getString(2),
                            rs.getBoolean(3), rs.getString(4), rs.getString(5), md,rs.getBoolean(7));
                }

            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return depositoBuscado;
    }

    public Deposito Update(int idDeposito, int idEmpresa, boolean esPricipal, String direccion,
                       String nombre, int idModoDeposito, boolean activo, String codigoDepositoCliente) throws SQLException, ExcepcionesLogica {
        Deposito depositoActualizado = null;
        System.out.println("Entro");
        if(idEmpresa <= 0){throw new ExcepcionesLogica("idEmpresa incorrecto");}
        if(idDeposito <= 0){throw new ExcepcionesLogica("idDeposito incorrecto");}
        if(nombre.length() > 50 ){throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        if(direccion.length() > 50 ){throw new ExcepcionesLogica("La direccion debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try{
            String query = "UPDATE Deposito SET esPrincipal = "+esPricipal+", " +
                    "direccion = '"+direccion+"', nombre ='"+nombre+"', modoDeposito = "+ idModoDeposito +", " +
                    " activo = "+ activo +", depositoCliente = '"+ codigoDepositoCliente +"' WHERE empresa ="+idEmpresa+" AND idDeposito ="+idDeposito+";";
            System.out.println(query);
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            if(rs > 0){
                RepositorioModoDeposito rmd = new RepositorioModoDeposito();
                ModoDeposito md = rmd.Search(idModoDeposito);//new ModoDeposito(idModoDeposito);
                depositoActualizado = new Deposito(idDeposito, codigoDepositoCliente, esPricipal, direccion, nombre,
                        md, activo);
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return depositoActualizado;
    }
}
