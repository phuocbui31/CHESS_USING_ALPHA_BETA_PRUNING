package model;

public class Move {
    public int startLocation;
    public int targetLocation;

    public Move() {
        startLocation = -1;
        targetLocation = -1;
    }

    public Move(int startLocation, int targetLocation) {
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
    }

    @Override
    public String toString() {
        return "Move [startLocation=" + startLocation + ", targetLocation=" + targetLocation + "]";
    }
    public void setCurrentPosition( int startLocation){
        this.startLocation = startLocation;
    }
    public void setDestinationPosition(int targetLocation){
        this.targetLocation = targetLocation;
    }
    public int getCurrentPosition(){
        return startLocation;
    }
    public int getDestinationPosition(){
        return targetLocation;
    }
}

