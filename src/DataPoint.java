/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Prajwal S
 */
public class DataPoint {
    private String date;
    private int value;

    public DataPoint(String date, int value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return date + ": " + value;
    }
}

