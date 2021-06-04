package com.mycab.Driver.Activity.Fragment.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.mycab.R;
import com.mycab.databinding.ActivityUploadDocumnetBinding;
import com.mycab.utils.Api;
import com.mycab.utils.Appconstant;
import com.mycab.utils.ImageUtils;
import com.mycab.utils.ProgressBarCustom.CustomDialog;
import com.mycab.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;

public class UploadDocumnetActivity extends AppCompatActivity {
    ActivityUploadDocumnetBinding binding;
    private static final String IMAGE_DIRECTORY = "/directory";
    private int GALLERY = 1, CAMERA = 2;
    String stName = "", stTax = "";
    File f;

    private File front_gallery_file;
    private File back_gallery_file;
    private  File fileGovtFront,fileGovtBack,fileDrivingBack,fileDrivingFront,fileCommericalFront,fileCommericalBack,fileRCFront,fileRCBack;
    String fronIvGovt = "", backIvGovt = "", backIvDriving = "", frontIvDriving = "", backIvCommerical = "", frontIvCommerical = "",frontIvRc="",backIvRc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDocumnetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(UploadDocumnetActivity.this,AddBankDetailActivity.class));
                stName = binding.edtUserName.getText().toString().trim();
                stTax = binding.etTax.getText().toString().trim();
                if (stName.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }else if (stTax.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Enter your Valid Tax number", Toast.LENGTH_SHORT).show();
                }
                else if (fronIvGovt.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the GovtId Front Image ", Toast.LENGTH_SHORT).show();
                }
                else if (backIvGovt.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the GovtId Back Image ", Toast.LENGTH_SHORT).show();
                }
                else if (frontIvDriving.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the Driving License Front Image ", Toast.LENGTH_SHORT).show();
                }
                else if (backIvDriving.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the Driving License back Image ", Toast.LENGTH_SHORT).show();
                }

                else if (frontIvCommerical.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the Commercial Insurance Front Image ", Toast.LENGTH_SHORT).show();
                }
                else if (backIvCommerical.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the Commercial Insurance back Image ", Toast.LENGTH_SHORT).show();
                }
                else if (frontIvRc.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the Registration Id Front Image ", Toast.LENGTH_SHORT).show();
                }
                else if (backIvRc.equals("")){
                    Toast.makeText(UploadDocumnetActivity.this, "Please Choose the Registration Id back Image ", Toast.LENGTH_SHORT).show();
                }
                else {
                    upload_Document();
                }
            }
        });

        binding.btnDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_DrivingImage();

            }
        });
        binding.btnGovt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_GovtImage();

            }
        });

        binding.btnCommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_CommercialImage();

            }
        });

        binding.btnRc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_RCImage();

            }
        });

    }

    public void showPictureDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture image from camera"};

        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        choosePhotoFromGallery();
                        break;

                    case 1:
                        captureFromCamera();
                        break;
                }

            }
        });

        builder.show();
    }


    public void choosePhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY);
    }

    public void captureFromCamera() {

        Intent intent_2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_2, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    Log.e("ProfileActivity", "path: " + path);
                    Log.e("ProfileActivity", "bitmap: " + bitmap);
                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    binding.profileImg.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            binding.profileImg.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");

            Log.e("ProfileActivity", "f: " + f);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("ProfileActivity", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void upload_GovtImage() {

        final Dialog dialog = new Dialog(UploadDocumnetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.upload_image_layout);
        ImageView imgFront = (ImageView) dialog.findViewById(R.id.imgFront);
        ImageView imgBack = (ImageView) dialog.findViewById(R.id.imgBack);
        Button btDone = (Button) dialog.findViewById(R.id.btDone);
        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(front_gallery_file).into(imgFront);
                            fronIvGovt = front_gallery_file.toString();
                            fileGovtFront = new File(fronIvGovt);
                            Log.e("govtFront", "govtFront: " + fronIvGovt);


                        });

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap2 = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            back_gallery_file = ImageUtils.bitmapToFile(bitmap2, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(back_gallery_file).into(imgBack);
                            backIvGovt = back_gallery_file.toString();
                            fileGovtBack = new File(backIvGovt);
                            Log.e("govtBack", "govtBack: " + fileGovtBack);
                        });

            }
        });
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void upload_DrivingImage() {

        final Dialog dialog = new Dialog(UploadDocumnetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.upload_image_layout);
        ImageView imgFront = (ImageView) dialog.findViewById(R.id.imgFront);
        ImageView imgBack = (ImageView) dialog.findViewById(R.id.imgBack);
        Button btDone = (Button) dialog.findViewById(R.id.btDone);
        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(front_gallery_file).into(imgFront);
                            frontIvDriving = front_gallery_file.toString();
                            fileDrivingFront = new File(frontIvDriving);
                            Log.e("drivingFront", "drivingFront: " + fileDrivingFront);


                        });

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap2 = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            back_gallery_file = ImageUtils.bitmapToFile(bitmap2, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(back_gallery_file).into(imgBack);
                            backIvDriving = back_gallery_file.toString();
                            fileDrivingBack = new File(backIvDriving);
                            Log.e("drivingBack", "drivingBack: " + fileDrivingBack);
                        });

            }
        });
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private void upload_CommercialImage() {

        final Dialog dialog = new Dialog(UploadDocumnetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.upload_image_layout);
        ImageView imgFront = (ImageView) dialog.findViewById(R.id.imgFront);
        ImageView imgBack = (ImageView) dialog.findViewById(R.id.imgBack);
        Button btDone = (Button) dialog.findViewById(R.id.btDone);
        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(front_gallery_file).into(imgFront);
                            frontIvCommerical = front_gallery_file.toString();
                            fileCommericalFront = new File(frontIvCommerical);
                            Log.e("CommericalFront", "CommericalFront: " + fileCommericalFront);


                        });

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap2 = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            back_gallery_file = ImageUtils.bitmapToFile(bitmap2, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(back_gallery_file).into(imgBack);
                            backIvCommerical = back_gallery_file.toString();
                            fileCommericalBack = new File(backIvCommerical);
                            Log.e("CommericalBack", "CommericalBack: " + fileCommericalBack);
                        });

            }
        });
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private void upload_RCImage() {

        final Dialog dialog = new Dialog(UploadDocumnetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.upload_image_layout);
        ImageView imgFront = (ImageView) dialog.findViewById(R.id.imgFront);
        ImageView imgBack = (ImageView) dialog.findViewById(R.id.imgBack);
        Button btDone = (Button) dialog.findViewById(R.id.btDone);
        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(front_gallery_file).into(imgFront);
                            frontIvRc = front_gallery_file.toString();
                            fileRCFront = new File(frontIvRc);
                            Log.e("RcFront", "RcFront: " + fileRCFront);


                        });

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(UploadDocumnetActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap2 = ImageUtils.imageCompress(ImageUtils.getRealPath(UploadDocumnetActivity.this, filepath));
                            back_gallery_file = ImageUtils.bitmapToFile(bitmap2, UploadDocumnetActivity.this);
                            Glide.with(UploadDocumnetActivity.this).load(back_gallery_file).into(imgBack);
                            backIvRc = back_gallery_file.toString();
                            fileRCBack = new File(backIvRc);
                            Log.e("RcBack", "RcBack: " + fileRCFront);
                        });

            }
        });
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void upload_Document(){
        String UserID = SharedHelper.getKey(getApplicationContext(), Appconstant.UserID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.upload(Api.BASE_URL + Api.add_document)
                .addMultipartParameter("driver_id", UserID)
                .addMultipartParameter("name", stName)
                .addMultipartParameter("tax_number", stTax)
                .addMultipartFile("government_id", fileGovtFront)
                .addMultipartFile("government_id_back",fileGovtBack )
                .addMultipartFile("driving_license", fileDrivingFront)
                .addMultipartFile("driving_license_back", fileDrivingBack)
                .addMultipartFile("commerciel_insurance", fileCommericalFront)
                .addMultipartFile("commerciel_insurance_back", fileCommericalBack)
                .addMultipartFile("RC", fileRCFront)
                .addMultipartFile("RC_back",fileRCBack)
                .addMultipartFile("image",f)
                .setTag("text")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dfcvsdfh", response.toString());
                        dialog.hideDialog();
                        try {

                            String strResult = response.getString("result");
                            if (strResult.equals("successfully")) {


                                startActivity(new Intent(UploadDocumnetActivity.this,AddBankDetailActivity.class));
                                finish();
                            } else {

                                Toast.makeText(UploadDocumnetActivity.this, response.getString("result"), Toast.LENGTH_LONG).show();
                                dialog.hideDialog();
                            }


                        } catch (JSONException e) {
                            Log.e("ghsh", e.getMessage());
                            dialog.hideDialog();

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dshsjkdh", anError.getMessage());
                        dialog.hideDialog();


                    }
                });


    }

}
