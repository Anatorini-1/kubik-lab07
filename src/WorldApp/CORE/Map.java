package WorldApp.CORE;

import WorldApp.GUI.WorldFrame;
import WorldApp.WorldApp;
import lombok.SneakyThrows;
import model.Artifact;
import model.Category;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Map {
    private Sector[][] map;


    public Category artifactToPlace = Category.EMPTY;
    public int artifactExcavationTime = 5000;

    public Map() {
        this.map = new Sector[WorldApp.sectors][WorldApp.sectors];
        for (int i = 0; i < WorldApp.sectors; i++) {
            map[i] = new Sector[WorldApp.sectors];
            for (int j = 0; j < WorldApp.sectors; j++) {
                map[i][j] = new Sector();
            }
        }
    }

    public Artifact getField(int sectorX, int sectorY, int fieldX, int fieldY) {
        //System.out.println("Map.getField() called. Args: " + sectorX + ", " + sectorY + ", " + fieldX + ", " + fieldY);
        return map[sectorX][sectorY].getField(fieldX, fieldY);
    }

    public void placeArtifact(int sectorX, int sectorY, int fieldX, int fieldY, Artifact a) {
        map[sectorX][sectorY].setField(fieldX, fieldY, a);
    }


    public Artifact explore(int sectorX, int sectorY, int fieldX, int fieldY) {
        try {
            //System.out.println("Map.explore() called. Args: " + sectorX + ", " + sectorY + ", " + fieldX + ", " + fieldY);
            if (getField(sectorX, sectorY, fieldX, fieldY) == null) return null;
            int excavationTime = getField(sectorX, sectorY, fieldX, fieldY).getExcavationTime();
            Artifact toReturn = getField(sectorX, sectorY, fieldX, fieldY);
            placeArtifact(sectorX, sectorY, fieldX, fieldY, null);
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
