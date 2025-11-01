package pt.unl.fct.pds.proj1server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "med_data_deidentified")
public class MedDataDeidentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    // @Column(nullable = false)
    // private String name;
    @Column(nullable = false)
    private String age; // generalize by 5 years instead
    // @Column(nullable = false)
    // private String address;
    // @Column(nullable = false, unique = true)
    // private String email;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String postalCode; // remove last two digits?
    @Column(nullable = false)
    private String diagnosis;

    public MedDataDeidentified() {
    }

    public MedDataDeidentified(String id,
            // String name,
            String age,
            // String address,
            // String email,
            String postalCode,
            String gender,
            String diagnosis) {
        this.id = id;
        // this.name = name;
        this.age = age;
        // this.address = address;
        // this.email = email;
        this.postalCode = postalCode;
        this.gender = gender;
        this.diagnosis = diagnosis;
    }

    public String getId() {
        return id;
    }

    // public String getName() {return name;}
    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setId(String id) {
        this.id = id;
    }

    // public void setName(String name) {this.name = name;}
    // public void setAddress(String Address) {this.address = address;}
    public void setAge(String age) {
        this.age = age;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
