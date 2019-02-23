package com.example.hector.floridapark.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Plazas implements Parcelable {
    private int ID;
    private String plaza;
    private boolean ocupado;

    public Plazas() {
    }

    public Plazas(int ID, String plaza, boolean ocupado) {
        this.ID = ID;
        this.plaza = plaza;
        this.ocupado = ocupado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPlaza() {
        return plaza;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    protected Plazas(Parcel in) {
        ID = in.readInt();
        plaza = in.readString();
        ocupado = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(plaza);
        dest.writeByte((byte) (ocupado ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Plazas> CREATOR = new Parcelable.Creator<Plazas>() {
        @Override
        public Plazas createFromParcel(Parcel in) {
            return new Plazas(in);
        }

        @Override
        public Plazas[] newArray(int size) {
            return new Plazas[size];
        }
    };

    @Override
    public String toString() {
        return "Plazas{" +
                "ID=" + ID +
                ", plaza='" + plaza + '\'' +
                ", ocupado=" + ocupado +
                '}';
    }
}