package Modulo;

import Exceptions.InvalidMicroException;
import Exceptions.InvalididTravelException;

public class Viaje {
    private int idViaje;
    private Ruta rutaAsignada;
    private Micro microAsignado;
    private String fecha;
    private int prioridad; // 1: Alta, 2: Media, 3: Baja

    public Viaje(int idViaje, Ruta rutaAsignada, Micro microAsignado, String fecha) {
        if (idViaje < 0) {
            throw new InvalididTravelException("El ID del viaje no puede ser negativo.");
        }
        if (rutaAsignada == null) {
            throw new InvalididTravelException("Todo viaje debe tener una ruta asignada.");
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new InvalididTravelException("La fecha del viaje no puede estar vacía.");
        }
        if (microAsignado != null && !microAsignado.isDisponible()) {
            throw new InvalidMicroException("El micro " + microAsignado.getPatente() + " no está disponible.");
        }

        this.idViaje = idViaje;
        this.rutaAsignada = rutaAsignada;
        this.microAsignado = microAsignado;
        this.fecha = fecha.trim();
        this.prioridad = 2;
    }

    public void setMicroAsignado(Micro microAsignado) {
        if (microAsignado != null && !microAsignado.isDisponible()) {
            throw new InvalidMicroException("No se puede asignar: El micro " + microAsignado.getPatente() + " no está disponible.");
        }
        this.microAsignado = microAsignado;
    }

    public int getIdViaje() { return idViaje; }
    public Ruta getRutaAsignada() { return rutaAsignada; }
    public void setRutaAsignada(Ruta rutaAsignada) {
        if (rutaAsignada == null) throw new InvalididTravelException("No se puede desasignar la ruta de un viaje activo.");
        this.rutaAsignada = rutaAsignada;
    }
    public Micro getMicroAsignado() { return microAsignado; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) throw new InvalididTravelException("La fecha modificada no puede estar vacía.");
        this.fecha = fecha.trim();
    }
    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    @Override
    public String toString() {
        String infoMicro = (microAsignado != null) ? microAsignado.getPatente() : "SIN ASIGNAR";
        return "Viaje #" + idViaje + " | " + rutaAsignada + " | Micro: " + infoMicro + " | Fecha: " + fecha;
    }
}
