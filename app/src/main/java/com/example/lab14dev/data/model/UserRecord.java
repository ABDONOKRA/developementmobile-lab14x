package com.example.lab14dev.data.model;

/**
 * Représente une entité utilisateur pour la démonstration de stockage JSON.
 */
public class UserRecord {
    private final int id;
    private final String fullName;
    private final int ageValue;

    public UserRecord(int id, String fullName, int ageValue) {
        this.id = id;
        this.fullName = fullName;
        this.ageValue = ageValue;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public int getAgeValue() { return ageValue; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nom: " + fullName + " | Âge: " + ageValue;
    }
}
