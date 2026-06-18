package Interfaces;

public interface SimpleDictionaryADT {
    // Retorna el conjunto de claves
    SetADT getKeys();
    // Devuelve el valor asociado a esa key
    int get(int key);
    // Agrega un elemento a la estructura
    void add(int key, int value);
    // Elimina el elemento de la estructura
    void remove(int key);
    // Debe comprobar si la estructura tiene o no valores
    boolean isEmpty();
}
