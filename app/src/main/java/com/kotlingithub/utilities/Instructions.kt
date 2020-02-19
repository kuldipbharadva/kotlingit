package com.kotlingithub.utilities

class Instructions {

    /* TODO : Camera & Gallery Instruction
    *
    *  This is mainly used for pick image from gallery or capture from camera.
    *  First you need to give permission in manifest
    *  TODO <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    *  TODO <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    *  Need to add following dependency for cropping image
    *  TODO implementation 'com.theartofdev.edmodo:android-image-cropper:2.6.0'
    *  add to manifest : register CropImageActivity in manifest
    *  TODO <activity
                android:name="com.theartofdev.edmodo.cropper.CropImageActivity"
                android:theme="@style/Base.Theme.AppCompat" />
    *
    * Then you need to add ImageSelectorFunctions in your app.
    * And call this class on click of select button for pick or capture image like
    * TODO ImageSelectorFunctions.getImageUploadInstance().openSelectionDialog(context)
    *
    * you can use profilePicUri(Uri) as static to get/set image uri anywhere
    * Done :)
    */

    /* //////////////////////////////////////////////////////////////// */

    /* TODO : Localization Instruction
    *
    * Localization
    * setLocale() used to change language of app and you can store that in preference to maintain selected language.
    * you need to reopen activity by using getIntent() especially when arabic language set.
    * you need to used reopenActivity method when onRestart method called because when you came back to previous
    * activity from next activity that time need to set selected language of that next activity's.
    * why onRestart : because onRestart called when activity goes on pause mode and for manage of selected lang when
    * get back to previous activity.
    * LocalizationTesting activity only for testing of language change effect.
    * NOTE : if there is no activity in back stack then no need to call reopenActivity method.
    */

    /* //////////////////////////////////////////////////////////////// */

    /* TODO : Parallax Scrolling Instruction
    *
    *  Parallax Scrolling means you can stick toolbar on top when user scroll to bottom.
    *  For this you need to use CoordinatorLayout in xml as Root tag.
    *  Without CoordinatorLayout tag you can't get this feature.
    *  With the help of CollapsingToolbarLayout we can collapse-expand our layout based on scrolling.
    *  We need to set collapse mode in xml.
    *  For this feature in your app just put activity_parallax_scrolling xml file.
    *  Also Add basic java code which is available in ParallaxScrollingActivity where the management given of parallax scrolling.
    *  You can customise layout as per your need/requirements.
    *  You can put your layout customise code inside Toolbar tag which stick on top and you can set any layout for
    *  rest of your screen.
    */

    /* //////////////////////////////////////////////////////////////// */

    /* TODO : Realm database Instruction
    *
    *   Realm does not use SQLite.
    *   Realm does not support Java outside of Android.
    *   You can use Realm db in Java, Swift, Objective-C, Javascript, .Net
    *
    *   Here is the sample example of Realm database.
    *   You need to extend ReamObject when create model for Realm db.
    *   Just check MyRealmTestModel model for reference.
    *   You can implement RealmChangeListener and they call onChange method when Realm db record change
    *   and you can do your stuff in onChange method.
    *
    *   Todo : Dependency
    *      implementation 'io.realm:realm-android:0.87.4'
    *      annotationProcessor 'io.realm:realm-android:0.87.4'
    *
    *   Todo : Reference Link
    *      https://realm.io/docs/java/latest/#the-default-realm
    *      https://www.thedroidsonroids.com/blog/android/realm-database-example/
    *
    *   Todo :  Initialize Realm in your app class
    *    RealmHelper.getRealmInstance(getApplicationContext());
    */

    /* //////////////////////////////////////////////////////////////// */

    /* TODO : Deeplink Instruction
    *
    * Deeplink is concept of open app from shared link if app installed in device.
    * Deeplink support API 23+
    * When you tapped on link then its give option to link open in app or browser if we deeplinking in our app.
    * For deeplink we need to add intent-filter in manifest.
    * For Ex.
    * TODO      <intent-filter>
                    <action android:name="android.intent.action.VIEW" />
                    <category android:name="android.intent.category.DEFAULT" />
                    <category android:name="android.intent.category.BROWSABLE" />
                   <data
                       android:host="google.com"
                       android:scheme="https" />
                   <!-- for reference : you can pass pathPrefix too -->
                   <!--<data-->
                       <!--android:host="www.google.com"-->
                       <!--android:pathPrefix="/api/"-->
                       <!--android:scheme="https" />-->
                </intent-filter>
    * This intent-filter block basically put in splash or starting activity so you can handle deeplinking.
    * DeeplinkActivity shows how to handle deeplink url.
    * Then you need to create link and shared to user.
    * You need to create link based on given 'host' in manifest or you need to set 'host' based on your link.
    * You can set multiple 'host' i mean <data> tag in intent-filter.
    * You can give query parameter in link like get url and fetch when deeplink open app.
    * For Ex. you can use below function to get param value. you can used your query param.

      private void getDeepLinkData() {
            Intent appLinkIntent = getIntent();
            try {
                Uri appLinkData = appLinkIntent.getData();
                if (appLinkData != null) {
                    if (appLinkIntent.getData().getQuery().contains("eid"))
                        eventId = appLinkIntent.getData().getQueryParameter("eid");
                    else if (appLinkIntent.getData().getQuery().contains("linkid"))
                        linkId = appLinkIntent.getData().getQueryParameter("linkid");
                } else if (appLinkIntent.getExtras() != null && appLinkIntent.getExtras().containsKey("eid") && appLinkIntent.getExtras().get("eid").toString().trim().length() > 0) {
                    eventId = appLinkIntent.getExtras().get("eid").toString();
                } else if (appLinkIntent.getExtras() != null && appLinkIntent.getExtras().containsKey("WALLET_DATA") && appLinkIntent.getExtras().get("WALLET_DATA") != null) {
                    walletData = appLinkIntent.getExtras().get("WALLET_DATA");
                } else if (appLinkIntent.getExtras() != null && appLinkIntent.getExtras().containsKey("TYPE") && appLinkIntent.getExtras().get("TYPE") != null) {
                    type = appLinkIntent.getExtras().getInt("TYPE");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    */

    /* //////////////////////////////////////////////////////////////// */

    /*  TODO: Pip Mode Instruction
     *
     *  PiP : Picture in Picture Mode
     *  This feature work only Oreo or above version.
     *  You can show your activity or view in small screen view.
     *  Generally this is used when you want to show some information when user goto foreground in app.
     *  Basically in map pip mode used more also used in show video etc.
     *  TODO:
     *    ~ Manifest file
     *    <activity android:name=".pipMode.PiPModeActivity"
            android:configChanges="screenSize|smallestScreenSize|screenLayout|orientation"
            android:resizeableActivity="true"
            android:supportsPictureInPicture="true"
            android:launchMode="singleTask"
            tools:targetApi="n" />
     *  PiPModeActivity have code of PiP.
     */

    /* //////////////////////////////////////////////////////////////// */

    /*  TODO: Mapping Instruction
    *
    *    MapActivity used for show map and add marker on current location
    *   Need to give permission in manifest
    *   TODO
    *       <permission android:name="android.permission.ACCESS_FINE_LOCATION" />
            <permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    *   Need to set <meta-data> tag for google map usage
    *   <!-- google map -->
        TODO <meta-data
                android:name="com.google.android.gms.version"
                android:value="@integer/google_play_services_version" />
        <!-- your google map project api key -->
        TODO <meta-data
                android:name="com.google.android.geo.API_KEY"
                android:value="@string/google_map_key" />
    *   TODO: MapWithPlacesActivity
    *   used for search google places
    *   need to enable google places api in google console
    *
    *   TODO: CustomGooglePlacesActivity
    *   used for create custom google place picker
    *   PlacesAutoCompleteAdapter used for custom place picker
    *   you can customize layout as per your design requirement.
    *   you need to set your map api key in PlaceAutoCompleteAdapter API_KEY
    *   TODO: API_KEY = "YOUR MAP API KEY"
    *
    *   TODO: MyClusterRender
    *   class used to show on map clustering.
    *   You can customize you cluster bg, textSize, textColor etc. in this class.
    *   If we have multiple pins/marker at same place(i.e, near at that place) that time Cluster used to
    *   show single pins/marker and count on that marker.
    *   You can set onClick event on cluster and you can do your stuff.
    *   MapClusterActivity in this activity implement's cluster usage.
    *   for cluster add dependency
    *   TODO
    *    implementation 'com.google.maps.android:android-maps-utils:0.5+'
    *
    *   Need to create MyItem model class
    *   Put setClusterOnMap() method in your MapActivity and addItem in cluster manager.
    *
    *   TODO: Map Styling Usage instruction
    *   You can do map styling.
    *   You just need to map json file to put in row folder and used in in map.
    *   TODO: mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.map));
    *   Just put this line in onMapReady() method to map styling
    *   You can change road, highway, park etc color on map for that you just need to modify json file
    *   and change color as your need..
    *
    *   TODO:
    *    MUST ENABLE API FOR ROUTE DRAW ON MAP
    *     ~ Google Maps JavaScript API
    *     ~ Google Maps GeoCoding API
    *     ~ Google Maps Places API - For Place Picker
    */

    // https://www.figma.com/proto/sKUp0OerpBlHFC97HkgrBy/Application?node-id=0%3A2&viewport=254%2C-161%2C1.40023672580719&scaling=min-zoom
    /* //////////////////////////////////////////////////////////////// */


}