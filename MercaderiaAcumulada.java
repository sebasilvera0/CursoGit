package logica.MODULO_Almacenamiento;

import java.util.List;

public class MercaderiaAcumulada {
    private int idMercaderiaAcumulada;
    private String descripcion;
    private List<ArticuloCantidad_MercaderiaAcumulada> articuloCantidad;

    public MercaderiaAcumulada(int idMercaderiaAcumulada, String descripcion, List<ArticuloCantidad_MercaderiaAcumulada> articuloCantidad) {
        this.idMercaderiaAcumulada = idMercaderiaAcumulada;
        this.descripcion = descripcion;
        this.articuloCantidad = articuloCantidad;
    }

    public MercaderiaAcumulada(int idMercaderiaAcumulada) {
        this.idMercaderiaAcumulada = idMercaderiaAcumulada;
    }

    public MercaderiaAcumulada(int idMercaderiaAcumulada, String descripcion) {
        this.idMercaderiaAcumulada = idMercaderiaAcumulada;
        this.descripcion = descripcion;
    }

    public int getIdMercaderiaAcumulada() {
        return idMercaderiaAcumulada;
    }

    public void setIdMercaderiaAcumulada(int idMercaderiaAcumulada) {
        this.idMercaderiaAcumulada = idMercaderiaAcumulada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ArticuloCantidad_MercaderiaAcumulada> getArticuloCantidad() {
        return articuloCantidad;
    }

    public void setArticuloCantidad(List<ArticuloCantidad_MercaderiaAcumulada> articuloCantidad) {
        this.articuloCantidad = articuloCantidad;
    }
}
