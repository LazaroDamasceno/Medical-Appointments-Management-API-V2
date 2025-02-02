package com.api.v2.doctors.services;

import com.api.v2.doctors.domain.Doctor;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import com.api.v2.people.domain.Person;
import com.api.v2.people.dtos.PersonModificationDto;
import com.api.v2.people.services.interfaces.PersonModificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DoctorModificationServiceImpl implements DoctorModificationService {

    private final DoctorFinderUtil doctorFinderUtil;
    private final DoctorRepository doctorRepository;
    private final PersonModificationService personModificationService;

    public DoctorModificationServiceImpl(DoctorFinderUtil doctorFinderUtil,
                                         DoctorRepository doctorRepository,
                                         PersonModificationService personModificationService
    ) {
        this.doctorFinderUtil = doctorFinderUtil;
        this.doctorRepository = doctorRepository;
        this.personModificationService = personModificationService;
    }

    @Override
    public ResponseEntity<Void> modify(String medicalLicenseNumber, PersonModificationDto modificationDto) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        Person modifiedPerson = personModificationService.modify(doctor.getPerson(), modificationDto);
        doctor.setPerson(modifiedPerson);
        doctorRepository.save(doctor);
        return ResponseEntity.noContent().build();
    }
}
