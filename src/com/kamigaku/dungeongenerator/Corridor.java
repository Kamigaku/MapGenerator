package com.kamigaku.dungeongenerator;

public class Corridor {
    
    public int x;
    public int y;
    public Room r1;
    public Room r2;
    public CorridorType connectionType;
    
    public enum CorridorType {
        DOOR, EMPTY, LOCKED_DOOR
    }
    
    public Corridor(int x, int y, Room r1, Room r2, CorridorType c) {
        this.x = x;
        this.y = y;
        this.r1 = r1;
        this.r2 = r2;
        this.connectionType = c;
    } 
}
