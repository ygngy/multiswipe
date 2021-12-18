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

*Each left and right sides could have zero, one or more swipes.  
If user swipes greater than half of the view's width then only swipe action will be executed.  
Otherwise if there is more than one swipe (in each side) and user swipes less than half of the view's width then swipe switches to next one.  
Here **half** of the view could be any fraction that the developer chooses.*

## Requirements

The library requires Android **API level 16+**.

## Setup in Maven

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

1. Add it in project's root `build.gradle` at the end of repositories:

    ```groovy
    allprojects {
        repositories {
            //...
            maven { url 'https://jitpack.io' }
        }
    }
    ```

2. Add the dependency to app's `build.gradle`:

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
`onSwipeDone` will be called with `swipeId` when user swipes.

### Create swipe lists at `ViewHolder`'s bind method
There are two ways to create swipes (customized and default), below is concise way using default colors:
```java
// Each swipe contains of at least an id and an icon
Swipe likeSwipe = new Swipe(
        context, // context used to extract default colors and margins from resources
        SWIPE_TO_LIKE_ID, // swipe id will be sent to onSwipeDone method
        getDrawable(R.drawable.ic_like_24), // active swipe icon
        getLabel(R.string.like), // OPTIONAL active swipe label
        getDrawable(R.drawable.ic_like_accept_24),// OPTIONAL accept icon used when swipe move is enough to accept it
        getLabel(R.string.like_accept),// OPTIONAL accept label used when swipe move is enough to accept it
        getDrawable(R.drawable.ic_disabled_like_24)// OPTIONAL icon used for inactive themes
    )

// Create other swipes (shareSwipe, copySwipe, ...) in similar way.

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

At this point all setups are done however if you want to customize backgrounds or color of labels or margins read next section.

------------------------------------------------

## How to create custom Swipe themes
If you want to change backgrounds or color of labels or margins between icons you need to create custom swipe themes.
Below is a sample using custom swipe theme:

```java
// Setting swipe icon and margins
SwipeIcon shareIcon = new SwipeIcon(
    getDrawable(R.drawable.ic_baseline_share_24),// icon's drawable
    getDimension(R.dimen.swipe_edge_margin),// margin between edge of view and this icon
    getDimension(R.dimen.swipe_icon_margin),// margin between this icon and first inactive icon
    getDimension(R.dimen.swipe_tail_margin)// same margin used for separating inactive icons
);

// Create an OPTIONAL swipe label
SwipeLabel shareLabel = new SwipeLabel(
    getString(R.string.share_label),// label's text
    getColor(R.color.swipe_text),// label's text color
    getDimension(R.dimen.swipe_text_size),// label's text size
    getDimension(R.dimen.swipe_text_margin)// margin between label and last icon
);

// Create a `SwipeTheme` for each swipe
SwipeTheme shareTheme = new SwipeTheme(
        shareIcon,// swipe icon
        shareLabel,// OPTIONAL swipe label (could be null)
        getColor(R.color.swipe_background),// background color for swipes
        getColor(R.color.view_background_color)// OPTIONAL color to use for itemView's background
        // To hide icons and label below each recyclerView's item you have to use viewColor OR layout background
        // viewColor is visible only if recyclerView's item layout don't have a background
);

// Create an OPTIONAL swipe label for when user swipes enough to accept
SwipeLabel shareAcceptLabel = new SwipeLabel(
        getString(R.string.share_label),// accept label's text
        getColor(R.color.swipe_accept_text),// accept label's text color
        getDimension(R.dimen.swipe_text_size),// accept label's text size
        getDimension(R.dimen.swipe_text_margin)// margin between label and last icon
);

// You can create an OPTIONAL accept theme to use when swipe is in accept state
SwipeTheme shareAcceptTheme = new SwipeTheme(
        shareIcon,// swipe's accept icon (could be different for accepted swipes)
        shareAcceptLabel,// OPTIONAL swipe's accept label (could be null)
        getColor(R.color.swipe_accept_background),// background color for accepted swipe
        getColor(R.color.view_background_accept_color)// OPTIONAL color to use for itemView's background
);

// Each swipe contains of at least an id and a theme and optionally acceptTheme and inactiveIcon
Swipe shareSwipe = new Swipe(
        SWIPE_TO_SHARE_ID,// swipe id will be sent to onSwipeDone
        shareTheme,// theme used when user is interacting with this swipe
        shareAcceptTheme,// OPTIONAL accept theme (default is same as activeTheme)
        getDrawable(R.drawable.ic_disabled_share_24)// OPTIONAL icon used for inactive themes
);

// If row has left swipes create left swipe list in the desired order
mLeftSwipeList = new LeftSwipeList(shareSwipe, copySwipe, cutSwipe);
// If row has right swipes create right swipe list in the desired order
mRightSwipeList = new RightSwipeList(likeSwipe, editSwipe, delSwipe);
```

----------------------------------------------------------------------

## Credits

Creator: **"Mohamadreza Amani Yeganegi"**  
My Email: [ygnegy@gmail.com](mailto:ygnegy@gmail.com)  
My Github Profile: [https://github.com/ygngy](https://github.com/ygngy)  

## Copyright and License

### Copyright (c) 2021 "Mohamadreza Amani Yeganegi"  

Licensed under the [MIT license](LICENSE)
