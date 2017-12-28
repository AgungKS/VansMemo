package com.lycan.vansstore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtPrice;
    Button btnChoose, btnAdd, btnList;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY=999;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    //public  static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("shoes");
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference("shoes");
        //Membuat Database Shoes
        //sqLiteHelper=new SQLiteHelper(this, "ShoesDB.sqlite",null, 1);

        //sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS SHOES (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, image BLOG)");

        //Click Listener untuk Button Choose dan Get Image dari Gallery kedalam ImageView
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        //Click Listener untuk Button Add dan untuk Insert Data kedalam Database
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertToFirebase();
                /*try {
                    sqLiteHelper.insertData(
                            edtName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                }
                catch (Exception e){
                    e.printStackTrace();
                }*/
            }
        });

        //Click Listener untuk Button Shoes List
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ShoesList.class);
                startActivity(intent);
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap=((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }

    //Override Untuk Request Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_GALLERY){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(getApplicationContext(),"Anda Tidak Punya Ijin Untuk Mengakses Lokasi File",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data !=null){
            Uri uri=data.getData();

            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        edtName=(EditText)findViewById(R.id.edtName);
        edtPrice=(EditText)findViewById(R.id.edtPrice);
        btnChoose=(Button)findViewById(R.id.btnChoose);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnList=(Button)findViewById(R.id.btnList);
        imageView=(ImageView)findViewById(R.id.imageView);
    }

    private void insertToFirebase(){
        final AlertDialog dialog = new AlertDialog.Builder(this).setMessage("MEMPROSES . . .").setCancelable(false).create();
        dialog.setCanceledOnTouchOutside(false);
        final String key = mReference.push().getKey();
        dialog.show();
        mStorageReference.child(key).putBytes(imageViewToByte(imageView)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Shoes data = new Shoes(key, edtName.getText().toString().trim(),edtPrice.getText().toString().trim(),
                        taskSnapshot.getDownloadUrl().toString());
                mReference.child(key).setValue(data, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(getApplicationContext(), "Telah Ditambahkan", Toast.LENGTH_SHORT).show();
                        edtName.setText("");
                        edtPrice.setText("");
                        imageView.setImageResource(R.mipmap.ic_launcher);
                        dialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal Menammbahkan", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }
}
