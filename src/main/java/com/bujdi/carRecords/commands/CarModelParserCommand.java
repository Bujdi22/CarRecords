package com.bujdi.carRecords.commands;

import com.bujdi.carRecords.model.CarModel;
import com.bujdi.carRecords.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class CarModelParserCommand implements CommandLineRunner {
    @Autowired
    private CarModelRepository carModelRepository;

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && "parseCars".equalsIgnoreCase(args[0])) {
            System.out.println("Starting car data parsing...");

            String csvFilePath = "data_full.csv";

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
                String line;
                List<CarModel> cars = new ArrayList<>();

                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");

                    // Assuming the CSV columns are: make, model, year
                    String make = values[0].trim();
                    String model = removeMakeFromModel(values[1].trim(), make);
                    CarModel car = new CarModel();
                    car.setMake(make);
                    car.setModel(model);
                    cars.add(car);
                }

                Set<CarModel> uniqueCars = new LinkedHashSet<>(cars);

                List<CarModel> result = new ArrayList<>(uniqueCars);

                for (CarModel car : result) {
                    carModelRepository.save(car);
                }
            }
        }
    }

    private String removeMakeFromModel(String model, String make) {
        if (model.startsWith(make)) {
            return model.substring(make.length()).trim();
        }

        String[] splitModel = model.split(" ");
        if (splitModel.length >= 3 && (splitModel[0] + splitModel[1]).equals(make)) {
            return String.join(" ", Arrays.copyOfRange(splitModel, 2, splitModel.length)).trim();
        }

        return model;
    }
}

