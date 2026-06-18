package Modulo;

import Exceptions.InvalidTerminalException;

public class Terminal {
    private int id;
    private String codigo;
    private String descripcion;

    public Terminal(int id, String codigo, String descripcion){
        if (id < 0) {
            throw new InvalidTerminalException("El ID de la terminal no puede ser negativo.");
        }
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new InvalidTerminalException("El código de la terminal no puede estar vacío.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new InvalidTerminalException("La descripción de la terminal no puede estar vacía.");
        }

        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getId(){
        return id;
    }

    public String getCodigo(){
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new InvalidTerminalException("El código modificado no puede estar vacío.");
        }
        this.codigo = codigo.toUpperCase().trim();
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new InvalidTerminalException("La descripción modificada no puede estar vacía.");
        }
        this.descripcion = descripcion.trim();
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + descripcion + " (ID: " + id + ")";
    }
}
