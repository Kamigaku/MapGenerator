package com.kamigaku.dungeongenerator;

import com.kamigaku.dungeongenerator.utility.Utility;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Room {
    
    public enum RoomSize {
        SMALL, MEDIUM, BIG
    };
     
    private final ArrayList<Point> _bordersWithoutAngle;
    private final ArrayList<Room> _connections;
    private final ArrayList<Point> _ground;
    private final ArrayList<Point> _walls;
    
    private final Rectangle _rectangle;
    
    public boolean isEntry = false;
    public boolean isExit = false;
    
    public RoomSize roomSize;
    
    private final long _seed;
    
    public final Point origin;
    
    public Room(char[][] mainMap, ArrayList<Point> ground, ArrayList<Point> walls, Random r) {
        this._bordersWithoutAngle = new ArrayList<>();
        this._ground = ground;
        this._connections = new ArrayList<>();
        this._walls = walls;
        
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;
        for(int i = 0; i < walls.size(); i++) {
            Point wall = walls.get(i);
            if(wall.x < minX)
                minX = wall.x;
            if(wall.x > maxX)
                maxX = wall.x;
            if(wall.y < minY)
                minY = wall.y;
            if(wall.y > maxY)
                maxY = wall.y;
            if(!Utility.checkXandYSurrondings(mainMap, wall.x, wall.y, 'W')) {
                this._bordersWithoutAngle.add(new Point(wall.x, wall.y));
            }
        }
        this._seed = r.nextLong();
        this.origin = ground.get(0);
        this._rectangle = new Rectangle(minX - 1, minY - 1, maxX - minX + 1, maxY - minY + 1);
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
    
    public ArrayList<Point> getAllGround() {
        return this._ground;
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
    
    public Rectangle getRectangle() {
        return this._rectangle;
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
