package piedel.piotr.thesis.ui.fragment.ocr.googledrive

import android.Manifest
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class ImportFromImageMultiplePermissionsListener(var view: ImportFromImageDriveContract.ImportFromImageDriveView?) : MultiplePermissionsListener {
    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        handlePermissionResponses(report)
    }

    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
        token?.continuePermissionRequest()
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
        this.view?.showFileChooserGalleryAndCamera()
    }

    private fun showToastWhenPermissionPermanentlyDenied() {
        this.view?.onPermissionPermanentlyDenied()
    }

    private fun showToastWhenNoStoragePermissionGiven() {
        this.view?.showToastWithRequestOfPermissions()
    }

    private fun showFilePickerWithoutCamera() {
        this.view?.showFileChooserOnlyGallery()
    }
}