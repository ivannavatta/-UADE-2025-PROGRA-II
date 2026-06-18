package Exceptions;


public class FullStructureException extends RuntimeException {
    public String getMessage() {
        return "Error: La estructura estática alcanzó su límite de capacidad (Overflow). ";
    }
}
