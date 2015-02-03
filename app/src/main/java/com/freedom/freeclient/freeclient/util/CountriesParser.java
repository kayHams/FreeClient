package com.freedom.freeclient.freeclient.util;


import android.util.Xml;

import com.freedom.freeclient.freeclient.Country;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by kemihambolu on 1/23/15.
 */
public class CountriesParser {
    // We don't use namespaces
    private static final String ns = null;
    public ArrayList parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readCountries(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList readCountries(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList countries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "countries");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("country")) {
                countries.add(readCountry(parser));
            } else {
                skip(parser);
            }
        }
        return countries;
    }

    // Processes title tags in the feed.
    private Country readCountry(XmlPullParser parser) throws IOException, XmlPullParserException {
        Country country = new Country();
        parser.require(XmlPullParser.START_TAG, ns, "country");
        String country_id = parser.getAttributeValue(null, "id");
        country.setId(country_id);
        String name = readText(parser);
        country.setName(name);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "country");
        return country;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
        }
        return result;
    }
}
