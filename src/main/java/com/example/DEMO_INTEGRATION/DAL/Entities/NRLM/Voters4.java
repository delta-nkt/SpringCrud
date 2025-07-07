package com.example.DEMO_INTEGRATION.DAL.Entities.NRLM;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Voters4\"", schema = "public")  // Use exact name with quotes
@Data
public class Voters4 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer id;

    @Column(name = "\"Name\"", nullable = false, length = 100)
    private String name;

    @Column(name = "\"Age\"", nullable = false)
    private Integer age;

    @Column(name = "\"IsAdult\"", nullable = false)
    private Boolean isAdult;

    // --- Constructors ---
    public Voters4() {
    }

    public Voters4(Integer id, String name, Integer age, Boolean isAdult) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isAdult = isAdult;
    }

    // --- Getter and Setter for id ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // --- Getter and Setter for name ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // --- Getter and Setter for age ---
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // --- Getter and Setter for isAdult ---
    public Boolean getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(Boolean isAdult) {
        this.isAdult = isAdult;
    }

}