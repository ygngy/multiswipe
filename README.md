# Multiswipe

A multi swipe library for Android `RecyclerView`.

## Table of Contents (Kotlin) [Java](README_JAVA.md)

- [Demo](#demo)
- [Requirements](#requirements)
- [Setup in Gradle](#setup-in-gradle)
- [Usage in Kotlin](#usage-in-kotlin)
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

2. Add the dependency to app's `build.gradle`

    ```groovy
    dependencies {
        implementation 'com.github.ygngy:multiswipe:1.0'
    }
    ```

## Usage in Kotlin

`ViewHolder` must implement `MultiSwipe`:

```kotlin
interface MultiSwipe {
    val leftSwipeList: LeftSwipeList?
    val rightSwipeList: RightSwipeList?
    fun onSwipeDone(swipeId: String): Any?
}
```

`leftSwipeList` and `rightSwipeList` must return swipe lists for left and right side or `null` for no swipe.  
`onSwipeDone` will be called with `swipeId` when user swipes.

### Create swipe lists at `ViewHolder`'s bind method

```kotlin
// Setting swipe icon and margins
val shareIcon = SwipeIcon(drawable = getDrawable(R.drawable.ic_baseline_share_24)!!,
        edgeHorMargin = getDimension(R.dimen.swipe_edge_margin),// margin between edge of view and first icon
        iconHorMargin = getDimension(R.dimen.swipe_icon_margin),// margin between first icon and second icon
        tailHorMargin = getDimension(R.dimen.swipe_tail_margin)// same margin used for separating other icons
)

// Create an optional swipe label
val shareLabel = SwipeLabel(
        text = getString(R.string.share_label),// label's text
        textColor = getColor(R.color.swipe_text),// label's text color
        textSize = getDimension(R.dimen.swipe_text_size),// label's text size
        margin = getDimension(R.dimen.swipe_text_margin),// margin between label and last icon
)
// Create a `SwipeTheme` for each swipe
val shareTheme = SwipeTheme (
        icon = shareIcon,// swipe icon
        label = shareLabel,// optional swipe label (could be null)
        backgroundColor = getColor(R.color.swipe_background),// background color for swipes
        viewColor = getColor(R.color.view_background_color)// optional color to use for itemView's background
        // To hide icons and label below each recyclerView's item you have to use viewColor OR layout background
        // viewColor is visible only if recyclerView's item layout don't have a background
)
// You can create an optional accept theme to use when swipe is in accept state
val shareAcceptTheme = shareTheme.copy(
        backgroundColor = getColor(R.color.swipe_accept_background),
        label = shareLabel.copy(textColor = getColor(R.color.swipe_accept_text)),
        viewColor = getColor(R.color.view_background_accept_color)
)

// Each swipe contains of at least an id and a theme and optionally acceptTheme and inactiveIcon
shareSwipe = Swipe (
       id = SWIPE_TO_SHARE_ID,// swipe id will be sent to onSwipeDone
       activeTheme = shareTheme,// theme used when user is interacting with this swipe
       acceptTheme = shareAcceptTheme,// optional accept theme (default is same as activeTheme)
       inactiveIcon = getDrawable(R.drawable.ic_disabled_share_24)// optional icon used for inactive themes
)
// If row has left swipes create left swipe list in the desired order 
mLeftSwipeList = LeftSwipeList (shareSwipe, copySwipe, cutSwipe)
// If row has right swipes create right swipe list in the desired order
mRightSwipeList = RightSwipeList (likeSwipe, editSwipe, delSwipe)
```

### Return swipe lists from `leftSwipeList` and `rightSwipeList`  

```kotlin
// Don't recreate swipes or any object here 
override val leftSwipeList: LeftSwipeList?
            get() = mLeftSwipeList
// Don't recreate swipes or any object here 
override val rightSwipeList: RightSwipeList?
            get() = mRightSwipeList
```

### At `onSwipeDone` of `ViewHolder` react to swipes

```kotlin
// Here react to swipe and return some data to MultiSwipeListener 
override fun onSwipeDone(swipeId: String): Any? {
    // Instead you may choose to only return data 
    // from this method to consume event at Activity or Fragment
    when(swipeId) {
        SWIPE_TO_SHARE_ID -> {
            // SHARE
        }
        SWIPE_TO_COPY_ID -> {
            // COPY
        }
        //...
    }
    return MyData()// return any data to Activity or Fragment
}
```

### At `Activity` or `Fragment` attach `MultiSwipeAdapter` to `RecyclerView`

```kotlin
recyclerView.multiSwiping(supportsRtl = true, // Switches leftSwipeList with rightSwipeList for RTL
                          hideInactiveIcons = true, // hides inactive icons when accepting swipe
                          swipeThreshold = .5F ,// The fraction of the View to be considered as accepted swiped
                          listener = object: MultiSwipeListener { // optional listener

                // This method is called after onSwipeDone of ViewHolder
                // and data is the returned value of onSwipeDone of ViewHolder
                override fun onSwipeDone(swipeId: String, data: Any?) {
                    // data is the return value of "ViewHolder.onSwipeDone"
                    // cast to data you returned from "ViewHolder.onSwipeDone"
                    val myData = data as MyData
                    when(swipeId) {
                        SWIPE_TO_SHARE_ID -> shareItem(myData)
                        SWIPE_TO_COPY_ID -> copyItem(myData)
                        //...
                    }
                }

                // This method could be used to clear on screen widgets such as FABs
                // direction may be START, END, NONE
                override fun swiping(direction: SwipeDirection, swipeListSize: Int) {
                    // here i hide FAB when user is swiping end side actively
                    if (direction == SwipeDirection.END) fab.hide() else fab.show()
                }
            })
```

## Credits

Creator: **"Mohamadreza Amani Yeganegi"**  
My Email: [ygnegy@gmail.com](mailto:ygnegy@gmail.com)  
My Github Profile: [https://github.com/ygngy](https://github.com/ygngy)  

## Copyright and License

### Copyright (c) 2021 "Mohamadreza Amani Yeganegi"  

Licensed under the [MIT license](LICENSE)
