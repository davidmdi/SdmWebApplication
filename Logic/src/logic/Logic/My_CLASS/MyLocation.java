package logic.Logic.My_CLASS;


import SDM_CLASS.Location;

public class MyLocation {
    private Location sdmLocation;
    private int X ;
    private  int Y;

    public MyLocation(Location sdmLocation) {
        this.sdmLocation = sdmLocation;
        this.X = sdmLocation.getX();
        this.Y = sdmLocation.getY();
    }

    public Location getSdmLocation() {
        return sdmLocation;
    }

    public void setSdmLocation(Location sdmLocation) {
        this.sdmLocation = sdmLocation;
    }

    @Override
    public String toString() {
        return  "(x=" + this.getSdmLocation().getX() +
                ",y=" + this.getSdmLocation().getY() + ")";

    }
}
