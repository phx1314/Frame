//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility.application;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import com.mdx.framework.utility.application.MContact;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MContacts {
    public MContacts() {
    }

    public List<MContact> getContact(Context ctx, MContacts.OnContactAddListener oncont) {
        ArrayList retn = new ArrayList();
        Cursor cur = ctx.getContentResolver().query(Contacts.CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);
        if(cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex("_id");
            int displayNameColumn = cur.getColumnIndex("display_name");
            int photo = cur.getColumnIndex("photo_id");

            do {
                long contactid = cur.getLong(idColumn);
                String name = cur.getString(displayNameColumn);
                long photoid = cur.getLong(photo);
                int phoneCount = cur.getInt(cur.getColumnIndex("has_phone_number"));
                if(phoneCount > 0) {
                    Cursor phones = ctx.getContentResolver().query(Phone.CONTENT_URI, (String[])null, "contact_id = " + contactid, (String[])null, (String)null);
                    if(phones.moveToFirst()) {
                        do {
                            String phoneNumber = phones.getString(phones.getColumnIndex("data1"));
                            MContact cont = new MContact();
                            cont.setContactId(contactid);
                            cont.setName(name);
                            cont.setPhotoId(photoid);
                            cont.setPhone(phoneNumber);
                            retn.add(cont);
                            if(oncont != null) {
                                try {
                                    oncont.onAdd(cont);
                                } catch (Exception var18) {
                                    oncont = null;
                                }
                            }
                        } while(phones.moveToNext());

                        phones.close();
                    }
                }
            } while(cur.moveToNext());
        }

        cur.close();
        return retn;
    }

    public Drawable getPhoto(Context ctx, MContact cont) {
        ContentResolver resolver = ctx.getContentResolver();
        Long photoid = Long.valueOf(cont.getPhotoId());
        if(photoid.longValue() > 0L) {
            Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, cont.getContactId());
            InputStream input = Contacts.openContactPhotoInputStream(resolver, uri);
            return Drawable.createFromStream(input, "photo" + photoid);
        } else {
            return null;
        }
    }

    public interface OnContactAddListener {
        void onAdd(MContact var1) throws Exception;
    }
}
