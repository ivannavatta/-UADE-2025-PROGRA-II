package Exceptions;


public class IndexOutOfBoundsException extends RuntimeException {
    public String getMessage() {
       return  "Error: El índice solicitado está fuera de los límites de la lista.";
    }
}
