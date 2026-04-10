USE library_db;

DELIMITER $$
CREATE PROCEDURE borrow_book(
	IN  p_member_id INT,
	IN  p_book_id INT,
	OUT p_result INT
)
BEGIN
	DECLARE v_available INT DEFAULT 0;
	DECLARE v_status    VARCHAR(20);

    START TRANSACTION;
		SELECT available_copies INTO v_available
        FROM books
        WHERE book_id = p_book_id
		FOR UPDATE;

		SELECT status INTO v_status
        FROM members
		WHERE member_id = p_member_id;

        IF v_status != 'ACTIVE' THEN
			SET p_result = 2;
            ROLLBACK;
		ELSEIF v_available < 1 THEN
			SET p_result = 1;
            ROLLBACK;
		ELSE
			UPDATE books
            SET available_copies = available_copies - 1
            WHERE book_id = p_book_id;

            INSERT INTO loans (book_id, member_id, start_date, end_date, status)
            VALUES (p_book_id, p_member_id, CURRENT_DATE, CURRENT_DATE + INTERVAL 14 DAY, 'ACTIVE');

            SET p_result = 0;
            COMMIT;
		END IF;
END$$

CREATE PROCEDURE return_book(
	IN p_loan_id INT,
	OUT p_result INT
)
BEGIN
	DECLARE v_book_id INT;
	DECLARE v_end_date DATE;
    DECLARE v_status VARCHAR(20);

    START TRANSACTION;
		SELECT book_id, end_date, status INTO v_book_id, v_end_date, v_status
        FROM loans
        WHERE loan_id = p_loan_id
		FOR UPDATE;

        IF v_status != 'ACTIVE' AND v_status != 'OVERDUE' THEN
            SET p_result= 1;
            ROLLBACK;
		ELSE
			UPDATE loans
            SET return_date = CURRENT_DATE,  status = 'RETURNED'
			WHERE loan_id = p_loan_id;

            UPDATE books
            SET available_copies = available_copies + 1
            WHERE book_id = v_book_id;

            IF CURRENT_DATE > v_end_date THEN
				CALL calculate_fine(p_loan_id);
                SET p_result = 2;
			ELSE
				SET p_result = 0;
            END IF;

            COMMIT;
		END IF;
END$$

CREATE PROCEDURE calculate_fine(
	IN p_loan_id INT
)
BEGIN
	DECLARE v_end_date DATE;
    DECLARE v_return_date DATE;
    DECLARE v_days_late INT;
    DECLARE v_amount DECIMAL(8,2);
    DECLARE v_rate DECIMAL(4,2) DEFAULT 0.50;

    SELECT end_date, COALESCE(return_date, CURRENT_DATE)
    INTO v_end_date, v_return_date
    FROM loans
    WHERE loan_id = p_loan_id;

    SET v_days_late = DATEDIFF(v_return_date, v_end_date);

    IF v_days_late > 0 THEN
		SET v_amount = v_days_late * v_rate;

        INSERT INTO fines(loan_id, amount, status)
        VALUES (p_loan_id, v_amount, 'UNPAID')
        ON DUPLICATE KEY UPDATE amount = v_amount;
	END IF;
END$$

CREATE PROCEDURE get_member_loan_history(
	IN p_member_id INT
)
BEGIN
	SELECT
		l.loan_id,
		b.title,
		CONCAT(a.first_name, ' ', a.last_name) AS author_name,
        l.start_date,
        l.end_date,
        l.return_date,
        l.status AS loan_status,
        COALESCE(f.amount, 0.00) AS fine_amount,
        COALESCE(f.status, 'N/A') AS fine_status
	FROM loans l
    JOIN books b ON l.book_id = b.book_id
    JOIN authors a ON b.author_id = a.author_id
    LEFT JOIN fines f ON f.loan_id = l.loan_id
    WHERE l.member_id = p_member_id
    ORDER BY l.start_date DESC;
END$$

DELIMITER ;