public class ArrayList<E>{
    private E[] array;
    private int size;
    private int capacity;

    //CONSTRUCTOR//
    public ArrayList(){
        capacity = 100;
        size = 0;

        array = (E[])new Object[capacity];
    }

    //Adder (Calls resize() method)
    public void add(E e){
        array[size] = e;
        size++;

        if(size == capacity-1){
            resize();
        }
    }
    //return maximum element of the array
    //method yanlis calisiyor.
    public int IntegerMax(){
        int max = (int)array[0];
        for(int i = 0; i < size; i++){
            if((int)array[i]> max){
                max = (int)array[i];
            }
        }
        return max;
    }

    public void resize(){
        E[] temp = (E[])new Object[capacity*2];
        for(int i=0 ; i < array.length ; i++){
            temp[i] = array[i];
        }
        array = temp;
        capacity = capacity*2;
    }

    //Getter
    public E get(int i){
        return array[i];
    }

    public int size(){
        return size;
    }
    //-
    public void setSize(int new_size){
        size = new_size;
    }

    //Setter
    public void set(int i , E e){
        array[i] = e;
    }


    /*
        This class is a basic implementation of ArrayList<>
     */
}
