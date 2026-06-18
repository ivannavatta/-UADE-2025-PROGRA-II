package Modulo;

import Exceptions.InvalidRuteException;

public class Ruta {
    private int idRuta;
    private int idOrigen;
    private int idDestino;

    public Ruta(int idRuta, int idOrigen, int idDestino) {
        if (idRuta < 0) {
            throw new InvalidRuteException("El ID de la ruta no puede ser negativo.");
        }
        if (idOrigen == idDestino) {
            throw new InvalidRuteException("El origen y el destino no pueden ser la misma terminal.");
        }
        if (idOrigen < 0 || idDestino < 0) {
            throw new InvalidRuteException("Los IDs de las terminales de origen y destino deben ser válidos.");
        }

        this.idRuta = idRuta;
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
    }

    public int getIdRuta() {

        return idRuta;
    }

    public int getIdOrigen() {

        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        if (idOrigen == this.idDestino) {
            throw new InvalidRuteException("No se puede cambiar el origen al mismo ID que el destino.");
        }
        this.idOrigen = idOrigen;
    }

    public int getIdDestino() {

        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        if (idDestino == this.idOrigen) {
            throw new InvalidRuteException("No se puede cambiar el destino al mismo ID que el origen.");
        }
        this.idDestino = idDestino;
    }

    public boolean esIgualA(int origen, int destino) {

        return this.idOrigen == origen && this.idDestino == destino;
    }

    @Override
    public String toString() {
        return "Ruta #" + idRuta + " (Origen ID: " + idOrigen + " -> Destino ID: " + idDestino + ")";
    }
}