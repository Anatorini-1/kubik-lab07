package OfficeApp.CORE;

import lombok.Data;
import model.Artifact;

import java.util.UUID;

@Data
public class ArtifactModel {
    private Artifact artifact;
    private String sector;
    private String field;
    private String state;
    private UUID uuid;

    public ArtifactModel(Artifact artifact, String sector, String field, String state) {
        this.artifact = artifact;
        this.sector = sector;
        this.field = field;
        this.state = state;
        this.uuid = UUID.randomUUID();
    }
}
