# SE320 Homework 5 - GUI Testing Write-Up
- Author: Samii Shabuse
- Course: SE320 - Software Verification and Validation
- Professor: Colin Gordan
- Assignment: Homework 5 - GUI Testing

---

## 1. Test Suite Based on High-Level TODO List Specification

The application under test is a very simple TODO list implemented in a static HTML page called `index.html`
Based on a reasonable interpretation of the homework assignment instructions "this is a todo-list manager" the following key behaviors should be testable:

### 1. Reveal and Hide the Controls
The UI begins with the control panel (`controls1`) hidden.
My test `testControlsCanBeRevealedAndHidden` verifies:

- Controls start hidden
- Clicking "+" reveals the input controls and shows the "-" button
- Clicking "-" hides the controls and stores the "+" button

This ensures consistent state control for UI visibility.

---

### 2. Adding ToDo Items
I wrote multiple tests that validate adding items:

- `testAddSingleTodoShowsUpWithCorrectText`
    Confirms one item is added with correct text and correct ID (`item1`).

- `testAddMultipleTodosInOrder`
    Confirms three items can be added sequentially and that:
    - They appear in the correct order inside the `<ul>`
    - Their IDs (`item1`,`item2`,`item3`) match insertion order.
    - Their text matches teh input provided.

These test ensure list insertion behavior is correct.

--- 

### 3. Removing Items Without Affecting Others
The test `testRemovingTodoDoesNotAffectOthers` checks:
- Adding three items
- Deleting the middle one ('item2')
- Confirming:
    - `item2` is removed using `findElements`
    - `item1` and `item3` remains unchanged
    - List size updates correctly

This verifies correct delete behavior and item independence.

---

### 4. Handling the 3-Item Capacity Limit
Per the HTML/JS implementation, the list is limited to 3 items at a time.

`testAddButtonHiddenAtThreeItemsAndReappearsAfterDelete` ensures:
- After adding 3 items, the Add button is hidden
- No 4th item is created
- Deleting one itme makes the Add button visible again
- Adding another item restores the list to size 3

This confirms the proper enforcement of the capacity constraint and correct UI state transitions.

--- 

### 5. Empty Input Edge Cases
The test `testAddEmptyStringCreatesBlankTodoItem` documents real-life system behavior:

- Adding an empty string still produces a Todo item
- The created `<li>` contains only the Delete button.

This is useful because it reveals a specification ambiguity:
If the spec intended to prevent empty Todos, the current implementation does not enforce that.

This test was created not to "fix" this behavior, but to record and show the ambiguity. This is
aligned with the assignment because it verifies the GUI design, and doesn't modify or change anything.

---

### 6. Toggling Controls Does Not Affect List State
`testTogglingControlsDoesNotAffectTodoList` verifies:

- An item is added
- Controls are collapsed and expanded
- The Todo list remains unchanged

This validates independence between UI visibility and stored items.

---

### Overall Coverage
Across all the test, the test suite covers:

- UI visibility toggling
- Input handling
- Consistent insertion odering
- Deletion correctness
- State-dependent behavior (Add button hidden at capacity)
- Proper list recovery after deletions
- Edge-case behavior on empty input

This satisifies the assignment's instructions on having a "reasonable set of tests" based on the high-level spec.

---

## 2. Discussion of Trade-Offs

Designing the GUI tests requires balancing thoroughness, brittlness, and maintability based of the lecture notes.

So some of my tradeoffs in my test suite:

### A. Using fixed elements IDs vs flexible locators
I used DOM IDs such as `item1`, `button2`, `controls1plus`.
Advantages:
- Tests are simple, readable, and deterministic.
- They directly validate implementation details 
- Easy to write and develop for testing
Disadvantages:
- The tests become brittle if the HTML changes or the ID's are renamed.
- These tests are assuming that the developer's ID are correct.

This is fine though because this is small project and assessment are simple.

### B. Observing actual behavior vs enforcing ideal behavior

Example: empty input creates a blank Todo.

I chose not to treat empty input as an error because:
- The assignment is about testing production GUI implementation
- There is no stated specification on what the output should be.
- Test should document what UI actually does as well

This matches real-world validation testing where the implementation defines what the output should be, unless specificed what the output should be the documentation.

---

### C. Simple UI-State Checks vs More Complex Event Modeling
I used Selenium commands:

- `isDisplayed()`
- `findElements()`
- `getText()`
- Counting `<li>` children

This helps avoid overcomplicating the testing. I didn't do the advanced one such as waiting for events, handling animations (it was static), etc. as it wasn't necessary for the assignment. 

--- 

### D. Independence of UI Componenets
Testing control-panel visibility separately form list content. An Example is toggling controls tests.

This from the lecture notes as not all events are available, and UI elements must only affect the parts they are intended to.

---

## 3. Event Flow Graph (EFG) and the 1-Item Modeling Mismatch

The assignment asks us to consider building an event flow graph that models UI behavior under the assumption that "only 1 Todo item" ever exists.

### A. Issues When Creating a 1-Item EFG
A limited EFG must impose restrictions such as:
- Adding a second item would not be represented in the model  
- No edges would exist for “delete item 2” or “add item when 1 already exists”  
- UI states like “3 items, Add button hidden” would not exist  
- Event availability changes such as Add button hides would be missing  

Also, events such as clicking Delete are only meaningful when an item exists.  
The EFG must encode these state-dependent constraints or else it wouldn't be meaningful testing.

---

### B. Problems the 1-Item Model Cannot Detect
Since the real application allows 3 items, a 1-item EFG would fail to detect:
- Incorrect ordering when multiple items are added  
- Bugs where deleting the middle item affects another item  
- Capacity limit bugs (Add button hiding/reappearing)  
- DOM ID assignment issues for item2 and item3  
- Edge cases involving insertion beyond the first element  

A concrete example missed by the model is:  

Add three items --> delete the second --> confirm items 1 and 3 remain untouched. This behavior does not exist in a 1-item event graph.

---

### C. How the Mismatch Might Simplify Testing
There are benefits to a simplified model:

- Fewer states mean easier to generate event sequences  
- Reduced graph complexity reduces test explosion  
- Helps verify core behaviors like:  
  - revealing/hiding controls  
  - adding the first item  
  - deleting the only item  

This matches the lecture's idea that simplified models can test core functionality even if they miss complex behaviors.

---

### D. Does the Model Encourage Impossible Scenarios?
A 1-item model generally avoids impossible scenarios, but it may:

- Implicitly assume the Add button is always available  
- Fail to represent states where the Add button is hidden  
- Overlook multi-item interactions  

This can create assumptions not reflected in real UI constraints and be a problem in testing.

---

### E. What If We Modeled 5 Items Instead of 1?
A model with capacity 5 (while the real UI allows only 3):

- Would produce tests expecting item4 and item5  
- These tests would fail, revealing a spec vs implementation mismatch
- This could help detect under-specification or design limitations  

Therefore, a larger model improves behavioral expectations but increases complexity.

---

## Conclusion

My Selenium test suite thoroughly exercises the core behaviors of the TODO-list GUI, including visibility toggling, adding/removing items, preserving list integrity, and enforcing the 3-item limit.  
The trade-offs reflect practical testing decisions in event-driven interfaces, and the Event Flow Graph discussion highlights how simplified models shape test coverage. Overall, the tests and reasoning satisfy the assignment’s requirements for GUI testing, state modeling, and analysis of specification constraints.
