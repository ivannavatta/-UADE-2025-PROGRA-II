package Modulo;

import Exceptions.InvalidMicroException;

public class Micro {
    private String patente;
    private String tipo;
    private boolean disponible;
    private int contadorAsignaciones;

    public Micro(String patente, String tipo) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new InvalidMicroException("La patente o interno del micro no puede estar vacía.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new InvalidMicroException("El tipo de micro no puede estar vacío.");
        }

        String tipoUpper = tipo.toUpperCase().trim();
        if (!tipoUpper.equals("EJECUTIVO") && !tipoUpper.equals("SEMI-CAMA") && !tipoUpper.equals("CAMA")) {
            throw new InvalidMicroException("Tipo de micro inválido. Los permitidos son: Ejecutivo, Semi-cama o Cama.");
        }

        this.patente = patente.toUpperCase().trim();
        this.tipo = tipoUpper;
        this.disponible = true;
        this.contadorAsignaciones = 0;
    }

    public String getPatente() {
        return patente;
    }

    public String getTipo() {

        return tipo;
    }

    public boolean isDisponible() {

        return disponible;
    }

    public void setDisponible(boolean disponible) {

        this.disponible = disponible;
    }

    public void incrementarAsignaciones() {
        this.contadorAsignaciones++;
    }

    public int getContadorAsignaciones() {

        return contadorAsignaciones;
    }

    @Override
    public String toString() {
        return "Micro [" + patente + "] Tipo: " + tipo + " | Disponible: " + (disponible ? "SÍ" : "NO") + " | Viajes: " + contadorAsignaciones;
    }
}
