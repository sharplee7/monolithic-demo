-- Monolithic Bank Sample Data
-- Insert sample data for testing

-- Sample Users (password: 'password123' with different salts)
INSERT INTO TB_USER (USER_ID, PASSWORD_HASH, SALT, EMAIL, NAME, STATUS) VALUES 
('user001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXDALKIbYFF6lrn/y6pL5KeIKSu', 'salt001', 'user001@example.com', 'John Doe', 'ACTIVE'),
('user002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXDALKIbYFF6lrn/y6pL5KeIKSu', 'salt002', 'user002@example.com', 'Jane Smith', 'ACTIVE'),
('user003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXDALKIbYFF6lrn/y6pL5KeIKSu', 'salt003', 'user003@example.com', 'Bob Johnson', 'ACTIVE');

-- Sample Customers
INSERT INTO TB_CSTM (CSTM_ID, CSTM_NM, CSTM_AGE, CSTM_GND, CSTM_PN, CSTM_ADR) VALUES 
('CUST001', '김철수', '35', 'M', '010-1234-5678', '서울시 강남구 테헤란로 123'),
('CUST002', '이영희', '28', 'F', '010-2345-6789', '서울시 서초구 서초대로 456'),
('CUST003', '박민수', '42', 'M', '010-3456-7890', '서울시 송파구 올림픽로 789'),
('CUST004', '최지영', '31', 'F', '010-4567-8901', '서울시 마포구 월드컵로 321'),
('CUST005', '정대한', '39', 'M', '010-5678-9012', '서울시 용산구 한강대로 654');

-- Sample Transfer Limits
INSERT INTO TB_TRNF_LMT (CSTM_ID, ONE_TM_TRNF_LMT, ONE_DY_TRNF_LMT) VALUES 
('CUST001', 10000000, 50000000),
('CUST002', 5000000, 30000000),
('CUST003', 20000000, 100000000),
('CUST004', 3000000, 20000000),
('CUST005', 15000000, 80000000);

-- Sample Accounts
INSERT INTO TB_ACNT (ACNT_NO, CSTM_ID, CSTM_NM, ACNT_NM, NEW_DTM, ACNT_BLNC) VALUES 
('110-123-456789', 'CUST001', '김철수', '김철수 주거래통장', '2024-01-15 09:30:00', 5000000),
('110-234-567890', 'CUST002', '이영희', '이영희 적금통장', '2024-01-20 14:15:00', 3000000),
('110-345-678901', 'CUST003', '박민수', '박민수 급여통장', '2024-02-01 10:45:00', 8500000),
('110-456-789012', 'CUST004', '최지영', '최지영 자유통장', '2024-02-10 16:20:00', 2200000),
('110-567-890123', 'CUST005', '정대한', '정대한 사업자통장', '2024-02-15 11:30:00', 12000000),
('110-678-901234', 'CUST001', '김철수', '김철수 적금통장', '2024-03-01 13:45:00', 1500000);

-- Sample Transaction History
INSERT INTO TB_TRNS_HST (ACNT_NO, SEQ, DIV_CD, STS_CD, TRNS_AMT, ACNT_BLNC, TRNS_BRNCH, TRNS_DTM) VALUES 
-- 김철수 주거래통장 거래내역
('110-123-456789', 1, 'D', '1', 5000000, 5000000, '강남지점', '2024-01-15 09:30:00'),
('110-123-456789', 2, 'W', '1', 200000, 4800000, '강남지점', '2024-01-16 14:20:00'),
('110-123-456789', 3, 'D', '1', 300000, 5100000, 'ATM', '2024-01-18 10:15:00'),
('110-123-456789', 4, 'W', '1', 100000, 5000000, '온라인', '2024-01-20 16:30:00'),

-- 이영희 적금통장 거래내역
('110-234-567890', 1, 'D', '1', 3000000, 3000000, '서초지점', '2024-01-20 14:15:00'),
('110-234-567890', 2, 'D', '1', 500000, 3500000, 'ATM', '2024-01-25 09:45:00'),
('110-234-567890', 3, 'W', '1', 500000, 3000000, '서초지점', '2024-02-01 11:20:00'),

-- 박민수 급여통장 거래내역
('110-345-678901', 1, 'D', '1', 8500000, 8500000, '송파지점', '2024-02-01 10:45:00'),
('110-345-678901', 2, 'W', '1', 1000000, 7500000, 'ATM', '2024-02-05 18:30:00'),
('110-345-678901', 3, 'D', '1', 1000000, 8500000, '온라인', '2024-02-10 12:15:00'),

-- 최지영 자유통장 거래내역
('110-456-789012', 1, 'D', '1', 2200000, 2200000, '마포지점', '2024-02-10 16:20:00'),
('110-456-789012', 2, 'W', '1', 300000, 1900000, 'ATM', '2024-02-12 14:45:00'),
('110-456-789012', 3, 'D', '1', 300000, 2200000, '마포지점', '2024-02-15 10:30:00'),

-- 정대한 사업자통장 거래내역
('110-567-890123', 1, 'D', '1', 12000000, 12000000, '용산지점', '2024-02-15 11:30:00'),
('110-567-890123', 2, 'W', '1', 2000000, 10000000, '온라인', '2024-02-18 15:20:00'),
('110-567-890123', 3, 'D', '1', 2000000, 12000000, '용산지점', '2024-02-20 09:45:00'),

-- 김철수 적금통장 거래내역
('110-678-901234', 1, 'D', '1', 1500000, 1500000, '강남지점', '2024-03-01 13:45:00');

-- Sample Transfer History
INSERT INTO TB_TRNF_HST (CSTM_ID, SEQ, DIV_CD, STS_CD, DPST_ACNT_NO, WTHD_ACNT_NO, WTHD_ACNT_SEQ, SND_MM, RCV_MM, RCV_CSTM_NM, TRNF_AMT, TRNF_DTM) VALUES 
('CUST001', 1, 'D', '3', '110-234-567890', '110-123-456789', 2, '생활비 송금', '생활비 입금', '이영희', 200000, '2024-01-16 14:20:00'),
('CUST002', 1, 'D', '3', '110-123-456789', '110-234-567890', 3, '대출 상환', '대출금 수령', '김철수', 500000, '2024-02-01 11:20:00'),
('CUST003', 1, 'D', '3', '110-456-789012', '110-345-678901', 2, '용돈 송금', '용돈 입금', '최지영', 300000, '2024-02-05 18:30:00'),
('CUST004', 1, 'W', '0', 'EXT-BANK-001', '110-456-789012', 2, '타행 이체', '외부 입금', 'Amazon Web Services', 300000, '2024-02-12 14:45:00'),
('CUST005', 1, 'D', '3', '110-123-456789', '110-567-890123', 2, '사업자금 지원', '사업자금 수령', '김철수', 100000, '2024-02-18 15:20:00');

-- Sample Products
INSERT INTO TB_PRODUCT (ID, NAME, DESCRIPTION, INTEREST_RATE, CURRENCY) VALUES 
('PROD001', '자유적금', '자유롭게 입출금이 가능한 적금상품', 2.5000, 'KRW'),
('PROD002', '정기예금', '일정 기간 동안 고정 금리를 제공하는 예금상품', 3.2000, 'KRW'),
('PROD003', '주택청약통장', '주택 구입을 위한 청약 전용 통장', 2.8000, 'KRW'),
('PROD004', '외화예금(USD)', '미국 달러 외화 예금상품', 1.8000, 'USD'),
('PROD005', '펀드연계예금', '펀드 수익률과 연동된 예금상품', 4.1000, 'KRW'),
('PROD006', '청년우대적금', '만 34세 이하 청년을 위한 우대 적금', 3.5000, 'KRW'),
('PROD007', '퇴직연금', '퇴직 후 안정적인 노후를 위한 연금상품', 2.9000, 'KRW'),
('PROD008', 'ISA계좌', '개인종합자산관리계좌', 3.0000, 'KRW');

-- Reset sequences to current max values
SELECT setval('SEQ_ACCOUNT_TRANSACTION_HISTORY', (SELECT COALESCE(MAX(SEQ), 0) FROM TB_TRNS_HST) + 1);
SELECT setval('SEQ_TRANSFER_HISTORY', (SELECT COALESCE(MAX(SEQ), 0) FROM TB_TRNF_HST) + 1);

-- Verify data insertion
SELECT 'Users' as table_name, COUNT(*) as count FROM TB_USER
UNION ALL
SELECT 'Customers', COUNT(*) FROM TB_CSTM
UNION ALL
SELECT 'Accounts', COUNT(*) FROM TB_ACNT
UNION ALL
SELECT 'Transactions', COUNT(*) FROM TB_TRNS_HST
UNION ALL
SELECT 'Transfers', COUNT(*) FROM TB_TRNF_HST
UNION ALL
SELECT 'Transfer Limits', COUNT(*) FROM TB_TRNF_LMT
UNION ALL
SELECT 'Products', COUNT(*) FROM TB_PRODUCT;