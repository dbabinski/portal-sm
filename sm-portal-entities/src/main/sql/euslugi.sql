DO $$
BEGIN
	-------------------------
	--SERWIS
	-------------------------
        CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

	CREATE SCHEMA IF NOT EXISTS serwis AUTHORIZATION softmedica;	

	CREATE OR REPLACE FUNCTION serwis.czy_schemat_istnieje(
		param_nazwa_schematu text)
	RETURNS boolean AS
	$czy_schemat_istnieje$
	DECLARE
	
	BEGIN	    
		IF EXISTS (SELECT * FROM pg_catalog.pg_namespace WHERE nspname = param_nazwa_schematu) THEN
			RETURN TRUE;
		END IF;
		RETURN FALSE;
	END;
	$czy_schemat_istnieje$
	LANGUAGE plpgsql;

	CREATE OR REPLACE FUNCTION serwis.czy_tabela_istnieje(
		param_nazwa_tabeli text,
		param_nazwa_schematu text)
	RETURNS boolean AS
	$czy_tabela_istnieje$
	DECLARE
		var_wynik 	boolean := false;	
	BEGIN	    
		SELECT true INTO var_wynik FROM pg_tables WHERE tablename = param_nazwa_tabeli AND schemaname = param_nazwa_schematu;
		RETURN coalesce(var_wynik, false);
	END;
	$czy_tabela_istnieje$
	LANGUAGE plpgsql;

	CREATE OR REPLACE FUNCTION serwis.czy_kolumna_istnieje(
		param_nazwa_tabeli text,
		param_nazwa_kolumn text)
	RETURNS boolean AS
	$czy_kolumna_istnieje$
	DECLARE
		var_index       integer;
		var_schemat     text;
		var_tabela      text;
		var_sqltable 	text;
		var_sqlcolumn 	text;
		var_licznik 	integer := 0;	
	BEGIN	
		var_schemat := null;
		var_tabela := null;
		SELECT position('.' in param_nazwa_tabeli) INTO var_index;
		IF var_index > 0 THEN
			SELECT substring(param_nazwa_tabeli for (var_index-1)) INTO var_schemat;
			SELECT substring(param_nazwa_tabeli from (var_index+1)) INTO var_tabela;
		ELSE 
			var_tabela := param_nazwa_tabeli;
		END IF;
		
		var_sqltable := E'
			SELECT 	count(*)
			FROM 	INFORMATION_SCHEMA.tables t
			WHERE	t.table_name = \'' || var_tabela || E'\'';
		IF var_schemat IS NOT NULL THEN
			var_sqltable := var_sqltable || E' AND t.table_schema = \'' || var_schemat || E'\'';
		END IF;

		var_sqlcolumn := E'
			SELECT 	count(*)
			FROM 	INFORMATION_SCHEMA.columns c
			WHERE	c.table_name = \'' || var_tabela || E'\'
				AND c.column_name = \'' || param_nazwa_kolumn || E'\'';
		IF var_schemat IS NOT NULL THEN
			var_sqlcolumn := var_sqlcolumn || E' AND c.table_schema = \'' || var_schemat || E'\'';
		END IF;
		
		EXECUTE var_sqltable INTO var_licznik;	
		IF var_licznik > 0 THEN
			EXECUTE var_sqlcolumn INTO var_licznik;
			IF var_licznik > 0 THEN
				RAISE INFO 'W tabeli % istnieje kolumna %.', param_nazwa_tabeli, param_nazwa_kolumn;
				RETURN TRUE;		
			END IF;
		ELSE
			RAISE EXCEPTION 'Brak tabeli %.', param_nazwa_tabeli;
		END IF;	

		RAISE INFO 'W tabeli % brak kolumny %.', param_nazwa_tabeli, param_nazwa_kolumn;
		RETURN FALSE;
	END;
	$czy_kolumna_istnieje$
	LANGUAGE plpgsql;

	CREATE OR REPLACE FUNCTION serwis.czy_kolumna_istnieje(
		param_nazwa_tabeli text,
		param_nazwa_kolumn text,
		param_typ_kolumny text)
	RETURNS boolean AS
	$czy_kolumna_istnieje$
	DECLARE
		var_index       integer;
		var_schemat     text;
		var_tabela      text;
		var_sqltable 	text;
		var_sqlcolumn 	text;
		var_licznik 	integer := 0;	
	BEGIN	
		var_schemat := null;
		var_tabela := null;
		SELECT position('.' in param_nazwa_tabeli) INTO var_index;
		IF var_index > 0 THEN
			SELECT substring(param_nazwa_tabeli for (var_index-1)) INTO var_schemat;
			SELECT substring(param_nazwa_tabeli from (var_index+1)) INTO var_tabela;
		ELSE 
			var_tabela := param_nazwa_tabeli;
		END IF;
		
		var_sqltable := E'
			SELECT 	count(*)
			FROM 	INFORMATION_SCHEMA.tables t
			WHERE	t.table_name = \'' || param_nazwa_tabeli || E'\'';
		IF var_schemat IS NOT NULL THEN
			var_sqltable := var_sqltable || E' AND t.table_schema = \'' || var_schemat || E'\'';
		END IF;

		var_sqlcolumn := E'
			SELECT 	count(*)
			FROM 	INFORMATION_SCHEMA.columns c
			WHERE	c.table_name = \'' || param_nazwa_tabeli || E'\'
				AND c.column_name = \'' || param_nazwa_kolumn || E'\'
				AND c.data_type = \'' || param_typ_kolumny || E'\'';
		IF var_schemat IS NOT NULL THEN
			var_sqlcolumn := var_sqlcolumn || E' AND c.table_schema = \'' || var_schemat || E'\'';
		END IF;
		
		EXECUTE var_sqltable INTO var_licznik;	
		IF var_licznik > 0 THEN
			EXECUTE var_sqlcolumn INTO var_licznik;
			IF var_licznik > 0 THEN
			RAISE INFO 'W tabeli % istnieje kolumna %.', param_nazwa_tabeli, param_nazwa_kolumn;
			RETURN TRUE;		
			END IF;
		ELSE
			RAISE EXCEPTION 'Brak tabeli %.', param_nazwa_tabeli;
		END IF;	

		RAISE INFO 'W tabeli % brak kolumny %.', param_nazwa_tabeli, param_nazwa_kolumn;
		RETURN FALSE;
	END;
	$czy_kolumna_istnieje$
	LANGUAGE plpgsql;

	CREATE OR REPLACE FUNCTION serwis.dodaj_kolumne(
		param_nazwa_tabeli text,
		param_nazwa_kolumny text,
		param_typ_kolumny text)
	RETURNS void AS
	$dodaj_kolumne$
	BEGIN
	EXECUTE serwis.dodaj_kolumne(param_nazwa_tabeli, param_nazwa_kolumny, param_typ_kolumny, NULL);
	RETURN;
	END;
	$dodaj_kolumne$
	LANGUAGE plpgsql;

	CREATE OR REPLACE FUNCTION serwis.dodaj_kolumne(
		param_nazwa_tabeli text,
		param_nazwa_kolumny text,
		param_typ_kolumny text,
		param_komentarz text)
	RETURNS void AS
	$dodaj_kolumne$
	BEGIN
	IF NOT serwis.czy_kolumna_istnieje(param_nazwa_tabeli, param_nazwa_kolumny) THEN	
		EXECUTE 'ALTER TABLE ' || param_nazwa_tabeli || ' ADD COLUMN ' || param_nazwa_kolumny || ' ' || param_typ_kolumny;
		RAISE INFO 'Dodano kolumnę % w tabeli %', param_nazwa_kolumny, param_nazwa_tabeli;	
	END IF;
	IF 
		param_komentarz IS NOT NULL 
	THEN
		EXECUTE 'COMMENT ON COLUMN ' || param_nazwa_tabeli || '.' || param_nazwa_kolumny || ' IS ' || quote_literal(param_komentarz);
	END IF;
	RETURN;
	END;
	$dodaj_kolumne$
	LANGUAGE plpgsql;

	CREATE OR REPLACE FUNCTION serwis.dodaj_kolumne(
		param_nazwa_tabeli text,
		param_nazwa_kolumny text,
		param_typ_kolumny text,
		param_komentarz text,
		param_referencje_klucza_obcego text)
	RETURNS void AS
	$dodaj_kolumne$
	BEGIN
		IF NOT serwis.czy_kolumna_istnieje(param_nazwa_tabeli, param_nazwa_kolumny) THEN	
			EXECUTE 'ALTER TABLE ' || param_nazwa_tabeli || ' ADD COLUMN ' || param_nazwa_kolumny || ' ' || param_typ_kolumny;
			RAISE INFO 'Dodano kolumnę % w tabeli %', param_nazwa_kolumny, param_nazwa_tabeli;

			IF param_referencje_klucza_obcego IS NOT NULL THEN		
				EXECUTE 'ALTER TABLE ' || param_nazwa_tabeli || ' ADD FOREIGN KEY (' || param_nazwa_kolumny || ') ' || param_referencje_klucza_obcego;
				RAISE INFO 'Dodano klucz obcy dla kolumny % w tabeli %', param_nazwa_kolumny, param_nazwa_tabeli;
			END IF;		
		END IF;
		
		IF param_komentarz IS NOT NULL THEN
			EXECUTE 'COMMENT ON COLUMN ' || param_nazwa_tabeli || '.' || param_nazwa_kolumny || ' IS ' || quote_literal(param_komentarz);
		END IF;		
		RETURN;
	END;
	$dodaj_kolumne$
	LANGUAGE plpgsql;

        -------------------------
	-- DROP KONTRAHENCI , PRACOWNICY
	-------------------------
        IF serwis.czy_tabela_istnieje('kontrahenci', 'public') = TRUE 
        THEN 
            DROP TABLE public.kontrahenci CASCADE;
        END IF;

        IF serwis.czy_tabela_istnieje('pracownicy', 'public') = TRUE 
        THEN 
            DROP TABLE public.pracownicy CASCADE;
        END IF;



        -------------------------
	--PUBLIC
	-------------------------

	-------------------------
	--PUBLIC - PACJENCI
	-------------------------
	IF serwis.czy_tabela_istnieje('pacjenci', 'public') = true
        THEN 
            DROP table public.pacjenci CASCADE;
        END IF;

	IF serwis.czy_tabela_istnieje('klienci', 'public') = false --pkt 2B
	THEN
		CREATE TABLE public.klienci
		(
			id serial,
			CONSTRAINT klienci_pkey PRIMARY KEY (id)
		);
	END IF;																

	EXECUTE serwis.dodaj_kolumne('public.klienci', 'uuid', 'text');
        EXECUTE serwis.dodaj_kolumne('public.klienci', 'nazwa_klienta', 'text'); --pkt 3
        EXECUTE serwis.dodaj_kolumne('public.klienci', 'nip', 'text'); --pkt 3
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'imie', 'text'); --pkt 3
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'nazwisko', 'text'); --pkt 3
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'nr_licencji', 'text'); --pkt 4
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'telefon_kontaktowy', 'text'); --pkt 5
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'email', 'text'); --pkt 6
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'miejscowosc', 'TEXT'); --pkt 9
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'kod_pocztowy', 'TEXT'); --pkt 9
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'ulica', 'TEXT'); --pkt 9
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'nr_domu', 'TEXT'); --pkt 9
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'nr_lokalu', 'TEXT'); --pkt 9		
	EXECUTE serwis.dodaj_kolumne('public.klienci', 'potwierdzenie_danych', 'boolean NOT NULL default false'); --pkt 39
	UPDATE public.klienci SET uuid = replace(uuid_generate_v4()::text, '-', '') WHERE uuid IS NULL;			


	-------------------------
	--SERWIS - USTAWIENIA PACJENTA
	-------------------------

	IF serwis.czy_tabela_istnieje('ustawienia_pacjenta', 'serwis') = false --pkt 10
	THEN
		CREATE TABLE serwis.ustawienia_pacjenta
		(
			id serial,
			CONSTRAINT ustawienia_pacjenta_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_data_urodzenia', 'boolean NOT NULL DEFAULT false'); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_miejce_urodzenia', 'boolean NOT NULL DEFAULT false'); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_telefon', 'boolean NOT NULL DEFAULT false'); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_dane_adresowe', 'boolean NOT NULL DEFAULT false'); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'maksymalny_wiek_pacjenta_powiazanego', 'INTEGER'); --pkt 48, 79
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'maksymalna_ilosc_pacjentow_powiazanych', 'INTEGER'); --pkt 80
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'minimalny_wiek_pacjenta', 'INTEGER'); --pkt 56

        IF NOT EXISTS (SELECT * FROM serwis.ustawienia_pacjenta) THEN
            INSERT INTO serwis.ustawienia_pacjenta (
                id,         
                obowiazkowe_data_urodzenia,
                obowiazkowe_miejce_urodzenia,
                obowiazkowe_telefon,
                obowiazkowe_dane_adresowe
            ) VALUES (
                default,
                false,
                false,
                false,
                false
            );
        END IF;

        -- Dla danych wprowadzanych samodzielnie przez pacjenta dodano oddzielne ustawienia
        EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_data_urodzenia_samodzielnie', 'boolean NOT NULL DEFAULT false', null); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_miejce_urodzenia_samodzielnie', 'boolean NOT NULL DEFAULT false'); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_telefon_samodzielnie', 'boolean NOT NULL DEFAULT false'); --pkt 10
	EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_dane_adresowe_samodzielnie', 'boolean NOT NULL DEFAULT false'); --pkt 10
        -- Dodatkowe ustawienie dla pola e-mail (z dokumentacji nie wynika jasno czy ma być obowiązkowe
        EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_email', 'boolean NOT NULL DEFAULT false');
        EXECUTE serwis.dodaj_kolumne('serwis.ustawienia_pacjenta', 'obowiazkowe_email_samodzielnie', 'boolean NOT NULL DEFAULT false');

	-------------------------
	--SERWIS - PARAMETRY HASLA
	-------------------------
	
	IF serwis.czy_tabela_istnieje('parametry_hasla', 'serwis') = false --pkt 22
	THEN
		CREATE TABLE serwis.parametry_hasla
		(
			id serial NOT NULL,
			CONSTRAINT parametry_hasla_pkey PRIMARY KEY (id)
		);
	END IF;
	
	EXECUTE serwis.dodaj_kolumne('serwis.parametry_hasla', 'minimalna_dlugosc', 'integer NOT NULL DEFAULT 8'); --pkt 22
	EXECUTE serwis.dodaj_kolumne('serwis.parametry_hasla', 'liczba_znakow_specjalnych', 'integer NOT NULL DEFAULT 1'); --pkt 22
	EXECUTE serwis.dodaj_kolumne('serwis.parametry_hasla', 'liczba_cyfr', 'integer NOT NULL DEFAULT 1'); --pkt 22
	EXECUTE serwis.dodaj_kolumne('serwis.parametry_hasla', 'wielkosc_liter', 'boolean NOT NULL DEFAULT false'); --pkt 22

        IF NOT EXISTS (SELECT * FROM serwis.parametry_hasla) THEN 
            INSERT INTO serwis.parametry_hasla (id) VALUES (default);
        END IF;
	-------------------------
	--SERWIS - BLOKADY KONTA
	-------------------------
	
	IF serwis.czy_tabela_istnieje('blokada_konta', 'serwis') = false --pkt 24
	THEN
		CREATE TABLE serwis.blokada_konta
		(
			id serial,
			CONSTRAINT blokada_konta_pkey PRIMARY KEY (id)
		);
	END IF;
	
	EXECUTE serwis.dodaj_kolumne('serwis.blokada_konta', 'liczba_bledow', 'integer NOT NULL DEFAULT 3'); --pkt 24, 73
	EXECUTE serwis.dodaj_kolumne('serwis.blokada_konta', 'czas_blokady', 'integer NOT NULL DEFAULT 5'); --pkt 24, 74

	-------------------------
	--SERWIS - DOSTEP
	-------------------------

	IF serwis.czy_tabela_istnieje('dostep', 'serwis') = false --pkt 24
	THEN
		CREATE TABLE serwis.dostep
		(
			id serial,
			CONSTRAINT dostep_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('serwis.dostep', 'dlugosc_sesji_http', 'integer NOT NULL DEFAULT 5'); --pkt 72
	EXECUTE serwis.dodaj_kolumne('serwis.dostep', 'ip_rejestracja', 'text'); --pkt 70
	EXECUTE serwis.dodaj_kolumne('serwis.dostep', 'ip_logowanie', 'text'); --pkt 71
        EXECUTE serwis.dodaj_kolumne('serwis.dostep', 'czas_waznosci_kodu_jednorazowego', 'integer NOT NULL DEFAULT 2'); --pkt 69

	-------------------------
	--SERWIS - KONFIGURACJA
	-------------------------

	IF serwis.czy_tabela_istnieje('konfiguracja', 'serwis') = false
	THEN
		CREATE TABLE serwis.konfiguracja
		(
			id serial,
			CONSTRAINT konfiguracja_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'logowanie', 'boolean not null default false');
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'aktualnosci_na_stronie_glownej', 'boolean NOT NULL DEFAULT TRUE'); --pkt 75
	/*przeniesione do innej tabeli*/
	/*
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'adres_serwera_poczty', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'port_serwera_poczty', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'protokol_serwera_poczty', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'haslo_serwera_poczty', 'TEXT'); --pkt 77
	*/
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'recaptcha_site_key', 'TEXT'); --pkt 13
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'recaptcha_secret_key', 'TEXT'); --pkt 13
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'szablon_email_potwierdzenie_zalozenia_konta', 'TEXT'); --pkt 19
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'szablon_email_zmiana_hasla', 'TEXT'); --pkt 28
        
        -- porządki po wcześniejszej wersji skryptu, zamiana nazwy kolumny 
        -- szablon_email_powiadomienie_o_wykorzystaniu_danych -> szablon_email_powiadomienie_o_wykorzystaniu_danych_pesel
        IF serwis.czy_kolumna_istnieje('serwis.konfiguracja', 'szablon_email_powiadomienie_o_wykorzystaniu_danych') THEN
            IF NOT serwis.czy_kolumna_istnieje('serwis.konfiguracja', 'szablon_email_powiadomienie_o_wykorzystaniu_danych_pesel') THEN
                ALTER TABLE serwis.konfiguracja RENAME szablon_email_powiadomienie_o_wykorzystaniu_danych TO szablon_email_powiadomienie_o_wykorzystaniu_danych_pesel;
            ELSE
                ALTER TABLE serwis.konfiguracja DROP COLUMN szablon_email_powiadomienie_o_wykorzystaniu_danych;
            END IF;            
        END IF;
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'szablon_email_powiadomienie_o_wykorzystaniu_danych_pesel', 'TEXT'); --pkt 20
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'szablon_email_powiadomienie_o_wykorzystaniu_danych_email', 'TEXT'); --pkt 20
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'szablon_email_powiadomienie_o_zmianie_hasla', 'TEXT'); --pkt 31
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'url_serwera', 'TEXT');
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'nazwa_serwisu', 'TEXT');
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'email_administratora', 'TEXT');
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'logo', 'TEXT');        
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'szablon_email_powiadomienie_o_zablokowaniu_konta', 'TEXT');
	
	IF serwis.czy_kolumna_istnieje('serwis.konfiguracja', 'adres_serwera_poczty')
	THEN
		ALTER TABLE serwis.konfiguracja DROP COLUMN adres_serwera_poczty;
	END IF;
	
	IF serwis.czy_kolumna_istnieje('serwis.konfiguracja', 'port_serwera_poczty')
	THEN
		ALTER TABLE serwis.konfiguracja DROP COLUMN port_serwera_poczty;
	END IF;
	
	IF serwis.czy_kolumna_istnieje('serwis.konfiguracja', 'protokol_serwera_poczty')
	THEN
		ALTER TABLE serwis.konfiguracja DROP COLUMN protokol_serwera_poczty;
	END IF;
	
	IF serwis.czy_kolumna_istnieje('serwis.konfiguracja', 'haslo_serwera_poczty')
	THEN
		ALTER TABLE serwis.konfiguracja DROP COLUMN haslo_serwera_poczty;
	END IF;

	-------------------------
	--SERWIS - KONFIGURACJA SERWERA POCZTY --pkt 77, 167
	-------------------------

	IF serwis.czy_tabela_istnieje('konfiguracja_serwera_poczty', 'serwis') = false
	THEN
		CREATE TABLE serwis.konfiguracja_serwera_poczty
		(
			id serial,
			CONSTRAINT konfiguracja_serwera_poczty_pkey PRIMARY KEY (id)
		);
	END IF;
	
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'uzytkownik', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'haslo_serwera_poczty', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'adres_serwera_poczty', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'port_serwera_poczty', 'TEXT'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'ssl', 'BOOLEAN'); --pkt 77
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'nadawca', 'TEXT');
        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_poczty', 'email', 'TEXT');

        ----------------------------------------------
	--SERWIS - KONFIGURACJA SERWERA SMS --pkt 167
	----------------------------------------------

	IF serwis.czy_tabela_istnieje('konfiguracja_serwera_sms', 'serwis') = false
	THEN
		CREATE TABLE serwis.konfiguracja_serwera_sms
		(
			id serial,
			CONSTRAINT konfiguracja_serwera_sms_pkey PRIMARY KEY (id)
		);
	END IF;
	
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_sms', 'sms_api_login', 'TEXT'); --pkt 167
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_sms', 'sms_api_password', 'TEXT'); --pkt 167
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_sms', 'serwer_sms_login', 'TEXT'); --pkt 167
	EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja_serwera_sms', 'serwer_sms_password', 'TEXT'); --pkt 167
	-------------------------
	--SLOWNIKI
	-------------------------
	
	CREATE SCHEMA IF NOT EXISTS slowniki AUTHORIZATION softmedica;		
	
	-------------------------
	--SLOWNIKI - TYPY DOKUMENTOW
	-------------------------
	
	IF serwis.czy_tabela_istnieje('typy_dokumentow', 'slowniki') = false --pkt 76
	THEN
		CREATE TABLE slowniki.typy_dokumentow
		(
			id serial,
			CONSTRAINT typy_dokumentow_pkey PRIMARY KEY (id)
		);
	END IF;
	
	EXECUTE serwis.dodaj_kolumne('slowniki.typy_dokumentow', 'nazwa_dokumentu_tozsamosci', 'text NOT NULL'); --pkt 17, 76
	EXECUTE serwis.dodaj_kolumne('slowniki.typy_dokumentow', 'format_numeracji_regex', 'text NOT NULL');
	EXECUTE serwis.dodaj_kolumne('slowniki.typy_dokumentow', 'format_numeracji_opis', 'text NOT NULL');
	

	-------------------------
	--SLOWNIKI - TYPY KOMUNIKATOW
	-------------------------
	
	IF serwis.czy_tabela_istnieje('typy_komunikatow', 'slowniki') = false 
	THEN
		CREATE TABLE slowniki.typy_komunikatow
		(
			id serial,
			CONSTRAINT typy_komunikatow_pkey PRIMARY KEY (id)
		);
	END IF;
	
	EXECUTE serwis.dodaj_kolumne('slowniki.typy_komunikatow', 'opis', 'text NOT NULL'); --pkt 23, 39B

	-------------------------
	--SLOWNIKI - TYPY E_DOKUMENTOW
	-------------------------

	IF serwis.czy_tabela_istnieje('typy_e_dokumentow', 'slowniki') = false 
	THEN
		CREATE TABLE slowniki.typy_e_dokumentow
		(
			id serial,
			CONSTRAINT typy_e_dokumentow_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('slowniki.typy_e_dokumentow', 'opis', 'text NOT NULL'); --pkt 152

	-------------------------
	--SLOWNIKI - KOMORKI ORGANIZACYJNE
	-------------------------

	IF serwis.czy_tabela_istnieje('komorki_organizacyjne', 'slowniki') = false --pkt 152
	THEN
		CREATE TABLE slowniki.komorki_organizacyjne
		(
			id serial,
			id_komorki_organizacyjnej integer,
			CONSTRAINT komorki_organizacyjne_pkey PRIMARY KEY (id),
			CONSTRAINT komorki_organizacyjne_id_komorki_organizacyjnej_fkey FOREIGN KEY (id)
				REFERENCES slowniki.komorki_organizacyjne (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('slowniki.komorki_organizacyjne', 'nazwa', 'text NOT NULL'); --pkt 152
	EXECUTE serwis.dodaj_kolumne('slowniki.komorki_organizacyjne', 'opis', 'text'); --pkt 152

	-------------------------
	--SLOWNIKI - TYPY_GRUP
	-------------------------
	
	IF serwis.czy_tabela_istnieje('typy_grup', 'slowniki') = false --pkt 152
	THEN
		CREATE TABLE slowniki.typy_grup
		(
			id serial,
			CONSTRAINT typy_grup_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('slowniki.typy_grup', 'nazwa', 'text NOT NULL');

        -------------------------
	--SLOWNIKI -TYPY_POWIADOMIEN --pkt 168
	-------------------------
        
        IF serwis.czy_tabela_istnieje('typy_powiadomien','slowniki') = false --pkt 168
        THEN 
                CREATE TABLE slowniki.typy_powiadomien
                (
                        id serial,
                        CONSTRAINT typy_powiadomien_pkey PRIMARY KEY (id)
                );
        END IF;
        
        EXECUTE serwis.dodaj_kolumne('slowniki.typy_powiadomien', 'opis', 'text NOT NULL');
        EXECUTE serwis.dodaj_kolumne('slowniki.typy_powiadomien', 'email', 'boolean NOT NULL DEFAULT true');
        EXECUTE serwis.dodaj_kolumne('slowniki.typy_powiadomien', 'sms', 'boolean NOT NULL DEFAULT true');

	-------------------------
	--UZYTKOWNICY
	-------------------------

	CREATE SCHEMA IF NOT EXISTS uzytkownicy AUTHORIZATION softmedica;

        -------------------------
	--UZYTKOWNICY -ZMIANA_HASLA --pkt 31
	-------------------------
        IF serwis.czy_tabela_istnieje('zmiana_hasla', 'uzytkownicy') = false
	THEN
            CREATE TABLE uzytkownicy.zmiana_hasla (
                id serial NOT NULL,
                uuid character varying,
                token character varying,
                znacznik_czasu_utworzenia timestamp without time zone,
                CONSTRAINT zmiana_hasla_pkey PRIMARY KEY (id)
            );
        END IF;
	-------------------------
	--UZYTKOWNICY - GRUPY --pkt 38
	-------------------------

	IF serwis.czy_tabela_istnieje('grupy', 'uzytkownicy') = false
	THEN
		CREATE TABLE uzytkownicy.grupy
		(
			id serial,
			id_typ_grupy integer,
			CONSTRAINT grupy_pkey PRIMARY KEY (id),
			CONSTRAINT grupy_id_typ_grupy_fkey FOREIGN KEY (id_typ_grupy)
				REFERENCES slowniki.typy_grup (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE	
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('uzytkownicy.grupy', 'opis', 'TEXT NOT NULL');
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.grupy', 'aktywna', 'BOOLEAN NOT NULL DEFAULT FALSE'); --pkt 66
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.grupy', 'domyslna', 'BOOLEAN NOT NULL DEFAULT FALSE');

	-------------------------
	--UZYTKOWNICY - KONTA
	-------------------------

	IF serwis.czy_tabela_istnieje('konta', 'uzytkownicy') = false --pkt 32
	THEN
		CREATE TABLE uzytkownicy.konta
		(
			id serial,
			id_grupy integer,
			CONSTRAINT konta_pkey PRIMARY KEY (id),
			CONSTRAINT konta_id_grupy_fkey FOREIGN KEY (id_grupy)
				REFERENCES uzytkownicy.grupy (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'login', 'text'); --pkt 21
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'email', 'text'); --pkt 21
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'haslo', 'text NOT NULL'); --pky 21
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'konto_aktywne', 'BOOLEAN NOT NULL DEFAULT FALSE'); --pkt 19
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'akceptacja_regulaminu', 'timestamp without time zone'); --pkt 18, 32
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'blokada_konta_do', 'timestamp without time zone'); --pkt 23B
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'blokada_konta', 'BOOLEAN NOT NULL DEFAULT FALSE'); --pkt 33
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'potwierdzenie', 'BOOLEAN NOT NULL DEFAULT FALSE'); --pkt 39
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'uuid', 'character varying');
	EXECUTE serwis.dodaj_kolumne('uzytkownicy.konta', 'liczba_prob_logowania', 'integer NOT NULL DEFAULT 0');
	UPDATE uzytkownicy.konta SET uuid = replace(uuid_generate_v4()::text, '-', '') WHERE uuid IS NULL;

  
	DROP TABLE IF EXISTS uzytkownicy.konta_powiazania; /*przeniesienie powiazania z tabeli konta_powiazania do tabeli pracownicy_powiazania*/
	
	-------------------------
	--UZYTKOWNICY - KLIENCI_POWIAZANIA
	-------------------------
	IF serwis.czy_tabela_istnieje('pacjenci_powiazania', 'uzytkownicy') = TRUE 
        THEN 
            DROP TABLE uzytkownicy.pacjenci_powiazania CASCADE;
        END IF;
		
	IF serwis.czy_tabela_istnieje('klienci_powiazania', 'uzytkownicy') = TRUE 
        THEN 
            DROP TABLE uzytkownicy.klienci_powiazania CASCADE;
        END IF;

	IF serwis.czy_tabela_istnieje('klienci_powiazania', 'uzytkownicy') = false --pkt 32
	THEN
		CREATE TABLE uzytkownicy.klienci_powiazania
		(
			id serial,
			id_konta integer,
			id_klienta integer,
			nadrzedne boolean NOT NULL,
			CONSTRAINT klienci_powiazania_pkey PRIMARY KEY (id),
			CONSTRAINT klienci_powiazania_id_kontoa_fkey FOREIGN KEY (id_konta)
				REFERENCES uzytkownicy.konta (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE,
			CONSTRAINT klienci_powiazania_id_klienta_fkey FOREIGN KEY (id_klienta)
				REFERENCES public.klienci(id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE	
		);

                IF serwis.czy_kolumna_istnieje('uzytkownicy.uprawnienia', 'dodawanie_pacjentow_powiazanych') = true
                THEN
                    ALTER TABLE uzytkownicy.uprawnienia DROP COLUMN dodawanie_pacjentow_powiazanych;
                END IF;

                IF serwis.czy_kolumna_istnieje('uzytkownicy.uprawnienia', 'dostep_do_listy_pacjentow') = true
                THEN
                    ALTER TABLE uzytkownicy.uprawnienia DROP COLUMN dostep_do_listy_pacjentow;
                END IF;

                IF serwis.czy_kolumna_istnieje('uzytkownicy.uprawnienia', 'dostep_do_kartoteki_pacjenta_powiazanego') = true
                THEN
                    ALTER TABLE uzytkownicy.uprawnienia DROP COLUMN dostep_do_kartoteki_pacjenta_powiazanego;
                END IF;

                IF serwis.czy_kolumna_istnieje('uzytkownicy.uprawnienia', 'planowanie_wizyt') = true
                THEN
                    ALTER TABLE uzytkownicy.uprawnienia DROP COLUMN planowanie_wizyt;
                END IF;


	END IF;						  
	-------------------------
	--UZYTKOWNICY - KONTA GRUPY
	-------------------------

        IF serwis.czy_tabela_istnieje('konta_grupy','uzytkownicy') = false
        THEN
            CREATE TABLE uzytkownicy.konta_grupy (
                id serial NOT NULL,
                id_konta integer,
                id_grupy integer,
                CONSTRAINT konta_grupy_pkey PRIMARY KEY (id),
                CONSTRAINT konta_grupy_id_konta_fkey FOREIGN KEY (id_konta)
                    REFERENCES uzytkownicy.konta (id) MATCH SIMPLE
                    ON UPDATE RESTRICT
                    ON DELETE CASCADE,
               CONSTRAINT konta_grupy_id_grupy_fkey FOREIGN KEY (id_grupy)
                    REFERENCES uzytkownicy.grupy (id) MATCH SIMPLE
                    ON UPDATE RESTRICT
                    ON DELETE CASCADE
            );

        DROP FUNCTION IF EXISTS uzytkownicy_aktualizuj_powiazania_grupy();

        CREATE OR REPLACE FUNCTION uzytkownicy.aktualizuj_powiazania_grupy()
        RETURNS boolean AS
        $done$
        DECLARE
        var_rekord record;
        BEGIN
            FOR var_rekord IN (
                SELECT id, id_grupy FROM uzytkownicy.konta
            )   LOOP
                INSERT INTO uzytkownicy.konta_grupy (id, id_konta, id_grupy ) VALUES (DEFAULT, var_rekord.id, var_rekord.id_grupy);
            END LOOP;
        RETURN true;
        END;
        $done$
        LANGUAGE 'plpgsql';

        PERFORM uzytkownicy.aktualizuj_powiazania_grupy();

        END IF;

	-------------------------
	--UZYTKOWNICY - UPRAWNIENIA --pkt 38, 49
	-------------------------
	
	IF serwis.czy_tabela_istnieje('uprawnienia', 'uzytkownicy' ) = false --pkt 39, 49
	THEN
		CREATE TABLE uzytkownicy.uprawnienia
		(
			id serial,
			id_grupy integer,
			CONSTRAINT uprawnienia_pkey PRIMARY KEY (id),
			CONSTRAINT id_grupy_fkey FOREIGN KEY (id_grupy) 
				REFERENCES uzytkownicy.grupy (id) ON UPDATE CASCADE ON DELETE CASCADE
		);
	END IF;
        
        IF serwis.czy_tabela_istnieje('uprawnienia_konta', 'uzytkownicy' ) = true
        THEN
                DROP TABLE uzytkownicy.uprawnienia_konta;
--                 CREATE TABLE uzytkownicy.uprawnienia_konta 
--                 (
--                         id serial,
--                         id_konta integer,
--                         dostep_do_kartoteki_pacjenta_powiazanego BOOLEAN NOT NULL DEFAULT FALSE,
--                         CONSTRAINT uprawnienia_konta_pkey PRIMARY KEY (id),
--                         CONSTRAINT id_konta_fkey FOREIGN KEY (id_konta) 
--                                 REFERENCES uzytkownicy.konta (id) ON UPDATE CASCADE ON DELETE CASCADE
--                 );
        END IF;

	EXECUTE serwis.dodaj_kolumne('uzytkownicy.uprawnienia', 'blokowanie_konta', 'BOOLEAN NOT NULL DEFAULT FALSE'); --pkt 53
        EXECUTE serwis.dodaj_kolumne('uzytkownicy.uprawnienia', 'administracja', 'BOOLEAN NOT NULL DEFAULT FALSE'); --pkt 53
        

        IF (SELECT data_type = 'boolean'
            FROM information_schema.columns
            WHERE table_schema = 'uzytkownicy' AND table_name = 'uprawnienia' AND column_name = 'blokowanie_konta')
        THEN 
            ALTER TABLE uzytkownicy.uprawnienia ALTER blokowanie_konta DROP NOT null;
            ALTER TABLE uzytkownicy.uprawnienia ALTER blokowanie_konta SET DEFAULT null;
            ALTER TABLE uzytkownicy.uprawnienia ALTER blokowanie_konta TYPE integer USING CASE WHEN blokowanie_konta THEN 15 ELSE 0 END;
            ALTER TABLE uzytkownicy.uprawnienia ALTER blokowanie_konta SET DEFAULT 0;
        END IF;

        IF (SELECT data_type = 'boolean'
            FROM information_schema.columns
            WHERE table_schema = 'uzytkownicy' AND table_name = 'uprawnienia' AND column_name = 'administracja')
        THEN 
            ALTER TABLE uzytkownicy.uprawnienia ALTER administracja DROP NOT null;
            ALTER TABLE uzytkownicy.uprawnienia ALTER administracja SET DEFAULT null;
            ALTER TABLE uzytkownicy.uprawnienia ALTER administracja TYPE integer USING CASE WHEN administracja THEN 15 ELSE 0 END;
            ALTER TABLE uzytkownicy.uprawnienia ALTER administracja SET DEFAULT 0;
        END IF;



        -- usuwanie wyzwalacza
	IF EXISTS (
        SELECT  * 
        FROM    INFORMATION_SCHEMA.TRIGGERS 
        WHERE   trigger_name = 'dodaj_rekord_uzytkownicy_uprawnienia_trigger' AND 
            event_object_schema = 'uzytkownicy' AND event_object_table = 'grupy' )
            THEN		
        RAISE INFO 'Wyzwalacz dodaj_rekord_uzytkownicy_uprawnienia_trigger istnieje, usuwam istniejący trigger';
        DROP TRIGGER dodaj_rekord_uzytkownicy_uprawnienia_trigger ON uzytkownicy.grupy CASCADE ;			
	END IF;

	-- tworzenie procedury wyzwalanej
	CREATE OR REPLACE FUNCTION uzytkownicy.dodaj_rekord_uzytkownicy_uprawnienia()
	RETURNS trigger AS
	$dodaj_rekord_uzytkownicy_uprawnienia$		
	BEGIN
        IF NOT EXISTS (
            SELECT * FROM uzytkownicy.uprawnienia WHERE id_grupy = NEW.id)
        THEN
            INSERT INTO uzytkownicy.uprawnienia (id_grupy) VALUES (NEW.id);
        END IF;
        RETURN NEW;
	END;
	$dodaj_rekord_uzytkownicy_uprawnienia$
	LANGUAGE 'plpgsql';
	COMMENT ON FUNCTION uzytkownicy.dodaj_rekord_uzytkownicy_uprawnienia() IS 'dodaje rekord w tabeli uzytkownicy.uprawnienia dla każdego nowego rekord w tabeli uzytkownicy.grupy';
	RAISE INFO 'Dodano procedurę wyzwalaną uzytkownicy.dodaj_rekord_uzytkownicy_uprawnienia';
    
	-- tworzenie wyzwalacza
	CREATE TRIGGER dodaj_rekord_uzytkownicy_uprawnienia_trigger
	AFTER INSERT ON uzytkownicy.grupy
	FOR EACH ROW
	EXECUTE PROCEDURE uzytkownicy.dodaj_rekord_uzytkownicy_uprawnienia();
	RAISE INFO 'Dodano trigger dodaj_rekord_uzytkownicy_uprawnienia_trigger';	
																					
	-------------------------
	--PORTAL
	-------------------------

	CREATE SCHEMA IF NOT EXISTS portal AUTHORIZATION softmedica;

	-------------------------
	--PORTAL - AKTUALNOSCI
	-------------------------

	IF serwis.czy_tabela_istnieje('aktualnosci', 'portal') = false --pkt 37, 78
	THEN
		CREATE TABLE portal.aktualnosci
		(
			id serial,
			CONSTRAINT aktualnosci_pkey PRIMARY KEY (id)
		);
	END IF;

        IF serwis.czy_kolumna_istnieje('portal.aktualnosci', 'data_dodania') THEN
            ALTER TABLE portal.aktualnosci RENAME data_dodania TO data_publikacji;
        END IF;
	EXECUTE serwis.dodaj_kolumne('portal.aktualnosci', 'data_publikacji', 'DATE NOT NULL DEFAULT (''now''::text)::date'); --pkt 37
	EXECUTE serwis.dodaj_kolumne('portal.aktualnosci', 'tresc', 'text NOT NULL'); --pkt 37
        EXECUTE serwis.dodaj_kolumne('portal.aktualnosci', 'tytul', 'text NOT NULL'); --pkt 37
        EXECUTE serwis.dodaj_kolumne('portal.aktualnosci', 'skrot', 'text'); --pkt 37
        EXECUTE serwis.dodaj_kolumne('portal.aktualnosci', 'autor', 'text'); --pkt 37
        EXECUTE serwis.dodaj_kolumne('portal.aktualnosci', 'publikacja', 'boolean NOT NULL DEFAULT false'); --pkt 37        

	-------------------------
	--PORTAL - REGULAMIN
	-------------------------

	IF serwis.czy_tabela_istnieje('regulamin', 'portal') = false --pkt 32
	THEN
		CREATE TABLE portal.regulamin
		(
			id serial,
			CONSTRAINT regulamin_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('portal.regulamin', 'data_dodania', 'DATE NOT NULL'); --pkt 32
	EXECUTE serwis.dodaj_kolumne('portal.regulamin', 'tresc', 'text NOT NULL'); --pkt 32	

        IF serwis.czy_tabela_istnieje('tresci', 'portal') = false --pkt  87, 88, 89
        THEN
            CREATE TABLE portal.tresci (
                id serial,
                dane_kontaktowe text,
                dane_adresowe text,
                naglowek text,
                stopka text,
                CONSTRAINT tresci_pkey PRIMARY KEY (id)
            );
	END IF;

	-------------------------
	--DOKUMENTY
	-------------------------

	CREATE SCHEMA IF NOT EXISTS dokumenty AUTHORIZATION softmedica;

	-------------------------
	--DOKUMENTY - DESKRYPTORY --pkt 154.3
	-------------------------

	IF serwis.czy_tabela_istnieje('deskryptory', 'dokumenty') = false 
	THEN
		CREATE TABLE dokumenty.deskryptory
		(
			id serial,
			CONSTRAINT deskryptory_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'tytul', 'text not null'); --pkt 154.4
	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'pacjent', 'text not null'); --pkt 154.5
	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'komorka_organizacyjna', 'text not null'); --pkt 154.6
	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'kierujacy', 'text'); --pkt 154.7
	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'data_zatwierdzenia', 'date'); --pkt 154.8
	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'data_utworzenia', 'date not null'); --pkt 154.9
	EXECUTE serwis.dodaj_kolumne('dokumenty.deskryptory', 'informacja_o_aktualnosci', 'text'); --pkt 154.10

	-------------------------
	--ANKIETY --pkt 160
	-------------------------

	CREATE SCHEMA IF NOT EXISTS ankiety AUTHORIZATION softmedica;

	-------------------------
	--POWIADOMIENIA --pkt 167
	-------------------------

	CREATE SCHEMA IF NOT EXISTS powiadomienia AUTHORIZATION softmedica;

        -----------------------------------
	--POWIADOMIENIA -SZABLONY --pkt 169
	-----------------------------------

        IF serwis.czy_tabela_istnieje('szablony_powiadomien', 'powiadomienia') = false 
	THEN
		CREATE TABLE powiadomienia.szablony_powiadomien
		(
			id serial,
                        id_sl_typy_powiadomien INT NOT NULL,
			CONSTRAINT id_szablony_powiadomien_pkey PRIMARY KEY (id),
                        CONSTRAINT szablony_powiadomien_id_sl_typy_powiadomien_fkey FOREIGN KEY (id_sl_typy_powiadomien)
				REFERENCES slowniki.typy_powiadomien(id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT
		);
	END IF;
	
        EXECUTE serwis.dodaj_kolumne('powiadomienia.szablony_powiadomien','szablony_powiadomien_email_naglowek','text');
        EXECUTE serwis.dodaj_kolumne('powiadomienia.szablony_powiadomien','szablony_powiadomien_email_tresc','text');
        EXECUTE serwis.dodaj_kolumne('powiadomienia.szablony_powiadomien','szablony_powiadomien_sms_tresc','text');

        DROP TABLE IF EXISTS powiadomienia.szablony;

--      IF serwis.czy_tabela_istnieje('szablony', 'powiadomienia') = false 
-- 	THEN
-- 		CREATE TABLE powiadomienia.szablony
-- 		(
-- 			id serial,
--                         id_sl_typy_powiadomien INT NOT NULL,
-- 			CONSTRAINT id_szablony_pkey PRIMARY KEY (id),
--                         CONSTRAINT szablony_id_sl_typy_powiadomien_fkey FOREIGN KEY (id_sl_typy_powiadomien)
-- 				REFERENCES slowniki.typy_powiadomien(id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT
-- 		);
-- 	END IF;
-- 	
--         EXECUTE serwis.dodaj_kolumne('powiadomienia.szablony','szablony_email_naglowek','text');
--         EXECUTE serwis.dodaj_kolumne('powiadomienia.szablony','szablony_email_tresc','text');
--         EXECUTE serwis.dodaj_kolumne('powiadomienia.szablony','szablony_sms_tresc','text');
        
        
    -------------------------
	--AUTENTYKACJA
	-------------------------

        IF serwis.czy_tabela_istnieje('autentykacja', 'uzytkownicy') = false THEN
            CREATE TABLE uzytkownicy.autentykacja (
                id serial,
                id_konta integer NOT NULL,            
                token character varying COLLATE pg_catalog."default" NOT NULL,
                token_wazny_do timestamp without time zone,
                ip character varying COLLATE pg_catalog."default",
                CONSTRAINT autentykacja_pkey PRIMARY KEY (id),
                CONSTRAINT autentykacja_id_personel_fkey FOREIGN KEY (id_konta)
                        REFERENCES uzytkownicy.konta (id) MATCH SIMPLE
                        ON UPDATE RESTRICT
                        ON DELETE CASCADE
            );
        END IF;

	-------------------------
	--LOGI
	-------------------------

	CREATE SCHEMA IF NOT EXISTS logi AUTHORIZATION softmedica;

	CREATE EXTENSION IF NOT EXISTS HSTORE;
	
	-------------------------
	--LOGI - LOGOWANIA
	-------------------------
	IF serwis.czy_tabela_istnieje('logowania', 'logi') = false THEN
		CREATE TABLE logi.logowania
		(
			id serial, 
			data date NOT NULL DEFAULT ('now'::TEXT)::DATE, 
			czas time without time zone NOT NULL DEFAULT ('now'::TEXT)::TIME WITHOUT TIME ZONE,
			ip text,   
			opis text, 
			uuid_konta text, 
			CONSTRAINT logowania_pkey PRIMARY KEY (id)
		);
	END IF;
	
	-------------------------
	--LOGI - OPERACJE
	-------------------------

	IF serwis.czy_tabela_istnieje('operacje', 'logi') = false --pkt 10
	THEN
		CREATE TABLE logi.operacje
		(
			id serial,
			CONSTRAINT operacje_pkey PRIMARY KEY (id)
		);
	END IF;

	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'data', 'date NOT NULL DEFAULT (''now''::text)::date');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'czas', 'time without time zone NOT NULL DEFAULT (''now''::text)::time without time zone');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'ip', 'character varying DEFAULT (inet_client_addr())::text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'login', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'opis', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'id_obiektu', 'integer');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'tabela_obiektu', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'id_obiektu_nadrzednego', 'integer');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'tabela_obiektu_nadrzednego', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'pid', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'typ_operacji', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'tabela', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'aplikacja', 'text');
	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'dodatkowe_dane', 'text');

        IF serwis.czy_tabela_istnieje('logi.operacje', 'login') = false 
 	THEN
                ALTER TABLE logi.operacje DROP COLUMN login;
 	END IF;

 	EXECUTE serwis.dodaj_kolumne('logi.operacje', 'uuid_konta', 'text');

        IF serwis.czy_tabela_istnieje('logi.operacje', 'aplikacja') = false 
 	THEN
                ALTER TABLE logi.operacje DROP COLUMN aplikacja;
 	END IF;

        IF serwis.czy_tabela_istnieje('logi.operacje', 'dodatkowe_dane') = false 
 	THEN
                ALTER TABLE logi.operacje DROP COLUMN dodatkowe_dane;
 	END IF;

        IF serwis.czy_tabela_istnieje('logi.operacje', 'tabela') = false 
 	THEN
                ALTER TABLE logi.operacje DROP COLUMN tabela;
 	END IF;
 	
        DROP FUNCTION IF EXISTS serwis.wylacz_logowanie();

        CREATE OR REPLACE FUNCTION logi.wylacz_logowanie() 
                RETURNS void AS $wylacz_logowanie$
        DECLARE
                r RECORD;
        BEGIN   
                FOR r IN
                        select 
                                trigger_schema,
                                event_object_table as table_name,
                                trigger_name
                        from 
                                information_schema.triggers
                        WHERE
                                trigger_name like '%_log_%'
                        group by 
                                trigger_schema,
                                event_object_table,
                                trigger_name
                        order by 
                                trigger_schema, 
                                table_name
                LOOP
                        EXECUTE 'DROP TRIGGER IF EXISTS dodaj_' || r.trigger_schema || '_log_' || r.table_name || '_trigger ON '|| r.trigger_schema || '.' || r.table_name || ';';
                        EXECUTE 'DROP TRIGGER IF EXISTS usun_' || r.trigger_schema || '_log_' || r.table_name || '_trigger ON ' || r.trigger_schema || '.' || r.table_name || ';';
                END LOOP;
        END;
	$wylacz_logowanie$
        LANGUAGE plpgsql;

        CREATE OR REPLACE FUNCTION logi.wlacz_logowanie() 
        RETURNS void AS $wlacz_logowanie$
        DECLARE
                r RECORD;

                tabele_wykluczone TEXT[] := ARRAY['logi.operacje', 'logi.logowania']::TEXT[];

                triger_func TEXT;
                triger_func_name TEXT;
                triger_func_insert_columns TEXT;
                triger_func_insert_values TEXT;
                
                triger_func_insert_parent_id TEXT;
                triger_func_insert_parent_id_query TEXT;
                triger_func_insert_parent TEXT;
                triger_func_insert_parent_query TEXT;
                
                triger TEXT;
                
                new_line TEXT := E'\n';
                tab TEXT := E'\t';
        BEGIN

                PERFORM logi.wylacz_logowanie();

                triger_func_insert_columns := tab||tab||tab|| 'uuid_konta,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'opis,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'tabela_obiektu,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'id_obiektu,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'tabela_obiektu_nadrzednego,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'id_obiektu_nadrzednego,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'typ_operacji,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'pid,'||new_line;
                triger_func_insert_columns := triger_func_insert_columns ||tab||tab||tab||'ip'||new_line;

                triger_func_insert_values := tab||tab||tab|| 'user_info.uuid,' || new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'var_opis,'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'TG_TABLE_SCHEMA||''.''||TG_TABLE_NAME,'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||':TABLE.id,'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'var_tabela_nadrzedna,'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'var_id_tabela_nadrzedna,'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'TG_OP,'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'pg_backend_pid(),'||new_line;
                triger_func_insert_values := triger_func_insert_values ||tab||tab||tab||'user_info.ip'||new_line;
                RAISE INFO '%', triger_func_insert_values;
                
                FOR R IN 
                        SELECT
                                TRIM(BOTH ' ' FROM  substring(nazwa_tabeli from 0 FOR position('.' IN nazwa_tabeli))) schemat,
                                TRIM(BOTH ' ' FROM  substring(nazwa_tabeli from position('.' IN nazwa_tabeli)+1 FOR char_length(nazwa_tabeli))) tabela,
                                *
                        FROM
                        (
                        With powiazania AS ( 
                                select
                                        DISTINCT
                                        *
                                FROM
                                (
                                SELECT
                                        CASE WHEN conrelid::regclass::TEXT LIKE '-' THEN NULL ELSE CASE WHEN conrelid::regclass::TEXT LIKE '%.%' THEN conrelid::regclass::TEXT ELSE 'public.' || conrelid::regclass::TEXT END END nazwa_tabeli,
                                        b.attname id_tabeli,
                                        case WHEN confrelid::regclass::TEXT LIKE '-' THEN NULL ELSE CASE WHEN confrelid::regclass::TEXT LIKE '%.%' THEN confrelid::regclass::TEXT ELSE 'public.' || confrelid::regclass::TEXT END END nazwa_tabeli_powiazana,
                                        a.attname id_tabeli_powiazanej
                                FROM
                                        pg_constraint t1
                                                left JOIN pg_attribute a ON a.attnum = ANY (t1.confkey) AND a.attrelid = t1.confrelid
                                                left JOIN pg_attribute b ON b.attnum = ANY (t1.conkey) AND b.attrelid = t1.conrelid
                                ) d
                                WHERE 
                                        nazwa_tabeli is not null
                                order by
                                        nazwa_tabeli
                                )

                        SELECT
                                nazwa_tabeli,
                                array_agg(id_tabeli) id_tabeli,
                                array_agg(nazwa_tabeli_powiazana) nazwa_tabeli_powiazana,
                                array_agg(id_tabeli_powiazanej) id_tabeli_powiazanej
                        FROM
                                powiazania
                        GROUP BY
                                nazwa_tabeli
                        ) d
                        WHERE
                                nazwa_tabeli not in (SELECT unnest(tabele_wykluczone))
                LOOP
                        triger_func_name := r.schemat || '.log_' || r.tabela;
                        RAISE INFO '%', triger_func_name;

                        triger_func := 'CREATE OR REPLACE FUNCTION ' || triger_func_name || '()' || new_line;
                        triger_func := triger_func || 'RETURNS trigger AS $' || '$' || new_line;
                        triger_func := triger_func || 'DECLARE' || new_line;
                        triger_func := triger_func || tab || 'var_opis'|| tab ||'text;' || new_line;
                        triger_func := triger_func || tab || 'var_uuid'|| tab ||'text;' || new_line;
                        triger_func := triger_func || tab || 'var_tabela_nadrzedna'|| tab ||'text;' || new_line;
                        triger_func := triger_func || tab || 'var_id_tabela_nadrzedna'|| tab ||'integer;' || new_line;
                        triger_func := triger_func || tab || 'user_info'|| tab ||'RECORD;' || new_line;
                        triger_func := triger_func || 'BEGIN' || new_line;
                        RAISE INFO '%', triger_func;

                        triger_func := triger_func || tab || 'SELECT' || new_line;
                        triger_func := triger_func || tab || tab || 'NULLIF(application_name[1], '''') uuid,' || new_line;
                        triger_func := triger_func || tab || tab || 'CASE WHEN NULLIF(application_name[2], '''') IS NULL' || new_line;
                        triger_func := triger_func || tab || tab || 'THEN' || new_line;
                        triger_func := triger_func || tab || tab || tab || 'CASE' || new_line;
                        triger_func := triger_func || tab || tab || tab || tab || 'WHEN inet_client_addr()::TEXT IS NULL THEN NULL' || new_line;
                        triger_func := triger_func || tab || tab || tab || tab || 'ELSE inet_client_addr()::TEXT END' || new_line;
                        triger_func := triger_func || tab || tab || 'ELSE' || new_line;
                        triger_func := triger_func || tab || tab || tab || 'NULLIF(application_name[2], '''')' || new_line;
                        triger_func := triger_func || tab || tab || 'END ip' || new_line;
                        triger_func := triger_func || tab || 'FROM' || new_line;
                        triger_func := triger_func || tab || tab || '(' || new_line;
                        triger_func := triger_func || tab || tab || tab || 'SELECT' || new_line;
                        triger_func := triger_func || tab || tab || tab || tab || 'regexp_split_to_array(((regexp_split_to_array(application_name, ''@''))[1]), ''#'') application_name' || new_line;
                        triger_func := triger_func || tab || tab || tab || 'FROM' || new_line;
                        triger_func := triger_func || tab || tab || tab || tab ||'pg_stat_activity' || new_line;
                        triger_func := triger_func || tab || tab || tab || 'WHERE' || new_line;
                        triger_func := triger_func || tab || tab || tab || tab || 'pid = pg_backend_pid()' || new_line;
                        triger_func := triger_func || tab || tab || ') activity' || new_line;
                        triger_func := triger_func || tab || 'INTO user_info;' || new_line;
                        RAISE INFO '%', triger_func;

                        IF (CASE WHEN array_length(r.id_tabeli, 1) != 1 THEN case when 'id_konta' = ANY( r.id_tabeli) THEN 'id_konta' ELSE COALESCE(r.id_tabeli[2]::TEXT, 'NULL') end ELSE 'NULL' END) != 'NULL'
                        THEN
                                triger_func_insert_parent_id := tab||tab||'var_id_tabela_nadrzedna := :TABLE.' || CASE WHEN array_length(r.id_tabeli, 1) != 1 THEN case when 'id_konta' = ANY( r.id_tabeli) THEN 'id_konta' ELSE COALESCE(r.id_tabeli[2]::TEXT, 'NULL') end ELSE 'NULL' END || ';' || new_line;
                                triger_func_insert_parent := tab||tab||'var_tabela_nadrzedna := ''' || r.nazwa_tabeli_powiazana[array_position(r.id_tabeli::TEXT[], (CASE WHEN array_length(r.id_tabeli, 1) != 1 THEN case when 'id_konta' = ANY( r.id_tabeli) THEN 'id_konta' ELSE r.id_tabeli[2]::TEXT end END))] || ''';' || new_line;
                        ELSE 
                                triger_func_insert_parent_id := '';
                                triger_func_insert_parent := '';
                                
                        END IF;
                        RAISE INFO '############################ %', triger_func_insert_parent_id;

                        triger_func := triger_func || tab || 'IF (TG_OP = ''INSERT'') THEN' || new_line;
                        triger_func := triger_func || tab || tab || 'var_opis := hstore(NEW)::JSON::TEXT;' || new_line;
                        triger_func := triger_func || replace(triger_func_insert_parent_id, ':TABLE', 'NEW');
                        triger_func := triger_func || triger_func_insert_parent;
                        triger_func := triger_func || tab || tab || 'INSERT INTO logi.operacje(' || new_line;
                        triger_func := triger_func || triger_func_insert_columns;
                        triger_func := triger_func || tab || tab ||  ')' || new_line;
                        triger_func := triger_func || tab || tab || 'VALUES (' || new_line;
                        RAISE INFO '%', triger_func;

                        triger_func := triger_func || replace(triger_func_insert_values, ':TABLE', 'NEW') || new_line;
                        RAISE INFO '%', triger_func;
                        
                        triger_func := triger_func || tab || tab || ');' || new_line;
                        triger_func := triger_func || tab || tab || 'RETURN NEW;' || new_line;
                        triger_func := triger_func || tab || 'END IF;' || new_line;
                        RAISE INFO '%', triger_func;

                        triger_func := triger_func || tab || 'IF (TG_OP = ''UPDATE'') THEN' || new_line;
                        triger_func := triger_func || tab || tab || 'var_opis := ''{"old":'' || slice(hstore(OLD), akeys(hstore(NEW) - hstore(OLD)))::JSON::TEXT || E'',\n"new":''' || ' || slice(hstore(NEW), akeys(hstore(NEW) - hstore(OLD)))::JSON::TEXT || ''}'';' || new_line;
                        triger_func := triger_func || replace(triger_func_insert_parent_id, ':TABLE', 'OLD');
                        triger_func := triger_func || triger_func_insert_parent;
                        triger_func := triger_func || tab || tab || 'INSERT INTO logi.operacje(' || new_line;
                        triger_func := triger_func || triger_func_insert_columns;
                        triger_func := triger_func || tab || tab ||  ')' || new_line;
                        triger_func := triger_func || tab || tab || 'VALUES (' || new_line;
                        RAISE INFO '%', triger_func;
                        
                        triger_func := triger_func || replace(triger_func_insert_values, ':TABLE', 'OLD') || new_line;
                        RAISE INFO '%', triger_func;
                        
                        triger_func := triger_func || tab || tab || ');' || new_line;
                        triger_func := triger_func || tab || tab || 'RETURN NEW;' || new_line;
                        triger_func := triger_func || tab || 'END IF;' || new_line;
                        RAISE INFO '%', triger_func;
                        
                        triger_func := triger_func || tab || 'IF (TG_OP = ''DELETE'') THEN' || new_line;
                        triger_func := triger_func || tab || tab || 'var_opis := hstore(OLD)::JSON::TEXT;' || new_line;
                        triger_func := triger_func || replace(triger_func_insert_parent_id, ':TABLE', 'OLD');
                        triger_func := triger_func || triger_func_insert_parent;
                        triger_func := triger_func || tab || tab || 'INSERT INTO logi.operacje(' || new_line;
                        triger_func := triger_func || triger_func_insert_columns;
                        triger_func := triger_func || tab || tab || ')' || new_line;
                        triger_func := triger_func || tab || tab || 'VALUES (' || new_line;
                        RAISE INFO '%', triger_func;
                        
                        triger_func := triger_func || replace(triger_func_insert_values, ':TABLE', 'OLD') || new_line;
                        RAISE INFO '%', triger_func;
                        
                        triger_func := triger_func || tab || tab || ');' || new_line;
                        triger_func := triger_func || tab || tab || 'RETURN OLD;' || new_line;
                        triger_func := triger_func || tab || 'END IF;' || new_line;
                        triger_func := triger_func || tab || 'RETURN NEW;' || new_line;
                        triger_func := triger_func || 'END;' || new_line;
                        triger_func := triger_func || '$'||'$' || new_line;
                        triger_func := triger_func || 'LANGUAGE plpgsql;' || new_line;
                        RAISE INFO '%', triger_func;
                        
                        EXECUTE triger_func;

                        triger := 'CREATE TRIGGER dodaj_' || replace(triger_func_name, '.', '_') || '_trigger AFTER INSERT OR UPDATE ON ' || r.schemat || '.' || r.tabela || ' FOR EACH ROW EXECUTE PROCEDURE ' || triger_func_name || '();';
                        RAISE INFO '%', triger;
                        EXECUTE triger;

                        triger := 'CREATE TRIGGER usun_' || replace(triger_func_name, '.', '_') ||'_trigger  BEFORE DELETE ON ' || r.schemat || '.' || r.tabela || ' FOR EACH ROW EXECUTE PROCEDURE ' || triger_func_name || '();';
                        RAISE INFO '%', triger;
                        EXECUTE triger;
                END LOOP;
        END;
	$wlacz_logowanie$
        LANGUAGE plpgsql;
        
        PERFORM logi.wlacz_logowanie();

        EXECUTE serwis.dodaj_kolumne('serwis.konfiguracja', 'domena', 'TEXT');
END $$;