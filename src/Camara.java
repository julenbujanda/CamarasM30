public class Camara {

    private String longitud;
    private String latitud;
    private String imagen;

    public Camara(String latitud, String longitud, String imagen) {
        this.longitud = longitud;
        this.latitud = latitud;
        this.imagen = imagen;
    }


    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getImagen() {
        return imagen;
    }
}
