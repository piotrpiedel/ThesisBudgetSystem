package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.CAMERA_PIC_REQUEST_CODE
import piedel.piotr.thesis.util.FILE_SELECT_REQUEST_CODE
import piedel.piotr.thesis.util.getPath
import piedel.piotr.thesis.util.showToast


class ChoosePictureSourceDialogPresenter : BasePresenter<ChoosePictureSourceDialogView>() {

    fun onLoadFromGalleryClick(passedActivityFragment: FragmentActivity) {
        checkPermissionForReadAndWriteStorage(passedActivityFragment)
    }

    fun onLoadFromCameraClick(passedActivityFragment: FragmentActivity) {
        checkPermissionForCameraAndStorage(passedActivityFragment)
    }

    private fun checkPermissionForCameraAndStorage(passedActivityFragment: FragmentActivity) {
        Dexter.withActivity(passedActivityFragment)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)//Manifest.permission.READ_EXTERNAL_STORAGE
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        when {
                            report?.areAllPermissionsGranted() == true -> view?.showCamera()
                            report?.isAnyPermissionPermanentlyDenied == false && !report.areAllPermissionsGranted() -> {
                                showToast(passedActivityFragment, "Application require permissions to store receipts")
                            }
                            report?.isAnyPermissionPermanentlyDenied == true -> view?.onPermissionDenied()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }

                })
                .check()

    }


    private fun checkPermissionForReadAndWriteStorage(passedActivityFragment: FragmentActivity) {
        Dexter.withActivity(passedActivityFragment)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)//Manifest.permission.READ_EXTERNAL_STORAGE
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        when {
                            report?.areAllPermissionsGranted() == true -> view?.showFileChooser()
                            report?.isAnyPermissionPermanentlyDenied == false && !report.areAllPermissionsGranted() -> {
                                showToast(passedActivityFragment, "Application require permissions to store receipts")
                            }
                            report?.isAnyPermissionPermanentlyDenied == true -> view?.onPermissionDenied()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }

                })
                .check()

    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, passedActivityFragment: FragmentActivity) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                FILE_SELECT_REQUEST_CODE -> {
                    val uri = data?.data    // Get the Uri of the selected file
                    val picturePath = getPath(passedActivityFragment, uri as Uri) as String  // Get the FilePath of the selected file
                    view?.passPicturePath(picturePath)
                }
                CAMERA_PIC_REQUEST_CODE -> {
                    view?.passIntentWithPicture(data)
                }
            }
    }

}