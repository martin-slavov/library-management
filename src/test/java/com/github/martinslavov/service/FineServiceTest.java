package com.github.martinslavov.service;

import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.model.enums.FineStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FineServiceTest {

    @Mock
    FineDAO fineDAO;

    @InjectMocks
    FineService fineService;

    private Fine validFine;

    @BeforeEach
    void setUp() {
        validFine = new Fine(1, new BigDecimal("10.00"), FineStatus.UNPAID);
    }

    @Test
    @DisplayName("Pay fine with valid data should update fine")
    void payFine_withValidData_shouldUpdateFine() {
        when(fineDAO.findById(validFine.getFineId())).thenReturn(Optional.of(validFine));

        fineService.payFine(validFine);

        verify(fineDAO).update(validFine);
    }

    @Test
    @DisplayName("Pay fine with fine not found should throw LibraryException")
    void payFine_withFineNotFound_shouldThrowLibraryException() {
        when(fineDAO.findById(validFine.getFineId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> fineService.payFine(validFine));
    }

    @Test
    @DisplayName("Pay fine with fine status paid should throw LibraryException")
    void payFine_withFineStatusPaid_shouldThrowLibraryException() {
        validFine.setStatus(FineStatus.PAID);
        when(fineDAO.findById(validFine.getFineId())).thenReturn(Optional.of(validFine));

        assertThrows(LibraryException.class, () -> fineService.payFine(validFine));
    }

    @Test
    @DisplayName("Waive fine with valid data should update fine")
    void waiveFine_withValidData_shouldUpdateFine() {
        when(fineDAO.findById(validFine.getFineId())).thenReturn(Optional.of(validFine));

        fineService.waiveFine(validFine);

        verify(fineDAO).update(validFine);
    }

    @Test
    @DisplayName("Waive fine with fine not found should throw LibraryException")
    void waiveFine_withFineNotFound_shouldThrowLibraryException() {
        when(fineDAO.findById(validFine.getFineId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> fineService.waiveFine(validFine));
    }

    @Test
    @DisplayName("Waive fine with fine status waived should throw LibraryException")
    void waiveFine_withFineStatusWaived_shouldThrowLibraryException() {
        validFine.setStatus(FineStatus.WAIVED);
        when(fineDAO.findById(validFine.getFineId())).thenReturn(Optional.of(validFine));

        assertThrows(LibraryException.class, () -> fineService.waiveFine(validFine));
    }
}