package model.goodsandservices;

import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {
    Shop shop1;
    Shop shop2;

    Item item1;
    Item item2;
    Item item3;

    @BeforeEach
    void runBefore() {
        try {
            shop1 = new Shop("Shop1");
            shop2 = new Shop("Shop2");

            item1 = new Item("Chicken", "Food");
            item2 = new Item("Squeaky Mouse", "Toy");
            item3 = new Item("Bone", "Toy");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Item> items = new ArrayList<>(Arrays.asList(item1, item2));
        ArrayList<Integer> prices = new ArrayList<>(Arrays.asList(item1.getPrice(), item2.getPrice()));
        ArrayList<Integer> stocks = new ArrayList<>(Arrays.asList(20, 10));
        shop1.setShopItems(items);
        shop1.setPriceOfItems(prices);
        shop1.setQuantityInStock(stocks);
    }

    @Test
    void addShopItemTest() {
        shop1.addShopItem(item3,30);
        ArrayList<Item> expectedVal1A =
                new ArrayList<>(Arrays.asList(item1, item2, item3));
        ArrayList<Integer> expectedVal1B =
                new ArrayList<>(Arrays.asList(item1.getPrice(), item2.getPrice(), item3.getPrice()));
        ArrayList<Integer> expectedVal1C = new ArrayList<>(Arrays.asList(20, 10, 30));

        assertEquals(expectedVal1A, shop1.getShopItems());
        assertEquals(expectedVal1B, shop1.getPriceOfItems());
        assertEquals(expectedVal1C, shop1.getQuantityInStock());

        shop1.addShopItem(item3,30);
        assertEquals(expectedVal1A, shop1.getShopItems());
        assertEquals(expectedVal1B, shop1.getPriceOfItems());
        assertEquals(expectedVal1C, shop1.getQuantityInStock());
    }

    @Test
    void removeShopItemTest() {
        shop1.removeShopItem(item2);
        ArrayList<Item> expectedVal1A = new ArrayList<>();
        expectedVal1A.add(item1);
        ArrayList<Integer> expectedVal1B = new ArrayList<>();
        expectedVal1B.add(item1.getPrice());
        ArrayList<Integer> expectedVal1C = new ArrayList<>();
        expectedVal1C.add(20);

        assertEquals(expectedVal1A, shop1.getShopItems());
        assertEquals(expectedVal1B, shop1.getPriceOfItems());
        assertEquals(expectedVal1C, shop1.getQuantityInStock());

        shop1.removeShopItem(item2);
        assertEquals(expectedVal1A, shop1.getShopItems());
        assertEquals(expectedVal1B, shop1.getPriceOfItems());
        assertEquals(expectedVal1C, shop1.getQuantityInStock());
    }

    @Test
    void getItemQuantityTest() {
        assertEquals(20, shop1.getItemQuantity(item1));
        assertEquals(10, shop1.getItemQuantity(item2));
    }

    @Test
    void setItemQuantityTest() {
        shop1.setItemQuantity(item1, 999);
        shop1.setItemQuantity(item2, 0);
        shop1.setItemQuantity(item3, 5);

        ArrayList<Integer> expectedVal = new ArrayList<>(Arrays.asList(999, 0));
        assertEquals(expectedVal, shop1.getQuantityInStock());
    }

    @Test
    void setItemPriceTest() {
        shop1.setItemPrice(item1, 999);
        shop1.setItemPrice(item2, 0);
        shop1.setItemPrice(item3, 20);

        ArrayList<Integer> expectedVal = new ArrayList<>(Arrays.asList(999, 0));
        assertEquals(expectedVal, shop1.getPriceOfItems());
    }

    @Test
    void changeItemQuantityTest() {
        shop1.changeItemQuantity(item1, 10);
        ArrayList<Integer> expectedVal1A = new ArrayList<>(Arrays.asList(30, 10));
        assertEquals(expectedVal1A, shop1.getQuantityInStock());

        shop1.changeItemQuantity(item2, 80);
        shop1.changeItemQuantity(item3, 10);
        ArrayList<Integer> expectedVal1B = new ArrayList<>(Arrays.asList(30, 90));
        assertEquals(expectedVal1B, shop1.getQuantityInStock());

        shop1.changeItemQuantity(item1, -15);
        ArrayList<Integer> expectedVal2A = new ArrayList<>(Arrays.asList(15, 90));
        assertEquals(expectedVal2A, shop1.getQuantityInStock());

        shop1.changeItemQuantity(item2, -5);
        shop1.changeItemQuantity(item3, -5);
        ArrayList<Integer> expectedVal2B = new ArrayList<>(Arrays.asList(15, 85));
        assertEquals(expectedVal2B, shop1.getQuantityInStock());
    }

    @Test
    void changeItemQuantityZeroTest() {
        shop1.changeItemQuantity(item1, -20);
        ArrayList<Integer> expectedVal1 = new ArrayList<>();
        expectedVal1.add(10);
        assertEquals(expectedVal1, shop1.getQuantityInStock());

        shop1.changeItemQuantity(item2, -10);
        ArrayList<Integer> expectedVal2 = new ArrayList<>();
        assertEquals(expectedVal2, shop1.getQuantityInStock());
    }

    @Test
    void restockAllTest() {
        shop1.stockAllExisting(10);
        ArrayList<Integer> expectedVal1 = new ArrayList<>(Arrays.asList(30, 20));
        assertEquals(expectedVal1, shop1.getQuantityInStock());

        shop1.stockAllExisting(100);
        ArrayList<Integer> expectedVal2 = new ArrayList<>(Arrays.asList(130, 120));
        assertEquals(expectedVal2, shop1.getQuantityInStock());
    }

    @Test
    void checkIsInShopTest() {
        assertTrue(shop1.checkIsInShop(item1));
        assertTrue(shop1.checkIsInShop(item2));
        assertFalse(shop1.checkIsInShop(item3));
    }

    @Test
    void stockWithRandomItemsTest() {
        shop1.setShopItems(new ArrayList<>());
        shop1.setPriceOfItems(new ArrayList<>());
        shop1.setQuantityInStock(new ArrayList<>());

        shop1.stockWithRandomItems(2, 10);
        int expectedVal1A = shop1.getQuantityInStock().get(0);
        int expectedVal1B= shop1.getQuantityInStock().get(1);
        assertEquals(2, shop1.getShopItems().size());
        assertEquals(expectedVal1A, 10);
        assertEquals(expectedVal1B, 10);
    }

    @Test
    void stockWithRandomItemsFullTest() {
        shop1.setShopItems(new ArrayList<>());
        shop1.setPriceOfItems(new ArrayList<>());
        shop1.setQuantityInStock(new ArrayList<>());

        shop1.stockWithRandomItems(shop1.allPossibleItems.size(), 10);
        assertEquals(shop1.allPossibleItems.size(), shop1.getShopItems().size());
        shop1.stockWithRandomItems(1, 10);
        assertEquals(shop1.allPossibleItems.size(), shop1.getShopItems().size());
    }

    @Test
    void getShopNameTest() {
        assertEquals("Shop1",shop1.getShopName());
        assertEquals("Shop2",shop2.getShopName());
    }
}