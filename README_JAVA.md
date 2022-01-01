# Multiswipe

A swipe library for RecyclerView in Android.

## Table of Contents (Java) [Kotlin](README.md)

- [Demo](#demo)
- [Requirements](#requirements)
- [Setup in Maven](#setup-in-maven)
- [Setup in Gradle](#setup-in-gradle)
- [Usage in Java](#usage-in-java)
- [Credits](#credits)
- [Copyright and License](#copyright-and-license)

## Demo

Below watch the **Multiswipe** library in action:

![Multiswipe](screenshots/multiswipe.gif "Multiswipe in action")

*Each left and right sides could have zero, one or more swipe types.  
If user swipes greater than half of the view's width then only swipe action will be executed.  
Otherwise if there is more than one swipe type in current side and user swipes less than half of the view's width then swipe switches to next one.  
Here **half** of the view could be any fraction that the developer chooses once for all recyclerview rows.*

As you see in above image, FAB hides when it is in same side with swiped view. This is an optional choice for the developer and developer can easily hide views (or do any action) if needed at swipes. Also, developer can easily customize colors, icons, actions and many other properties of this library. 

## Requirements

The library requires Android **API level 16** or above.

You may use Maven or Gradle to build your app here is needed steps for both:

## Setup in Maven

If you are using Maven build system use these steps to add multiswipe library to your project.

1. Add the JitPack repository to your build file:

    ```maven
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```

2. Add the dependency:

    ```maven
    <dependency>
        <groupId>com.github.ygngy</groupId>
        <artifactId>multiswipe</artifactId>
        <version>1.2.1</version>
    </dependency>
    ```

## Setup in Gradle

If you are using Gradle build system use these steps to add multiswipe library to your project.

1. Add `jitpack.io` to your project’s repositories. Your repositories list may be in `settings.gradle` file or in project’s root `build.gradle` file:

    ```groovy
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
    ```

2. Add the required dependency to app's `build.gradle`:

    ```groovy
    dependencies {
        implementation 'com.github.ygngy:multiswipe:1.2.1'
    }
    ```

## Usage in Java

`ViewHolder` must implement `MultiSwipe`:

```java
public interface MultiSwipe {
    @Nullable
    LeftSwipeList getLeftSwipeList();

    @Nullable
    RightSwipeList getRightSwipeList();

    @Nullable
    Object onSwipeDone(int swipeId);
}
```

`getLeftSwipeList` and `getRightSwipeList` must return swipe lists for left and right side or `null` for no swipe.  
Once swipe is done `onSwipeDone` will be called with `swipeId`. `onSwipeDone` is the method that we should do the `swipeId` action or return a value to do the action at activity or fragment.

### Create swipe lists at adapter's constructor or `ViewHolder`'s bind method
As said above you must return swipe lists for left and right side or null from `getLeftSwipeList` and `getRightSwipeList`. But you should not create any object in these methods because they may be called too many times in each swipe. So, we should create swipe lists before and only return it in these methods.
There are many ways to create swipes, below is a concise way using default colors. I will explain how to create custom swipes later:
```java
// Each swipe contains of at least an id and an icon
Swipe likeSwipe = new Swipe(
        context, // context used to extract default colors and margins from resources
        SWIPE_TO_LIKE_ID, //id: swipe id will be sent to onSwipeDone when swipe is accepted
        getDrawable(R.drawable.ic_like_24), //activeIcon: active swipe icon
        getString(R.string.like), //activeLabel: OPTIONAL active swipe label
        getDrawable(R.drawable.ic_like_accept_24),//acceptIcon: OPTIONAL accept icon used when swipe move is enough to be accepted
        getString(R.string.like_accept),//acceptLabel: OPTIONAL accept label used when swipe move is enough to be accepted
        getDrawable(R.drawable.ic_disabled_like_24)//inactiveIcon: OPTIONAL icon used when this swipe is inactive or in queue
    )

// Create other swipes (shareSwipe, copySwipe, ...) in a similar way.

// If row has left swipes create left swipe list in the desired order
mLeftSwipeList = new LeftSwipeList(shareSwipe, copySwipe, cutSwipe);
// If row has right swipes create right swipe list in the desired order
mRightSwipeList = new RightSwipeList(likeSwipe, editSwipe, delSwipe);
```

### Return swipe lists from `getLeftSwipeList` and `getRightSwipeList` of `ViewHolder` 

```java
@Nullable
@Override
public LeftSwipeList getLeftSwipeList() {
    // Don't recreate swipes or any object here
    return mLeftSwipeList;
}

@Nullable
@Override
public RightSwipeList getRightSwipeList() {
    // Don't recreate swipes or any object here
    return mRightSwipeList;
}
```

### At `onSwipeDone` of `ViewHolder` handle swipe event
Now you have implemented `getLeftSwipeList` and `getRightSwipeList` of `MultiSwipe` interface in `ViewHolder` and you need to implement `onSwipeDone` of it. 
Once swipe is done `onSwipeDone` will be called with `swipeId`. `onSwipeDone` is the method that we should do the `swipeId` action or return a value to do the action at activity or fragment. You can handle swipes here and/or return an optional value from this method that will be sent to an optional swipe listener in activity or fragment.
```java
@Nullable
@Override
public Object onSwipeDone(int swipeId) {
    // Here handle swipe event and return some data to MultiSwipeListener
    // Instead you may choose to only return data
    // from this method to consume event at Activity or Fragment
    switch (swipeId) {
        case SWIPE_TO_SHARE_ID:
            // share
            break;
        case SWIPE_TO_COPY_ID:
            // copy
            break;
        //...
    }
    return new MyData();// return any data to Activity or Fragment
}
```

### At `Activity` or `Fragment` attach `MultiSwipeAdapter` to `RecyclerView`

This is the last step to setup this library.

```java
multiSwiping(recyclerView, new MultiSwipeListener() { // OPTIONAL listener
        // This method is called after onSwipeDone of ViewHolder
        // and data is the returned value of onSwipeDone of ViewHolder
        @Override
        public void onSwipeDone(int swipeId, @Nullable Object data) {
            // data is the return value of "ViewHolder.onSwipeDone"
            // cast to data you returned from "ViewHolder.onSwipeDone"
            MyData myData = (MyData) data;
            switch (swipeId) {
                case SWIPE_TO_SHARE_ID:
                    shareItem(myData);
                    break;
                case SWIPE_TO_COPY_ID:
                    copyItem(myData);
                    break;
                //...
            }
        }

        // This method could be used to clear on screen widgets such as FABs
        // direction may be START, END, NONE
        @Override
        public void swiping(@NotNull SwipeDirection direction, int swipeListSize) {
            // here i hide FAB when user is swiping end side actively
            if (direction == SwipeDirection.END) fab.hide();
            else fab.show();
        }
    });
```

There is more overloads for `multiSwiping` method with more optional arguments:
-	`listener`: An optional listener (`MultiSwipeListener`) to handle swipe events. 
-	`supportsRtl`: Is an optional Boolean argument and if it is **true** and layout direction is **RTL** then **left** swipe list will be used at **right** side and **right** list at **left** side of each row. Default value for this argument is **false** meaning left list is always on left side and right list is always on right side.
-	`hideInactiveIcons`: Is an optional Boolean argument and if it is true when swipe move is enough to be accepted, inactive icons will be hidden. Default value for this argument is true.
-	`swipeThreshold`: Is an optional `float` argument and is the fraction that the user should move the `view` to be considered as complete swipe. The fraction is calculated with respect to the view's bounds. Default value is .5f, which means, to swipe a `view`, user must move the `view` at least half of the `view`'s width. This value must be between 0 and 1. Default value is .5F.
-	`drawer`: Used to draw backgrounds, icons and labels for swipes. To change draws, use a custom subclass of `SwipeDrawerImpl` or implementation of `SwipeDrawer`. This argument should be used only in advanced situations where you need to override swipe draw events or draw something between drawing of background, icon, label and view. There is **no** need to use this argument just to change colors and icons. At next section I will show you how change colors and icons easily **without** using `drawer`. 



Listener argument in `multiswiping` function is an object implementing interface: `MultiSwipeListener`. This interface has two methods: `onSwipeDone` and `swiping`.
-	`onSwipeDone` method of this interface is the last place to handle swipe action. when swipe is done `onSwipeDone` of `MultiSwipeListener` is called after `onSwipeDone` method of `ViewHolder`. This method receives swipe id and any object returned from `onSwipeDone` of `ViewHolder`. If you wonder that there is no `onSwipeDone` in `ViewHolder`, remember that `ViewHolder` must implement `MultiSwipe` interface which has `onSwipeDone` method.
-	`multiswiping` method is called when user swipes (moves) view. This method has two arguments, `SwipeDirection` which specifies swipe state and is an enum and may be `START` for swipe at view’s start side, `END` for swipe at view’s end side or `NONE` for swipe closing. Second argument is `swipeListSize` which specifies the number of available swipes in that side and dictates the number of swipe icons and required vision space in that side. In this method you may hide some on screen views such as FABs or Floating Action Buttons that are covering swipe icons.

At this point all setups are done however if you want to customize backgrounds, color of labels, label size or margins read next section.

------------------------------------------------

## How to create custom Swipe themes
Swipe lists for left and right side is represented with `LeftSwipeList` and `RightSwipeList` classes. These classes get `Swipe` objects in constructor. So, to create swipes you need to create objects of type `Swipe`. This section explains how to create custom swipes. 

The simplest way to create swipe is the constructor that only needs icons and labels and uses default colors and margins:

```java
// Each swipe contains of at least an id and an icon
likeSwipe = new Swipe(
        context, //context: context used to extract default colors and margins from resources
        SWIPE_TO_LIKE_ID, //id: swipe id will be sent to onSwipeDone when swipe is accepted
        getDrawable(R.drawable.ic_like_24), //activeIcon: swipe icon
        getString(R.string.like), //activeLabel: OPTIONAL swipe label
        getDrawable(R.drawable.ic_like_accept_24),//acceptIcon: OPTIONAL icon used when swipe move is enough to be accepted
        getString(R.string.like_accept),//acceptLabel: OPTIONAL label used when swipe move is enough to be accepted
        getDrawable(R.drawable.ic_disabled_like_24)//inactiveIcon: OPTIONAL icon used when this swipe is inactive or in a queue
    )
//also overloaded constructor without optional arguments could be used
```
But if you want to customize colors or margins you need to use another `Swipe` constructor instead of above constructor. This constructor needs two swipe themes instead of icon for two states of swipe:

```java
// Each swipe contains of at least an id and a theme and optionally acceptTheme and inactiveIcon
shareSwipe = new Swipe (
       SWIPE_TO_SHARE_ID,//id: swipe id will be sent to onSwipeDone
       shareTheme,//activeTheme: theme used when user is interacting with this swipe
       shareAcceptTheme,//acceptTheme: OPTIONAL accept theme (default is same as activeTheme)
       getDrawable(R.drawable.ic_disabled_share_24)//inactiveIcon: OPTIONAL icon used when this swipe is inactive or in a queue
)
//also overloaded constructor without optional arguments could be used
```
To use this constructor, you need to create objects of type `SwipeTheme`. The `SwipeTheme` has two constructors if you do **NOT** need to customize label **size** or **margins** you can use following constructor: 

```java
SwipeTheme (
        Context context, // used to extract default values from resources.
        Drawable icon, // a drawable used to draw swipe icon.
        String label, // optional label to explain swipe action.
        boolean isAcceptTheme, // used to select default colors for label and backgrounds (default value is false)
        int labelColor, // color used to draw label (if isAcceptTheme is true default is pale red otherwise default is pale green)
        int backgroundColor, // color used to draw background below icon and label (if isAcceptTheme is true default is red otherwise default is green)
        int viewColor // color used to draw view background (in day theme of Android if isAcceptTheme is true default is pale red otherwise is pale green. in night theme of Android view background may be gray or black)
)
//also overloaded constructor without optional arguments could be used
```

If you specify all color arguments of this constructor then no default color will be used thus `isAcceptTheme` argument value has no effect. But if you do not specify a color argument in this constructor `isAcceptTheme` distinguishes default color for that.

If you do not need to customize labels size or margins between icons and labels then you can skip to next section. Below classes is only needed for customizing text size and margins between text and icons. 

Second constructor of `SwipeTheme` only is needed to customize text size and margins in addition to colors.

```java
SwipeTheme (
       SwipeIcon icon, // contains swipe's icon and margin between icons.
       SwipeLabel label, // optional label contains text, text color, text size and margin between text and icon (label could be null for no label)
       int backgroundColor, // will be drawn fully below icons and texts and rows of recyclerView (here default is transparent).
       int viewColor // view’s background which is above swipe icons and text (here default is transparent).
)
//also overloaded constructor without optional arguments could be used. in overloaded constructor colors are transparent and label is null
```
Usage:
```java
SwipeTheme shareTheme = new SwipeTheme(
        shareIcon,// swipe icon
        shareLabel,// OPTIONAL swipe label (could be null)
        getColor(R.color.swipe_background),// background color for swipes
        getColor(R.color.view_background_color)// OPTIONAL color to use for itemView's background
        // To hide icons and label below each recyclerView's item you have to use viewColor OR layout background
        // viewColor is visible only if recyclerView's item layout don't have a background
);
```


There is two options to hide swipe icons below view: background color in view's layout file or `viewColor` here.

To use above constructor, you need to create object of `SwipeIcon` and optionally (if you want a label) `SwipeLabel`. Below is constructor definition of these classes. 
If you want to customize label’s size or margin you need to use this constructor of `SwipeLabel`:

```java
SwipeLabel (
      String text, // text of the label
      int textColor, // text color of the label
      float textSize, // text size of the label
      float margin // margin between text and the icon next to it
)
```

Usage:
```java
SwipeLabel shareLabel = new SwipeLabel(
    getString(R.string.share_label),// label's text
    getColor(R.color.swipe_text),// label's text color
    getDimension(R.dimen.swipe_text_size),// label's text size
    getDimension(R.dimen.swipe_text_margin)// margin between label and last icon
);
```

You can use following constructor to customize margins between icons:
```java
SwipeIcon (
      Drawable drawable, // drawable will be drawn as active swipe icon.
      float edgeHorMargin, // horizontal margin between this active icon and edge of view.
      float iconHorMargin, // horizontal margin between this active icon and first inactive icon (if there is any other icons).
      float tailHorMargin // horizontal margin between inactive icons when this icon is active and there is more than one inactive icon.
)
```

Usage:
```java
// Setting swipe icon and margins
SwipeIcon shareIcon = new SwipeIcon(
    getDrawable(R.drawable.ic_baseline_share_24),// icon's drawable
    getDimension(R.dimen.swipe_edge_margin),// margin between edge of view and this icon
    getDimension(R.dimen.swipe_icon_margin),// margin between this icon and first inactive icon
    getDimension(R.dimen.swipe_tail_margin)// same margin used for separating inactive icons
);
```


# Best Practices

- **Create objects once**: 
The recycler view may have many rows and many rows may have similar swipe actions. Instead of repeatedly creating swipes for every row, you should create each swipe type once and reuse it in all rows. You can create a special factory class that creates all swipes in constructor and exposes them as class property like [this](https://github.com/ygngy/multiswipe/blob/main/demo/src/main/java/com/github/ygngy/demo/swipesample/SwipeCreator.kt) and [this](https://github.com/ygngy/multiswipe/blob/main/demo/src/main/java/com/github/ygngy/demo/swipesample/DefaultSwipeCreator.kt) then create only one instance of this class at recyclerview [adapter](https://github.com/ygngy/multiswipe/blob/main/demo/src/main/java/com/github/ygngy/demo/swipesample/RecyclerDemoAdapter.kt) as in demo app.

- Do not create any object in `getLeftSwipeList` and `getRightSwipeList` of `MultiSwipe`
As previously said `ViewHolder` must implement `MultiSwipe` interface and get swipe lists in `getLeftSwipeList` and `getRightSwipeList` methods. But these methods may be called repeatedly in each swipe so you should not create any object in these methods. Because if you create objects in these methods, extra objects will be created in memory. Also creating objects in these methods may slows down swipe animations because creating objects may be time consuming.
So where create swipes? Answer: If your swipes are repetitive in rows then best location for creating swipes is in adapter of recyclerView  because it will be created once. But if your swipes are not similar in rows and swipe actions and themes are very dependent to the content of the row then best location to create swipes is in bind method of ViewHolder.


## Credits

Creator: **"Mohamadreza Amani Yeganegi"**  
My Email: [ygnegy@gmail.com](mailto:ygnegy@gmail.com)  
My Github Profile: [https://github.com/ygngy](https://github.com/ygngy)  

## Copyright and License

### Copyright (c) 2021 "Mohamadreza Amani Yeganegi"  

Licensed under the [MIT license](LICENSE)
