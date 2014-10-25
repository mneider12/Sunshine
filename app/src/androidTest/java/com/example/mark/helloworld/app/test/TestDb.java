package com.example.mark.helloworld.app.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.mark.helloworld.app.data.WeatherContract;
import com.example.mark.helloworld.app.data.WeatherDbHelper;


/**
 * Created by Mark on 10/25/2014.
 */
public class TestDb extends AndroidTestCase {

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();

    }

    public void testInsertReadDb() {

        final String LOG_TAG = TestDb.class.getSimpleName();

        // Test data we're going to insert into the DB to
        String testName = "North Pole";
        String testLocationSetting = "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.355;

        String testDate = "20141205";
        double testDegrees = 1.1;
        double testHumidity = 1.2;
        double testPressure = 1.3;
        int testMax = 75;
        int testMin = 65;
        String testDesc = "Asteroids";
        double testWind = 5.5;
        int testId = 321;

        WeatherDbHelper dbHelper =
                new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testName);
        values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG,testLongitude);

        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);

        //Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATETEXT, testDate);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, testDegrees);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, testHumidity);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, testPressure);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, testMax);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, testMin);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, testDesc);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, testWind);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, testId);

        long weatherRowId;
        weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);

        //Verify we got a row back.
        assertTrue(weatherRowId != -1);
        Log.d(LOG_TAG, "New row id: " + weatherRowId);

        // Specify which column you want
        String[] columns = {
                WeatherContract.LocationEntry._ID,
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
                WeatherContract.LocationEntry.COLUMN_CITY_NAME,
                WeatherContract.LocationEntry.COLUMN_COORD_LAT,
                WeatherContract.LocationEntry.COLUMN_COORD_LONG
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,   // Table to query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.
            int locationIndex = cursor.getColumnIndex(
                    WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex((WeatherContract.LocationEntry.COLUMN_CITY_NAME));
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex((WeatherContract.LocationEntry.COLUMN_COORD_LAT));
            double latitude = cursor.getDouble(latIndex);

            int longIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(longIndex);

            assertEquals(testName, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);
        } else {
            fail("No values returned :(");
        }

        // Specify which column you want
        String[] WeatherColumns = {
                WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_LOC_KEY,
                WeatherContract.WeatherEntry.COLUMN_DATETEXT,
                WeatherContract.WeatherEntry.COLUMN_DEGREES,
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
        };

        // A cursor is your primary interface to the query results.
        cursor = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME,   // Table to query
                WeatherColumns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.

            int dateIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_DATETEXT);
            String date = cursor.getString(dateIndex);

            int degreesIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_DEGREES);
            double degrees = cursor.getDouble(degreesIndex);

            int humidityIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_HUMIDITY);
            double humidity = cursor.getDouble(humidityIndex);

            int pressureIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_PRESSURE);
            double pressure = cursor.getDouble(pressureIndex);

            int maxIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
            int max = cursor.getInt(maxIndex);

            int minIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
            int min = cursor.getInt(minIndex);

            int descIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
            String desc = cursor.getString(descIndex);

            int windIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_WIND_SPEED);
            double wind = cursor.getDouble(windIndex);

            int weatherIndex = cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_WEATHER_ID);
            int weatherId = cursor.getInt(weatherIndex);

            assertEquals(testDate, date);
            assertEquals(testDegrees,degrees);
            assertEquals(testHumidity, humidity);
            assertEquals(testPressure, pressure);
            assertEquals(testMax, max);
            assertEquals(testMin, min);
            assertEquals(testDesc, desc);
            assertEquals(testWind, wind);
            assertEquals(testId, weatherId);

        } else {
            fail("No values returned :(");
        }



        cursor.close();
        db.close();
    }
}
