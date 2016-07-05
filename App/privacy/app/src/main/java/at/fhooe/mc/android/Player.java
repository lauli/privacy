package at.fhooe.mc.android;

/**
 * Created by laureenschausberger on 16.06.16.
 */
public class Player {
    private String name;
    private int points;
    private int difference;

    public String getName(){
        return name;
    }

    public int getPoints(){
        return points;
    }

    public int getDifference(){
        return difference;
    }

    public void setName(String n){
        name = n;
    }

    public void setPoints(int p){
        points = p;
    }

    public void setDifference(int d){
        difference = d;
    }
}
