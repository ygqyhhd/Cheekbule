package com.meeblue.celsius.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by c9736 on 2016/7/13.
 */


public class Book extends SugarRecord implements Parcelable {

    /*
    @Column这个注解意思是说你想强制按照你的规定的名字来创建表中对应的字段名字，所以这里的skuId在Goods表中的字段名就不是默认的sku_id了，而是你自己给的sku_ID
    @Ignore这个注解强调这个属性在表中不要创建对应的字段

     */
    String name;//书名
    String number;//编号
    String author;//作者
    protected Book(Parcel in) {
        name = in.readString();
        number = in.readString();
        author = in.readString();
    }

    public Book() {
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(author);
    }

    @Override
    public String toString() {
        return getId()+"/"+getAuthor()+"/"+getName()+"/"+getNumber();
    }
}