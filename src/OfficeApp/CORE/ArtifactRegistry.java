package OfficeApp.CORE;

import OfficeApp.OfficeApp;
import model.Artifact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ArtifactRegistry {
    private ArrayList<ArtifactModel> artifacts;
    private HashMap<String, OfficeSector> sectors;

    public ArtifactRegistry() {
        artifacts = new ArrayList<>();
        sectors = new HashMap<>();
    }

    public void addArtifact(ArtifactModel artifactModel) {
        artifacts.add(artifactModel);
        if (!sectors.containsKey(artifactModel.getSector())) {
            sectors.put(artifactModel.getSector(), new OfficeSector());
        }
        sectors.get(artifactModel.getSector()).addArtifact(artifactModel.getField(), artifactModel);
    }

    public ArtifactModel getArtifact(UUID uuid) {
        for (ArtifactModel artifactModel : artifacts) {
            if (artifactModel.getUuid().equals(uuid)) {
                return artifactModel;
            }
        }
        return null;
    }

    public ArtifactModel getArtifact(String sector, String field) {
        if (!sectors.containsKey(sector)) return null;
        return sectors.get(sector).getArtifact(field);
    }

    public ArtifactModel getArtifact(int sectorX, int sectorY, int fieldX, int fieldY) {
        String sector = Util.Util.coordsToLabel(sectorX, sectorY, OfficeApp.sectors);
        String field = Util.Util.coordsToLabel(fieldX, fieldY, OfficeApp.fieldsPerSector);
        return getArtifact(sector, field);
    }
}
