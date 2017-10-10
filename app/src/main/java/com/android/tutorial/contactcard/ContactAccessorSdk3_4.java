package com.android.tutorial.contactcard;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.Contacts.People.Phones;

/**
 * Created by Mannan on 10/9/2017.
 */

@SuppressWarnings("deprecation")
public class ContactAccessorSdk3_4 extends ContactAccessor {
    /**
     * Returns a Pick Contact intent using the pre-Eclair "people" URI.
     */
    @Override
    public Intent getPickContactIntent() {
        return new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
    }
    /**
     * Retrieves the contact information.
     */
    @Override
    public ContactInfo loadContact(ContentResolver contentResolver, Uri contactUri) {
        ContactInfo contactInfo = new ContactInfo();
        Cursor cursor = contentResolver.query(contactUri,
                new String[]{People.DISPLAY_NAME}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                contactInfo.setDisplayName(cursor.getString(0));
            }
        } finally {
            cursor.close();
        }
        Uri phoneUri = Uri.withAppendedPath(contactUri, Phones.CONTENT_DIRECTORY);
        cursor = contentResolver.query(phoneUri,
                new String[]{Phones.NUMBER}, null, null, Phones.ISPRIMARY + " DESC");
        try {
            if (cursor.moveToFirst()) {
                contactInfo.setPhoneNumber(cursor.getString(0));
            }
        } finally {
            cursor.close();
        }
        return contactInfo;
    }
}
