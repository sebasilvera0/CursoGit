package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.Permiso;
import logica.MODULO_Almacenamiento.Rol;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RepositorioRol {

    public Rol Add(int empresa, String nombre, List<Permiso> listaPermiso) throws SQLException, ExcepcionesLogica {
        if (nombre.length() > 20) {
            throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        Rol rolcito = null;
        try {
            consulta = con.prepareStatement("INSERT INTO Rol(empresa, nombre) " + "VALUES (" + empresa + ",'" + nombre + "');",
                                                PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Se creo el tipo de Rol: " + nombre);
                int idRol = rs.getInt(1);
                if (listaPermiso.size() != 0) {
                    for (Permiso permiso : listaPermiso) {
                        int idPermiso = permiso.getIdPermiso();
                        String query = "INSERT INTO Rol_Permiso (idRol, idPermiso, empresa) VALUES" +
                                "(" + idRol + ", " + idPermiso + ","+empresa+");";
                        consulta = con.prepareStatement(query);
                        consulta.executeUpdate();
                        System.out.println(query);
                    }
                }
                rolcito = new Rol(idRol, nombre, listaPermiso);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return rolcito;
    }


    public boolean Delete(int empresa, int idRol) throws SQLException, ExcepcionesLogica {
        boolean borrado = false;
        if (idRol <= 0) {
            throw new ExcepcionesLogica("idRol incorrecto");
        }
        if (empresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM Rol where empresa = " + empresa + " AND idRol = " + idRol + ";";
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

    //dEVUELVE UN ROL
    public Rol Search(int empresa, int idRol) throws SQLException, ExcepcionesLogica {
        Rol RolBuscado = null;
        if (empresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        if (idRol <= 0) {
            throw new ExcepcionesLogica("idRol incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Rol WHERE idRol = " + idRol + " AND empresa = " + empresa;
        System.out.println(query);
        try {
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if (rs.next()) {
                Statement consulta2;
                consulta2 = con.createStatement();
                List<Permiso> listaPermiso = new ArrayList<>();
                String query2 = "SELECT p.* FROM Permiso p\n" +
                        " INNER JOIN Rol_Permiso rp ON p.idPermiso = rp.idPermiso\n" +
                        " WHERE rp.idRol =" + rs.getInt(1);
                ResultSet rs2 = consulta2.executeQuery(query2);
                while (rs2.next()) {
                    Permiso permiso = new Permiso(rs2.getInt(1),
                            rs2.getString(2), rs2.getString(3));
                    listaPermiso.add(permiso);
                }
                System.out.println(listaPermiso.size());
                RolBuscado = new Rol(rs.getInt(1), rs.getString(2), listaPermiso);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        if (RolBuscado == null) {
            throw new ExcepcionesLogica("El Rol no se encuentra en el sistema");
        }
        return RolBuscado;
    }

    public List<Rol> SearchAll(int empresa) throws SQLException, ExcepcionesLogica {
        List<Rol> rolesBuscados = new ArrayList<>();
        if (empresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT * FROM Rol WHERE empresa = " + empresa;
        System.out.println(query);
        try {
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while (rs.next()) {
                Statement consulta2;
                consulta2 = con.createStatement();
                List<Permiso> listaPermiso = new ArrayList<>();
                String query2 = "SELECT p.* FROM Permiso p\n" +
                        " INNER JOIN Rol_Permiso rp ON p.idPermiso = rp.idPermiso\n" +
                        " WHERE rp.idRol =" + rs.getInt(1);
                System.out.println(query2);
                ResultSet rs2 = consulta2.executeQuery(query2);
                while (rs2 != null && rs2.next()) {
                    Permiso permiso = new Permiso(rs2.getInt(1),
                            rs2.getString(2), rs2.getString(3));
                    listaPermiso.add(permiso);
                }
                System.out.println(listaPermiso.size());
                Rol RolBuscado = null;
                RolBuscado = new Rol(rs.getInt(1), rs.getString(2), listaPermiso);
                rolesBuscados.add((RolBuscado));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        // if(RolBuscado == null){throw new ExcepcionesLogica("El Rol no se encuentra en el sistema");}
        return rolesBuscados;
    }


    /*public List<Empresa> SearchMany(int empresa) throws SQLException, ExcepcionesLogica {
        //Sin implementar
        return null;
    }*/


    public Rol Update(int empresa, int idRol, String nombre, List<Permiso> listaPermiso) throws SQLException, ExcepcionesLogica {
        Rol rol = null;
        if (nombre.length() > 50) {
            throw new ExcepcionesLogica("El nombre debe tener 50 caracteres o menos");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        con.setAutoCommit(false);
        try {
            String query = "UPDATE Rol SET nombre = '" + nombre + "' WHERE idRol =" + idRol + " AND empresa = " + empresa;
            consulta = con.createStatement();
            int rs = consulta.executeUpdate(query);
            System.out.println(query);
            if (rs > 0) {
                Statement consulta2;
                String query2 = "DELETE FROM Rol_Permiso where idRol = " + idRol + " AND empresa =" + empresa;
                consulta2 = con.createStatement();
                consulta2.executeUpdate(query2);

                for (Permiso permiso : listaPermiso) {
                    int idPermiso = permiso.getIdPermiso();
                    String query3 = "INSERT INTO Rol_Permiso (idRol, idPermiso, empresa) VALUES" +
                            "(" + idRol + ", " + idPermiso + ","+ empresa+");";
                    consulta = con.prepareStatement(query);
                    consulta.executeUpdate(query3);
                }
                rol = new Rol(idRol);
                con.commit();
            }
        } catch (SQLException ex) {
            con.rollback();
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return rol;
    }
}

