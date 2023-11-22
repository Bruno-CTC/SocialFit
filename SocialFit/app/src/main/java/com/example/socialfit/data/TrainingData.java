package com.example.socialfit.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TrainingData {
    public String description;
    public String name;
    public int id;
    public HashMap<String, ArrayList<ExerciseData>> days;

    public TrainingData() {
        description = "";
        name = "";
        id = 0;
        days = new HashMap<>();
    }
}
