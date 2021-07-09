package com.panchmukhi.eclinic.Agent.AllAppointment;

public class AppointmentListModel {

    String address,city,country,dob,gender,landmark,locality,name,number,pincode,state,
            patientUid,doctorUid,agentUid,uniqueCallingId,age,appointmentDate,appointmentTime,
    doctorOrderNo,agentOrderNo,description;
    int orderNo;

    public AppointmentListModel(String address, String city, String country, String dob, String gender,
                                String landmark, String locality, String name, String number, String pincode,
                                String state, String patientUid,String doctorUid,String agentUid,
                                String uniqueCallingId,String age,String appointmentDate,String appointmentTime,
                                String doctorOrderNo,String agentOrderNo,String description,int orderNo) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.dob = dob;
        this.gender = gender;
        this.landmark = landmark;
        this.locality = locality;
        this.name = name;
        this.number = number;
        this.pincode = pincode;
        this.state = state;
        this.patientUid = patientUid;
        this.uniqueCallingId = uniqueCallingId;
        this.age = age;
        this.orderNo = orderNo;
        this.doctorUid = doctorUid;
        this.agentUid = agentUid;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.doctorOrderNo = doctorOrderNo;
        this.agentOrderNo = agentOrderNo;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctorOrderNo() {
        return doctorOrderNo;
    }

    public void setDoctorOrderNo(String doctorOrderNo) {
        this.doctorOrderNo = doctorOrderNo;
    }

    public String getAgentOrderNo() {
        return agentOrderNo;
    }

    public void setAgentOrderNo(String agentOrderNo) {
        this.agentOrderNo = agentOrderNo;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getAgentUid() {
        return agentUid;
    }

    public void setAgentUid(String agentUid) {
        this.agentUid = agentUid;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderUid() {
        return patientUid;
    }

    public void setOrderUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getUniqueCallingId() {
        return uniqueCallingId;
    }

    public void setUniqueCallingId(String uniqueCallingId) {
        this.uniqueCallingId = uniqueCallingId;
    }
}
