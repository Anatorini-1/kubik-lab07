package OfficeApp.CORE;

import interfaces.IClub;
import lombok.Data;
import model.Report;

import java.awt.*;
import java.util.ArrayList;

@Data
public class ClubModel {
    private String name;
    private IClub club;
    private ArrayList<Report> reports;
    private Color color;
}
