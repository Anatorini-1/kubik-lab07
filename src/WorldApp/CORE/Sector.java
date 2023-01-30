package WorldApp.CORE;

import WorldApp.WorldApp;
import model.Artifact;


public class Sector {
    private final Artifact[][] fields;

    public Sector() {
        this.fields = new Artifact[WorldApp.fieldsPerSector][WorldApp.fieldsPerSector];
        for (int i = 0; i < WorldApp.fieldsPerSector; i++) {
            fields[i] = new Artifact[WorldApp.fieldsPerSector];
            for (int j = 0; j < WorldApp.fieldsPerSector; j++) {
                fields[i][j] = null;
            }
        }

    }

    public Artifact getField(int x, int y) {
        return fields[x][y];
    }


    public void setField(int x, int y, Artifact a) {
        this.fields[x][y] = a;
    }
}
