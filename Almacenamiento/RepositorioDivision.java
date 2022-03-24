package persistencia.Almacenamiento;

import logica.ExcepcionesLogica;
import logica.MODULO_Almacenamiento.*;
import persistencia.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RepositorioDivision {
    public Division Add(String nombre, int idDivisionPadre,
                        int empresa, int deposito, List<Estanteria> listaestanterias, List<MercaderiaAcumulada> mercaderiaAcumulada) throws SQLException, ExcepcionesLogica {
        if (idDivisionPadre <= 0) {
            throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
        }
        Division dv = null;
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        con.setAutoCommit(false);
        try {
            consulta = con.prepareStatement("INSERT INTO Division (nombre, padre,deposito,empresa) VALUES" +
                    "('" + nombre + "', " + idDivisionPadre + ", " + deposito + ", " + empresa + ");", PreparedStatement.RETURN_GENERATED_KEYS);
            consulta.executeUpdate();
            ResultSet rs = consulta.getGeneratedKeys();
            if (rs != null && rs.next()) {
                for (int i = 0; i < listaestanterias.size(); i++) {
                    Statement consulta2;
                    String query = "UPDATE Estanteria SET division = '" + rs.getInt(1) + "' WHERE idEstanteria =" + listaestanterias.get(i).getIdEstanteria() + " AND empresa = " + empresa;
                    consulta2 = con.createStatement();
                    consulta2.executeUpdate(query);
                }

                for (int i = 0; i < mercaderiaAcumulada.size(); i++) {
                    Statement consulta3;
                    String query2 = "UPDATE MercaderiaAcumulada SET division = '" + rs.getInt(1) + "' WHERE idMercaderiaAcumulada =" + mercaderiaAcumulada.get(i).getIdMercaderiaAcumulada() + " AND empresa = " + empresa;
                    consulta3 = con.createStatement();
                    consulta3.executeUpdate(query2);
                }
                Division div = new Division(idDivisionPadre);
                dv = new Division(rs.getInt(1), nombre, div, listaestanterias, mercaderiaAcumulada);
                System.out.println("Se creo el Division nro" + rs.getInt(1));
                con.commit();
            }
        } catch (SQLException ex) {
            con.rollback();
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return dv;
    }

    public boolean Delete(int idDivision, int idEmpresa) throws SQLException, ExcepcionesLogica {
        if (idDivision <= 0) {
            throw new ExcepcionesLogica("idDivision incorrecto");
        }
        boolean borrado = false;
        if (idDivision <= 0) {
            throw new ExcepcionesLogica("idDivision incorrecto");
        }
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("empresa incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "DELETE FROM Division WHERE idDivision = " + idDivision + " and empresa =  " + idEmpresa + ";";
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
    public Division Search(int idDivision, int idEmpresa, int idDeposito) throws SQLException, ExcepcionesLogica {
        Division division = null;
        List<MercaderiaAcumulada> listaMA = new ArrayList<>();
        List<Estanteria> listaEstanterias = new ArrayList<>();
        if (idDivision <= 0) {
            throw new ExcepcionesLogica("idDivision incorrecto");
        }
        if (idDeposito <= 0) {
            throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT d.*  FROM Division d" + " WHERE idDivision =" + idDivision + " AND d.empresa =" + idEmpresa;
        try {
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            if (rs.next() && rs != null) {
                Statement consulta2;
                String query2 = "SELECT  e.* FROM Estanteria e WHERE e.division =" + rs.getInt(1);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                if (rs2.next() && rs2 != null) {
                    Division div = new Division(rs2.getInt(2));
                    Estanteria est = new Estanteria(rs2.getInt(1), rs2.getString(3), div);
                    listaEstanterias.add(est);
                }
                Statement consulta3;
                String query3 = "SELECT  mc.* FROM MercaderiaAcumulada mc WHERE  mc.division=" + rs.getInt(1);
                consulta3 = con.createStatement();
                ResultSet rs3 = consulta3.executeQuery(query3);
                if (rs3.next() && rs3 != null) {
                    MercaderiaAcumulada mc = new MercaderiaAcumulada(rs3.getInt(1), rs.getString(2));
                    listaMA.add(mc);
                }
                Division divPadre = new Division(rs.getInt(3));
                division = new Division(rs.getInt(1), rs.getString(2), divPadre, listaEstanterias, listaMA);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return division;
    }

    // Si quiero buscar en todo los depositos agrego un 0

    public List<Division> SearchAll(int idEmpresa, int idDeposito) throws SQLException, ExcepcionesLogica {
        Division division = null;
        List<MercaderiaAcumulada> listaMA = new ArrayList<>();
        List<Estanteria> listaEstanterias = new ArrayList<>();
        List<Division> listaDivisiones = new ArrayList<>();
        if (idEmpresa <= 0) {
            throw new ExcepcionesLogica("idEmpresa incorrecto");
        }
        if (idDeposito <= 0) {
            throw new ExcepcionesLogica("EL idDeposito debe ser Positivo");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        Statement consulta;
        String query = "SELECT d.*  FROM Division d" + " WHERE d.empresa =" + idEmpresa;

        if (idDeposito != 0) {
            query = "SELECT d.*  FROM Division d" + " WHERE d.empresa =" + idEmpresa + " AND d.deposito=" + idDeposito;
        }
        try {
            consulta = con.createStatement();
            ResultSet rs = consulta.executeQuery(query);
            while (rs.next() && rs != null) {
                Statement consulta2;
                String query2 = "SELECT  e.* FROM Estanteria e WHERE e.division =" + rs.getInt(1);
                consulta2 = con.createStatement();
                ResultSet rs2 = consulta2.executeQuery(query2);
                if (rs2.next() && rs2 != null) {
                    Division div = new Division(rs2.getInt(2));
                    Estanteria est = new Estanteria(rs2.getInt(1), rs2.getString(3), div);
                    listaEstanterias.add(est);
                }
                Statement consulta3;
                String query3 = "SELECT  mc.* FROM MercaderiaAcumulada mc WHERE  mc.division=" + rs.getInt(1);
                consulta3 = con.createStatement();
                ResultSet rs3 = consulta3.executeQuery(query3);
                if (rs3.next() && rs3 != null) {
                    MercaderiaAcumulada mc = new MercaderiaAcumulada(rs3.getInt(1), rs3.getString(2));
                    listaMA.add(mc);
                }
                Division divPadre = new Division(rs.getInt(3));
                division = new Division(rs.getInt(1), rs.getString(2), divPadre, listaEstanterias, listaMA);
                listaDivisiones.add(division);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }
        return listaDivisiones;
    }


    public Division Update(int idDivision, String nombre, int idDivisionPadre,
                           int empresa, int deposito, List<Estanteria> listaestanterias, List<MercaderiaAcumulada> mercaderiaAcumulada)
            throws SQLException, ExcepcionesLogica {
        if (empresa <= 0) {
            throw new ExcepcionesLogica("idEmpresa incorrecto");
        }
        if (nombre.length() <= 0) {
            throw new ExcepcionesLogica("nombre incorrecto");
        }
        Conexion cnn = new Conexion();
        Connection con = cnn.getConection();
        PreparedStatement consulta;
        Division div = null;
        try {
            Division divP = new Division(idDivisionPadre);
            String query = "UPDATE Division SET  nombre = '" + nombre + "', padre = " + idDivisionPadre + "," +
                    "empresa = " + empresa + " ,deposito =" + deposito + " " +
                    " WHERE idDivision=" + idDivision + ";";
            System.out.println(query);
            consulta = con.prepareStatement(query);
            div = new Division(idDivision, nombre, divP, listaestanterias, mercaderiaAcumulada);

            consulta.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            cnn.cerrar();
        }

        return div;
    }
}

