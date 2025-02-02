package com.api.v2.doctors.services;

import com.api.v2.doctors.domain.Doctor;
import com.api.v2.doctors.domain.DoctorAuditTrail;
import com.api.v2.doctors.domain.DoctorAuditTrailRepository;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.exceptions.ImmutableDoctorStatusException;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DoctorRehireServiceImpl implements DoctorRehireService {

    private final DoctorFinderUtil doctorFinderUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorAuditTrailRepository doctorAuditTrailRepository;

    public DoctorRehireServiceImpl(DoctorFinderUtil doctorFinderUtil,
                                        DoctorRepository doctorRepository,
                                        DoctorAuditTrailRepository doctorAuditTrailRepository
    ) {
        this.doctorFinderUtil = doctorFinderUtil;
        this.doctorRepository = doctorRepository;
        this.doctorAuditTrailRepository = doctorAuditTrailRepository;
    }

    @Override
    public ResponseEntity<Void> rehire(String medicalLicenseNumber) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        onActiveDoctor(doctor);
        DoctorAuditTrail doctorAuditTrail = DoctorAuditTrail.create(doctor);
        doctorAuditTrailRepository.save(doctorAuditTrail);
        doctor.markAsRehired();
        doctorRepository.save(doctor);
        return ResponseEntity.noContent().build();
    }

    private void onActiveDoctor(Doctor doctor) {
        if (doctor.getTerminatedAt() == null) {
            String message = "Doctor whose medical license number is %s is active.";
            throw new ImmutableDoctorStatusException(message);
        }
    }
}
