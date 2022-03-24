package logica.MODULO_Almacenamiento;

import java.util.List;

public class BultoRecepcion {
    private int idBultoRecepcion;
    private String codigoBulto;
    private TipoBulto tipoBulto;
    private String urlEtiqueta;
    private List<ArticuloCantidad_BultoRecepcion> articuloCantidad;



    public BultoRecepcion(int idBultoRecepcion, String codigoBulto, TipoBulto tipoBulto, String urlEtiqueta, List<ArticuloCantidad_BultoRecepcion> articuloCantidad) {
        this.idBultoRecepcion = idBultoRecepcion;
        this.codigoBulto = codigoBulto;
        this.tipoBulto = tipoBulto;
        this.urlEtiqueta = urlEtiqueta;
        this.articuloCantidad = articuloCantidad;
    }

    public BultoRecepcion(int idBultoRecepcion) {
        this.idBultoRecepcion = idBultoRecepcion;
    }

    public BultoRecepcion(String codigoBulto){ this.codigoBulto = codigoBulto;}

    public int getIdBultoRecepcion() {
        return idBultoRecepcion;
    }

    public void setIdBultoRecepcion(int idBultoRecepcion) {
        this.idBultoRecepcion = idBultoRecepcion;
    }

    public String getCodigoBulto() {
        return codigoBulto;
    }

    public void setCodigoBulto(String codigoBulto) {
        this.codigoBulto = codigoBulto;
    }

    public TipoBulto getTipoBulto() {
        return tipoBulto;
    }

    public void setTipoBulto(TipoBulto tipoBulto) {
        this.tipoBulto = tipoBulto;
    }

    public String getUrlEtiqueta() {
        return urlEtiqueta;
    }

    public void setUrlEtiqueta(String urlEtiqueta) {
        this.urlEtiqueta = urlEtiqueta;
    }

    public List<ArticuloCantidad_BultoRecepcion> getArticuloCantidad() {
        return articuloCantidad;
    }

    public void setArticuloCantidad(List<ArticuloCantidad_BultoRecepcion> articuloCantidad) {
        this.articuloCantidad = articuloCantidad;
    }
}
