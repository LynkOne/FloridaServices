package com.example.hector.floridapark.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Personas implements Parcelable {
    private String dni;
    private String nombre;
    private String apellidos;
    private int telefono;
    private String correo;
    private String password;
    private boolean tipo; //0 estudiante y 1 profesor
    private int cod_prof;

    public Personas(String dni, String nombre, String apellidos, int telefono, String correo) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;

    }

    public Personas(String dni, String nombre, String apellidos, int telefono, String correo, String password, boolean tipo, int cod_prof) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.password = password;
        this.tipo = tipo;
        this.cod_prof = cod_prof;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public int getCod_prof() {
        return cod_prof;
    }

    public void setCod_prof(int cod_prof) {
        this.cod_prof = cod_prof;
    }

    protected Personas(Parcel in) {
        dni = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        telefono = in.readInt();
        correo = in.readString();
        password = in.readString();
        tipo = in.readByte() != 0x00;
        cod_prof =in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dni);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeInt(telefono);
        dest.writeString(correo);
        dest.writeString(password);
        dest.writeByte((byte) (tipo ? 0x01 : 0x00));
        dest.writeInt(cod_prof);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Personas> CREATOR = new Parcelable.Creator<Personas>() {
        @Override
        public Personas createFromParcel(Parcel in) {
            return new Personas(in);
        }

        @Override
        public Personas[] newArray(int size) {
            return new Personas[size];
        }
    };

    @Override
    public String toString() {
        return "Personas{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono=" + telefono +
                ", correo='" + correo + '\'' +
                ", password='" + password + '\'' +
                ", tipo=" + tipo +
                ", cod_prof=" + cod_prof +
                '}';
    }
}