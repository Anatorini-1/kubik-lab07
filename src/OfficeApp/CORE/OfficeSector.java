package OfficeApp.CORE;

import model.Artifact;

import java.util.HashMap;

public class OfficeSector {
    private HashMap<String, ArtifactModel> artifacts;

    public OfficeSector() {
        artifacts = new HashMap<>();
    }

    public void addArtifact(String pos, ArtifactModel artifact) {
        artifacts.put(pos, artifact);
    }

    public ArtifactModel getArtifact(String pos) {
        return artifacts.get(pos);
    }
}
