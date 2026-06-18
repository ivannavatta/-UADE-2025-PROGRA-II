package Exceptions;

public class KeyNotFoundException extends RuntimeException {
    public String getMessage() {
        return  "Error: La clave no existe en la estructura.";
  }
}
