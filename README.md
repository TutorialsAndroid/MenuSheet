![](https://github.com/TutorialsAndroid/MenuSheet/blob/main/sample/src/main/res/mipmap-xxhdpi/ic_launcher.png)

# MenuSheet

MenuSheet is an Android library which presents a view from the bottom of the screen. This library will help you to show bottom sheet menu on your app.

## ScreenShot
![](https://github.com/TutorialsAndroid/MenuSheet/blob/main/screenshot/Screenshot_1615727481.png)
![](https://github.com/TutorialsAndroid/MenuSheet/blob/main/screenshot/Screenshot_1615727486.png)

## Setup
The simplest way to use MenuSheet is to add the library as dependency to your build.

## Gradle

Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Step 2. Add the dependency

    dependencies {
            implementation 'com.github.TutorialsAndroid:MenuSheet:v1.0'
    }

## Getting Started

Get started by wrapping your layout in a `MenuSheetLayout`. So if you currently have this:

```xml
<!-- If your layout is like this then change to below one as i have given -->
<LinearLayout
    android:id="@+id/root"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <View
        android:id="@+id/view1"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
```

You would have to update it to look like this:

```xml
<com.menu.sheet.MenuSheetLayout
    android:id="@+id/bottomsheet"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Put your views here -->
    <LinearLayout
        android:id="@+id/root"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <View
            android:id="@+id/view1"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </LinearLayout>

</com.menu.sheet.MenuSheetLayout>
```

Back in your activity or fragment you would get a reference to the MenuSheetLayout like any other view.

```java
private MenuSheetLayout menuSheet;
menuSheet = (MenuSheetLayout) findViewById(R.id.bottomsheet);
menuSheet.setPeekOnDismiss(true); //to dismiss the menu onBackPress
```

### MenuSheetView

This component presents a BottomSheet view that's backed by a menu. It behaves similarly to the new `NavigationView` in the Design support library, and is intended to mimic the examples in the Material Design spec. It supports list and grid states, with the former adding further support for separators and subheaders.

Example from the sample app.

```java
MenuSheetView menuSheetView =
        new MenuSheetView(MenuActivity.this, MenuSheetView.MenuType.LIST, "Create...", new MenuSheetView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MenuActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                }
                return true;
            }
        });
menuSheetView.inflateMenu(R.menu.create); //pass your menu here like i have pass R.menu.create
menuSheet.showWithSheetView(menuSheetView);
```

 ## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2021 MenuSheet

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.