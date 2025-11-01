package pt.unl.fct.pds.proj1server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "med_data_k_anonymous")
public class MedDataKAnonymous {
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

    public MedDataKAnonymous() {
    }

    public MedDataKAnonymous(String id,
            // String name,
            String age,
            // String address,
            // String email,
            String gender,
            String postalCode,
            String diagnosis) {
        this.id = id;
        // this.name = name;
        this.age = age;
        // this.address = address;
        // this.email = email;
        this.gender = gender;
        this.postalCode = postalCode;
        this.diagnosis = diagnosis;
    }

    public String getId() {
        return id;
    }

    // public String getName() {return name;}
    public String getAge() {
        return age;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getGender() {
        return gender;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setId(String id) {
        this.id = id;
    }

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
