package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    boolean hasWhippedCream = false;
    boolean hasChocolate = false;
    String name = "";
    int price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText editText = (EditText) findViewById(R.id.name);
        name = editText.getText().toString();

        displayMessage(createOrderSummaryV2());

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("geo:47.6,-122.3"));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }

        makeEmailIntent();

    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view){
        if((quantity + 1) > 100){
            display(100);
            makeToast("Holy smokes, ya wanna caffeine overdose?!");
        }
        else {
            quantity++;
            display(quantity);
        }
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view){
        if ((quantity - 1) < 0) {
            display(0);
            makeToast("Ya gotta order some coffee ya know!");
        }
        else {
            quantity--;
            display(quantity);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     *
     * @param number Quantity to be displayed
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     *
     * @param message Text to be displayed
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * Method which calculates the price of the order
     *
     * @return The price of the order
     */
    private int calculatePrice(){
        int basePrice = 5;

        if(hasWhippedCream)
            basePrice += 1;
        if(hasChocolate)
            basePrice += 2;

        price = basePrice*quantity;

        return price;
    }

    /**
     * Method which checks if the whipped cream checkbox was initialized
     *
     * @param view
     */

    public void toppingsCheck(View view){
//        Log.i("MainActivity.java", "Value of hasWhippedCream before click:" + hasWhippedCream);
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckbox = (CheckBox) findViewById(R.id.chocolate_checkbox);

        //Checking for the whipped cream checkbox
        if(whippedCreamCheckbox.isChecked())
            hasWhippedCream = true;
        else
            hasWhippedCream = false;

        //Checking for the chocolate checkbox
        if(chocolateCheckbox.isChecked())
            hasChocolate = true;
        else
            hasChocolate = false;

//        Log.i("MainActivity.java", "Value of hasWhippedCream after click:" + hasWhippedCream);
    }

    /**
     * Generates the summary text of the order
     *
     * @return Summary text of the order
     */
    private String createOrderSummary(){
        String printMessage = "Name: " + name;

        //Checking if whipped cream was ordered
        if(hasWhippedCream)
            printMessage += "\nWhipped cream? Yes";
        else{
            printMessage += "\nWhipped cream? No";
        }

        //Checking if chocolate was ordered
        if(hasChocolate)
            printMessage += "\nChocolate? Yes";
        else{
            printMessage += "\nChocolate? No";
        }

        printMessage += "\nQuantity: " + quantity;
        printMessage += "\nPrice: $" + calculatePrice();
        printMessage += "\nThank you!";
        return printMessage;
    }

    /**
     * Generates the summary text of the order; a small variation of the first rendition of this
     * code, where String values from the strings.xml file were used instead
     *
     * @return Text which displays a summary of the customer's order
     */
    private String createOrderSummaryV2(){
        String printMessage = getResources().getString(R.string.order_summary_name, name);

        printMessage += "\n" + getResources().getString(R.string.order_summary_whipped_cream, hasWhippedCream);
        printMessage += "\n" + getResources().getString(R.string.order_summary_chocolate, hasChocolate);
        printMessage += "\n" + getResources().getString(R.string.order_summary_quantity, quantity);

        calculatePrice();

        printMessage += "\n" + getResources().getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price));
        printMessage += "\n" + getResources().getString(R.string.thank_you);
        return printMessage;
    }

    private void makeToast (CharSequence message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }

    private void makeEmailIntent() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummaryV2());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}