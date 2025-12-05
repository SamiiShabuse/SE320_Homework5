// Based on the example code at https://www.selenium.dev/documentation/
package edu.drexel.se320;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SeleniumTest {

    // UPDATE THIS PATH
    protected final String uiPath = "file:///C:/Users/sshab/Documents/School/SE320/SE320_Homework5/web/index.html";


    // Helper: a  create a Firefox driver with a small implicit wait.
    protected WebDriver makeDriver() {
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(2));
        return driver;
    }
    
    // Helper: add a single todo item with the given text
    protected void addTodoItem(WebDriver driver, String itemText) {
        // Check to see if its already open if not click to open
        WebElement elt = driver.findElement(By.id("controls1plus"));
        // Only click the plus control if its visible and its text is exactly "+"
        String plusText = elt.getText();
        if (elt.isDisplayed() && "+".equals(plusText)) {
            elt.click();
        }
        
        // Find the form field
        WebElement input = driver.findElement(By.id("itemtoadd"));

        // Make up a todo
        // Clear any existing text so sendKeys doesn't append
        input.clear();
        input.sendKeys(itemText);

        // Find and click the "Add to list" button
        WebElement addButton = driver.findElement(By.id("addbutton"));
        addButton.click();
    }
    
    /**
     * Verifies the correct behavior of the control panel toggle functionality.
     * 
     * Test confirms:
     * - The control section is hidden initially
     * - Clicking the plus icon reveals the controls and shows the minus icon
     * - Clicking the minus icon hides the controls and shows the plus icon
     * 
     * This helps ensure the UI maintains consistent visiblity rules for its 
     * control elements.
     */
    @Test
    public void testControlsCanBeRevealedAndHidden() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            WebElement controls = driver.findElement(By.id("controls1"));
            WebElement plus = driver.findElement(By.id("controls1plus"));
            WebElement minus = driver.findElement(By.id("controls1minus"));

            // Initial state: controls hidden, plus visible, minus hidden
            assertFalse(controls.isDisplayed(), "Controls should start hidden in UI");
            assertTrue(plus.isDisplayed(), "Plus icon should start visible UI");
            assertFalse(minus.isDisplayed(), "Minus icon should start hidden UI");

            // Click plus -> controls become visible, minus visible, plus hidden
            plus.click();
            assertTrue(controls.isDisplayed(), "Controls should be visible after clicking + in UI");
            assertFalse(plus.isDisplayed(), "Plus icon should be hidden after expanding in UI");
            assertTrue(minus.isDisplayed(), "Minus icon should be visible after expanding in UI");

            // Click minus -> controls hidden again, plus visible, minus hidden
            minus.click();
            assertFalse(controls.isDisplayed(), "Controls should be hidden after clicking - in UI");
            assertTrue(plus.isDisplayed(), "Plus icon should be visible after collapsing in UI");
            assertFalse(minus.isDisplayed(), "Minus icon should be hidden after collapsing in UI");
        } finally {
            driver.quit();
        }
    }

    /**
     * Validates that adding a single todo item inserts a list element
     * with the correct user-provided text.
     * 
     * Test confirms:
     * - The item is added to the list.
     * - It appears in the expected position with the correct text.
     * 
     * This helps verify basic input handling and item creation behavior.
     */
    @Test
    public void testAddSingleTodoShowsUpWithCorrectText() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);
            addTodoItem(driver, "First task");

            WebElement li = driver.findElement(By.id("item1"));

            // startsWith because the Delete button is there
            assertTrue(li.getText().startsWith("First task"), "Checking correct text for added element");
        } finally {
            driver.quit();
        }
    }
    
    /**
     * Ensure that adding multiple todoitems can be added sequentially and that
     * their order and the order is reflected in the DOM structure.
     * 
     * Test confirms:
     * - Three items can be added in sequence.
     * - They appear in the correct order within the list.
     * - Each item contains the expected text.
     * 
     * This validates that the application maintains consistent list ordering.
     */
    @Test
    public void testAddMultipleTodosInOrder() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            addTodoItem(driver, "First task");
            addTodoItem(driver, "Second task");
            addTodoItem(driver, "Third task");

            List<WebElement> items = driver.findElements(By.cssSelector("#thelist li"));
            assertEquals(3, items.size(), "There should be three TODOs");

            WebElement item1 = driver.findElement(By.id("item1"));
            WebElement item2 = driver.findElement(By.id("item2"));
            WebElement item3 = driver.findElement(By.id("item3"));

            assertTrue(item1.getText().startsWith("First task"));
            assertTrue(item2.getText().startsWith("Second task"));
            assertTrue(item3.getText().startsWith("Third task"));

            // Check that they appear in order within the UL
            assertEquals("item1", items.get(0).getAttribute("id"));
            assertEquals("item2", items.get(1).getAttribute("id"));
            assertEquals("item3", items.get(2).getAttribute("id"));
        } finally {
            driver.quit();
        }
    }

    /**
     * Confirms that deleting a specific todo item removes only that item
     * and does not affect or alter the other items in the list.
     * 
     * Test confirms:
     * - Three items can be added.
     * - Deleting the middle item removes only that item.
     * - The other two items remain unchanged in text and position.
     * - The list size is correctly updated.
     * 
     * This ensure that deletion logic targets only the intended item.
     */
    @Test
    public void testRemovingTodoDoesNotAffectOthers() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            addTodoItem(driver, "Keep me (1)");
            addTodoItem(driver, "Delete me");
            addTodoItem(driver, "Keep me (2)");

            // Delete the middle item through its delete button
            WebElement deleteButton2 = driver.findElement(By.id("button2"));
            deleteButton2.click();

            // Item2 should be gone
            assertEquals(0, driver.findElements(By.id("item2")).size(),
                    "Second item should be removed");

            // Item1 and Item3 should still exist with their texts unchanged
            WebElement item1 = driver.findElement(By.id("item1"));
            WebElement item3 = driver.findElement(By.id("item3"));

            assertTrue(item1.getText().startsWith("Keep me (1)"),
                    "First item should remain after deleting second");
            assertTrue(item3.getText().startsWith("Keep me (2)"),
                    "Third item should remain after deleting second");

            // Only two items should remain in the list
            List<WebElement> remaining = driver.findElements(By.cssSelector("#thelist li"));
            assertEquals(2, remaining.size(), "Two TODOs should remain after deletion");
        } finally {
            driver.quit();
        }
    }

    /**
     * Test validates that the Add button is hidden when three todo items
     * exist, and reappears after one is deleted.
     *
     * Test confirms:
     * - Adding three items hides the Add button.
     * - Attempting to add a fourth item fails as expected.
     * - Deleting one item makes the Add button visible again.
     * - A new item can be added again, maintaining the limit of three.
     * 
     * This test ensures the UI enforces the maximum item limit correctly.
     */
    @Test
    public void testAddButtonHiddenAtThreeItemsAndReappearsAfterDelete() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            addTodoItem(driver, "Task 1");
            addTodoItem(driver, "Task 2");
            addTodoItem(driver, "Task 3");

            // When there are three active items, the addbutton.hidden = true
            WebElement addButton = driver.findElement(By.id("addbutton"));
            assertFalse(addButton.isDisplayed(),
                    "Add button should be hidden once three items exist");

            // Try and fail to add a fourth item: the button is not interactable
            WebElement input = driver.findElement(By.id("itemtoadd"));
            input.clear();
            input.sendKeys("Should NOT be added");
            assertEquals(3, driver.findElements(By.cssSelector("#thelist li")).size(), "Should still only have three active items");
            assertEquals(0, driver.findElements(By.id("item4")).size(),
                        "No fourth item should be created");

            // Confirm there is no fourth item
            assertEquals(0, driver.findElements(By.id("item4")).size(),
                    "There should never be an item4 when the limit is three");

            // Now delete one item and check that the Add button comes back
            WebElement deleteButton1 = driver.findElement(By.id("button1"));
            deleteButton1.click();

            addButton = driver.findElement(By.id("addbutton"));
            assertTrue(addButton.isDisplayed(),
                    "Add button should reappear after an item is deleted");

            // And we can add another item again, keeping the list size at 3
            addTodoItem(driver, "Task 4");
            List<WebElement> items = driver.findElements(By.cssSelector("#thelist li"));
            assertEquals(3, items.size(),
                    "After deleting one and adding another, there should still be three active items");
        } finally {
            driver.quit();
        }
    }

    /**
     * Validates that deleting all todo items results in an empty list
     * and that the Add button is visible again for new entries.
     * 
     * Test confirms:
     * - Two items can be added.
     * - Deleting both items leaves the list empty.
     * - The Add button is visible again after all items are removed.
     * 
     * This ensures proper UI state reset when the list is cleared.
     */
    @Test
    public void testDeleteAllTodosLeavesEmptyList() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            addTodoItem(driver, "Task 1");
            addTodoItem(driver, "Task 2");

            // Delete both items
            driver.findElement(By.id("button1")).click();
            driver.findElement(By.id("button2")).click();

            // The UL should contain no LI children
            List<WebElement> items = driver.findElements(By.cssSelector("#thelist li"));
            assertEquals(0, items.size(), "All items should be removed from the list");

            // And the Add button must be visible again
            WebElement addButton = driver.findElement(By.id("addbutton"));
            assertTrue(addButton.isDisplayed(),
                    "Add button should be visible when the list is empty");
        } finally {
            driver.quit();
        }
    }

    /**
     * Tests the behavior when attempting to add an empty string as a todo item.
     * 
     * Test confirms:
     * - Adding an empty string creates a blank list item.
     * - The created item contains no text other than the Delete button.
     * 
     * This verifies how the application handles empty input cases.
     */
    @Test
    public void testAddEmptyStringCreatesBlankTodoItem() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            WebElement controls = driver.findElement(By.id("controls1"));
            if (!controls.isDisplayed()) {
                WebElement plus = driver.findElement(By.id("controls1plus"));
                plus.click();
                assertTrue(controls.isDisplayed(), "Controls should be visible after clicking +");
            }

            WebElement input = driver.findElement(By.id("itemtoadd"));
            WebElement addButton = driver.findElement(By.id("addbutton"));

            // Try to add an empty item
            input.clear();
            addButton.click();

            // The current implementation still creates a list item
            List<WebElement> items = driver.findElements(By.cssSelector("#thelist li"));
            assertEquals(1, items.size(),
                    "Adding an empty string currently creates a single blank TODO item.");

            // No items should have any text, its just an empty bullet with a delete button
            WebElement item1 = driver.findElement(By.id("item1"));
            assertTrue(item1.getText().trim().equals("Delete"),
                    "The created item should have no text other than the Delete button.");
        } finally {
            driver.quit();
        }
    }

    /**
     * Ensures that toggling the visibility of the control panel
     * does not affect the contents of the todo list.
     * 
     * Test confirms:
     * - An item can be added to the list.
     * - Toggling controls hidden and visible does not remove or alter the item.
     * - The list remains unchanged after control visibility changes.
     * 
     * This verifies that UI control state changes do not impact data integrity.
     */
    @Test
    public void testTogglingControlsDoesNotAffectTodoList() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);

            // Add one item
            addTodoItem(driver, "Task 1");

            // Confirm it exists
            WebElement item1 = driver.findElement(By.id("item1"));
            assertTrue(item1.getText().startsWith("Task 1"),
                    "Item must exist before toggling controls.");

            // Collapse controls
            WebElement minus = driver.findElement(By.id("controls1minus"));
            minus.click();
            assertFalse(driver.findElement(By.id("controls1")).isDisplayed(),
                    "Controls should be hidden after clicking '-'.");

            // Expand controls again
            WebElement plus = driver.findElement(By.id("controls1plus"));
            plus.click();
            assertTrue(driver.findElement(By.id("controls1")).isDisplayed(),
                    "Controls should be visible again after clicking '+'.");

            // List should still contain exactly the same item
            List<WebElement> items = driver.findElements(By.cssSelector("#thelist li"));
            assertEquals(1, items.size(), "List size should be unchanged after toggling controls.");
            assertTrue(items.get(0).getText().startsWith("Task 1"),
                    "Item text should not change after toggling controls.");

        } finally {
            driver.quit();
        }
    }


    /**
     * Example test written by Professor to demonstrate basic Selenium usage.
     */
    @Test
    public void testOneItem() {
        WebDriver driver = makeDriver();
        try {
            driver.get(uiPath);
            // Find the + to click to display the form to add a todo
            // Looking up by the id, not the name attribute
            WebElement elt = driver.findElement(By.id("controls1plus"));

            // Click on the [+]
            elt.click();

            // Find the form field
            WebElement input = driver.findElement(By.id("itemtoadd"));

            // Make up a todo
            input.sendKeys("Something to do");

            // Find and click the "Add to list" button
            WebElement addButton = driver.findElement(By.id("addbutton"));
            addButton.click();

            /* The first element added to the list will have id "item1"
             * Subsequent list items will have IDs item2, item3, etc.
             * Arguably this is too brittle, but rather than forcing you
             * all to become experts on the DOM, you may assume this is done
             * correctly, and/or you're testing this functionality implicitly. */
            WebElement li = driver.findElement(By.id("item1"));
            // We use startsWith because getText includes the text of the Delete button
            assertTrue(li.getText().startsWith("Something to do"), "Checking correct text for added element");
        } finally {
            driver.quit();
        }
    }

}
