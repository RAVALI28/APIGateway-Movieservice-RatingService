package com.ravali.spbt.Model;
import lombok.NoArgsConstructor;




@NoArgsConstructor
public class Rating {

    private long id;

     private String name;
     private double AvgRating;
     private int count;


    public Rating(Long id, String name, double avgRating, int count) {

        this.name = name;
        AvgRating = avgRating;
        this.count = count;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAvgRating() {
        return AvgRating;
    }

    public void setAvgRating(double avgRating) {
        AvgRating = avgRating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
