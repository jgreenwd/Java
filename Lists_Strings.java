import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class Exercises {
 
/* -------- List/String programming exercises -------- */
// #1. 1. Write a function that returns the largest element in a list.
    public static int ex1(ArrayList list) {
        int max_ = (int) list.get(0), temp = 0;
        
        for(int i = 0; i < list.size(); i++) {
            temp = (int)list.get(i);
            if (temp > max_) max_ = temp;
        }
        
        return max_;
    }

 /* -------- -------- -------- */
 public static void main(String[] args) {
        // ... insert function call to exercise here ...
    }    
}
