package model.pets;

import model.configurables.FileLocations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// represents an example pet
public class ExampleAnimal extends Pet {
    private final String dataKey = "ExampleAnimal";

    // EFFECTS: constructs an ExampleAnimal with name and breed
    public ExampleAnimal(String name, String breed) throws IOException {
        super(name);
        super.setAnimalType(dataKey);
        super.setBreed(breed);
        super.setPetDataDir(new File(FileLocations.getDataDir(dataKey)));
        super.setSpritesDir(FileLocations.getSpritesDir(dataKey));
        super.gatherPetData();
    }

    // EFFECTS: returns a random onomatopoeia from the noise list of the animal
    @Override
    public String makeNoise() {
        ArrayList<String> noises = super.getAllNoises();
        int randomIndex = rng.randomNumberUpTo(noises.size());
        return noises.get(randomIndex);
    }
}
