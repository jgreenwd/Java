 // #1. 1. Write a function that returns the largest element in a list.
    public static int exer(ArrayList list) {
        int max_ = (int) list.get(0), temp = 0;
        
        for(int i = 0; i < list.size(); i++) {
            temp = (int)list.get(i);
            if (temp > max_) max_ = temp;
        }
        
        return max_;
    }
