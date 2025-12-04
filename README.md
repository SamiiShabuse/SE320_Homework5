
# SE320 Homework 5: GUI Testing

- Late Policy: Due to end of the term, the full reduced-points late policy *does not apply*. In addition to the due date for full credit, this assignment *must* be submitted prior to the end of the last week of classes, or it will receive no credit.

### Overview
The goal for this assignment is to give you just a taste of GUI testing.

You have been given a gradle project.  Inside the ```web/``` subdirectory, there is a single file ```index.html```.  If you open it in your web browser, you'll see that it is a very (very) simple TODO list.

Your job is to test the GUI functionality, including:

- Can you add TODOs?
- Can you remove TODOs?
- If you remove one TODO, are the others still there?
- etc.

The javadocs for the ```WebDriver``` will likely be useful: [http://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebDriver.html](http://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebDriver.html).  An important subtlety that people have lost time on in the past is that ```findElement``` (singular) should not be used to look for elements that are not there (like deleted elements); this is in the documentation, but not a natural assumption.  The documentation says to use ```findElements``` (plural) for this.  ```findElements``` returns a ```List<WebElement>```, and if a certain element shouldn't exist, looking up with ```findElements``` should return an empty (size 0) list.


### Skeleton Code and Dependencies
You've been given a test class with a single working test.  In order to make it work on your system, you'll need to do the following:

- Modify the uiPath string in that class to give the file URL to ```web/index.html``` on your system.  This is "file://" followed by the full system path to that file wherever you've unpacked the zip file.  The easiest way to get this is to open ```web/index.html``` in your favorite browser, and copy what the browser puts in your address bar over as the new value of uiPath.
    + Yes, there are ways to do this without hard-coding the path, but they're sensitive to how you run the tests. Since many students prefer to use (many various) IDEs in addition to Gradle, we've opted for the simplest thing that works for everyone.
- Install GeckoDriver ([https://github.com/mozilla/geckodriver](https://github.com/mozilla/geckodriver)), the program Selenium uses to control an instance of the Firefox web browser.  
    + For all platforms, you can download and install from [the release page](https://github.com/mozilla/geckodriver/releases)
        - On Mac, you may need to manually run the ```geckodriver``` binary by right-clicking, choosing Open, and then choosing Open (yes, again) in the resulting pop-up, or MacOS will disable it since you downloaded it from the internet. If you forget to do this, you'll get a pop-up complaining about geckodriver being from the internet.
    + On Mac, you can use homebrew to install geckodriver, but it will take a long time (it compiles the Rust compiler if you do this)
- Make sure geckodriver is in your system path.  There are directions for this in the [Selenium section of the GeckoDriver instructions](https://firefox-source-docs.mozilla.org/testing/geckodriver/geckodriver/Usage.html?highlight=selenium).  (As you read those instructions, don't worry about the Selenium version business; the gradle project uses an appropriate version.)
    + If you don't do this, your tests will fail with complaints about no geckodriver being installed, because Selenium won't know where to find it.

Once all three of the steps above are done, the existing test in the project should pass (```gradlew
test```).

We strongly encourage you to try this much on the early side, even if you don't intend to do the bulk of the work until later.  This will give us more time to help with any installation issues that may come up.

Note: You *must* use ```geckodriver```, and may not modify the assignment to use the WebDriver
support in Chrome, Edge, or other browsers.

### Graded Activities
There are several parts to this assignment:

1. Based on a reasonable interpretation of "this is a todo-list manager" as a high-level spec, write a reasonable set of tests to confirm that:
    + One can reveal and hide the controls
    + Todo items that are added show up in the right place in the list of items
    + Todo items that are removed disappear and do not affect other list items
    + As the list is limited to 3 items, this limit is handled appropriately (watch what happens to the "Add to list" button when you hit 3; how do you get the button back?)
2. Consider the process of creating an *event flow graph* covering traces with up to 1 item in the todo list. What kinds of issues would you encounter in creating such a model? Which events have restrictions on their ordering relative to other events (i.e., which events would *not* have arrows directly between them because they would require other things to happen in between)? You are not required to actually create or to submit such a diagram, but doing at least part of one can help clarify some of these issues.
3. Assume you *did* draw out the event flow graph above (again, you're not required to do this, but doing so might help you). That would be a model of the UI that assumes there is never more than one todo item. But per your tests above, you know the application *actually* allows up to 3. Please consider the consequences of this hypothetical mismatch.  If this event flow graph modeling a maximum of 1 list item were the basis for all of your tests, what sorts of problems are unlikely to be discovered? Can you describe a test whose behavior would not captured by such an event flow graph? Conversely, are there any ways in which the fixed size assumption might simplify testing in a good way?  Does it encourage you to write tests for impossible scenarios?  Would any of these issues be affected by working with a larger event flow graph (say, up to 5-element TODO lists)? (Note: this question is asking you about a kind of abstract model of the UI, so reflecting on not just the UI lecture but also the earlier discussions of state machine models might be useful.)


### Submission
Please submit 

1. a zip or tarball including the whole gradle project with tests
2. an appropriate text format (TXT, RTF, PDF) for your written explanation (ideally in the same directory as your build.gradle file)

### Grading

- 60% Tests that thoroughly test the expected behaviors of the application
- 20% Discussion of trade-offs
- 20% Discussion of fixed-size graph for a variable-sized UI

This assignment is itself worth 10% of your term grade.
