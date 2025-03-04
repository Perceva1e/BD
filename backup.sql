--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: bookings_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bookings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bookings_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Bookings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Bookings" (
    id integer DEFAULT nextval('public.bookings_id_seq'::regclass) NOT NULL,
    check_in_date date NOT NULL,
    check_out_date date NOT NULL,
    total_cost integer NOT NULL,
    status text NOT NULL,
    client_id integer NOT NULL,
    employee_id integer NOT NULL
);


ALTER TABLE public."Bookings" OWNER TO postgres;

--
-- Name: Bookings_Rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Bookings_Rooms" (
    booking_id integer NOT NULL,
    room_id integer NOT NULL
);


ALTER TABLE public."Bookings_Rooms" OWNER TO postgres;

--
-- Name: Bookings_Services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Bookings_Services" (
    booking_id integer NOT NULL,
    service_id integer NOT NULL
);


ALTER TABLE public."Bookings_Services" OWNER TO postgres;

--
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clients_id_seq OWNER TO postgres;

--
-- Name: Clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Clients" (
    id integer DEFAULT nextval('public.clients_id_seq'::regclass) NOT NULL,
    full_name text NOT NULL,
    phone character varying NOT NULL,
    passport_number character varying NOT NULL,
    birth_date date NOT NULL
);


ALTER TABLE public."Clients" OWNER TO postgres;

--
-- Name: employees_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.employees_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.employees_id_seq OWNER TO postgres;

--
-- Name: Employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Employees" (
    id integer DEFAULT nextval('public.employees_id_seq'::regclass) NOT NULL,
    full_name text NOT NULL,
    "position" text NOT NULL,
    salary integer NOT NULL,
    work_schedule date[] NOT NULL
);


ALTER TABLE public."Employees" OWNER TO postgres;

--
-- Name: payments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payments_id_seq OWNER TO postgres;

--
-- Name: Payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Payments" (
    id integer DEFAULT nextval('public.payments_id_seq'::regclass) NOT NULL,
    payment_type text NOT NULL,
    payment_date date NOT NULL,
    payment_method text NOT NULL,
    status text NOT NULL,
    booking_id integer NOT NULL
);


ALTER TABLE public."Payments" OWNER TO postgres;

--
-- Name: room_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.room_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_types_id_seq OWNER TO postgres;

--
-- Name: Room_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Room_types" (
    id integer DEFAULT nextval('public.room_types_id_seq'::regclass) NOT NULL,
    name text NOT NULL,
    comfort_level text NOT NULL,
    category text NOT NULL,
    cost_per_night integer NOT NULL
);


ALTER TABLE public."Room_types" OWNER TO postgres;

--
-- Name: rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rooms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rooms_id_seq OWNER TO postgres;

--
-- Name: Rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Rooms" (
    id integer DEFAULT nextval('public.rooms_id_seq'::regclass) NOT NULL,
    area integer NOT NULL,
    cost integer NOT NULL,
    max_guests integer NOT NULL,
    amenities text NOT NULL,
    room_type_id integer NOT NULL
);


ALTER TABLE public."Rooms" OWNER TO postgres;

--
-- Name: services_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.services_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.services_id_seq OWNER TO postgres;

--
-- Name: Services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Services" (
    id integer DEFAULT nextval('public.services_id_seq'::regclass) NOT NULL,
    name text NOT NULL,
    category text NOT NULL,
    cost integer NOT NULL,
    duration interval NOT NULL
);


ALTER TABLE public."Services" OWNER TO postgres;

--
-- Data for Name: Bookings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Bookings" (id, check_in_date, check_out_date, total_cost, status, client_id, employee_id) FROM stdin;
1	2023-01-01	2023-01-05	5000	Not Booked	1	2
2	2023-01-02	2023-01-06	6000	Booked	2	3
3	2023-01-03	2023-01-07	7000	Not Booked	3	4
4	2023-01-04	2023-01-08	8000	Booked	4	5
5	2023-01-05	2023-01-09	9000	Not Booked	5	6
6	2023-01-06	2023-01-10	10000	Booked	6	7
7	2023-01-07	2023-01-11	11000	Not Booked	7	8
8	2023-01-08	2023-01-12	12000	Booked	8	9
9	2023-01-09	2023-01-13	13000	Not Booked	9	10
10	2023-01-10	2023-01-14	14000	Booked	10	1
11	2023-01-11	2023-01-15	15000	Not Booked	11	2
12	2023-01-12	2023-01-16	16000	Booked	12	3
13	2023-01-13	2023-01-17	17000	Not Booked	13	4
14	2023-01-14	2023-01-18	18000	Booked	14	5
15	2023-01-15	2023-01-19	19000	Not Booked	15	6
16	2023-01-16	2023-01-20	20000	Booked	16	7
17	2023-01-17	2023-01-21	21000	Not Booked	17	8
18	2023-01-18	2023-01-22	22000	Booked	18	9
19	2023-01-19	2023-01-23	23000	Not Booked	19	10
20	2023-01-20	2023-01-24	24000	Booked	20	1
24	2000-09-09	2000-10-01	12	ывф	23	5
25	2025-03-04	2025-03-05	5	CONFIRMED	23	4
26	2025-10-10	2025-10-11	40000	PENDING	25	9
\.


--
-- Data for Name: Bookings_Rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Bookings_Rooms" (booking_id, room_id) FROM stdin;
1	1
1	2
2	3
2	4
3	5
3	6
4	7
4	8
5	9
5	10
6	11
6	12
7	13
7	14
8	15
8	16
9	17
9	18
10	19
10	20
\.


--
-- Data for Name: Bookings_Services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Bookings_Services" (booking_id, service_id) FROM stdin;
1	1
1	2
2	3
2	4
3	5
3	6
4	7
4	8
5	9
5	10
6	11
6	12
7	13
7	14
8	15
8	16
9	17
9	18
10	19
10	20
\.


--
-- Data for Name: Clients; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Clients" (id, full_name, phone, passport_number, birth_date) FROM stdin;
1	Ivanov Ivan	+3759161234567	1234567890	1990-01-01
2	Petrov Petr	+3759162345678	1234567891	1985-02-02
3	Sidorov Sidor	+3759163456789	1234567892	1980-03-03
4	Kuznetsov Alexey	+3759164567890	1234567893	1995-04-04
5	Morozova Anna	+3759165678901	1234567894	1992-05-05
6	Smirnova Olga	+3759166789012	1234567895	1988-06-06
7	Popov Sergey	+3759167890123	1234567896	1983-07-07
8	Vasiliev Konstantin	+3759168901234	1234567897	1993-08-08
9	Fedorov Igor	+3759169012345	1234567898	1986-09-09
10	Dmitrieva Natalia	+3759160123456	1234567899	1979-10-10
11	Kovalenko Pavel	+3759161234567	1234567800	1991-11-11
12	Lebedeva Svetlana	+3759162345678	1234567801	1994-12-12
13	Novikov Artem	+3759163456789	1234567802	1987-01-13
14	Borisova Ekaterina	+3759164567890	1234567803	1999-02-14
15	Zaitseva Irina	+3759165678901	1234567804	1982-03-15
16	Soloviev Vladimir	+3759166789012	1234567805	1984-04-16
17	Belov Denis	+3759167890123	1234567806	1989-05-17
18	Grigorieva Maria	+3759168901234	1234567807	1996-06-18
19	Romanov Roman	+3759169012345	1234567808	1997-07-19
23	Denis Shagun	+375295297796	1234567890	2004-10-08
20	Klimova Anna	+3759160123456	1234567809	1981-08-20
24	Shagun Denis	+375295297796	1234567890	2000-10-10
25	Kupriaynova Diana	+375291234567	0987654321	2000-10-10
\.


--
-- Data for Name: Employees; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Employees" (id, full_name, "position", salary, work_schedule) FROM stdin;
25	Baranova Kate	cleaner	2000	{2025-09-09,2025-11-11,2025-12-12}
1	Sidorov Alexey	Manager	50000	{2023-01-01,2023-01-02}
2	Petrova Maria	Receptionist	40000	{2023-01-01,2023-01-02}
3	Ivanov Sergey	Cleaner	30000	{2023-01-01,2023-01-02}
4	Kuznetsova Anna	Manager	55000	{2023-01-01,2023-01-02}
5	Smirnov Igor	Client Manager	45000	{2023-01-01,2023-01-02}
6	Popova Olga	Housekeeper	60000	{2023-01-01,2023-01-02}
7	Vasiliev Alexey	Accountant	70000	{2023-01-01,2023-01-02}
8	Fedorova Svetlana	Administrator	80000	{2023-01-01,2023-01-02}
9	Dmitriev Nikolay	Chef	40000	{2023-01-01,2023-01-02}
10	Lebedev Roman	Sales Specialist	55000	{2023-01-01,2023-01-02}
11	Kovalenko Irina	Secretary	35000	{2023-01-01,2023-01-02}
12	Zaitseva Natalia	Tour Guide	45000	{2023-01-01,2023-01-02}
13	Solovyov Pavel	Cashier	50000	{2023-01-01,2023-01-02}
14	Belov Konstantin	Client Manager	60000	{2023-01-01,2023-01-02}
15	Grigorieva Elena	Housekeeper	70000	{2023-01-01,2023-01-02}
16	Romanov Dmitry	Sales Specialist	45000	{2023-01-01,2023-01-02}
17	Klimova Yulia	Manager	50000	{2023-01-01,2023-01-02}
18	Sidorova Anastasia	Receptionist	40000	{2023-01-01,2023-01-02}
19	Pavlov Ilya	Cleaner	30000	{2023-01-01,2023-01-02}
20	Sergeev Artem	Manager	55000	{2023-01-01,2023-01-02}
21	denis	qwe	1234	{2000-10-10}
24	Shagun Denis	Doctoter	5	{2020-10-10,2020-09-09}
23	у	у	12	{2025-03-02,2025-03-03}
\.


--
-- Data for Name: Payments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Payments" (id, payment_type, payment_date, payment_method, status, booking_id) FROM stdin;
2	Credit Card	2025-01-02	Online	Paid	2
3	Cash	2025-01-03	Reception	Unpaid	3
4	Credit Card	2025-01-04	Online	Paid	4
5	Cash	2025-01-05	Reception	Paid	5
6	Credit Card	2025-01-06	Online	Unpaid	6
7	Cash	2025-01-07	Reception	Paid	7
8	Credit Card	2025-01-08	Online	Paid	8
9	Cash	2025-01-09	Reception	Unpaid	9
10	Credit Card	2025-01-10	Online	Paid	10
11	Cash	2025-01-11	Reception	Paid	11
12	Credit Card	2025-01-12	Online	Unpaid	12
13	Cash	2025-01-13	Reception	Paid	13
14	Credit Card	2025-01-14	Online	Paid	14
15	Cash	2025-01-15	Reception	Unpaid	15
16	Credit Card	2025-01-16	Online	Paid	16
17	Cash	2025-01-17	Reception	Paid	17
18	Credit Card	2025-01-18	Online	Unpaid	18
19	Cash	2025-01-19	Reception	Paid	19
20	Credit Card	2025-01-20	Online	Paid	20
1	Cash	2025-02-02	Reception	Paid	1
25	Cash	2000-09-09	ATM	Completed	24
\.


--
-- Data for Name: Room_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Room_types" (id, name, comfort_level, category, cost_per_night) FROM stdin;
1	Standard	Low	Single	1000
2	Comfort	Medium	Double	1500
3	Luxury	High	Quadruple	2500
4	Presidential	Very High	Triple	5000
5	Economy	Low	Single	800
6	Duplex	Medium	Duplex	3000
7	Super Comfort	High	Double	2000
8	Family	Medium	Quadruple	1800
9	Classic	Low	Single	1200
10	Modern	Medium	Double	1600
11	Romantic	High	Triple	2800
12	Sport	Medium	Double	1400
13	Cabinet	Low	Single	900
14	Designer	High	Double	2200
15	Villa	Very High	Quadruple	6000
16	Mini-Luxury	Medium	Double	2500
17	Cozy	Low	Single	950
18	Marine	High	Triple	3500
19	Mountain	Medium	Quadruple	2300
20	Luxury	Low	Double	1100
\.


--
-- Data for Name: Rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Rooms" (id, area, cost, max_guests, amenities, room_type_id) FROM stdin;
1	25	1000	1	WiFi,TV	1
2	30	1500	2	WiFi,TV,Fridge	2
3	40	2500	4	WiFi,AC,Minibar	3
4	35	5000	3	WiFi,Jacuzzi	4
5	20	800	1	WiFi	5
6	45	3000	5	WiFi,Kitchen	6
7	60	2000	3	WiFi,Balcony	7
8	55	1800	2	WiFi,"Pool View"	8
9	25	1200	1	WiFi,TV	9
10	30	1600	2	WiFi,Fridge	10
11	40	2100	4	WiFi,AC,TV	11
12	35	1900	3	WiFi,Minibar	12
13	50	2600	5	WiFi,"Pool View",AC	13
14	45	2400	4	WiFi,Jacuzzi	14
15	60	3200	6	WiFi,Kitchen,TV	15
16	55	3000	5	WiFi,Balcony	16
17	30	1300	3	WiFi,Minibar	17
18	25	1200	2	WiFi,AC	18
19	40	2200	4	WiFi,TV	19
20	35	2000	3	WiFi,Kitchen	20
\.


--
-- Data for Name: Services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Services" (id, name, category, cost, duration) FROM stdin;
1	Room Cleaning	Service	500	01:00:00
2	Breakfast	Food	300	02:00:00
3	Transfer	Transport	1500	00:30:00
4	Massage	Health	2000	01:00:00
5	Excursion	Entertainment	2500	03:00:00
6	Food Delivery	Food	700	01:00:00
7	Bicycle Rental	Entertainment	1000	02:00:00
8	Shoe Cleaning	Service	300	00:30:00
9	Laundry Service	Service	800	1 day
10	Fitness Gym	Sport	1000	01:00:00
11	Couples Massage	Health	3500	01:00:00
12	Movie Night	Entertainment	1200	02:00:00
13	Festive Dinner	Food	5000	03:00:00
14	Cooking Masterclass	Entertainment	3000	02:00:00
15	Sports Events	Entertainment	1500	03:00:00
16	Birthday Celebration	Entertainment	8000	1 day
17	Hiking	Entertainment	2000	04:00:00
18	Spa Treatments	Health	4000	02:00:00
19	City Guide	Entertainment	1500	01:00:00
20	Photo Session	Entertainment	2500	01:00:00
21	zxc	adsads	45	02:00:00
23	Spa	rest	5	1 day
\.


--
-- Name: bookings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bookings_id_seq', 26, true);


--
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.clients_id_seq', 25, true);


--
-- Name: employees_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.employees_id_seq', 25, true);


--
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payments_id_seq', 25, true);


--
-- Name: room_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_types_id_seq', 23, true);


--
-- Name: rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rooms_id_seq', 24, true);


--
-- Name: services_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.services_id_seq', 23, true);


--
-- Name: Bookings_Rooms Bookings_Rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings_Rooms"
    ADD CONSTRAINT "Bookings_Rooms_pkey" PRIMARY KEY (booking_id, room_id);


--
-- Name: Bookings_Services Bookings_Services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings_Services"
    ADD CONSTRAINT "Bookings_Services_pkey" PRIMARY KEY (booking_id, service_id);


--
-- Name: Bookings Bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings"
    ADD CONSTRAINT "Bookings_pkey" PRIMARY KEY (id);


--
-- Name: Clients Clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Clients"
    ADD CONSTRAINT "Clients_pkey" PRIMARY KEY (id);


--
-- Name: Employees Employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employees"
    ADD CONSTRAINT "Employees_pkey" PRIMARY KEY (id);


--
-- Name: Payments Payments_booking_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Payments"
    ADD CONSTRAINT "Payments_booking_id_key" UNIQUE (booking_id);


--
-- Name: Payments Payments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Payments"
    ADD CONSTRAINT "Payments_pkey" PRIMARY KEY (id);


--
-- Name: Room_types Room_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Room_types"
    ADD CONSTRAINT "Room_types_pkey" PRIMARY KEY (id);


--
-- Name: Rooms Rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Rooms"
    ADD CONSTRAINT "Rooms_pkey" PRIMARY KEY (id);


--
-- Name: Rooms Rooms_room_type_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Rooms"
    ADD CONSTRAINT "Rooms_room_type_id_key" UNIQUE (room_type_id);


--
-- Name: Services Services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Services"
    ADD CONSTRAINT "Services_pkey" PRIMARY KEY (id);


--
-- Name: Bookings_Rooms Bookings_Rooms_booking_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings_Rooms"
    ADD CONSTRAINT "Bookings_Rooms_booking_id_fkey" FOREIGN KEY (booking_id) REFERENCES public."Bookings"(id);


--
-- Name: Bookings_Rooms Bookings_Rooms_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings_Rooms"
    ADD CONSTRAINT "Bookings_Rooms_room_id_fkey" FOREIGN KEY (room_id) REFERENCES public."Rooms"(id);


--
-- Name: Bookings_Services Bookings_Services_booking_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings_Services"
    ADD CONSTRAINT "Bookings_Services_booking_id_fkey" FOREIGN KEY (booking_id) REFERENCES public."Bookings"(id);


--
-- Name: Bookings_Services Bookings_Services_service_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings_Services"
    ADD CONSTRAINT "Bookings_Services_service_id_fkey" FOREIGN KEY (service_id) REFERENCES public."Services"(id);


--
-- Name: Bookings Bookings_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings"
    ADD CONSTRAINT "Bookings_client_id_fkey" FOREIGN KEY (client_id) REFERENCES public."Clients"(id);


--
-- Name: Bookings Bookings_employee_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Bookings"
    ADD CONSTRAINT "Bookings_employee_id_fkey" FOREIGN KEY (employee_id) REFERENCES public."Employees"(id);


--
-- Name: Payments Payments_booking_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Payments"
    ADD CONSTRAINT "Payments_booking_id_fkey" FOREIGN KEY (booking_id) REFERENCES public."Bookings"(id);


--
-- Name: Rooms Rooms_room_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Rooms"
    ADD CONSTRAINT "Rooms_room_type_id_fkey" FOREIGN KEY (room_type_id) REFERENCES public."Room_types"(id);


--
-- PostgreSQL database dump complete
--

