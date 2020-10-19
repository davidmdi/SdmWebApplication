package logic.Logic.My_CLASS;


import SDM_CLASS.Location;

public class MyLocation {
    private Location sdmLocation;
    private int X ;
    private int Y;

    public MyLocation(Location sdmLocation) {
        this.sdmLocation = sdmLocation;
        this.X = sdmLocation.getX();
        this.Y = sdmLocation.getY();
    }

    public MyLocation(int x, int y) {
        this.X = x ;
        this.Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public Location getSdmLocation() {
        return sdmLocation;
    }

    public void setSdmLocation(Location sdmLocation) {
        this.sdmLocation = sdmLocation;
    }

    @Override
    public String toString() {
        return  "(x=" + this.getX() +
                ",y=" + this.getY() + ")";

    }

    public boolean compare(MyLocation location) {
        if(this.X == location.getSdmLocation().getX() && this.Y == location.sdmLocation.getY())
            return true;
        return false;
    }
}
