package service.medical;

import org.codehaus.plexus.util.cli.Arg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public class MedicalServiceTests {

    @ParameterizedTest
    @MethodSource("sourceBloodPressure")
    public void checkBloodPressureTest(BloodPressure bloodPressure) {
        //arrange
        PatientInfo patientInfo = new PatientInfo(
                "123",
                "Name",
                "Surname",
                LocalDate.of(2006, 10, 10),
                new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80))
        );

        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString())).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        //act
        medicalService.checkBloodPressure(patientInfo.getId(), bloodPressure);

        //assert
        if (patientInfo.getHealthInfo().getBloodPressure().equals(bloodPressure)) {
            Mockito.verify(alertService, Mockito.never()).send(Mockito.anyString());
        } else {
            Mockito.verify(alertService, Mockito.only()).send(Mockito.anyString());
        }
    }

    public static Stream<Arguments> sourceBloodPressure() {
        return Stream.of(
                Arguments.of(new BloodPressure(120, 80)),
                Arguments.of(new BloodPressure(125, 78))
        );
    }

    @Test
    public void checkBloodPressureMessageTest() {
        //arrange
        PatientInfo patientInfo = new PatientInfo(
                "123",
                "Name",
                "Surname",
                LocalDate.of(2006, 10, 10),
                new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80))
        );

        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString())).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        //act
        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(130, 98));

        //assert
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 123, need help", argumentCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("sourceTemperature")
    public void checkTemperatureTest(BigDecimal temperature) {
        //arrange
        PatientInfo patientInfo = new PatientInfo(
                "123",
                "Name",
                "Surname",
                LocalDate.of(2006, 10, 10),
                new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80))
        );

        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString())).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        //act
        medicalService.checkTemperature(patientInfo.getId(),temperature);

        //assert
        if (patientInfo.getHealthInfo().getNormalTemperature().compareTo(temperature) <= 0) {
            Mockito.verify(alertService, Mockito.never()).send(Mockito.anyString());
        } else {
            Mockito.verify(alertService, Mockito.only()).send(Mockito.anyString());
        }
    }

    public static Stream<Arguments> sourceTemperature() {
        return Stream.of(
                Arguments.of("36.6"),
                Arguments.of("-20.0")
        );
    }
}
