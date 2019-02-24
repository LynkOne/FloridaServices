package com.example.hector.floridapark.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.sql.Time;

public class Peceras implements Parcelable {

    private int id;
    private Time tiempo_reservado;
    private Date hora_de_reserva;
    private String dni_usuario_reserva;
    private Time fin_de_reserva;

    public Peceras() {
    }

    public Peceras(int id, Time tiempo_reservado, Date hora_de_reserva, String dni_usuario_reserva, Time fin_de_reserva) {
        this.id = id;
        this.tiempo_reservado = tiempo_reservado;
        this.hora_de_reserva = hora_de_reserva;
        this.dni_usuario_reserva = dni_usuario_reserva;
        this.fin_de_reserva=fin_de_reserva;
    }
    public Peceras(int id, Time tiempo_reservado, Date hora_de_reserva, String dni_usuario_reserva) {
        this.id = id;
        this.tiempo_reservado = tiempo_reservado;
        this.hora_de_reserva = hora_de_reserva;
        this.dni_usuario_reserva = dni_usuario_reserva;
    }

    public Time getFin_de_reserva() {
        return fin_de_reserva;
    }

    public void setFin_de_reserva(Time fin_de_reserva) {
        this.fin_de_reserva = fin_de_reserva;
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

    public Date getHora_de_reserva() {
        return hora_de_reserva;
    }

    public void setHora_de_reserva(Date hora_de_reserva) {
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
        hora_de_reserva = (Date) in.readValue(Date.class.getClassLoader());
        dni_usuario_reserva = in.readString();
        fin_de_reserva = (Time) in.readValue(Time.class.getClassLoader());
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
        dest.writeValue(fin_de_reserva);

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

    @Override
    public String toString() {
        return "Peceras{" +
                "id=" + id +
                ", tiempo_reservado=" + tiempo_reservado +
                ", hora_de_reserva=" + hora_de_reserva +
                ", dni_usuario_reserva='" + dni_usuario_reserva + '\'' +
                '}';
    }
}