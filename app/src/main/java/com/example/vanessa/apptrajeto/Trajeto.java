package com.example.vanessa.apptrajeto;

/**
 * Created by vanessa on 22/11/2015.
 */
public class Trajeto {
    private int id;
    private String nome;
    private int largura;
    private double area;
    private double ponto0Latitude;
    private double ponto1Longitude;
    private double ponto2Latitude;
    private double ponto3Longitude;
    private double ponto4Latitude;
    private double ponto5Longitude;
    private double ponto6Latitude;
    private double ponto7Longitude;
    private double ponto8Latitude;
    private double ponto9Longitude;
    private double ponto10Latitude;
    private double ponto11Longitude;

    public Trajeto(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPonto0Latitude() {
        return ponto0Latitude;
    }

    public void setPonto0Latitude(double ponto0Latitude) {
        this.ponto0Latitude = ponto0Latitude;
    }

    public double getPonto1Longitude() {
        return ponto1Longitude;
    }

    public void setPonto1Longitude(double ponto1Longitude) {
        this.ponto1Longitude = ponto1Longitude;
    }

    public double getPonto2Latitude() {
        return ponto2Latitude;
    }

    public void setPonto2Latitude(double ponto2Latitude) {
        this.ponto2Latitude = ponto2Latitude;
    }

    public double getPonto3Longitude() {
        return ponto3Longitude;
    }

    public void setPonto3Longitude(double ponto3Longitude) {
        this.ponto3Longitude = ponto3Longitude;
    }

    public double getPonto4Latitude() {
        return ponto4Latitude;
    }

    public void setPonto4Latitude(double ponto4Latitude) {
        this.ponto4Latitude = ponto4Latitude;
    }

    public double getPonto5Longitude() {
        return ponto5Longitude;
    }

    public void setPonto5Longitude(double ponto5Longitude) {
        this.ponto5Longitude = ponto5Longitude;
    }

    public double getPonto6Latitude() {
        return ponto6Latitude;
    }

    public void setPonto6Latitude(double ponto6Latitude) {
        this.ponto6Latitude = ponto6Latitude;
    }

    public double getPonto7Longitude() {
        return ponto7Longitude;
    }

    public void setPonto7Longitude(double ponto7Longitude) {
        this.ponto7Longitude = ponto7Longitude;
    }

    public double getPonto8Latitude() {
        return ponto8Latitude;
    }

    public void setPonto8Latitude(double ponto8Latitude) {
        this.ponto8Latitude = ponto8Latitude;
    }

    public double getPonto9Longitude() {
        return ponto9Longitude;
    }

    public void setPonto9Longitude(double ponto9Longitude) {
        this.ponto9Longitude = ponto9Longitude;
    }

    public double getPonto10Latitude() {
        return ponto10Latitude;
    }

    public void setPonto10Latitude(double ponto10Latitude) {
        this.ponto10Latitude = ponto10Latitude;
    }

    public double getPonto11Longitude() {
        return ponto11Longitude;
    }

    public void setPonto11Longitude(double ponto11Longitude) {
        this.ponto11Longitude = ponto11Longitude;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String toStringAll() {
        return "Trajeto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", largura=" + largura +
                ", area=" + area +
                ", ponto0Latitude=" + ponto0Latitude +
                ", ponto1Longitude=" + ponto1Longitude +
                ", ponto2Latitude=" + ponto2Latitude +
                ", ponto3Longitude=" + ponto3Longitude +
                ", ponto4Latitude=" + ponto4Latitude +
                ", ponto5Longitude=" + ponto5Longitude +
                ", ponto6Latitude=" + ponto6Latitude +
                ", ponto7Longitude=" + ponto7Longitude +
                ", ponto8Latitude=" + ponto8Latitude +
                ", ponto9Longitude=" + ponto9Longitude +
                ", ponto10Latitude=" + ponto10Latitude +
                ", ponto11Longitude=" + ponto11Longitude +
                '}';
    }
    @Override
    public String toString() {
        return nome;
    }
}
