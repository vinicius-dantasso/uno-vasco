package ufersa.edu.br.unoVasco.model.dataStructures;

public interface MyArrayListInterface<T> {
    int size();
    boolean isEmpty();
    boolean contains(T element);
    T get(int index);
    boolean add(T element);
    T remove(int index);
    int indexOf(T element);
}
