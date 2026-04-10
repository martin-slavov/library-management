package com.github.martinslavov.service;

import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.model.enums.FineStatus;

public class FineService {

    private final FineDAO fineDAO;

    public FineService(FineDAO fineDAO) {
        this.fineDAO = fineDAO;
    }

    public void payFine(Fine fine) {

        validateFineExists(fine);
        if (fine.getStatus() == FineStatus.PAID) {
            throw new LibraryException("Fine already paid");
        }

        fine.setStatus(FineStatus.PAID);
        fineDAO.update(fine);
    }

    public void waiveFine(Fine fine) {

        validateFineExists(fine);
        if (fine.getStatus() == FineStatus.WAIVED) {
            throw new LibraryException("Fine already waived");
        }

        fine.setStatus(FineStatus.WAIVED);
        fineDAO.update(fine);
    }

    private void validateFineExists(Fine fine) {
        if (fineDAO.findById(fine.getFineId()).isEmpty()) {
            throw new LibraryException("Fine not found");
        }
    }
}
