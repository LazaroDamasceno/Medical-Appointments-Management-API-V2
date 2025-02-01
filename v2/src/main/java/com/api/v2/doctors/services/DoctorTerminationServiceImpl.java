package com.api.v2.doctors.services;

import com.api.v2.doctors.domain.Doctor;
import com.api.v2.doctors.domain.DoctorAuditTrail;
import com.api.v2.doctors.domain.DoctorAuditTrailRepository;
import com.api.v2.doctors.domain.DoctorRepository;
import com.api.v2.doctors.exceptions.ImmutableDoctorStatusException;
import com.api.v2.doctors.utils.DoctorFinderUtil;
import org.springframework.stereotype.Service;

@Service
public class DoctorTerminationServiceImpl implements DoctorTerminationService {

    private final DoctorFinderUtil doctorFinderUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorAuditTrailRepository doctorAuditTrailRepository;

    public DoctorTerminationServiceImpl(DoctorFinderUtil doctorFinderUtil,
                                        DoctorRepository doctorRepository,
                                        DoctorAuditTrailRepository doctorAuditTrailRepository
    ) {
        this.doctorFinderUtil = doctorFinderUtil;
        this.doctorRepository = doctorRepository;
        this.doctorAuditTrailRepository = doctorAuditTrailRepository;
    }

    @Override
    public void terminate(String medicalLicenseNumber) {
        Doctor doctor = doctorFinderUtil.findByMedicalLicenseNumber(medicalLicenseNumber);
        onTerminatedDoctor(doctor);
        DoctorAuditTrail doctorAuditTrail = DoctorAuditTrail.create(doctor);
        doctorAuditTrailRepository.save(doctorAuditTrail);
        doctor.markAsTerminated();
        doctorRepository.save(doctor);
    }

    private void onTerminatedDoctor(Doctor doctor) {
        if (doctor.getTerminatedAt() != null) {
            String message = "Doctor whose medical license number is %s is already terminated.";
            throw new ImmutableDoctorStatusException(message);
        }
    }
}
