package model.goodsandservices;

import model.configurables.FileLocations;
import model.configurables.RandomGenerator;
import model.persistence.Writable;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static model.persistence.ConverterJsonArrays.*;

// represents a shop that sells pet items
public class Shop implements Writable {
    private final FileLocations fileLoc = new FileLocations();
    private final RandomGenerator rng = new RandomGenerator();

    private final String shopName;

    private ArrayList<Item> shopItems;
    private ArrayList<Integer> priceOfItems;
    private ArrayList<Integer> quantityInStock;

    private final String dataKey = "Item";
    private final String itemsDir = FileLocations.getDataDir(dataKey);
    private final File itemsDataDir = new File(itemsDir);

    protected ArrayList<Item> allPossibleItems = new ArrayList<>();

    // EFFECTS: constructs a shop with a shopName
    public Shop(String shopName) throws IOException {
        this.shopName = shopName;
        this.shopItems = new ArrayList<>();
        this.priceOfItems = new ArrayList<>();
        this.quantityInStock = new ArrayList<>();

        fetchAllPossibleItems();
    }

    // MODIFIES: this
    // REQUIRES: price >= 0, and
    //           quantity > 0
    // EFFECTS: adds item to shopItems, and
    //          adds quantity to quantityInStock
    public void addShopItem(Item item, int quantity) {
        if (!checkIsInShop(item)) {
            ArrayList<Item> newShopItems = getShopItems();
            ArrayList<Integer> newPriceOfItems = getPriceOfItems();
            ArrayList<Integer> newQuantityInStock = getQuantityInStock();

            newShopItems.add(item);
            newPriceOfItems.add(item.getPrice());
            newQuantityInStock.add(quantity);

            setShopItems(newShopItems);
            setPriceOfItems(newPriceOfItems);
            setQuantityInStock(newQuantityInStock);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes item from shopItems,
    //          removes its price from priceOfItems, and
    //          removes its quantity from quantityInStock
    public void removeShopItem(Item item) {
        if (checkIsInShop(item)) {
            ArrayList<Item> newShopItems = getShopItems();
            ArrayList<Integer> newPriceOfItems = getPriceOfItems();
            ArrayList<Integer> newQuantityInStock = getQuantityInStock();
            int index = shopItems.indexOf(item);

            newShopItems.remove(index);
            newPriceOfItems.remove(index);
            newQuantityInStock.remove(index);

            setShopItems(newShopItems);
            setPriceOfItems(newPriceOfItems);
            setQuantityInStock(newQuantityInStock);
        }
    }

    // MODIFIES: this
    // REQUIRES: item is in shopItems
    // EFFECTS:  returns the quantity of a specific item
    public int getItemQuantity(Item item) {
        int index = shopItems.indexOf(item);
        return quantityInStock.get(index);
    }

    // MODIFIES: this
    // EFFECTS:  changes the quantity of the item in quantityInStock
    public void setItemQuantity(Item item, int quantity) {
        if (checkIsInShop(item)) {
            ArrayList<Integer> newQuantity = getQuantityInStock();
            int index = shopItems.indexOf(item);

            newQuantity.set(index, quantity);
            setQuantityInStock(newQuantity);
        }
    }

    // MODIFIES: this
    // REQUIRES: item is in shopItems
    // EFFECTS:  returns the price of a specific item
    public int getItemPrice(Item item) {
        int index = shopItems.indexOf(item);
        return priceOfItems.get(index);
    }

    // MODIFIES: this
    // EFFECTS:  changes the price of the item in priceOfItems
    public void setItemPrice(Item item, int price) {
        if (checkIsInShop(item)) {
            ArrayList<Integer> newPrices = getPriceOfItems();
            int index = shopItems.indexOf(item);

            newPrices.set(index, price);
            setPriceOfItems(newPrices);
        }
    }

    // MODIFIES: this
    // EFFECTS:  adds value to the quantity of item in quantityInStock
    //           removes item if quantity becomes 0
    public void changeItemQuantity(Item item, int value) {
        if (checkIsInShop(item)) {
            ArrayList<Integer> newStocks = getQuantityInStock();
            int index = shopItems.indexOf(item);
            int currentlyStocked = newStocks.get(index);
            int newStocked = currentlyStocked + value;

            if (newStocked == 0) {
                removeShopItem(item);
            } else {
                newStocks.set(index, newStocked);
                setQuantityInStock(newStocks);
            }
        }
    }

    // MODIFIES: this
    // REQUIRES: quantity > 0
    // EFFECTS:  restocks all items in shopItems by quantity
    public void stockAllExisting(int quantity) {
        for (Item item : shopItems) {
            changeItemQuantity(item, quantity);
        }
    }

    // MODIFIES: this
    // REQUIRES: 0 < numItems, and quantity > 0
    // EFFECTS:  adds a number of random items from the disjunction of
    //           allPossibleItems and shopItems to shopItems with a given quantity
    public void stockWithRandomItems(int numItems, int quantity) {
        ArrayList<Item> itemsNotInShop = new ArrayList<>(allPossibleItems);
        itemsNotInShop.removeAll(shopItems);

        for (int i = 0; i < numItems; i++) {
            if (itemsNotInShop.size() > 0) {
                int randomIndex = rng.randomNumberUpTo(itemsNotInShop.size());
                Item item = itemsNotInShop.get(randomIndex);

                addShopItem(item, quantity);
                itemsNotInShop.remove(item);
            } else {
                break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  grabs all possible items and data from Items.json
    //           and stores them in allPossibleItems
    private void fetchAllPossibleItems() throws IOException {
        String content = FileUtils.readFileToString(itemsDataDir, "utf-8");
        JSONObject itemsJson = new JSONObject(content);
        JSONArray itemsArray = itemsJson.getJSONArray("items");
        allPossibleItems = new ArrayList<>();

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemData = itemsArray.getJSONObject(i);
            String itemName = itemData.getString("itemName");
            String itemType = itemData.getString("itemType");

            allPossibleItems.add(new Item(itemName, itemType));
        }
    }


    // EFFECTS: converts all shop data to a JSONObject and returns it
    @Override
    public JSONObject toJsonObj() {
        JSONObject shopObject = new JSONObject();
        shopObject.put("items", arrayListItemToJson(shopItems));
        shopObject.put("quantities", arrayListIntToJson(quantityInStock));
        shopObject.put("prices", arrayListIntToJson(priceOfItems));
        shopObject.put("name", shopName);

        return shopObject;
    }

    // EFFECTS: returns true if item exists in shopItems
    public boolean checkIsInShop(Item item) {
        return shopItems.contains(item);
    }

    // GETTERS
    public String getShopName() {
        return shopName;
    }

    public ArrayList<Item> getShopItems() {
        return shopItems;
    }

    public ArrayList<Integer> getPriceOfItems() {
        return priceOfItems;
    }

    public ArrayList<Integer> getQuantityInStock() {
        return quantityInStock;
    }

    // SETTERS
    public void setShopItems(ArrayList<Item> shopItems) {
        this.shopItems = shopItems;
    }

    public void setPriceOfItems(ArrayList<Integer> priceOfItems) {
        this.priceOfItems = priceOfItems;
    }

    public void setQuantityInStock(ArrayList<Integer> quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
