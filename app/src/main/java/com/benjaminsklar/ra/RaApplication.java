package com.benjaminsklar.ra;

import android.app.Application;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by Ben on 5/29/2017.
 */

public class RaApplication extends Application {
    static final String identityPoolID="us-east-1:30433e73-7fc6-4cef-a811-c5d13c04e7b9";
    static final Regions region = Regions.US_EAST_1;
    private static RaApplication mRaApplication;
    private AmazonDynamoDBClient mDbClient;

    public static RaApplication getRaApplication() { return mRaApplication; }

    public AmazonDynamoDBClient getDbClient() { return mDbClient; }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(RaApplication.class.toString(), "onCreate");

        mRaApplication = this;

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                identityPoolID,
                region
        );
        mDbClient = new AmazonDynamoDBClient(credentialsProvider);
    }
}