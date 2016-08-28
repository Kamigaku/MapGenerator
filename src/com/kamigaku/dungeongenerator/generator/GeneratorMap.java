package com.kamigaku.dungeongenerator.generator;

import com.kamigaku.dungeongenerator.Map;
import com.kamigaku.dungeongenerator.Room;
import com.kamigaku.dungeongenerator.dijkstra.*;
import com.kamigaku.dungeongenerator.utility.Utility;
import java.awt.Point;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class GeneratorMap {
  
    // [TEMPORARY VARIABLES]
    private char[][] _map;
    private ArrayList<GeneratorRoom> _rooms;
    private Room _originRoom;
    
    
    // [MAP PROPERTIES]
    private final int _numberRoom;
    private long _seed;
    private Random _random;
    private Point _propWidthRoom;
    private Point _propHeightRoom;
    private String _stringMapSeed;
    
    // [RESULTS]
    private Map m;
    private ArrayList<Room> tRoom;
    
    public GeneratorMap(long seed) {
        this.init(seed);
        int minNumberRoom = Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 1)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 2)) * 10 + 10;
        int maxNumberRoom = (Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 3)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 4)) * 10) + minNumberRoom + 10;
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(long seed, int minNumberRoom, int maxNumberRoom) {
        this.init(seed);
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(long seed, int minSizeRoomWidth, int maxSizeRoomWidth, 
                        int minSizeRoomHeight, int maxSizeRoomHeight) {
        this.init(seed);
        this._propHeightRoom = new Point(minSizeRoomHeight, maxSizeRoomHeight);
        this._propWidthRoom = new Point(minSizeRoomWidth, maxSizeRoomHeight);
        int minNumberRoom = Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 1)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 2)) * 10 + 10;
        int maxNumberRoom = (Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 3)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 4)) * 10) + minNumberRoom + 10;
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(long seed, int minNumberRoom, int maxNumberRoom, 
                        int minSizeRoomWidth, int maxSizeRoomWidth, 
                        int minSizeRoomHeight, int maxSizeRoomHeight) {
        this.init(seed);
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this._propHeightRoom = new Point(minSizeRoomHeight, maxSizeRoomHeight);
        this._propWidthRoom = new Point(minSizeRoomWidth, maxSizeRoomWidth);
        this.executeProcess();
    }
    
    public GeneratorMap(int minNumberRoom, int maxNumberRoom) {
        this.init(Instant.now().getEpochSecond());
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(int minSizeRoomWidth, int maxSizeRoomWidth, 
                        int minSizeRoomHeight, int maxSizeRoomHeight) {
        this.init(Instant.now().getEpochSecond());
        this._propHeightRoom = new Point(minSizeRoomHeight, maxSizeRoomHeight);
        this._propWidthRoom = new Point(minSizeRoomWidth, maxSizeRoomHeight);
        int minNumberRoom = Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 1)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 2)) * 10 + 10;
        int maxNumberRoom = (Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 3)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 4)) * 10) + minNumberRoom + 10;
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(int minNumberRoom, int maxNumberRoom, 
                        int minSizeRoomWidth, int maxSizeRoomWidth, 
                        int minSizeRoomHeight, int maxSizeRoomHeight) {
        this.init(Instant.now().getEpochSecond());
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this._propHeightRoom = new Point(minSizeRoomHeight, maxSizeRoomHeight);
        this._propWidthRoom = new Point(minSizeRoomWidth, maxSizeRoomHeight);
        this.executeProcess();
    }
    
    private void init(long seed) {
        this._seed = seed;
        this._random = new Random(seed);
        this._rooms = new ArrayList<>();
        this._stringMapSeed = "" + this._random.nextLong();
    }
    
    private void executeProcess() {
        this.generateRooms();                                                   // Générer les rooms
        Point sizeMap = sumSizeRooms(_rooms);
        char[][] map = new char[sizeMap.y][sizeMap.x];
        Utility.fillArray(map, '#');
        ArrayList<Point> borders = new ArrayList<>();
        GeneratorRoom r1 = this._rooms.get(0);
        int x = (map[0].length / 2) - (r1._width / 2);
        int y = (map.length / 2) - (r1._height / 2);
        for(int i = 0; i < this._rooms.size(); i++) {
            map = this.placeRoom(map, this._rooms.get(i), x, y);
            borders.addAll(this._rooms.get(i).getWorldBorders());
            for(int j = 0; j < borders.size(); j++) {
                if(Utility.numberAllAroundSurrondings(map, borders.get(i).x, borders.get(i).y, '#') < 2) {
                    borders.remove(j);
                    j--;
                }
                
            }
            Point p = borders.get(Utility.nextInt(_random, 0, borders.size()));
            x = p.x;
            y = p.y;
        }
        borders.clear();
        map = this.cleanMap(map);                                               // Retire les murs qui ne sont pas nécessaires
        this.reduceMap(map);                                                    // Réduit la taille de la carte
        this.createRooms();                                                     // Créer les vraies salles
        this.connectRooms();                                                    // Connecte les salles entres elles
        this.cleanMap(this._map);                                               // Retire les murs qui ne sont pas nécessaires
        this.createEntrance();                                                  // Créer l'entrée du donjon
        this.boundRooms();                                                      // Connecte les salles avec leurs voisines
        this.createRooms();
        this.createEntrance();
        this.boundRooms();
        this.entryToExit();                                                   // Détermine le chemin entre l'entrée et la salle finale
        this.m = new Map(this._map, this.tRoom);
    }
    
    private void generateRooms() {
        if(this._propHeightRoom == null && this._propWidthRoom == null) {
            for(int i = 0; i < this._numberRoom; i++) {
                //System.out.println("[Generating room " + (i+1) + " / " + this._numberRoom + "]");
                GeneratorRoom r = new GeneratorRoom(this._random.nextLong());
                if(r.isValid) {
                    this._rooms.add(r);
                }
                else
                    i--;
            }
        }
        else {
            for(int i = 0; i < this._numberRoom; i++) {
                GeneratorRoom r = new GeneratorRoom(this._random.nextLong(), 
                        this._propWidthRoom.x, this._propWidthRoom.y, 
                        this._propHeightRoom.x, this._propHeightRoom.y);
                if(r.isValid) {
                    this._rooms.add(r);
                }
                else
                    i--;
            }
        }
    }
        
    private Point sumSizeRooms(ArrayList<GeneratorRoom> rooms) {
        int height = 0;
        int width = 0;
        for(int i = 0; i < rooms.size(); i++) {
            height += rooms.get(i)._height;
            width += rooms.get(i)._width;
        }
        height += 1;
        width += 1;
        return new Point(width, height);
    }
    
    private char[][] placeRoom(char[][] map, GeneratorRoom r, int x_ori, int y_ori) {
        if(x_ori + r._width > map[0].length) {
            char[][] tempMap = new char[map.length][map[0].length];
            Utility.copyArray(map, tempMap);
            map = new char[tempMap.length][x_ori + r._width + 1];
            Utility.fillArray(map, '#');
            Utility.copyArray(tempMap, map);
        }
        if(y_ori + r._height > map.length) {
            char[][] tempMap = new char[map.length][map[0].length];
            Utility.copyArray(map, tempMap);
            map = new char[y_ori + r._height + 1][tempMap[0].length];
            Utility.fillArray(map, '#');
            Utility.copyArray(tempMap, map);
        }
        for(int y = 0; y < r._map.length; y++) {
            for(int x = 0; x < r._map[y].length; x++) {
                if(r._map[y][x] != '#') {
                    map[y + y_ori][x + x_ori] = r._map[y][x];
                }
            }
        }
        r.setOriginXY(x_ori, y_ori);
        return map;
    }
    
    private char[][] cleanMap(char[][] map) {
        // Je retire les murs qui n'ont pas lieu d'être
        for(int y = 1; y < map.length - 1; y++) {
            for(int x = 1; x < map[y].length - 1; x++) {
                if(map[y][x] == 'W') {
                    if(!Utility.checkSquareSurrondings(map, x, y, ' '))
                        map[y][x] = '#';
                    else {
                        char[][] square = new char[3][3];
                        for(int i = 0; i < 3; i++)
                            for(int j = 0; j < 3; j++)
                                square[i][j] = map[y + (-1 + i)][x + (-1 + j)];
                        Square preUpdate = new Square(square);
                        square[1][1] = ' ';
                        Square postUpdate = new Square(square);
                        if(Square.compareSquare(preUpdate, postUpdate)) {
                            if(Utility.checkXorYSurrondings(map, x, y, ' '))
                                map[y][x] = ' ';
                            else
                                map[y][x] = '#';
                        }
                    }
                }
            }
        }
        return map;
    }
    
    private void reduceMap(char[][] map) {     
        int heightMinRoom = -1;
        int heightMaxRoom = map.length;
        int widthMinRoom = -1;
        int widthMaxRoom = -1;
        
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                if(map[y][x] == 'W') {
                    if(heightMinRoom == -1)
                        heightMinRoom = y;
                    if(widthMinRoom == -1)
                        widthMinRoom = x;
                    heightMaxRoom = y;
                    if(x < widthMinRoom) widthMinRoom = x;
                    if(x > widthMaxRoom) widthMaxRoom = x;
                }
            }
        }
        heightMinRoom = heightMinRoom == 0 ? 0 : heightMinRoom - 1;
        widthMinRoom = widthMinRoom == 0 ? 0 : widthMinRoom - 1;
        heightMaxRoom = heightMaxRoom >= (map.length - 1) ? map.length - 1 : heightMaxRoom + 1;
        widthMaxRoom = widthMaxRoom >= (map[0].length - 1) ? map[0].length - 1 : widthMaxRoom + 1;
        this._map = new char[heightMaxRoom - heightMinRoom + 1][widthMaxRoom - widthMinRoom + 1];
        for(int y = heightMinRoom; y <= heightMaxRoom; y++) {
            for(int x = widthMinRoom; x <= widthMaxRoom; x++) {
                this._map[y - heightMinRoom][x - widthMinRoom] = map[y][x];
            }
        }
    }
    
    private void createRooms() {        
        Dijkstra d = new Dijkstra(this._map);
        d.addRule(new Rule(' ', 'W', true, true));
        d.addRule(new Rule(' ', ' ', true, false));
        d.addRule(new Rule(' ', 'D', true, false));
        d.createNodes(true);
        Node[] nodes = d.getNodes();
        this.tRoom = new ArrayList<>();
        for(int i = 0; i < nodes.length; i++) {
            if(nodes[i] != null && !nodes[i].fetched) {
                int xPos = Dijkstra.XValue(i, this._map[0].length);
                int yPos = Dijkstra.YValue(i, this._map[0].length);
                ArrayList<Point> wall = new ArrayList<>();
                ArrayList<Point> ground = new ArrayList<>();
                ArrayList<Point> door = new ArrayList<>();
                ground.add(new Point(xPos, yPos));
                fetchNode(nodes[i], nodes, ground, wall, door);
                this.tRoom.add(new Room(this._map, ground, wall, this._random));
            }
        }
    }
    
    private void fetchNode(Node origin, Node[] nodes, ArrayList<Point> ground, ArrayList<Point> wall, ArrayList<Point> door) {
        origin.fetched = true;
        for(int i = 0; i < origin.neighbors.size(); i++) {
            Point p = new Point(Dijkstra.XValue(origin.neighbors.get(i), this._map[0].length), 
                                Dijkstra.YValue(origin.neighbors.get(i), this._map[0].length));
            if(this._map[p.y][p.x] == 'W')
                wall.add(p);  
            if(this._map[p.y][p.x] == 'D')
                door.add(p);
            else if(this._map[p.y][p.x] == ' ' && !nodes[origin.neighbors.get(i)].fetched) {
                ground.add(p);
                fetchNode(nodes[origin.neighbors.get(i)], nodes, ground, wall, door);
            }
        }
    }
    
    private void connectRooms() {
        for(int y = 0; y < this._map.length; y++) {
            for(int x = 0; x < this._map[y].length; x++) {
                if(this._map[y][x] == 'W') {
                    int result = Utility.checkDiagonalsSurrondings(this._map, x, y, ' ');
                    switch(result) {
                        case 1:
                        case -1:
                            this._map[y][x] = this._map[y][x - 1] = this._map[y][x + 1] = ' ';
                            this._map[y + 1][x + (1 * result)] = this._map[y + 1][x + (2 * result)] = 'W';
                            this._map[y - 1][x + (-1 * result)] = this._map[y - 1][x + (-2 * result)] = 'W';
                            break;
                        case 2:
                            for(int i = -1; i <= 1; i += 2) {
                                this._map[y][x] = this._map[y][x - 1] = this._map[y][x + 1] = ' ';
                                this._map[y + 1][x + (1 * i)] = this._map[y + 1][x + (2 * i)] = 'W';
                                this._map[y - 1][x + (-1 * i)] = this._map[y - 1][x + (-2 * i)] = 'W';
                            }
                            break;
                    }
                }
            }
        }
    }
    
    private void createEntrance() {
        Point indexRooms = new Point();
        int valueDistance = 0;
        for(int i = 0; i < this.tRoom.size(); i++) {
            for(int j = i + 1; j < this.tRoom.size(); j++) {
                Point p1 = this.tRoom.get(i).getFirstFloorTilesPosition();
                Point p2 = this.tRoom.get(j).getFirstFloorTilesPosition();
                if(p1.distance(p2.x, p2.y) > valueDistance) {
                    indexRooms.x = i;
                    indexRooms.y = j;
                }
            }
        }
        this.tRoom.get(indexRooms.x).isEntry = true;
        this.tRoom.get(indexRooms.y).isExit = true;
        this._originRoom = this.tRoom.get(indexRooms.x);
    }

    private void boundRooms() {
        for(int i = 0; i < this.tRoom.size(); i++) {
            Room r1 = this.tRoom.get(i);
            for(int j = i + 1; j < this.tRoom.size(); j++) {
                Room r2 = this.tRoom.get(j);
                ArrayList<Point> commonCoords = Utility.commonCoords(r1.getBordersWithoutAngles(),
                                                                       r2.getBordersWithoutAngles());
                if(commonCoords.size() > 0) {
                    r1.addNeighboorRoom(r2);
                    r2.addNeighboorRoom(r1);
                }
            }
            if(r1.getConnections().isEmpty()) { // Si une salle ne possède pas de voisin direct
                char[][] copyMap = new char[this._map.length][this._map[0].length];
                Utility.fillArray(copyMap, '#');
                for(int j = 0; j < this.tRoom.size(); j++) {
                    if(this.tRoom.get(j) != r1) {
                        ArrayList<Point> points = this.tRoom.get(j).getBordersWithoutAngles();
                        for(int k = 0; k < points.size(); k++) {
                            copyMap[points.get(k).y][points.get(k).x] = 'W';
                        }
                    }
                }
                copyMap[r1.origin.y][r1.origin.x] = 'O';
                Dijkstra dis = new Dijkstra(copyMap);
                dis.addRule(new Rule('O', '#', true, false));
                dis.addRule(new Rule('O', 'W', true, false));
                dis.addRule(new Rule('#', '#', true, false));
                dis.addRule(new Rule('#', 'W', true, false));
                dis.addRule(new Rule('W', '#', true, false));
                dis.createNodes(false);
                Point destination = dis.findFirst(r1.origin, 'W');
                Room r2 = null;
                for(int j = 0; j < this.tRoom.size(); j++) {
                    if(this.tRoom.get(j).getAllPoints().contains(destination)) {
                        r2 = this.tRoom.get(j);
                        break;
                    }
                }
                if(r2 != null)
                    connectTwoRooms(this.tRoom.get(i), r2, destination);
            }
        }
        
        // @TODO : faire une sorte d'arbre pour atteindre la room de fin
        boolean over = false;
        do {
            Node[] nodes = new Node[this.tRoom.size()];
            ArrayList<Point> allMapRooms = new ArrayList<>();
            for(int i = 0; i < this.tRoom.size(); i++) {
                nodes[i] = new Node(i);
                if(!tRoom.get(i).isEntry)
                    allMapRooms.add(new Point(i, -1));
                for(int j = 0; j < this.tRoom.get(i).getConnections().size(); j++) {
                    int index = this.tRoom.indexOf(this.tRoom.get(i).getConnections().get(j));
                    nodes[i].addNeighbors(index);
                }
            }
            int indexOrigin = this.tRoom.indexOf(this._originRoom); // je récupère l'entrée
            Dijkstra dijkstra = new Dijkstra(nodes);
            ArrayList<Point> connectToOrigin = dijkstra.allPossiblePath(indexOrigin); // je recupère toutes les salles connectés à l'origien
            if(connectToOrigin.size() < (allMapRooms.size() - 1)) { // si je n'ai pas autant de rooms cela signifie qu'elles ne sont pas toutes connectées
                ArrayList<Point> nonConnectToOrigin = new ArrayList<>(allMapRooms); // je copie toutes les rooms
                nonConnectToOrigin.removeAll(connectToOrigin); // je retire celles qui sont connectés
                if(connectToOrigin.size() < nonConnectToOrigin.size())
                    this.iterationOnArrayRoom(connectToOrigin, nonConnectToOrigin);
                else
                    this.iterationOnArrayRoom(nonConnectToOrigin, connectToOrigin);
            }
            else {
                over = true;
            }
        } while(!over);
        
    }
    
    private void iterationOnArrayRoom(ArrayList<Point> a1, ArrayList<Point> a2) {
        Room r1 = null;
        Room r2 = null;
        Point closestPoint = null;
        for(int i = 0; i < a1.size(); i++) {
            Room tempR1 = this.tRoom.get(a1.get(i).x);
            char[][] copyMap = new char[this._map.length][this._map[0].length];
            Utility.fillArray(copyMap, '#');
            for(int j = 0; j < this.tRoom.size(); j++) {
                if(a2.contains(new Point(j, -1))) {
                    ArrayList<Point> points = this.tRoom.get(j).getBordersWithoutAngles();
                    for(int k = 0; k < points.size(); k++) {
                        copyMap[points.get(k).y][points.get(k).x] = 'W';
                    }
                }
            }
            copyMap[tempR1.origin.y][tempR1.origin.x] = 'O';
            Dijkstra dis = new Dijkstra(copyMap);
            dis.addRule(new Rule('O', '#', true, false));
            dis.addRule(new Rule('O', 'W', true, false));
            dis.addRule(new Rule('#', '#', true, false));
            dis.addRule(new Rule('#', 'W', true, false));
            dis.addRule(new Rule('W', '#', true, false));
            dis.createNodes(false);
            Point destination = dis.findFirst(tempR1.origin, 'W');
            if(closestPoint == null || 
               destination.distance(tempR1.origin) < closestPoint.distance(r1.origin)) {
                r1 = tempR1;
                closestPoint = new Point(destination.x, destination.y);
            }
        }
        for(int j = 0; j < this.tRoom.size(); j++) {
            if(this.tRoom.get(j).getAllPoints().contains(closestPoint)) {
                r2 = this.tRoom.get(j);
                break;
            }
        }
        connectTwoRooms(r1, r2, closestPoint);
    }
    
    private void connectTwoRooms(Room r1, Room r2, Point destination) {
        char[][] tempMap = new char[this._map.length][this._map[0].length];
        Utility.fillArray(tempMap, '#');
        Dijkstra d = new Dijkstra(tempMap);
        d.addRule(new Rule('#', '#', true, false));
        d.createNodes(false);
        ArrayList<Point> corridor = d.shortestPathFromTo(r1.origin, destination);
        if(corridor != null) {
            for(int k = 0; k < corridor.size(); k++) {
                Point aCor = corridor.get(k);
                this._map[aCor.y][aCor.x] = ' ';
                Utility.searchAndReplace(this._map, aCor.x, aCor.y, '#', 'W');
            }
            for(int i = 0; i < corridor.size() - 1; i++) {
                if(Utility.checkXorYBothSide(this._map, corridor.get(i).x, corridor.get(i).y, 'W')) {
                    this._map[corridor.get(i).y][corridor.get(i).x] = 'W';
                    break;
                }
            }
            r1.addNeighboorRoom(r2);
            r2.addNeighboorRoom(r1);
        }
    }
    
    private void entryToExit() {
        /*ArrayList<Room> allRooms = new ArrayList<>(this.tRoom);
        allRooms.remove(this._originRoom);
        HashMap<Room, ArrayList<Room>> roomConnections;
        while(allRooms.size() >= 0) {
            Room currentRoom = allRooms.get(0);
            ArrayList<Room> connectedRooms = currentRoom.getConnections();
            long seed = currentRoom.getSeed();
        }*/
        Node[] nodes = new Node[this.tRoom.size()];
        ArrayList<Point> allMapRooms = new ArrayList<>();
        for(int i = 0; i < this.tRoom.size(); i++) {
            nodes[i] = new Node(i);
            if(!tRoom.get(i).isEntry)
                allMapRooms.add(new Point(i, -1));
            for(int j = 0; j < this.tRoom.get(i).getConnections().size(); j++) {
                int index = this.tRoom.indexOf(this.tRoom.get(i).getConnections().get(j));
                nodes[i].addNeighbors(index);
            }
            //this._map[this.tRoom.get(i).origin.y][this.tRoom.get(i).origin.x] = Character.forDigit(i, 10);
        }
        int indexOrigin = this.tRoom.indexOf(this._originRoom); // je récupère l'entrée
        Dijkstra dijkstra = new Dijkstra(nodes);
        ArrayList<ArrayList<Integer>> treePath = dijkstra.getTreePath(indexOrigin); // je recupère toutes les salles connectés à l'origien
        for (int i = 0; i < treePath.size() - 1; i++) {
            ArrayList<Integer> get = treePath.get(i);
            Integer i1 = get.get(get.size() - 1);
            Integer i2 = get.get(get.size() - 2);
            Room r1 = this.tRoom.get(i1);
            Room r2 = this.tRoom.get(i2);
            ArrayList<Point> commonPoints = Utility.commonCoords(
                    r1.getBordersWithoutAngles(), r2.getBordersWithoutAngles());
            if(commonPoints.size() > 0)
                this._map[commonPoints.get(0).y][commonPoints.get(0).x] = 'D';
        }
    }
    
    public Map getMap() {
        return this.m;
    }
}
