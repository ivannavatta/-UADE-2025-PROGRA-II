package Exceptions;

public class EmptyStructureException extends RuntimeException {
    public String GetMessage() {
        return "Error: La estructura está vacía y no se pueden realizar operaciones de extracción.";
    }
}
