package com.kamigaku.dungeongenerator;

import com.kamigaku.dungeongenerator.utility.Utility;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Room {
     
    private final ArrayList<Point> _bordersWithoutAngle;
    private final ArrayList<Room> _connections;
    private final ArrayList<Point> _ground;
    private final ArrayList<Point> _walls;
    
    public boolean isEntry = false;
    public boolean isExit = false;
    
    private final long _seed;
    
    public final Point origin;
    
    public Room(char[][] mainMap, ArrayList<Point> ground, ArrayList<Point> walls, Random r) {
        this._bordersWithoutAngle = new ArrayList<Point>();
        this._ground = ground;
        this._connections = new ArrayList<Room>();
        this._walls = walls;
        for(int i = 0; i < walls.size(); i++) {
            if(!Utility.checkXandYSurrondings(mainMap, walls.get(i).x, walls.get(i).y, 'W')) {
                this._bordersWithoutAngle.add(new Point(walls.get(i).x, walls.get(i).y));
            }
        }
        this._seed = r.nextLong();
        this.origin = ground.get(0);
    }
    
    public void addNeighboorRoom(Room r) {
        this._connections.add(r);
    }
        
    public ArrayList<Point> getBordersWithoutAngles() {
        return this._bordersWithoutAngle;
    }
        
    public Point getFirstFloorTilesPosition() {
        return this._ground.get(0);
    }
    
    public ArrayList<Room> getConnections() {
        return this._connections;
    }
    
    public long getSeed() {
        return this._seed;
    }
    
    public ArrayList<Point> getAllPoints() {
        ArrayList<Point> points = new ArrayList<>();
        points.addAll(this._ground);
        points.addAll(this._walls);
        return points;
    }
    
}
