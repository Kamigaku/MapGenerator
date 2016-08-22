package com.kamigaku.dungeongenerator;

import java.util.ArrayList;

/**
 *
 * @author Kamigaku
 */
public class Map {
    
    private final char[][] _map;
    private final ArrayList<Room> _rooms;
    private Room _entryRoom;
    private Room _exitRoom;
    
    public Map(char[][] map, ArrayList<Room> rooms) {
        this._rooms = rooms;
        this._map = map;
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).isEntry)
                _entryRoom = rooms.get(i);
            if(rooms.get(i).isExit)
                _exitRoom = rooms.get(i);
        }
    }
    
    public Room getEntryRoom() {
        return this._entryRoom;
    }
    
    public Room getExitRoom() {
        return this._exitRoom;
    }
    
    public char[][] getMap() {
        return this._map;
    }
    
}
