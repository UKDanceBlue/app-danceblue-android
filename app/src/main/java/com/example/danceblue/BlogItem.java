//Created by Adrian Carideo, Joe Clements, Kendall Conley
//Copyright © 2019 DanceBlue. All rights reserved.
package com.example.danceblue;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//This class defines the BlogItem object to be used and displayed on the blog page.
public class BlogItem implements Comparable<BlogItem> {
    private boolean isValid;
    private String id, imageURL, author, title, formattedDate;
    //make sure the date has a default value so it can be sorted even if invalid
    private Date date = new Date();
    private static final String TAG = "BlogItem.java";
    private DataSnapshot chunksDSS;

    //constructor using a dataSnapshot
    public BlogItem (DataSnapshot dataSnapshot){
        isValid = true; //assume valid info passed until proved otherwise

        //get the id, over-engineered due to spelling errors in DB
        Object tempId = null; //stays null if the id is otherwise misnamed or missing
        if (dataSnapshot.child("id").exists()) {
            tempId = dataSnapshot.child("id").getValue();
        }
        else if (dataSnapshot.child("Id").exists()) {
            tempId = dataSnapshot.child("Id").getValue();
        }
        else if (dataSnapshot.child("id value").exists()) {
            tempId = dataSnapshot.child("id value").getValue();
        }

        //read in the needed info from the details child
        DataSnapshot detailsSnapshot = dataSnapshot.child("details");
        Object tempAuthor = detailsSnapshot.child("author").getValue();
        Object tempTitle = detailsSnapshot.child("title").getValue();
        Object tempTimeStamp = detailsSnapshot.child("timestamp").getValue();
        Object tempImage = detailsSnapshot.child("image").getValue();

        //convert to strings, checking for null DB children
        id = (tempId != null) ? tempId.toString() : "";
        author = (tempAuthor != null) ? tempAuthor.toString() : "";
        title = (tempTitle != null) ? tempTitle.toString() : "";
        imageURL = (tempImage != null) ? tempImage.toString() : "";
        String timestamp = (tempTimeStamp != null) ? tempTimeStamp.toString() : "";

        //check validity of each string created above before moving on
        if (id.equals("") || author.equals("") || title.equals("") || timestamp.equals("")) {
            isValid = false;
            Log.e(TAG, "Blog FAIL with: "+isValid()+" "+getId()+" "+getAuthor()+" "+getTitle());
            return;
        }

        //convert timestamp to a date so they can be sorted
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try { //catch date string parsing errors
            date = formatter.parse(timestamp);
        } catch (ParseException e) { //print error to log, mark this object as invalid
            Log.e("onViewCreated", "countdownEnd date couldn't be parsed");
            Log.e("onViewCreated", e.getMessage());
            isValid = false;
            Log.e(TAG, "Blog FAIL with: "+isValid()+" "+getId()+" "+getAuthor()+" "+getTitle());
            date = new Date();
        }

        SimpleDateFormat displayFormatter = new SimpleDateFormat("EEEE', 'MMMM' 'd', 'yyyy", Locale.US);
        formattedDate = displayFormatter.format(date);

        //store the whole chunks child to be parsed later when clicked, reduce up-front workload
        chunksDSS = dataSnapshot.child("chunks");
        if (!chunksDSS.exists()) {
            isValid = false;
            Log.e(TAG, "Invalid chunks in blog: "+getId()+" "+getAuthor()+" "+getTitle());
        }

        Log.d(TAG, "Blog made with: "+isValid()+" "+getId()+" "+getAuthor()+" "+getTitle());
    }

    //methods
    public boolean isValid() {
        return isValid;
    }
    public String getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public String getImageURL() {
        return imageURL;
    }
    public Date getDate() {return date;}
    public String getFormattedDate() {return formattedDate;}
    public DataSnapshot getChunksDSS() {
        return chunksDSS;
    }

    @Override //allows Collections.sort() to sort these objects in descending order by date
    public int compareTo(BlogItem o) {
        return o.date.compareTo(this.date);
    }
}
