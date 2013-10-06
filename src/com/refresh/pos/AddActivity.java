package com.refresh.pos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

@SuppressLint("NewApi")
public class AddActivity extends Activity {
	
	private EditText itemBarcode;
	private ProductCatalogController productCatalogController;
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		Log.d("BARCODE", "BARCODE 'onActivityResult' Successfully.");
		
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
//
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
//			Toast.makeText(AddActivity.this,"got >> " + scanContent , Toast.LENGTH_SHORT).show();
			itemBarcode.setText(scanContent);
//
		} else {
			Toast.makeText(AddActivity.this,
					"Failed to retrieve barcode." + resultCode, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		super.onCreate(savedInstanceState);
		
		ProductDao productDao = new ProductDaoAndroid(this);
		productCatalogController = new ProductCatalogController(productDao);
		
		final EditText itemName = (EditText) findViewById(R.id.nameTxt);
		itemBarcode = (EditText) findViewById(R.id.barcodeTxt);
		final EditText itemCost = (EditText) findViewById(R.id.costTxt);
		final EditText itemPrice = (EditText) findViewById(R.id.priceTxt);
		final EditText itemAmount = (EditText) findViewById(R.id.amountTxt);
		final EditText itemDetail = (EditText) findViewById(R.id.detailTxt);
		
		final Button scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentIntegrator scanIntegrator = new IntentIntegrator(AddActivity.this);
				scanIntegrator.initiateScan();
				
				
			}
		});
		
		final Button addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean chitemName =itemName.getText().toString().isEmpty(); 
				if(chitemName||itemBarcode.getText().toString().isEmpty()||itemPrice.getText().toString().isEmpty()){
					Toast.makeText(AddActivity.this, "It's still have some blank",
							Toast.LENGTH_SHORT).show();
				}
				else{
					boolean success = productCatalogController.add(itemName.getText().toString(),itemBarcode.getText().toString(),Double.parseDouble(itemPrice.getText().toString()));
					if(success){
						Toast.makeText(AddActivity.this,
								"Successfully Add : "+itemName.getText().toString(), Toast.LENGTH_SHORT)
								.show();
					}
					else{
						Toast.makeText(AddActivity.this, "Failed to insert data",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		final Button clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				itemName.setText("");
				itemBarcode.setText("");
				itemCost.setText("");
				itemPrice.setText("");
				itemAmount.setText("");
				itemDetail.setText("");
				
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
