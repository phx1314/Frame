//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility.application;

public class MContact {
    private long contactId = 0L;
    private String name = "";
    private long photoId = 0L;
    private String phone = "";
    private String pinyin = "";

    public MContact() {
    }

    public boolean search(String search) {
        return search != null && search.length() != 0?(this.pinyin + this.name + this.phone).indexOf(search) >= 0:true;
    }

    public long getContactId() {
        return this.contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;

        try {
            this.pinyin = this.getpinyin(name);
        } catch (Exception var3) {
            ;
        }

    }

    private String getpinyin(String str) {
        return null;
    }

    public long getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
