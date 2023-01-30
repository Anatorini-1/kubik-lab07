package ClubApp.CORE;

import OfficeApp.CORE.ClubRegistry;
import interfaces.ISeeker;
import lombok.Data;
import model.Report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class ClubSeekerModel implements Serializable {
    public String name;
    public ISeeker iSeeker;
    public ArrayList<Report> reportsFiled;
    public ExecutorService executor;

    public ClubSeekerModel(ISeeker is, String name) {
        this.iSeeker = is;
        this.name = name;
        this.reportsFiled = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
    }
}
