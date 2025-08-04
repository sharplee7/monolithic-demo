--
-- PostgreSQL database dump
--

-- Dumped from database version 16.6
-- Dumped by pg_dump version 16.9 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: seq_account_transaction_history; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_account_transaction_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_account_transaction_history OWNER TO postgres;

--
-- Name: seq_transfer_history; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_transfer_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seq_transfer_history OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: tb_acnt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_acnt (
    acnt_no character varying(20) NOT NULL,
    cstm_id character varying(20) NOT NULL,
    cstm_nm character varying(20) NOT NULL,
    acnt_nm character varying(20) NOT NULL,
    new_dtm character varying(20) NOT NULL,
    acnt_blnc bigint DEFAULT 0
);


ALTER TABLE public.tb_acnt OWNER TO postgres;

--
-- Name: TABLE tb_acnt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_acnt IS 'Account';


--
-- Name: COLUMN tb_acnt.acnt_no; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_acnt.acnt_no IS 'Account Number';


--
-- Name: COLUMN tb_acnt.cstm_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_acnt.cstm_id IS 'Customer ID';


--
-- Name: COLUMN tb_acnt.cstm_nm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_acnt.cstm_nm IS 'Customer Name';


--
-- Name: COLUMN tb_acnt.acnt_nm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_acnt.acnt_nm IS 'Account Name';


--
-- Name: COLUMN tb_acnt.new_dtm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_acnt.new_dtm IS 'Creation Date and Time';


--
-- Name: COLUMN tb_acnt.acnt_blnc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_acnt.acnt_blnc IS 'Account Balance';


--
-- Name: tb_cstm; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_cstm (
    cstm_id character varying(20) NOT NULL,
    cstm_nm character varying(100),
    cstm_age character varying(3),
    cstm_gnd character varying(1),
    cstm_pn character varying(20),
    cstm_adr character varying(1000),
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.tb_cstm OWNER TO postgres;

--
-- Name: TABLE tb_cstm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_cstm IS 'Customer';


--
-- Name: COLUMN tb_cstm.cstm_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_cstm.cstm_id IS 'Customer ID';


--
-- Name: COLUMN tb_cstm.cstm_nm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_cstm.cstm_nm IS 'Customer Name';


--
-- Name: COLUMN tb_cstm.cstm_age; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_cstm.cstm_age IS 'Age';


--
-- Name: COLUMN tb_cstm.cstm_gnd; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_cstm.cstm_gnd IS 'Gender';


--
-- Name: COLUMN tb_cstm.cstm_pn; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_cstm.cstm_pn IS 'Phone Number';


--
-- Name: COLUMN tb_cstm.cstm_adr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_cstm.cstm_adr IS 'Address';


--
-- Name: tb_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_product (
    id character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    interest_rate numeric(5,4) DEFAULT 0.0000,
    currency character varying(10) DEFAULT 'KRW'::character varying,
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.tb_product OWNER TO postgres;

--
-- Name: TABLE tb_product; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_product IS 'Product';


--
-- Name: COLUMN tb_product.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_product.id IS 'Product ID';


--
-- Name: COLUMN tb_product.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_product.name IS 'Product Name';


--
-- Name: COLUMN tb_product.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_product.description IS 'Product Description';


--
-- Name: COLUMN tb_product.interest_rate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_product.interest_rate IS 'Interest Rate';


--
-- Name: COLUMN tb_product.currency; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_product.currency IS 'Currency';


--
-- Name: tb_trnf_hst; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_trnf_hst (
    cstm_id character varying(20) NOT NULL,
    seq integer NOT NULL,
    div_cd character varying(1) NOT NULL,
    sts_cd character varying(1) NOT NULL,
    dpst_acnt_no character varying(20),
    wthd_acnt_no character varying(20),
    wthd_acnt_seq integer,
    snd_mm character varying(100),
    rcv_mm character varying(100),
    rcv_cstm_nm character varying(100),
    trnf_amt bigint DEFAULT 0,
    trnf_dtm character varying(20)
);


ALTER TABLE public.tb_trnf_hst OWNER TO postgres;

--
-- Name: TABLE tb_trnf_hst; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_trnf_hst IS 'Transfer History';


--
-- Name: COLUMN tb_trnf_hst.cstm_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.cstm_id IS 'Customer ID';


--
-- Name: COLUMN tb_trnf_hst.seq; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.seq IS 'Sequence Number';


--
-- Name: COLUMN tb_trnf_hst.div_cd; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.div_cd IS 'Division Code';


--
-- Name: COLUMN tb_trnf_hst.sts_cd; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.sts_cd IS 'Status Code';


--
-- Name: COLUMN tb_trnf_hst.dpst_acnt_no; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.dpst_acnt_no IS 'Deposit Account Number';


--
-- Name: COLUMN tb_trnf_hst.wthd_acnt_no; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.wthd_acnt_no IS 'Withdrawal Account Number';


--
-- Name: COLUMN tb_trnf_hst.wthd_acnt_seq; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.wthd_acnt_seq IS 'Withdrawal Account Sequence';


--
-- Name: COLUMN tb_trnf_hst.snd_mm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.snd_mm IS 'Sender Memo';


--
-- Name: COLUMN tb_trnf_hst.rcv_mm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.rcv_mm IS 'Receiver Memo';


--
-- Name: COLUMN tb_trnf_hst.rcv_cstm_nm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.rcv_cstm_nm IS 'Receiver Customer Name';


--
-- Name: COLUMN tb_trnf_hst.trnf_amt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.trnf_amt IS 'Transfer Amount';


--
-- Name: COLUMN tb_trnf_hst.trnf_dtm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_hst.trnf_dtm IS 'Transfer Date and Time';


--
-- Name: tb_trnf_lmt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_trnf_lmt (
    cstm_id character varying(20) NOT NULL,
    one_tm_trnf_lmt bigint DEFAULT 0,
    one_dy_trnf_lmt bigint DEFAULT 0,
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.tb_trnf_lmt OWNER TO postgres;

--
-- Name: TABLE tb_trnf_lmt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_trnf_lmt IS 'Transfer Limit';


--
-- Name: COLUMN tb_trnf_lmt.cstm_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_lmt.cstm_id IS 'Customer ID';


--
-- Name: COLUMN tb_trnf_lmt.one_tm_trnf_lmt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_lmt.one_tm_trnf_lmt IS 'One Time Transfer Limit';


--
-- Name: COLUMN tb_trnf_lmt.one_dy_trnf_lmt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trnf_lmt.one_dy_trnf_lmt IS 'One Day Transfer Limit';


--
-- Name: tb_trns_hst; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_trns_hst (
    acnt_no character varying(20) NOT NULL,
    seq integer NOT NULL,
    div_cd character varying(1) NOT NULL,
    sts_cd character varying(1) NOT NULL,
    trns_amt bigint DEFAULT 0,
    acnt_blnc bigint DEFAULT 0,
    trns_brnch character varying(20) NOT NULL,
    trns_dtm character varying(20) NOT NULL
);


ALTER TABLE public.tb_trns_hst OWNER TO postgres;

--
-- Name: TABLE tb_trns_hst; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_trns_hst IS 'Transaction History';


--
-- Name: COLUMN tb_trns_hst.acnt_no; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.acnt_no IS 'Account Number';


--
-- Name: COLUMN tb_trns_hst.seq; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.seq IS 'Sequence Number';


--
-- Name: COLUMN tb_trns_hst.div_cd; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.div_cd IS 'Division Code';


--
-- Name: COLUMN tb_trns_hst.sts_cd; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.sts_cd IS 'Status Code';


--
-- Name: COLUMN tb_trns_hst.trns_amt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.trns_amt IS 'Transaction Amount';


--
-- Name: COLUMN tb_trns_hst.acnt_blnc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.acnt_blnc IS 'Account Balance';


--
-- Name: COLUMN tb_trns_hst.trns_brnch; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.trns_brnch IS 'Transaction Branch';


--
-- Name: COLUMN tb_trns_hst.trns_dtm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_trns_hst.trns_dtm IS 'Transaction Date and Time';


--
-- Name: tb_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_user (
    user_id character varying(20) NOT NULL,
    password_hash character varying(255) NOT NULL,
    salt character varying(255) NOT NULL,
    email character varying(100),
    name character varying(100),
    status character varying(20) DEFAULT 'ACTIVE'::character varying,
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.tb_user OWNER TO postgres;

--
-- Name: TABLE tb_user; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tb_user IS 'User Authentication';


--
-- Name: COLUMN tb_user.user_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_user.user_id IS 'User ID';


--
-- Name: COLUMN tb_user.password_hash; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_user.password_hash IS 'Password Hash';


--
-- Name: COLUMN tb_user.salt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_user.salt IS 'Password Salt';


--
-- Name: COLUMN tb_user.email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_user.email IS 'Email Address';


--
-- Name: COLUMN tb_user.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_user.name IS 'User Name';


--
-- Name: COLUMN tb_user.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tb_user.status IS 'User Status';


--
-- Data for Name: tb_acnt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_acnt (acnt_no, cstm_id, cstm_nm, acnt_nm, new_dtm, acnt_blnc) FROM stdin;
110-123-456789	CUST001	김철수	김철수 주거래통장	2024-01-15 09:30:00	5000000
110-234-567890	CUST002	이영희	이영희 적금통장	2024-01-20 14:15:00	3000000
110-345-678901	CUST003	박민수	박민수 급여통장	2024-02-01 10:45:00	8500000
110-456-789012	CUST004	최지영	최지영 자유통장	2024-02-10 16:20:00	2200000
110-567-890123	CUST005	정대한	정대한 사업자통장	2024-02-15 11:30:00	12000000
110-678-901234	CUST001	김철수	김철수 적금통장	2024-03-01 13:45:00	1500000
\.


--
-- Data for Name: tb_cstm; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_cstm (cstm_id, cstm_nm, cstm_age, cstm_gnd, cstm_pn, cstm_adr, created_date, updated_date) FROM stdin;
CUST001	김철수	35	M	010-1234-5678	서울시 강남구 테헤란로 123	2025-07-10 10:41:18.627197	2025-07-10 10:41:18.627197
CUST002	이영희	28	F	010-2345-6789	서울시 서초구 서초대로 456	2025-07-10 10:41:18.627197	2025-07-10 10:41:18.627197
CUST003	박민수	42	M	010-3456-7890	서울시 송파구 올림픽로 789	2025-07-10 10:41:18.627197	2025-07-10 10:41:18.627197
CUST004	최지영	31	F	010-4567-8901	서울시 마포구 월드컵로 321	2025-07-10 10:41:18.627197	2025-07-10 10:41:18.627197
CUST005	정대한	39	M	010-5678-9012	서울시 용산구 한강대로 654	2025-07-10 10:41:18.627197	2025-07-10 10:41:18.627197
\.


--
-- Data for Name: tb_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_product (id, name, description, interest_rate, currency, created_date, updated_date) FROM stdin;
PROD001	자유적금	자유롭게 입출금이 가능한 적금상품	2.5000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD002	정기예금	일정 기간 동안 고정 금리를 제공하는 예금상품	3.2000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD003	주택청약통장	주택 구입을 위한 청약 전용 통장	2.8000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD004	외화예금(USD)	미국 달러 외화 예금상품	1.8000	USD	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD005	펀드연계예금	펀드 수익률과 연동된 예금상품	4.1000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD006	청년우대적금	만 34세 이하 청년을 위한 우대 적금	3.5000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD007	퇴직연금	퇴직 후 안정적인 노후를 위한 연금상품	2.9000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
PROD008	ISA계좌	개인종합자산관리계좌	3.0000	KRW	2025-07-10 10:41:18.633179	2025-07-10 10:41:18.633179
\.


--
-- Data for Name: tb_trnf_hst; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_trnf_hst (cstm_id, seq, div_cd, sts_cd, dpst_acnt_no, wthd_acnt_no, wthd_acnt_seq, snd_mm, rcv_mm, rcv_cstm_nm, trnf_amt, trnf_dtm) FROM stdin;
CUST001	1	D	3	110-234-567890	110-123-456789	2	생활비 송금	생활비 입금	이영희	200000	2024-01-16 14:20:00
CUST002	1	D	3	110-123-456789	110-234-567890	3	대출 상환	대출금 수령	김철수	500000	2024-02-01 11:20:00
CUST003	1	D	3	110-456-789012	110-345-678901	2	용돈 송금	용돈 입금	최지영	300000	2024-02-05 18:30:00
CUST004	1	W	0	EXT-BANK-001	110-456-789012	2	타행 이체	외부 입금	Amazon Web Services	300000	2024-02-12 14:45:00
CUST005	1	D	3	110-123-456789	110-567-890123	2	사업자금 지원	사업자금 수령	김철수	100000	2024-02-18 15:20:00
\.


--
-- Data for Name: tb_trnf_lmt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_trnf_lmt (cstm_id, one_tm_trnf_lmt, one_dy_trnf_lmt, created_date, updated_date) FROM stdin;
CUST001	10000000	50000000	2025-07-10 10:41:18.627905	2025-07-10 10:41:18.627905
CUST002	5000000	30000000	2025-07-10 10:41:18.627905	2025-07-10 10:41:18.627905
CUST003	20000000	100000000	2025-07-10 10:41:18.627905	2025-07-10 10:41:18.627905
CUST004	3000000	20000000	2025-07-10 10:41:18.627905	2025-07-10 10:41:18.627905
CUST005	15000000	80000000	2025-07-10 10:41:18.627905	2025-07-10 10:41:18.627905
\.


--
-- Data for Name: tb_trns_hst; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_trns_hst (acnt_no, seq, div_cd, sts_cd, trns_amt, acnt_blnc, trns_brnch, trns_dtm) FROM stdin;
110-123-456789	1	D	1	5000000	5000000	강남지점	2024-01-15 09:30:00
110-123-456789	2	W	1	200000	4800000	강남지점	2024-01-16 14:20:00
110-123-456789	3	D	1	300000	5100000	ATM	2024-01-18 10:15:00
110-123-456789	4	W	1	100000	5000000	온라인	2024-01-20 16:30:00
110-234-567890	1	D	1	3000000	3000000	서초지점	2024-01-20 14:15:00
110-234-567890	2	D	1	500000	3500000	ATM	2024-01-25 09:45:00
110-234-567890	3	W	1	500000	3000000	서초지점	2024-02-01 11:20:00
110-345-678901	1	D	1	8500000	8500000	송파지점	2024-02-01 10:45:00
110-345-678901	2	W	1	1000000	7500000	ATM	2024-02-05 18:30:00
110-345-678901	3	D	1	1000000	8500000	온라인	2024-02-10 12:15:00
110-456-789012	1	D	1	2200000	2200000	마포지점	2024-02-10 16:20:00
110-456-789012	2	W	1	300000	1900000	ATM	2024-02-12 14:45:00
110-456-789012	3	D	1	300000	2200000	마포지점	2024-02-15 10:30:00
110-567-890123	1	D	1	12000000	12000000	용산지점	2024-02-15 11:30:00
110-567-890123	2	W	1	2000000	10000000	온라인	2024-02-18 15:20:00
110-567-890123	3	D	1	2000000	12000000	용산지점	2024-02-20 09:45:00
110-678-901234	1	D	1	1500000	1500000	강남지점	2024-03-01 13:45:00
\.


--
-- Data for Name: tb_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tb_user (user_id, password_hash, salt, email, name, status, created_date, updated_date) FROM stdin;
user001	$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXDALKIbYFF6lrn/y6pL5KeIKSu	salt001	user001@example.com	John Doe	ACTIVE	2025-07-10 10:41:18.615831	2025-07-10 10:41:18.615831
user002	$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXDALKIbYFF6lrn/y6pL5KeIKSu	salt002	user002@example.com	Jane Smith	ACTIVE	2025-07-10 10:41:18.615831	2025-07-10 10:41:18.615831
user003	$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXDALKIbYFF6lrn/y6pL5KeIKSu	salt003	user003@example.com	Bob Johnson	ACTIVE	2025-07-10 10:41:18.615831	2025-07-10 10:41:18.615831
\.


--
-- Name: seq_account_transaction_history; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_account_transaction_history', 5, true);


--
-- Name: seq_transfer_history; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_transfer_history', 2, true);


--
-- Name: tb_acnt tb_acnt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_acnt
    ADD CONSTRAINT tb_acnt_pkey PRIMARY KEY (acnt_no);


--
-- Name: tb_cstm tb_cstm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_cstm
    ADD CONSTRAINT tb_cstm_pkey PRIMARY KEY (cstm_id);


--
-- Name: tb_product tb_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_product
    ADD CONSTRAINT tb_product_pkey PRIMARY KEY (id);


--
-- Name: tb_trnf_hst tb_trnf_hst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_trnf_hst
    ADD CONSTRAINT tb_trnf_hst_pkey PRIMARY KEY (cstm_id, seq);


--
-- Name: tb_trnf_lmt tb_trnf_lmt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_trnf_lmt
    ADD CONSTRAINT tb_trnf_lmt_pkey PRIMARY KEY (cstm_id);


--
-- Name: tb_trns_hst tb_trns_hst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_trns_hst
    ADD CONSTRAINT tb_trns_hst_pkey PRIMARY KEY (acnt_no, seq);


--
-- Name: tb_user tb_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_user
    ADD CONSTRAINT tb_user_pkey PRIMARY KEY (user_id);


--
-- Name: idx_tb_acnt_cstm_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tb_acnt_cstm_id ON public.tb_acnt USING btree (cstm_id);


--
-- Name: idx_tb_trnf_hst_cstm_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tb_trnf_hst_cstm_id ON public.tb_trnf_hst USING btree (cstm_id);


--
-- Name: idx_tb_trnf_hst_trnf_dtm; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tb_trnf_hst_trnf_dtm ON public.tb_trnf_hst USING btree (trnf_dtm);


--
-- Name: idx_tb_trns_hst_acnt_no; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tb_trns_hst_acnt_no ON public.tb_trns_hst USING btree (acnt_no);


--
-- Name: idx_tb_trns_hst_trns_dtm; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tb_trns_hst_trns_dtm ON public.tb_trns_hst USING btree (trns_dtm);


--
-- Name: tb_acnt tb_acnt_cstm_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_acnt
    ADD CONSTRAINT tb_acnt_cstm_id_fkey FOREIGN KEY (cstm_id) REFERENCES public.tb_cstm(cstm_id);


--
-- Name: tb_trnf_hst tb_trnf_hst_cstm_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_trnf_hst
    ADD CONSTRAINT tb_trnf_hst_cstm_id_fkey FOREIGN KEY (cstm_id) REFERENCES public.tb_cstm(cstm_id);


--
-- Name: tb_trnf_lmt tb_trnf_lmt_cstm_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_trnf_lmt
    ADD CONSTRAINT tb_trnf_lmt_cstm_id_fkey FOREIGN KEY (cstm_id) REFERENCES public.tb_cstm(cstm_id);


--
-- Name: tb_trns_hst tb_trns_hst_acnt_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_trns_hst
    ADD CONSTRAINT tb_trns_hst_acnt_no_fkey FOREIGN KEY (acnt_no) REFERENCES public.tb_acnt(acnt_no);


--
-- PostgreSQL database dump complete
--

