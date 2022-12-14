package model.goodsandservices;

import model.configurables.FileLocations;
import model.persistence.Writable;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

// represents an item
public class Item implements Writable {
    private final FileLocations fileLoc = new FileLocations();

    private final String dataKey = "Item";
    private final String itemsDir = FileLocations.getDataDir(dataKey);
    private String spritesDir = FileLocations.getSpritesDir(dataKey);
    private final File itemsDataDir = new File(itemsDir);

    private JSONObject itemData;
    private final String name;
    private final String type;

    private int price = 0;
    private int happinessPoints = 0;
    private int hungerPoints = 0;
    private int thirstPoints = 0;
    private int healthPoints = 0;

    // EFFECTS: constructs an item with name and type
    public Item(String name, String type) throws IOException {
        this.name = name;
        this.type = type;
        this.fetchItemData();
        this.parseItemData();
    }

    // MODIFIES: this
    // REQUIRES: itemsDataDir exists
    // EFFECTS:  gathers data from itemsDataDir and stores it
    private void fetchItemData() throws IOException {
        String content = FileUtils.readFileToString(itemsDataDir, "utf-8");
        JSONObject itemsJson = new JSONObject(content);
        JSONArray itemsArray = itemsJson.getJSONArray("items");

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemData = itemsArray.getJSONObject(i);
            String itemName = itemData.getString("itemName");
            String itemType = itemData.getString("itemType");

            if (itemName.equals(this.name) && itemType.equals(this.type)) {
                this.itemData = itemData;
                // break; // commented out for code coverage...
            }
        }
    }

    // MODIFIES: this
    // REQUIRES: itemData contains item data and is not null
    // EFFECTS:  parses data from itemsData and assigns
    //           corresponding variables the data contained
    private void parseItemData() {
        JSONObject data = this.itemData;
        String fileName = this.name.toLowerCase() + "_" + this.type.toLowerCase();
        this.spritesDir += fileName.replaceAll("\\s+","") + "/";

        int price = data.getInt("price");
        JSONArray carePoints = data.getJSONArray("carePoints");

        this.price = price;
        this.happinessPoints = carePoints.getInt(0);
        this.hungerPoints  = carePoints.getInt(1);
        this.thirstPoints = carePoints.getInt(2);
        this.healthPoints = carePoints.getInt(3);
    }

    // EFFECTS:  converts all item data to a JSONObject and returns it
    @Override
    public JSONObject toJsonObj() {
        JSONObject itemDetails = new JSONObject();

        itemDetails.put("name", name);
        itemDetails.put("type", type);
//        itemDetails.put("happinessPoints", happinessPoints);
//        itemDetails.put("hungerPoints", hungerPoints);
//        itemDetails.put("thirstPoints", thirstPoints);
//        itemDetails.put("healthPoints", healthPoints);
//        itemDetails.put("price", price);

        return itemDetails;
    }

    // GETTERS
    public String getSpritesDir() {
        return spritesDir;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getHappinessPoints() {
        return happinessPoints;
    }

    public int getThirstPoints() {
        return thirstPoints;
    }

    public int getHungerPoints() {
        return hungerPoints;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
}
