package persistencia.Almacenamiento;


import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Rol;
import logica.MODULO_Almacenamiento.Usuario;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class RepositorioUsuario {
    public void Add(int empresa, boolean estado ,String nombre, String apellido , String usuario , String contrasena , String mail , List<Rol> listaRoles) throws SQLException, ExcepcionesLogica {
        if(apellido.length() > 50){ throw new ExcepcionesLogica("El apellido debe tener 50 caracteres o menos");}
        if(nombre.length() > 50){ throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        if(usuario.length() > 50){ throw new ExcepcionesLogica("El usuario debe tener 50 caracteres o menos");}
        if(contrasena.length()>50) {throw new ExcepcionesLogica("La contrasena debe tener 50 caracteres o menos");}
        if(mail.length()>50){throw new ExcepcionesLogica("El mail debe tener 50 caracteres o menos");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        con.setAutoCommit(false);
        PreparedStatement consulta;
        String query = "INSERT INTO Usuarios(empresa, estado, nombre, apellido, usuario, contrasena ,mail) " +
                "VALUES ("+empresa+","+estado+", '"+nombre+"', '"+apellido+"', '"+usuario+"', '"+contrasena+"', '"+mail+"' );";
        System.out.println(query);
        try{
            consulta = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            int idUsuario = rs.getInt(1);
            if(listaRoles.size() != 0){
               for(Rol rol: listaRoles) {
                   int idRol = rol.getIdRol();
                   query = "INSERT INTO Usuario_Rol(idUsuario, idRol) VALUES" +
                           "(" + idUsuario + ", " + idRol + ");";
                   consulta = con.prepareStatement(query);
                   consulta.executeUpdate();
                   System.out.println(query);
               }
                   con.commit();
            } else {
                con.rollback();
                System.out.println("Rollback...");
            }
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
    }

    public boolean Delete(int empresa, int idUsuario) throws SQLException, ExcepcionesLogica {
        boolean borrado = false;
        if (idUsuario <= 0) {
            throw new ExcepcionesLogica("idDeposito incorrecto");
        }
        if (empresa <= 0) {
            throw new ExcepcionesLogica("idEmpresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try {
            String query = "DELETE FROM Usuario where empresa = " + empresa + " AND " +
                            "idUsuario = " + idUsuario + ";";
            System.out.println(query);
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

    public void DeleteLogico(int empresa, int idUsuario) throws SQLException, ExcepcionesLogica {
            if(empresa <= 0){throw new ExcepcionesLogica("empresa incorrecto");}
            Conexion cnn = new Conexion();
            Connection con = cnn.getConection();
            PreparedStatement consulta;
            try{
                consulta = con.prepareStatement("UPDATE Usuario SET estado = "+0+",   WHERE empresa ="+empresa+" AND idUsuario = "+idUsuario+";");
                consulta.executeUpdate();
                System.out.println("Se modifico el Estado del  Usuario con id: " +idUsuario);
            } catch( SQLException ex){
                System.out.println(ex.getMessage());
            } finally {
                cnn.cerrar();
            }
        }

    public Usuario Search(int empresa, int idUsuario) throws SQLException, ExcepcionesLogica {
        Usuario UsuarioBuscado = null;
        List<Rol> rolesUsuario = new ArrayList<>();
        if(empresa <= 0){throw new ExcepcionesLogica("empresa incorrecto");}
        if(idUsuario <= 0){throw new ExcepcionesLogica("idUsuario incorrecto");}
        Rol rol = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Usuario WHERE idUsuario = "+idUsuario+" AND empresa = "+empresa;
        System.out.println(query);
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if(rs.next()){
                Statement consulta2 = con.createStatement();
                String query2 = "SELECT * FROM Usuario_Rol WHERE idUsuario =" + rs.getInt(1);
                ResultSet rs2 = consulta2.executeQuery(query2);

                while(rs2.next()){
                    Statement consulta3 = con.createStatement();
                    String query3 = "SELECT * FROM Rol WHERE idRol =" + rs2.getInt(2);
                    ResultSet rs3 = consulta3.executeQuery(query3);
                    while(rs3.next()){

            //            Rol rol = new Rol(rs.getInt(1), rs.getString(2), listaPermisos);;
                      rolesUsuario.add(rol);
                    }
                    System.out.println(rolesUsuario.size());
                }
             /*  UsuarioBuscado = new Usuario(rs.getInt(1), rs.getInt(2),
                        rs.getBoolean(3), rs.getString(4), rs.getString(5), rs.getString(6),
                        rs.getString(7), rs.getString(8), rolesUsuario);
          */  }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return UsuarioBuscado;
    }



    public List<Rol> SearchRolesDeUnUsuario(int idUsuario , int empresa) throws SQLException, ExcepcionesLogica {
        List<Rol> RolsEmpresa = new ArrayList<Rol>();
        if(idUsuario <= 0){throw new ExcepcionesLogica("idUsuario incorrecto");}
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT idRol FROM Usuario_Rol WHERE idUsuario = "+idUsuario;
        try{
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while(rs.next()){
                RepositorioRol RepoAux = new RepositorioRol();
                Rol RolAgregar =RepoAux.Search(empresa,rs.getInt(2));
                RolsEmpresa.add(RolAgregar);
            }

        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        } finally{
            cnn.cerrar();
        }
        return RolsEmpresa;
    }


    //tengo que consultar si tengo que meter un agregar un metodo para buscar la lista de permisos asociada a los roles ????????


    /*public List<Empresa> SearchMany(int empresa) throws SQLException, ExcepcionesLogica {
        //Sin implementar
        return null;
    }*/

    public void Update(int idUsuario, int empresa, boolean esPricipal, String direccion,
                       String nombre) throws SQLException, ExcepcionesLogica {
        if(empresa <= 0){throw new ExcepcionesLogica("empresa incorrecto");}
        if(idUsuario <= 0){throw new ExcepcionesLogica("idUsuario incorrecto");}
        if(nombre.length() > 50 ){throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");}
        if(direccion.length() > 50 ){throw new ExcepcionesLogica("La direccion debe tener 50 caracteres o menos");}

        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        try{
            String query = "UPDATE Usuarios SET esPrincipal = "+esPricipal+", " +
                    "direccion = '"+direccion+"', nombre ='"+nombre+"' WHERE empresa ="+empresa+" AND " +
                    "idUsuario ="+idUsuario+";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
            consulta.executeUpdate();
        } catch( SQLException ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
    }



    public Usuario login( int idEmpresa , String Usuario , String contrasena) throws SQLException {
        Usuario usu = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        try{
            String query = "SELECT * from Usuarios where usuario = '"+Usuario+"' AND contrasena = '"+contrasena+"' AND idEmpresa = "+idEmpresa+"";

            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            System.out.println(query);
            if (rs.next()) {
                List<Rol> RolsEmpresa = SearchRolesDeUnUsuario(rs.getInt(1),idEmpresa);
             //   usu = new Usuario(rs.getInt(1),rs.getInt(2),rs.getBoolean(3),rs.getString(4),rs.getString(5),rs.getString(6),
                //        rs.getString(7),rs.getString(8),RolsEmpresa);
            }
        } catch(SQLException | ExcepcionesLogica ex){
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return usu;
    }
}





