package com.kotlingithub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.bottomsheet.BottomSheetActivity
import com.kotlingithub.cameraGallery.CameraGalleryActivity
import com.kotlingithub.countryCodePicker.CustomCountryCodePickerActivity
import com.kotlingithub.db.LocalDbActivity
import com.kotlingithub.dbRealm.RealmActivity
import com.kotlingithub.deeplink.DeepLinkActivity
import com.kotlingithub.localization.LocalizationActivity
import com.kotlingithub.parallaxScrolling.ParallaxScrollingActivity
import com.kotlingithub.pipMode.PiPModeActivity
import com.kotlingithub.qrCode.QrCodeGeneratorActivity
import com.kotlingithub.qrCode.QrCodeScannerActivity
import com.kotlingithub.showCase.ShowCaseViewActivity
import com.kotlingithub.socialLogin.FacebookLoginActivity
import com.kotlingithub.socialLogin.GmailLoginActivity
import com.kotlingithub.viewPagerUse.ViewPagerCustomTabActivity
import com.kotlingithub.viewPagerUse.ViewPagerUsageActivity
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import com.kotlingithub.mapping.MapActivity
import com.kotlingithub.mapping.MapWithPlacesActivity
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnNeverAskAgain

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickEvent(v: View) {
        when (v.id) {
            R.id.tvCameraGallery -> makeIntent(CameraGalleryActivity::class.java)
            R.id.tvBottomSheet -> makeIntent(BottomSheetActivity::class.java)
            R.id.tvCountryCodePicker -> startActivityForResult(
                Intent(
                    this,
                    CustomCountryCodePickerActivity::class.java
                ), 1
            )
            R.id.tvLocalization -> makeIntent(LocalizationActivity::class.java)
            R.id.tvParallaxScrolling -> makeIntent(ParallaxScrollingActivity::class.java)
            R.id.tvViewPagerUse -> makeIntent(ViewPagerUsageActivity::class.java)
            R.id.tvViewPagerCustomTabUse -> makeIntent(ViewPagerCustomTabActivity::class.java)
            R.id.tvRealmDb -> makeIntent(RealmActivity::class.java)
            R.id.tvDeeplink -> makeIntent(DeepLinkActivity::class.java)
            R.id.tvShowCase -> makeIntent(ShowCaseViewActivity::class.java)
            R.id.tvQrScanner -> makeIntent(QrCodeScannerActivity::class.java)
            R.id.tvQrGenerator -> makeIntent(QrCodeGeneratorActivity::class.java)
            R.id.tvPiPMode -> makeIntent(PiPModeActivity::class.java)
            R.id.tvFbLogin -> makeIntent(FacebookLoginActivity::class.java)
            R.id.tvGmailLogin -> makeIntent(GmailLoginActivity::class.java)
            R.id.tvLocalDatabase -> makeIntent(LocalDbActivity::class.java)
            R.id.tvSimpleMap -> makeIntent(MapActivity::class.java)
            R.id.tvSimpleMapPlaces -> makeIntent(MapWithPlacesActivity::class.java)
        }
    }

    private fun makeIntent(c: Class<out Any>) {
        startActivity(Intent(this@MainActivity, c))
    }


    @NeedsPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun onAllow() {

    }

    @OnPermissionDenied(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun onDenied() {
    }

    @OnNeverAskAgain(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun onNeverAsk() {
    }
}
