package com.example.hector.floridapark.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

public class Peceras implements Parcelable {

    private int id;
    private Time tiempo_reservado;
    private Time hora_de_reserva;
    private String dni_usuario_reserva;

    public Peceras() {
    }

    public Peceras(int id, Time tiempo_reservado, Time hora_de_reserva, String dni_usuario_reserva) {
        this.id = id;
        this.tiempo_reservado = tiempo_reservado;
        this.hora_de_reserva = hora_de_reserva;
        this.dni_usuario_reserva = dni_usuario_reserva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getTiempo_reservado() {
        return tiempo_reservado;
    }

    public void setTiempo_reservado(Time tiempo_reservado) {
        this.tiempo_reservado = tiempo_reservado;
    }

    public Time getHora_de_reserva() {
        return hora_de_reserva;
    }

    public void setHora_de_reserva(Time hora_de_reserva) {
        this.hora_de_reserva = hora_de_reserva;
    }

    public String getDni_usuario_reserva() {
        return dni_usuario_reserva;
    }

    public void setDni_usuario_reserva(String dni_usuario_reserva) {
        this.dni_usuario_reserva = dni_usuario_reserva;
    }

    protected Peceras(Parcel in) {
        id = in.readInt();
        tiempo_reservado = (Time) in.readValue(Time.class.getClassLoader());
        hora_de_reserva = (Time) in.readValue(Time.class.getClassLoader());
        dni_usuario_reserva = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(tiempo_reservado);
        dest.writeValue(hora_de_reserva);
        dest.writeString(dni_usuario_reserva);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Peceras> CREATOR = new Parcelable.Creator<Peceras>() {
        @Override
        public Peceras createFromParcel(Parcel in) {
            return new Peceras(in);
        }

        @Override
        public Peceras[] newArray(int size) {
            return new Peceras[size];
        }
    };
}