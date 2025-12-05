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