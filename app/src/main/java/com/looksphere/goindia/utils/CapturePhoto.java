package com.looksphere.goindia.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.looksphere.goindia.R;

public class CapturePhoto {

	public static final int SHOT_IMAGE = 1;
	public static final int PICK_IMAGE = 2;
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";	
	
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private Activity activity;
	private String mCurrentPhotoPath;
	private int actionCode;
	
	public CapturePhoto(Activity activity) {
		
		this.activity = activity;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}
	
	public int getActionCode() {
		return this.actionCode;
	}
	
	public void dispatchTakePictureIntent(int actionCode, int requestCode) {
		Intent takePictureIntent = null;
		
		this.actionCode = actionCode;
		
		switch(actionCode) {
		case SHOT_IMAGE:
			File f = null;
			takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;
			
		case PICK_IMAGE :
			takePictureIntent = new Intent(Intent.ACTION_PICK,
				     MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			break;
		default:
			break;			
		} // switch

		if(takePictureIntent != null) {
			activity.startActivityForResult(takePictureIntent, requestCode);
		}
		
	}
	
	public void handleCameraPhoto(ImageView imageView) {		
		if (mCurrentPhotoPath != null) {
			
			/* Decode the JPEG file into a Bitmap */
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();

            /* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
            int targetW = 400;
            int targetH = 400;

		/* Get the size of the image */

            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
			
			Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(
					bmp, 
					imageView.getWidth(), 
					imageView.getHeight(),
                    ScalingUtilities.ScalingLogic.CROP,
	       		 	ScalingUtilities.getFileOrientation(mCurrentPhotoPath)
	       		 	);
	        bmp.recycle();

			/* Associate the Bitmap to the ImageView */
			imageView.setImageBitmap(scaledBitmap);
			imageView.setVisibility(View.VISIBLE);
			
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
	}
	
	public void handleMediaPhoto(Uri targetUri, ImageView imageView) {
		
		try {
			Bitmap bmp = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), targetUri);
			Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(
					bmp, 
					imageView.getWidth(), 
					imageView.getHeight(), 
	                ScalingUtilities.ScalingLogic.FIT,
	                ScalingUtilities.getGalleryOrientation(activity, targetUri));
			bmp.recycle();
			
			/* Associate the Bitmap to the ImageView */
			imageView.setImageBitmap(scaledBitmap);
			imageView.setVisibility(View.VISIBLE);
		} catch (Exception e) { } 
	}

//	private void setPic() {
//
//		/* There isn't enough memory to open up more than a couple camera photos */
//		/* So pre-scale the target bitmap into which the file is decoded */
//
//		/* Get the size of the ImageView */
//		int targetW = imageView.getWidth();
//		int targetH = imageView.getHeight();
//
//		/* Get the size of the image */
//		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//		bmOptions.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//		int photoW = bmOptions.outWidth;
//		int photoH = bmOptions.outHeight;
//		
//		/* Figure out which way needs to be reduced less */
//		int scaleFactor = 1;
//		if ((targetW > 0) || (targetH > 0)) {
//			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
//		}
//
//		/* Set bitmap options to scale the image decode target */
//		bmOptions.inJustDecodeBounds = false;
//		bmOptions.inSampleSize = scaleFactor;
//		bmOptions.inPurgeable = true;
//
//		/* Decode the JPEG file into a Bitmap */
//		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//		
//		/* Associate the Bitmap to the ImageView */
//		imageView.setImageBitmap(bitmap);
//		imageView.setVisibility(View.VISIBLE);
//	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    activity.sendBroadcast(mediaScanIntent);
	}	
	
	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		return f;
	}
	
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	
	private String getAlbumName() {
		return activity.getString(R.string.album_name);
	}
	
	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
		} else {
			Log.v(activity.getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		return storageDir;
	}	
	
}
