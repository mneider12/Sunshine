package com.example.mark.helloworld.app.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.mark.helloworld.app.data.WeatherContract;
import com.example.mark.helloworld.app.data.WeatherDbHelper;

import java.util.Map;
import java.util.Set;


/**
 * Created by Mark on 10/25/2014.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();

    }

    public void testInsertReadDb() {

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createNorthPoleLocationValues();

        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);

        //verify we got a row back
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        //Data's inserted. IN THEORY. Now pull some out to stare at it and verify it made
        // the round trip.


        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,   // Table to query
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        validateCursor(cursor, testValues);

        ContentValues weatherValues = createWeatherValues(locationRowId);

        long weatherRowId;
        weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);

        //Verify we got a row back.
        assertTrue(weatherRowId != -1);
        Log.d(LOG_TAG, "New row id: " + weatherRowId);

        Cursor weatherCursor = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME,
                null, null, null, null, null,null);

        validateCursor(weatherCursor, weatherValues);

        dbHelper.close();
    }

    protected static final String TEST_DATE = "20141205";
    protected static final double TEST_DEGREES = 1.1;
    protected static final double TEST_HUMIDITY = 1.2;
    protected static final double TEST_PRESSURE = 1.3;
    protected static final int TEST_MAX = 75;
    protected static final int TEST_MIN = 65;
    protected static final String TEST_DESC = "Asteroids";
    protected static final double TEST_WIND = 5.5;
    protected static final int TEST_ID = 321;
    static ContentValues createWeatherValues(long locationRowId) {

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATETEXT, TEST_DATE);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, TEST_DEGREES);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, TEST_HUMIDITY);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, TEST_PRESSURE);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, TEST_MAX);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, TEST_MIN);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, TEST_DESC);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, TEST_WIND);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, TEST_ID);

        return weatherValues;
    }

    protected static final String TEST_NAME = "North Pole";
    protected static final String TEST_LOCATION = "99705";
    protected static final double TEST_LATITUDE = 64.772;
    protected static final double TEST_LONGITUDE = -147.355;

    static ContentValues createNorthPoleLocationValues() {

        // Test data we're going to insert into the DB to
        String testName = TEST_NAME;
        String testLocationSetting = TEST_LOCATION;
        double testLatitude = TEST_LATITUDE;
        double testLongitude = TEST_LONGITUDE;

        //Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testName);
        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, testLongitude);

        return testValues;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }

        valueCursor.close();
    }
}
