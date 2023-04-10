package ru.netology.patient.repository;

import ru.netology.patient.entity.PatientInfo;

public interface PatientInfoRepository {

    PatientInfo getById(String id);

    String add(PatientInfo patientInfo);

    PatientInfo remove(String id);

    PatientInfo update(PatientInfo patientInfo);
}
