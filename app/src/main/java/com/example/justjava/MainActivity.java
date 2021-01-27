package com.example.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    @SuppressLint("StringFormatInvalid")
    public void submitOrder(View view) {
        CheckBox WhippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = WhippedCreamCheckBox.isChecked();
        CheckBox ChocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = ChocolateCheckBox.isChecked();
        EditText nameText = (EditText) findViewById(R.id.name_enter);
        String name = nameText.getText().toString();
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the.
     */
    public void displayQuantity(int displayQuantity) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + displayQuantity);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (quantity >= 100)
        {
            Toast.makeText(this, getString(R.string.increment_message), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, getString(R.string.decrement_message), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * Calculates the price of the order.
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int price;
        if (hasWhippedCream == true && hasChocolate == true) {
            price = quantity * (5+1+2);
        } else if (hasWhippedCream == true && hasChocolate != true) {
            price = quantity * (5+1);
        } else if (hasWhippedCream != true && hasChocolate == true) {
            price = quantity * (5+2);
        } else {
            price = quantity * (5);
        }
        return price;
    }

    /**
     * Takes price of order and returns name, quantity and price.
     * @return order
     * @return price of the order
     * @param hasWhippedCream is whipper cream wanted or not
     * @param hasChocolate is chocolate wanted or not
     */
    @SuppressLint("StringFormatInvalid")
    public String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String name) {
        String order = getString(R.string.order_summary_name, name);
        order += "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream);
        order += "\n" + getString(R.string.order_summary_chocolate, hasChocolate);
        order += "\n" + getString(R.string.order_summary_quantity, quantity);
        order += "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price));
        order += "\n" + getString(R.string.thank_you);
        return order;
    }
}