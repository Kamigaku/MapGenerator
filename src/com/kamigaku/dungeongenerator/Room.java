package com.kamigaku.dungeongenerator;

import com.kamigaku.dungeongenerator.Corridor.CorridorType;
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
        this._bordersWithoutAngle = new ArrayList<>();
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

    @Override
    public String toString() {
        System.out.println("Je possède : " + this._connections.size() + " voisins.");
        int minHeight = Integer.MAX_VALUE;
        int maxHeight = -1;
        int minWidth = Integer.MAX_VALUE;
        int maxWidth = -1;
        for(int i = 0; i < this._ground.size(); i++) {
            Point p = this._ground.get(i);
            if(p.x < minWidth) minWidth = p.x;
            else if(p.x > maxWidth) maxWidth = p.x;
            if(p.y < minHeight) minHeight = p.y;
            else if(p.y > maxHeight) maxHeight = p.y;
        }
        for(int i = 0; i < this._walls.size(); i++) {
            Point p = this._walls.get(i);
            if(p.x < minWidth) minWidth = p.x;
            else if(p.x > maxWidth) maxWidth = p.x;
            if(p.y < minHeight) minHeight = p.y;
            else if(p.y > maxHeight) maxHeight = p.y;
        }
        char[][] map = new char[maxHeight - minHeight + 1][maxWidth - minWidth + 1];
        Utility.fillArray(map, '#');
        for(int i = 0; i < this._ground.size(); i++) {
            Point p = this._ground.get(i);
            map[p.y - minHeight][p.x - minWidth] = ' ';
        }
        for(int i = 0; i < this._walls.size(); i++) {
            Point p = this._walls.get(i);
            map[p.y - minHeight][p.x - minWidth] = 'W';
        }
        Utility.displayEntity(map);
        return "";
    }    
}
