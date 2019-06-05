package piedel.piotr.thesis.util.listener

import android.Manifest
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

open class CameraAndStoragePermissionListener(val view: CameraAndStorageViewInterface?) : MultiplePermissionsListener {

    interface CameraAndStorageViewInterface {
        fun showFileChooserOnlyGallery()
        fun showFileChooserGalleryAndCamera()
        fun onPermissionPermanentlyDenied()
        fun showToastWithRequestOfPermissions()
    }

    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        handlePermissionResponses(report)
    }

    private fun handlePermissionResponses(report: MultiplePermissionsReport) {
        when {
            report.areAllPermissionsGranted() ->
                showFilePickerWhenAllPermissionsGiven()
            !report.areAllPermissionsGranted() && !isCameraPermissionGiven(report) && isStoragePermissionGiven(report) -> {
                showFilePickerWithoutCamera()
            }
            !report.areAllPermissionsGranted() && !isStoragePermissionGiven(report) -> {
                showToastWhenNoStoragePermissionGiven()
            }
            report.isAnyPermissionPermanentlyDenied ->
                showToastWhenPermissionPermanentlyDenied()
        }
    }

    private fun isCameraPermissionGiven(report: MultiplePermissionsReport) =
            report.grantedPermissionResponses.isNotEmpty() && report.grantedPermissionResponses
                    .any { p -> p.permissionName == Manifest.permission.CAMERA }


    private fun isStoragePermissionGiven(report: MultiplePermissionsReport) =
            report.grantedPermissionResponses.isNotEmpty() && report.grantedPermissionResponses
                    .any { p -> p.permissionName == Manifest.permission.READ_EXTERNAL_STORAGE }

    private fun showFilePickerWhenAllPermissionsGiven() {
        view?.showFileChooserGalleryAndCamera()
    }

    private fun showToastWhenPermissionPermanentlyDenied() {
        view?.onPermissionPermanentlyDenied()
    }

    private fun showToastWhenNoStoragePermissionGiven() {
        view?.showToastWithRequestOfPermissions()
    }

    private fun showFilePickerWithoutCamera() {
        view?.showFileChooserOnlyGallery()
    }
}