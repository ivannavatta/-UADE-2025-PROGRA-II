package Modulo;

import Exceptions.InvalidMicroException;

public class Micro {
    private String patente;              // Identificador único (patente o interno)
    private String tipo;                 // Ejecutivo, Semi-cama, Cama
    private boolean disponible;          // Control de estado de disponibilidad
    private int contadorAsignaciones;    // Rastreo de cantidad de viajes asignados

    public Micro(String patente, String tipo) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new InvalidMicroException("La patente o interno del micro no puede estar vacía.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new InvalidMicroException("El tipo de micro no puede estar vacío.");
        }

        String tipoUpper = tipo.toUpperCase().trim();
        // Validación estricta de los tipos permitidos por el enunciado
        if (!tipoUpper.equals("EJECUTIVO") && !tipoUpper.equals("SEMI-CAMA") && !tipoUpper.equals("CAMA")) {
            throw new InvalidMicroException("Tipo de micro inválido. Los permitidos son: Ejecutivo, Semi-cama o Cama.");
        }

        this.patente = patente.toUpperCase().trim();
        this.tipo = tipoUpper;
        this.disponible = true;          // Todo micro inicia operativo y disponible
        this.contadorAsignaciones = 0;
    }

    // Getters y Setters con validaciones internas
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

    public int getContadorAsignaciones() {
        return contadorAsignaciones;
    }

    @Override
    public String toString() {
        return "Micro [" + patente + "] Tipo: " + tipo + " | Disponible: " + (disponible ? "SÍ" : "NO") + " | Viajes: " + contadorAsignaciones;
    }
}
