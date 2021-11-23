package com.xploreict.blooddonation.ui.MyAccount;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.xploreict.blooddonation.R;
import com.xploreict.blooddonation.apicontroller;
import com.xploreict.blooddonation.login_response_model;
import com.xploreict.blooddonation.myprofile_response_model;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountFragment extends Fragment {

    private ImageView profile_img,txt_edit_box;
    private TextView name,mobile,city,bloodgroup,gender,age;
    private String email,id;
    SharedPreferences sp;
    private Context context;
    private ImageView profile_edit;
    private Button uploadbtn;
    Bitmap bitmap;
    String encodeImageString;
    List<myprofile_response_model> data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_fragment, container, false);
        context = view.getContext();
        //getting view by id
        name = view.findViewById(R.id.profile_name);
        profile_edit = view.findViewById(R.id.profile_img_edit);
        mobile = view.findViewById(R.id.profile_mobile);
        uploadbtn = view.findViewById(R.id.profile_upload_btn);
        city = view.findViewById(R.id.profile_city);
        bloodgroup = view.findViewById(R.id.profile_bloodgroup);
        gender = view.findViewById(R.id.profile_gender);
        age = view.findViewById(R.id.profile_age);
        profile_img = view.findViewById(R.id.profile_image);
        txt_edit_box = view.findViewById(R.id.txt_edit_box);
        sp = getActivity().getSharedPreferences("credentials", MODE_PRIVATE);
        email = sp.getString("username","");
        fetchprofile(email);

        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(context)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                                mGetContent.launch("image/*");
                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        txt_edit_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.profile_update_dialog);
                EditText dialog_name, dialog_mobile, dialog_city, dialog_gender, dialog_age;
                Button dialog_upload, dialog_cancel;

                dialog_name = dialog.findViewById(R.id.dialog_name);
                dialog_mobile = dialog.findViewById(R.id.dialog_mobile);
                dialog_city = dialog.findViewById(R.id.dialog_city);
                dialog_gender = dialog.findViewById(R.id.dialog_gender);
                dialog_age = dialog.findViewById(R.id.dialog_age);
                dialog_upload = dialog.findViewById(R.id.upload_btn);
                dialog_cancel = dialog.findViewById(R.id.cancel_btn);

                dialog_name.setText(data.get(0).getName());
                dialog_mobile.setText(data.get(0).getMobile());
                dialog_city.setText(data.get(0).getCity());
                dialog_gender.setText(data.get(0).getGender());
                dialog_age.setText(data.get(0).getAge());

                dialog_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateinfo(dialog_name.getText().toString(),dialog_mobile.getText().toString(),dialog_city.getText().toString(),dialog_gender.getText().toString(),dialog_age.getText().toString());
                        dialog.cancel();
                    }
                });

                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.create();
                dialog.show();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaddatatodb();
            }
        });
        return view;
    }

    private void updateinfo(String name, String mobile, String city, String gender, String age) {
        Call<login_response_model> call = apicontroller.getInstance()
                .getapi()
                .setupdate(id,name,mobile,city,gender,age);
        call.enqueue(new Callback<login_response_model>() {
            @Override
            public void onResponse(Call<login_response_model> call, Response<login_response_model> response) {
                login_response_model obj = response.body();
                String result = obj.getMessage();
                if (result.equals("success")){
                    Toast.makeText(context, "Profile Update Success!", Toast.LENGTH_SHORT).show();
                    fetchprofile(email);
                }if (result.equals("failed")){
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<login_response_model> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    try {
                        if (uri!=null) {
                            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            profile_img.setImageBitmap(bitmap);
                            encodeBitmapImage(bitmap);
                            uploadbtn.setVisibility(View.VISIBLE);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private void uploaddatatodb() {
        Call<login_response_model> call = apicontroller.getInstance()
                .getapi()
                .getupload(encodeImageString,email,id);
        call.enqueue(new Callback<login_response_model>() {
            @Override
            public void onResponse(Call<login_response_model> call, Response<login_response_model> response) {
                login_response_model obj = response.body();
                String result = obj.getMessage();
                if (result.equals("success")){
                    uploadbtn.setVisibility(View.GONE);
                    Toast.makeText(context, "Images Upload Success!", Toast.LENGTH_SHORT).show();
                    fetchprofile(email);
                    processdata(email);
                }if (result.equals("failed")){
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<login_response_model> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchprofile(String email) {
        Call<List<myprofile_response_model>> call = apicontroller.getInstance()
                .getapi()
                .getprofile(email);
        call.enqueue(new Callback<List<myprofile_response_model>>() {
            @Override
            public void onResponse(Call<List<myprofile_response_model>> call, Response<List<myprofile_response_model>> response) {
                data = response.body();
                name.setText(data.get(0).getName());
                id = data.get(0).getId().trim();
                mobile.setText("Mobile : "+data.get(0).getMobile());
                city.setText("City : "+data.get(0).getCity());
                bloodgroup.setText("Blood Group : "+data.get(0).getBloodgroup());
                gender.setText("Gender : "+data.get(0).getGender());
                age.setText("Age : "+data.get(0).getAge());
                String url = data.get(0).getImageurl();
                if (!url.equals("not applicable")){
                    Glide.with(context).load("https://blooddonation.gadgetlab.store/images/"+url).into(profile_img);
                }
            }

            @Override
            public void onFailure(Call<List<myprofile_response_model>> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processdata(String email) {

        Call<List<myprofile_response_model>> call = apicontroller.getInstance()
                .getapi()
                .getprofile(email);
        call.enqueue(new Callback<List<myprofile_response_model>>() {
            @Override
            public void onResponse(Call<List<myprofile_response_model>> call, Response<List<myprofile_response_model>> response) {
                List<myprofile_response_model> data = response.body();
                name.setText(data.get(0).getName());
                String url = data.get(0).getImageurl();
                if (!url.equals("not applicable")){
                    Glide.with(context).load("https://blooddonation.gadgetlab.store/images/"+url).into(profile_img);
                }
            }

            @Override
            public void onFailure(Call<List<myprofile_response_model>> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}