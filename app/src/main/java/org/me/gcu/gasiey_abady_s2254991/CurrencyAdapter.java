package org.me.gcu.gasiey_abady_s2254991;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<CurrencyItem> {

    private final Context context;
    // This is the live list used by the ListView
    private final ArrayList<CurrencyItem> items;
    // Backup of the full original list for restoring after search
    private final ArrayList<CurrencyItem> fullList;

    public CurrencyAdapter(Context context, ArrayList<CurrencyItem> items) {

        super(context, 0, items);
        this.context = context;
        this.items = items;                      // live list
        this.fullList = new ArrayList<>(items);  // backup
    }

    // Safely lowercase a string
    private String safeLower(String s) {
        return (s == null) ? "" : s.toLowerCase();
    }

    // Extract 3-letter currency code from the title:
    private String extractCurrencyCode(String title) {
        if (title == null) return null;

        int open = title.lastIndexOf("(");
        int close = title.lastIndexOf(")");

        if (open == -1 || close == -1 || close <= open + 1) {
            return null;
        }

        return title.substring(open + 1, close).trim().toUpperCase();
    }

    // Map 3-letter currency code to ISO 2-letter (or special) flag filename
    private String currencyToIso(String code) {

        if (code == null) return null;
        code = code.toUpperCase();

        switch (code) {

            case "EUR": return "eu";
            case "XOF":
            case "XAF": return "cf";
            case "ANG": return "cw";
            case "BTC": return null;   // no flag

            case "AED": return "ae";
            case "AFN": return "af";
            case "ALL": return "al";
            case "AMD": return "am";
            case "AOA": return "ao";
            case "ARS": return "ar";
            case "AUD": return "au";
            case "AWG": return "aw";
            case "AZN": return "az";
            case "BAM": return "ba";
            case "BBD": return "bb";
            case "BDT": return "bd";
            case "BGN": return "bg";
            case "BHD": return "bh";
            case "BIF": return "bi";
            case "BMD": return "bm";
            case "BND": return "bn";
            case "BOB": return "bo";
            case "BRL": return "br";
            case "BSD": return "bs";
            case "BTN": return "bt";
            case "BWP": return "bw";
            case "BYR": return "by";
            case "BYN": return "by";
            case "BZD": return "bz";
            case "CAD": return "ca";
            case "CDF": return "cd";
            case "CHF": return "ch";
            case "CLP": return "cl";
            case "CNY": return "cn";
            case "COP": return "co";
            case "CRC": return "cr";
            case "CUP": return "cu";
            case "CVE": return "cv";
            case "CZK": return "cz";
            case "DJF": return "dj";
            case "DKK": return "dk";
            case "DOP": return "do_flag";
            case "DZD": return "dz";
            case "EEK": return "ee";
            case "EGP": return "eg";
            case "ERN": return "er";
            case "ETB": return "et";
            case "FJD": return "fj";
            case "FKP": return "fk";
            case "GEL": return "ge";
            case "GGP": return "gg";
            case "GHS": return "gh";
            case "GIP": return "gi";
            case "GMD": return "gm";
            case "GNF": return "gn";
            case "GTQ": return "gt";
            case "GYD": return "gy";
            case "HKD": return "hk";
            case "HNL": return "hn";
            case "HRK": return "hr";
            case "HTG": return "ht";
            case "HUF": return "hu";
            case "IDR": return "id_flag";
            case "ILS": return "il";
            case "IMP": return "im";
            case "INR": return "in_flag";
            case "IQD": return "iq";
            case "IRR": return "ir";
            case "ISK": return "is";
            case "JMD": return "jm";
            case "JOD": return "jo";
            case "JPY": return "jp";
            case "KES": return "ke";
            case "KGS": return "kg";
            case "KHR": return "kh";
            case "KMF": return "km";
            case "KPW": return "kp";
            case "KRW": return "kr";
            case "KWD": return "kw";
            case "KYD": return "ky";
            case "KZT": return "kz";
            case "LAK": return "la";
            case "LBP": return "lb";
            case "LKR": return "lk";
            case "LRD": return "lr";
            case "LSL": return "ls";
            case "LTL": return "lt";
            case "LVL": return "lv";
            case "LYD": return "ly";
            case "MAD": return "ma";
            case "MDL": return "md";
            case "MGA": return "mg";
            case "MKD": return "mk";
            case "MMK": return "mm";
            case "MNT": return "mn";
            case "MOP": return "mo";
            case "MRO": return "mr";
            case "MUR": return "mu";
            case "MVR": return "mv";
            case "MWK": return "mw";
            case "MXN": return "mx";
            case "MYR": return "my";
            case "MZN": return "mz";
            case "NAD": return "na";
            case "NGN": return "ng";
            case "NIO": return "ni";
            case "NOK": return "no";
            case "NPR": return "np";
            case "NZD": return "nz";
            case "OMR": return "om";
            case "PAB": return "pa";
            case "PEN": return "pe";
            case "PGK": return "pg";
            case "PHP": return "ph";
            case "PKR": return "pk";
            case "PLN": return "pl";
            case "PYG": return "py";
            case "QAR": return "qa";
            case "RON": return "ro";
            case "RSD": return "rs";
            case "RUB": return "ru";
            case "RWF": return "rw";
            case "SAR": return "sa";
            case "SBD": return "sb";
            case "SCR": return "sc";
            case "SDG": return "sd";
            case "SEK": return "se";
            case "SGD": return "sg";
            case "SHP": return "sh";
            case "SKK": return "sk";
            case "SLL": return "sl";
            case "SOS": return "so";
            case "SRD": return "sr";
            case "SSP": return "ss";
            case "STD": return "st";
            case "SVC": return "sv";
            case "SYP": return "sy";
            case "SZL": return "sz";
            case "THB": return "th";
            case "TJS": return "tj";
            case "TMT": return "tm";
            case "TND": return "tn";
            case "TOP": return "to";
            case "TRY": return "tr";
            case "TTD": return "tt";
            case "TWD": return "tw";
            case "TZS": return "tz";
            case "UAH": return "ua";
            case "UGX": return "ug";
            case "USD": return "us";
            case "UYU": return "uy";
            case "UZS": return "uz";
            case "VEF": return "ve";
            case "VND": return "vn";
            case "VUV": return "vu";
            case "WST": return "ws";
            case "XCD": return "bq";
            case "XPF": return "pf";
            case "YER": return "ye";
            case "ZAR": return "za";
            case "ZMK": return "zm";
            case "ZMW": return "zm";
            case "ZWD": return "zw";
        }

        return null;
    }

    // Map currency code to full country name for searching
    private String currencyToCountry(String code) {

        if (code == null) return "";

        switch (code.toUpperCase()) {

            case "EUR": return "European Union";
            case "XOF":
            case "XAF": return "Central Africa";
            case "ANG": return "Curaçao";
            case "BTC": return "";

            case "AED": return "United Arab Emirates";
            case "AFN": return "Afghanistan";
            case "ALL": return "Albania";
            case "AMD": return "Armenia";
            case "AOA": return "Angola";
            case "ARS": return "Argentina";
            case "AUD": return "Australia";
            case "AWG": return "Aruba";
            case "AZN": return "Azerbaijan";
            case "BAM": return "Bosnia";
            case "BBD": return "Barbados";
            case "BDT": return "Bangladesh";
            case "BGN": return "Bulgaria";
            case "BHD": return "Bahrain";
            case "BIF": return "Burundi";
            case "BMD": return "Bermuda";
            case "BND": return "Brunei";
            case "BOB": return "Bolivia";
            case "BRL": return "Brazil";
            case "BSD": return "Bahamas";
            case "BTN": return "Bhutan";
            case "BWP": return "Botswana";
            case "BYN":
            case "BYR": return "Belarus";
            case "BZD": return "Belize";
            case "CAD": return "Canada";
            case "CDF": return "Congo";
            case "CHF": return "Switzerland";
            case "CLP": return "Chile";
            case "CNY": return "China";
            case "COP": return "Colombia";
            case "CRC": return "Costa Rica";
            case "CUP": return "Cuba";
            case "CVE": return "Cape Verde";
            case "CZK": return "Czech Republic";
            case "DJF": return "Djibouti";
            case "DKK": return "Denmark";
            case "DOP": return "Dominican Republic";
            case "DZD": return "Algeria";
            case "EGP": return "Egypt";
            case "ERN": return "Eritrea";
            case "ETB": return "Ethiopia";
            case "FJD": return "Fiji";
            case "FKP": return "Falkland Islands";
            case "GEL": return "Georgia";
            case "GGP": return "Guernsey";
            case "GHS": return "Ghana";
            case "GIP": return "Gibraltar";
            case "GMD": return "Gambia";
            case "GNF": return "Guinea";
            case "GTQ": return "Guatemala";
            case "GYD": return "Guyana";
            case "HKD": return "Hong Kong";
            case "HNL": return "Honduras";
            case "HRK": return "Croatia";
            case "HTG": return "Haiti";
            case "HUF": return "Hungary";
            case "IDR": return "Indonesia";
            case "ILS": return "Israel";
            case "IMP": return "Isle of Man";
            case "INR": return "India";
            case "IQD": return "Iraq";
            case "IRR": return "Iran";
            case "ISK": return "Iceland";
            case "JMD": return "Jamaica";
            case "JOD": return "Jordan";
            case "JPY": return "Japan";
            case "KES": return "Kenya";
            case "KGS": return "Kyrgyzstan";
            case "KHR": return "Cambodia";
            case "KMF": return "Comoros";
            case "KPW": return "North Korea";
            case "KRW": return "South Korea";
            case "KWD": return "Kuwait";
            case "KYD": return "Cayman Islands";
            case "KZT": return "Kazakhstan";
            case "LAK": return "Laos";
            case "LBP": return "Lebanon";
            case "LKR": return "Sri Lanka";
            case "LRD": return "Liberia";
            case "LSL": return "Lesotho";
            case "LYD": return "Libya";
            case "MAD": return "Morocco";
            case "MDL": return "Moldova";
            case "MGA": return "Madagascar";
            case "MKD": return "North Macedonia";
            case "MMK": return "Myanmar";
            case "MNT": return "Mongolia";
            case "MOP": return "Macau";
            case "MRU": return "Mauritania";
            case "MUR": return "Mauritius";
            case "MVR": return "Maldives";
            case "MWK": return "Malawi";
            case "MXN": return "Mexico";
            case "MYR": return "Malaysia";
            case "MZN": return "Mozambique";
            case "NAD": return "Namibia";
            case "NGN": return "Nigeria";
            case "NIO": return "Nicaragua";
            case "NOK": return "Norway";
            case "NPR": return "Nepal";
            case "NZD": return "New Zealand";
            case "OMR": return "Oman";
            case "PAB": return "Panama";
            case "PEN": return "Peru";
            case "PGK": return "Papua New Guinea";
            case "PHP": return "Philippines";
            case "PKR": return "Pakistan";
            case "PLN": return "Poland";
            case "PYG": return "Paraguay";
            case "QAR": return "Qatar";
            case "RON": return "Romania";
            case "RSD": return "Serbia";
            case "RUB": return "Russia";
            case "RWF": return "Rwanda";
            case "SAR": return "Saudi Arabia";
            case "SBD": return "Solomon Islands";
            case "SCR": return "Seychelles";
            case "SDG": return "Sudan";
            case "SEK": return "Sweden";
            case "SGD": return "Singapore";
            case "SHP": return "Saint Helena";
            case "SLL": return "Sierra Leone";
            case "SOS": return "Somalia";
            case "SRD": return "Suriname";
            case "SSP": return "South Sudan";
            case "STD": return "São Tomé";
            case "SYP": return "Syria";
            case "SZL": return "Eswatini";
            case "THB": return "Thailand";
            case "TJS": return "Tajikistan";
            case "TMT": return "Turkmenistan";
            case "TND": return "Tunisia";
            case "TOP": return "Tonga";
            case "TRY": return "Turkey";
            case "TTD": return "Trinidad";
            case "TWD": return "Taiwan";
            case "TZS": return "Tanzania";
            case "UAH": return "Ukraine";
            case "UGX": return "Uganda";
            case "USD": return "United States";
            case "UYU": return "Uruguay";
            case "UZS": return "Uzbekistan";
            case "VES": return "Venezuela";
            case "VND": return "Vietnam";
            case "VUV": return "Vanuatu";
            case "WST": return "Samoa";
            case "YER": return "Yemen";
            case "ZAR": return "South Africa";
            case "ZMW": return "Zambia";
            case "ZWL": return "Zimbabwe";
        }

        return "";
    }


    // Extract numeric rate from description
    private double getRateValue(String description) {
        try {
            String[] parts = description.split("=");
            if (parts.length < 2) return 0;

            String afterEquals = parts[1].trim();       // "204.8287 Japanese Yen"
            String numberOnly  = afterEquals.split(" ")[0].trim();
            return Double.parseDouble(numberOnly);
        } catch (Exception e) {
            return 0;
        }
    }


    public void updateList(String query) {

        String q = (query == null ? "" : query.trim().toLowerCase());
        items.clear();

        if (q.isEmpty()) {
            items.addAll(fullList);
            notifyDataSetChanged();
            return;
        }

        // Try match country names using the mapping
        for (CurrencyItem c : fullList) {
            String code = c.getForeignCode();
            String country = currencyToCountry(code).toLowerCase();

            if (country.contains(q)) {
                // exact country match to only show this currency
                items.add(c);
                notifyDataSetChanged();
                return;
            }
        }

        // Otherwise use normal contains search
        for (CurrencyItem c : fullList) {
            if (c.getTitle().toLowerCase().contains(q) ||
                    c.getForeignName().toLowerCase().contains(q) ||
                    c.getForeignCode().toLowerCase().contains(q)) {

                items.add(c);
            }
        }

        notifyDataSetChanged();
    }



    // Make sure ListView uses the filtered list size
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CurrencyItem getItem(int position) {
        return items.get(position);
    }


    // RENDER EACH ROW IN THE LIST

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.currency_row, parent, false);
        }

        CurrencyItem item = items.get(position);

        TextView titleView       = convertView.findViewById(R.id.titleText);
        TextView descriptionView = convertView.findViewById(R.id.descriptionText);
        TextView pubDateView     = convertView.findViewById(R.id.pubDateText);
        ImageView flagView       = convertView.findViewById(R.id.flagIcon);

        titleView.setText(item.getTitle());
        descriptionView.setText(item.getDescription());
        pubDateView.setText(item.getPubDate());

        // Flag icon
        String fxCode = extractCurrencyCode(item.getTitle());
        String iso    = currencyToIso(fxCode);

        if (iso != null) {
            int resId = context.getResources().getIdentifier(
                    iso, "drawable", context.getPackageName()
            );
            if (resId != 0) {
                flagView.setImageResource(resId);
            } else {
                flagView.setImageDrawable(null);
            }
        } else {
            flagView.setImageDrawable(null);
        }

        // Background colour depending on rate strength
        double rate = getRateValue(item.getDescription());
        int bg;

        if (rate < 1.0) {
            bg = Color.parseColor("#C8E6C9"); // strong currency (green)
        } else if (rate < 10.0) {
            bg = Color.parseColor("#FFF9C4"); // medium (yellow)
        } else if (rate < 100.0) {
            bg = Color.parseColor("#FFE0B2"); // weak (orange)
        } else {
            bg = Color.parseColor("#FFCDD2"); // very weak (red)
        }

        convertView.setBackgroundColor(bg);

        return convertView;
    }
}
