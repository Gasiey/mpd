
/*  Starter project for Mobile Platform Development - 1st diet 25/26
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Abdulrahman Gasiey
// Student ID           S2254991
// Programme of Study   Software Development
//

// UPDATE THE PACKAGE NAME to include your Student Identifier
package org.me.gcu.gasiey_abady_s2254991;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView rawDataDisplay;
    private Button startButton;
    private String urlSource = "https://www.fx-exchange.com/gbp/rss.xml";

    private ArrayList<CurrencyItem> alist = new ArrayList<>();
    private CurrencyAdapter adapter;
    private ListView currencyListView;

    private TextView usdRate, eurRate, jpyRate;
    private CurrencyItem usdItem, eurItem, jpyItem;
    private SearchView searchView;

    // Auto update every few mins
    private Handler autoHandler = new Handler();
    private final int AUTO_REFRESH_MS = 60 * 1000; // 1 minute for demo

    // threading
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private String lastGoodXML = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // UI links
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        currencyListView = findViewById(R.id.currencyListView);
        usdRate  = findViewById(R.id.usdRate);
        eurRate  = findViewById(R.id.eurRate);
        jpyRate  = findViewById(R.id.jpyRate);

        rawDataDisplay = findViewById(R.id.rawDataDisplay);
        searchView     = findViewById(R.id.searchView);


        // Restore state after rotation
        if (savedInstanceState != null) {

            ArrayList<CurrencyItem> restored =
                    (ArrayList<CurrencyItem>) savedInstanceState.getSerializable("currencyList");

            if (restored != null) {
                alist.clear();
                alist.addAll(restored);
                adapter = new CurrencyAdapter(this, alist);
                currencyListView.setAdapter(adapter);
            }

            String q = savedInstanceState.getString("searchQuery", "");
            searchView.setQuery(q, false);
            if (adapter != null) adapter.updateList(q);

            usdItem = (CurrencyItem) savedInstanceState.getSerializable("usdItem");
            eurItem = (CurrencyItem) savedInstanceState.getSerializable("eurItem");
            jpyItem = (CurrencyItem) savedInstanceState.getSerializable("jpyItem");

            if (usdItem != null) usdRate.setText("USD: " + usdItem.getDescription());
            if (eurItem != null) eurRate.setText("EUR: " + eurItem.getDescription());
            if (jpyItem != null) jpyRate.setText("JPY: " + jpyItem.getDescription());

            rawDataDisplay.setText(savedInstanceState.getString("infoText", ""));
        }

        // Auto update when app opens schedule future updates
        startProgress();
        startAutoRefresh();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("currencyList", alist);
        outState.putString("searchQuery", searchView.getQuery().toString());

        outState.putSerializable("usdItem", usdItem);
        outState.putSerializable("eurItem", eurItem);
        outState.putSerializable("jpyItem", jpyItem);

        outState.putString("infoText", rawDataDisplay.getText().toString());
    }


    @Override
    public void onClick(View v) {
        startProgress();
    }

    private void showResult(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Conversion Result")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }


    // ---------------- AUTO UPDATE ----------------
    private void startAutoRefresh() {
        autoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startProgress();
                autoHandler.postDelayed(this, AUTO_REFRESH_MS);
            }
        }, AUTO_REFRESH_MS);
    }


    // ---------------- THREADING  ----------------
    public void startProgress() {

        // Run network + parsing in background
        executor.execute(() -> {

            String dirty = fetchXML(urlSource);
            String xml = cleanXML(dirty);

// If offline or fetch failed, use last good XML
            if (xml == null) {
                if (lastGoodXML != null) {
                    xml = lastGoodXML;  // FALLBACK
                    Log.d("Offline", "Using cached XML (offline mode)");
                } else {
                    // No cached data show message and stop
                    mainHandler.post(() ->
                            Toast.makeText(MainActivity.this,
                                    "No Internet & no cached data available.",
                                    Toast.LENGTH_LONG).show()
                    );
                    return; // nothing more we can do
                }
            }

            ArrayList<CurrencyItem> parsedList = parseXMLSafe(xml);
            mainHandler.post(() -> updateUIfromThread(parsedList));

        });
    }


    private String cleanXML(String dirty) {

        if (dirty == null || dirty.isEmpty()) return "";

        int start = dirty.indexOf("<rss");
        int end   = dirty.lastIndexOf("</rss>");

        if (start == -1 || end == -1) {
            return "";
        }

        // Extract exact XML
        String xml = dirty.substring(start, end + 6);

        // Fix invalid '&' signs not used
        xml = xml.replaceAll("&(?!amp;|lt;|gt;|quot;|apos;)", "&amp;");

        return xml;
    }

    private String fetchXML(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection yc = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            lastGoodXML = sb.toString();
            return sb.toString();

        } catch (Exception e) {
            Log.e("XML", "Fetch error: " + e);
            return null;
        }
    }

    private ArrayList<CurrencyItem> parseXMLReturnList(String xml) {
        ArrayList<CurrencyItem> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));

            CurrencyItem item = null;
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    String tag = xpp.getName().toLowerCase();

                    if (tag.equals("item")) {
                        item = new CurrencyItem();
                    } else if (item != null) {

                        switch (tag) {

                            case "title":
                                String title = xpp.nextText();
                                item.setTitle(title);

                                try {
                                    String[] parts = title.split("/");
                                    String left = parts[0];
                                    String right = parts[1];

                                    item.setBaseName(left.substring(0, left.lastIndexOf("(")).trim());
                                    item.setBaseCode(left.substring(left.lastIndexOf("(") + 1, left.lastIndexOf(")")));

                                    item.setForeignName(right.substring(0, right.lastIndexOf("(")).trim());
                                    item.setForeignCode(right.substring(right.lastIndexOf("(") + 1, right.lastIndexOf(")")));
                                } catch (Exception ignore) {}

                                break;

                            case "description":
                                item.setDescription(xpp.nextText());
                                break;

                            case "link":
                                item.setLink(xpp.nextText());
                                break;

                            case "pubdate":
                                item.setPubDate(xpp.nextText());
                                break;
                        }
                    }
                }

                else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item") && item != null) {
                        list.add(item);
                        item = null;
                    }
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            Log.e("XML", "Parse error: " + e);
        }

        return list;
    }
    // parse XML into a list of CurrencyItem
    private ArrayList<CurrencyItem> parseXMLSafe(String xml) {

        ArrayList<CurrencyItem> parsedList = new ArrayList<>();

        if (xml == null || xml.isEmpty()) {
            return parsedList; // return empty list if no XML
        }

        try {

            parsedList = parseXMLReturnList(xml);

        } catch (Exception e) {
            Log.e("XML", "Parse error: " + e);
        }

        return parsedList;
    }





    private void updateUIfromThread(ArrayList<CurrencyItem> list) {

        alist.clear();
        alist.addAll(list);

        updateUI();
    }


    // ---------------- UI ----------------
    private void updateUI() {

        // Highlights
        for (CurrencyItem c : alist) {
            if (c.getTitle().contains("USD")) {
                usdItem = c;
                usdRate.setText("USD: " + c.getDescription());
            }
            if (c.getTitle().contains("EUR")) {
                eurItem = c;
                eurRate.setText("EUR: " + c.getDescription());
            }
            if (c.getTitle().contains("JPY")) {
                jpyItem = c;
                jpyRate.setText("JPY: " + c.getDescription());
            }
        }

        usdRate.setOnClickListener(v -> rawDataDisplay.setText(formatItem(usdItem)));
        eurRate.setOnClickListener(v -> rawDataDisplay.setText(formatItem(eurItem)));
        jpyRate.setOnClickListener(v -> rawDataDisplay.setText(formatItem(jpyItem)));

        adapter = new CurrencyAdapter(this, alist);
        currencyListView.setAdapter(adapter);

        currencyListView.setOnItemClickListener((p, v, pos, id) ->
                openInfoDialog(adapter.getItem(pos)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                adapter.updateList(q);
                return true;
            }
        });
    }


    // ---------------- INFO POPUP ----------------
    private void openInfoDialog(CurrencyItem clicked) {

        AlertDialog.Builder info = new AlertDialog.Builder(this);
        info.setTitle(clicked.getTitle());

        String details =
                "Currency: " + clicked.getTitle() + "\n\n" +
                        "Rate:\n" + clicked.getDescription() + "\n\n" +
                        "Published:\n" + clicked.getPubDate() + "\n\n" +
                        "More info:\n" + clicked.getLink();

        info.setMessage(details);
        info.setPositiveButton("Convert", (d, w) -> openConverter(clicked));
        info.setNegativeButton("Close", null);
        info.show();
    }


    // ---------------- CONVERTER ----------------
    private void openConverter(CurrencyItem clicked) {

        double rate;
        try {
            String afterEq = clicked.getDescription().split("=")[1].trim();
            rate = Double.parseDouble(afterEq.split(" ")[0].trim());
        } catch (Exception e) {
            rate = 0;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Currency Converter");

        View layout = getLayoutInflater().inflate(R.layout.converter_dialog, null);
        EditText amountInput = layout.findViewById(R.id.amountInput);
        RadioGroup directionGroup = layout.findViewById(R.id.directionGroup);
        dialog.setView(layout);

        double finalRate = rate;

        dialog.setPositiveButton("Convert", (d, w) -> {

            String txt = amountInput.getText().toString().trim();

            if (txt.isEmpty()) {
                Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!txt.matches("\\d*\\.?\\d+")) {
                Toast.makeText(this, "Numbers only", Toast.LENGTH_SHORT).show();
                return;
            }

            double input = Double.parseDouble(txt);
            if (input <= 0) {
                Toast.makeText(this, "Enter a number > 0", Toast.LENGTH_SHORT).show();
                return;
            }

            int choice = directionGroup.getCheckedRadioButtonId();
            double result;

            if (choice == R.id.gbpToForeign) {

                result = input * finalRate;

                showResult(String.format(
                        "%.2f GBP → %.2f %s (%s)",
                        input, result,
                        clicked.getForeignCode(),
                        clicked.getForeignName()
                ));

            } else {

                result = input / finalRate;

                showResult(String.format(
                        "%.2f %s (%s) → %.2f GBP",
                        input,
                        clicked.getForeignCode(),
                        clicked.getForeignName(),
                        result
                ));
            }
        });

        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }


    private String formatItem(CurrencyItem item) {
        if (item == null) return "";
        return "Currency: " + item.getTitle() + "\n\nRate:\n" + item.getDescription() +
                "\n\nPublished:\n" + item.getPubDate() +
                "\n\nMore info:\n" + item.getLink();
    }


    @Override
    protected void onPause() {
        super.onPause();
        autoHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoRefresh();
    }
}


