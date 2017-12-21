#### Kinh doanh ######

DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS customers
(
    customer_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_code VARCHAR(50),
    customer_name VARCHAR(200),
    customer_address VARCHAR(200),
    customer_phone VARCHAR(20),
    tax_code VARCHAR(20) COMMENT 'Mã số thuế',
    account_number VARCHAR(50) COMMENT 'Tài khoản ngân hàng',
    bank_id BIGINT COMMENT 'Tên ngân hàng',
    status INT DEFAULT 1 COMMENT '1: Hoạt động; 0: Không hoạt động',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(customer_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng khách hàng';

DROP TABLE IF EXISTS prices;
CREATE TABLE IF NOT EXISTS prices
(
    price_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_id BIGINT,
    pump_type INT COMMENT 'Loại máy bơm: bơm tĩnh, bơm qua cần phân phối,...',
    price_m3 DOUBLE COMMENT 'Đơn giá theo m3',
    price_shift DOUBLE COMMENT 'Đơn giá theo ca',
    price_wait DOUBLE COMMENT 'Đơn giá theo ca chờ',
    price_switch DOUBLE COMMENT 'Đơn giá theo ca chuyển chân',
    price_rent DOUBLE COMMENT 'Đơn giá thuê',
    convert_type INT COMMENT 'Loại chuyển đổi. 1: m3 sang m3; 2: m3 sang ca',
    convert_value DOUBLE COMMENT 'Giá trị tính chuyển đổi',
	status INT DEFAULT 1 COMMENT '1: Hoạt động; 0: Không hoạt động',
    price_type INT COMMENT '1: Giá bơm; 2: Giá cần phân phối',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(price_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng giá';

DROP TABLE IF EXISTS price_location;
CREATE TABLE IF NOT EXISTS price_location
(
    price_location_id BIGINT NOT NULL AUTO_INCREMENT,
    price_id BIGINT,
    location_type INT COMMENT 'Loại vị trí: 1 tầng; 2 các vị trí còn lại',
    location_min BIGINT COMMENT 'Từ tầng',
    location_max BIGINT COMMENT 'Đến tầng',
    price_location DOUBLE COMMENT 'Đơn giá vị trí theo m3',
    price_location_shift DOUBLE COMMENT 'Đơn giá vị trí theo ca',
	status INT DEFAULT 1 COMMENT '1: Hoạt động; 0: Không hoạt động',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(price_location_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng giá theo vị trí';

DROP TABLE IF EXISTS contracts;
CREATE TABLE IF NOT EXISTS contracts
(
    contract_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_code VARCHAR(50),
    contract_name VARCHAR(200),
    file_path VARCHAR(100),
    VAT DOUBLE COMMENT 'Thuế VAT, có giá trị thì là loại hợp đồng đã bao gồm thuế VAT',
    effective_date DATE COMMENT 'Ngày hợp đồng có hiệu lực',
    expiration_date DATE COMMENT 'Ngày hợp đồng hết hiệu lực',
    customer_id BIGINT ,
    discount DOUBLE COMMENT '% chiết khâu khách hàng',
    bill_money DOUBLE COMMENT '% làm hóa đơn',
    status INT DEFAULT 1 COMMENT '1: Hoạt động; 0: Không hoạt động',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(contract_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng hợp đồng';

DROP TABLE IF EXISTS construction;
CREATE TABLE IF NOT EXISTS construction
(
    construction_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_id BIGINT NOT NULL,
    construction_code VARCHAR(50),
    construction_name VARCHAR(100),
    construction_address VARCHAR(200),
    is_far INT DEFAULT 0 COMMENT 'Công trình xa hay không: 1 Xa; 0: không xa',
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(construction_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng công trình';

DROP TABLE IF EXISTS pumps;
CREATE TABLE IF NOT EXISTS pumps
(
    pump_id BIGINT NOT NULL AUTO_INCREMENT,
    pump_code VARCHAR(50),
    pump_name VARCHAR(200),
    pump_capacity VARCHAR(50) COMMENT 'Hiệu suất bơm',
    pump_high VARCHAR(50) COMMENT 'Khả năng bơm cao',
    pump_far VARCHAR(50) COMMENT 'Khả năng bơm xa',
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(pump_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng máy bơm';

DROP TABLE IF EXISTS distribute;
CREATE TABLE IF NOT EXISTS distribute
(
    distribute_id BIGINT NOT NULL AUTO_INCREMENT,
    distribute_code VARCHAR(50),
    distribute_name VARCHAR(200),
    distribute_year DATE COMMENT 'Năm sản xuất',
    distribute_remote INT DEFAULT 0 COMMENT 'Điều khiển từ xa 0: không có; 1: có',
    distribute_handheld INT DEFAULT 1 COMMENT 'Điều khiển cầm tay 0: không có; 1: có',
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(distribute_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng cần phân phối';

DROP TABLE IF EXISTS location;
CREATE TABLE IF NOT EXISTS location
(
    location_id BIGINT NOT NULL AUTO_INCREMENT,
    location_code VARCHAR(50),
    location_name VARCHAR(200),
    location_value INT,
    location_type INT COMMENT 'Loại vị trí 1: tầng, n các loại khác',
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(location_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng vị trí';

DROP TABLE IF EXISTS bills;
CREATE TABLE IF NOT EXISTS bills
(
    bill_id BIGINT NOT NULL AUTO_INCREMENT,
    bill_code VARCHAR(50),
    customer_id BIGINT,
    prd_id INT COMMENT 'Ngày nhập phiếu bơm',
    from_time DATETIME COMMENT 'Thời gian đến công trình',
    to_time DATETIME COMMENT 'Thời gian rời công trình',
    start_time DATETIME COMMENT 'Thời gian bắt đầu bơm',
    end_time DATETIME COMMENT 'Thời gian bơm xong',
    construction_id BIGINT,
    file_name VARCHAR(100),
    path VARCHAR(300),
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(bill_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng phiếu bơm';

DROP TABLE IF EXISTS bill_detail;
CREATE TABLE IF NOT EXISTS bill_detail
(
    bill_detail_id BIGINT NOT NULL AUTO_INCREMENT,
    bill_id BIGINT,
    pump_id BIGINT,
    pump_type INT COMMENT 'Loại máy bơm: bơm tĩnh, bơm qua cần phân phối,...',
    location_id BIGINT,
    location_type INT COMMENT 'Loại vị trí sàn, cột vách,...',
    quantity DOUBLE COMMENT 'Khối lượng bơm',
    quantity_approve DOUBLE COMMENT 'Khối lượng bơm đã duyệt',
    shift INT COMMENT 'Ca chờ',
    switch INT COMMENT 'Ca chuyển chân',
    total DOUBLE COMMENT 'Tổng tiền',
    total_approve DOUBLE COMMENT 'Tổng tiền đã duyệt',
    max_staff INT COMMENT 'Số công nhân tối đa. Mặc định là 3 đối với bơm CPP, 5 đối với bơm tĩnh, Có thể hơn nếu được GĐ duyệt',
	is_auto INT DEFAULT 1 COMMENT 'Quy đổi tự động hay không: 1 - có; 0 - không',
    quantity_convert DOUBLE COMMENT 'Giới hạn quy đổi cho công nhân',
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(bill_detail_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng phiếu bơm chi tiết';


#### Luong ######
DROP TABLE IF EXISTS staff;
CREATE TABLE IF NOT EXISTS staff
(
    staff_id BIGINT NOT NULL AUTO_INCREMENT,
    staff_code VARCHAR(10),
    staff_name VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(50),
    address VARCHAR(200),
    birthday DATE,
    department INT,
    position INT,
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(staff_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng nhân viên';

DROP TABLE IF EXISTS quantity_staff;
CREATE TABLE IF NOT EXISTS quantity_staff
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    staff_id BIGINT,
    bill_detail_id BIGINT,
    quantity DOUBLE COMMENT 'Khối lượng của từng công nhân',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng sản lượng của nhân viên theo phiếu bơm';



### USERS ####

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(100),
    full_name VARCHAR(200),
    password VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20) COMMENT 'Số điện thoai',
    address VARCHAR(200) COMMENT 'Địa chỉ',
    card VARCHAR(20) COMMENT 'Số CMT',
    type INT DEFAULT 0 COMMENT 'Loại người dùng: 1-admnin; 0-người dùng bình thường',
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng người dùng';

DROP TABLE IF EXISTS roles;
CREATE TABLE IF NOT EXISTS roles
(
    role_id BIGINT NOT NULL AUTO_INCREMENT,
    role_code VARCHAR(50),
    role_name VARCHAR(100),
    description VARCHAR(400),
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(role_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng vai trò';

DROP TABLE IF EXISTS objects;
CREATE TABLE IF NOT EXISTS objects
(
    object_id BIGINT NOT NULL AUTO_INCREMENT,
    object_code VARCHAR(50),
    object_name VARCHAR(200),
    object_type INT COMMENT 'loại đối tượng 1: chức năng danh mục; 2: chức năng báo cáo; 3: action',
    status INT DEFAULT 1,
    path VARCHAR(100) COMMENT 'đường dẫn chức năng',
    parent_id BIGINT COMMENT 'mã chức năng cha',
    icon VARCHAR(100) COMMENT 'icon của chức năng',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(object_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng danh mục chức năng';

DROP TABLE IF EXISTS role_object;
CREATE TABLE IF NOT EXISTS role_object
(
    role_object_id BIGINT NOT NULL AUTO_INCREMENT,
    role_id BIGINT,
    object_id BIGINT,
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(role_object_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng map vai trò với chức năng';

DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_role
(
    user_role_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    role_id BIGINT,
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_role_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng map user với vai trò';


DROP TABLE IF EXISTS params;
CREATE TABLE IF NOT EXISTS params
(
    param_id BIGINT NOT NULL AUTO_INCREMENT,
    param_key VARCHAR(50),
    param_value BIGINT,
    param_name VARCHAR(50),
    status INT DEFAULT 1,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(param_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Danh mục các tham số cấu hình';

# insert user admin
INSERT INTO users (user_name, full_name, password) VALUE ('admin','Admin', 'BIiUXiXpQAGjPulwa9L5Rg==');

DELIMITER $$
CREATE PROCEDURE `calculator_revenue`(IN p_construction_id BIGINT, IN p_pump_type INT, 
IN p_location_type INT, IN p_location_id BIGINT, IN p_quantity DOUBLE, IN p_num_wait INT, IN p_num_switch INT, IN p_auto_convert INT)
BEGIN
#biến dừng lặp cursor
DECLARE done INT DEFAULT FALSE;
#Biến tính tổng tiền
DECLARE total_revenue DOUBLE DEFAULT 0;
DECLARE v_m3_revenue DOUBLE DEFAULT 0;
DECLARE v_shift_revenue DOUBLE DEFAULT 0;
DECLARE v_wait_revenue DOUBLE DEFAULT 0;
DECLARE v_switch_revenue DOUBLE DEFAULT 0;
DECLARE v_location_revenue DOUBLE DEFAULT 0;

#Biến công thức tính
DECLARE v_formula_wait VARCHAR(50);
DECLARE v_formula_switch VARCHAR(50);
DECLARE v_formula_m3 VARCHAR(50);
DECLARE v_formula_shift VARCHAR(50);
DECLARE v_formula_loc_m3 VARCHAR(50);
DECLARE v_formula_loc_shift VARCHAR(50);
DECLARE v_formula VARCHAR(500) DEFAULT '';

#Biến select trong price
DECLARE v_price_m3, v_price_shift, v_price_wait, v_price_switch, v_price_location, v_price_location_shift, v_convert_value DOUBLE;
DECLARE v_convert_type INT;
DECLARE v_price_id, v_location_min, v_location_max BIGINT;

DECLARE v_is_shift DOUBLE DEFAULT 0;

DECLARE c_data CURSOR FOR 
SELECT p.price_id, p.price_m3, p.price_shift, p.price_wait, p.price_switch,  p.convert_type, p.convert_value 
FROM construction ct, contracts c, prices p 
WHERE  ct.contract_id=c.contract_id AND c.contract_id=p.contract_id AND ct.status=1 AND c.status=1 AND p.status=1
AND ct.construction_id=p_construction_id AND p.pump_type=p_pump_type AND p.price_type=1;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
OPEN c_data;		
        START_LOOP: LOOP
			FETCH c_data INTO v_price_id, v_price_m3, v_price_shift, v_price_wait, v_price_switch, v_convert_type, v_convert_value;
			IF done THEN
				LEAVE START_LOOP;
			END IF;
            BEGIN
				#Nếu có ca chờ --> Tính giá ca chờ
				IF(p_num_wait IS NOT NULL) THEN
					IF(v_price_wait IS NOT NULL) THEN
						SET v_wait_revenue=p_num_wait*v_price_wait;
						SET v_formula_wait=concat('(',p_num_wait,'*',v_price_wait,')');
                    END IF;
				END IF;
                #Nếu có ca chuyển chân --> Tính giá ca chuyển chân
				IF(p_num_switch IS NOT NULL) THEN
					IF(v_price_switch IS NOT NULL) THEN
						SET v_switch_revenue=p_num_switch*v_price_switch;
						SET v_formula_switch=concat('(',p_num_switch,'*',v_price_switch,')');
					END IF;
				END IF;
				#Tính giá bơm
                #Nếu loại quy đổi là m3
                IF(v_convert_type=1) THEN
					SET v_is_shift=0;
					#Nếu khối lượng < khối lượng giới hạn quy đổi
                    IF(p_quantity< v_convert_value AND p_auto_convert=1) THEN
						SET p_quantity = v_convert_value;
                    END IF;
                    SET v_m3_revenue =p_quantity*v_price_m3;
                    SET v_formula_m3=concat('(',p_quantity,'*',v_price_m3,')');
				#Nếu tính loại quy đổi là ca
				ELSE
					
					#Nếu khối lượng < khối lượng giới hạn quy đổi
                    IF(p_quantity< v_convert_value AND p_auto_convert=1) THEN
                        SET v_shift_revenue =v_price_shift;
                        SET v_is_shift=1;
                        SET v_formula_shift=concat('(',v_price_shift,')');
					ELSE
						SET v_is_shift=0;
						SET v_m3_revenue =p_quantity*v_price_m3;
                        SET v_formula_m3=concat('(',p_quantity,'*',v_price_m3,')');
                    END IF;
				END IF;
                #Tính giá vị trí
                SELECT location_value INTO @lo_value FROM location l WHERE l.location_id=p_location_id AND l.location_type=1;
                SELECT lmin.location_value, lmax.location_value, pl.price_location, pl.price_location_shift INTO @lo_min, @lo_max, @price_location, @price_location_shift FROM location lmin, location lmax, price_location pl  
				WHERE lmin.location_id=pl.location_min AND lmax.location_id=pl.location_max AND pl.location_type=1 AND pl.price_id=v_price_id;
                #Nêu vị trí nhỏ hơn vị trí min hoặc lớn hơn vị trí max thì tính lũy tiến
                IF(@lo_value<@lo_min) THEN
					IF(v_is_shift=0) THEN
						SET v_location_revenue= (@lo_min-@lo_value)*@price_location*p_quantity;
                        SET v_formula_loc_m3=concat('((',@lo_min,'-',@lo_value,')','*',@price_location,'*',p_quantity,')');
					ELSE
						SET v_location_revenue= (@lo_min-@lo_value)*@price_location_shift;
                        SET v_formula_loc_shift=concat('((',@lo_min,'-',@lo_value,')','*',@price_location_shift,')');
                    END IF;
                ELSE
					IF(@lo_value>@lo_max) THEN
						IF(v_is_shift=0) THEN
							SET v_location_revenue= (@lo_value-@lo_max)*@price_location*p_quantity;
                            SET v_formula_loc_m3=concat('((',@lo_value,'-',@lo_max,')','*',@price_location,'*',p_quantity,')');
						ELSE
							SET v_location_revenue= (@lo_value-@lo_max)*@price_location_shift;
                            SET v_formula_loc_shift=concat('((',@lo_value,'-',@lo_max,')','*',@price_location_shift,')');
						END IF;   
					END IF;
				END IF;
                #Nếu loại vị trí không phải là sàn
                IF(p_location_type !=1) THEN
                    SELECT price_location, price_location_shift INTO @price_location_other, @price_location_shift_other  FROM price_location p WHERE p.price_id=v_price_id AND p.location_type=p_location_type;
                    IF(v_is_shift=0) THEN
						SET v_location_revenue=v_location_revenue+@price_location_other*p_quantity;
                        SET v_formula_loc_m3=concat(v_formula_loc_m3,'+','(',price_location_other,'*',p_quantity,')');
					ELSE
						SET v_location_revenue=v_location_revenue+@price_location_shift_other;
                        SET v_formula_loc_shift=concat(v_formula_loc_shift,'+','(',price_location_shift_other,')');
					END IF;  
                END IF;
			END;
		END LOOP START_LOOP; 
        # Tổng doanh thu
        IF(v_location_revenue IS NOT NULL) THEN
			SET total_revenue=total_revenue+v_location_revenue;
        END IF;
        IF(v_m3_revenue IS NOT NULL) THEN
			SET total_revenue=total_revenue+v_m3_revenue;
        END IF;
        IF(v_wait_revenue IS NOT NULL) THEN
			SET total_revenue=total_revenue+v_wait_revenue;
        END IF;
        IF(v_shift_revenue IS NOT NULL) THEN
			SET total_revenue=total_revenue+v_shift_revenue;
        END IF;
        IF(v_switch_revenue IS NOT NULL) THEN
			SET total_revenue=total_revenue+v_switch_revenue;
        END IF;
        # Công thức
        IF(v_formula_m3 IS NOT NULL) THEN
			SET v_formula=concat(v_formula_m3);
        END IF;
        IF(v_formula_shift IS NOT NULL) THEN
			SET v_formula=concat(v_formula,'+',v_formula_shift);
        END IF;
        IF(v_formula_wait IS NOT NULL) THEN
			SET v_formula=concat(v_formula,'+',v_formula_wait);
        END IF;
        IF(v_formula_switch IS NOT NULL) THEN
			SET v_formula=concat(v_formula,'+',v_formula_switch);
        END IF;
        IF(v_formula_loc_m3 IS NOT NULL) THEN
			SET v_formula=concat(v_formula,'+',v_formula_loc_m3);
        END IF;
        IF(v_formula_loc_shift IS NOT NULL) THEN
			SET v_formula=concat(v_formula,'+',v_formula_loc_shift);
        END IF;
        SELECT total_revenue as revenue, v_formula as formula;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE `calculator_quantity`(in p_bill_detail_id bigint)
BEGIN
#Biến tính sản lượng công nhân
DECLARE v_quantity double DEFAULT 0;
#Giới hạn quy đổi sản lượng. Mặc định là 60
DECLARE v_quantity_limit INT DEFAULT 60;
# % tăng thêm đối với công trình đặc biệt
DECLARE v_percent double DEFAULT 0.25; # 25%


SELECT bd.max_staff, c.is_far, bd.quantity_convert, CASE WHEN bd.quantity_approve IS NOT NULL THEN bd.quantity_approve ELSE bd.quantity END quantity
INTO @max_staff, @is_far, @quantity_convert, @quantity
FROM bill_detail bd, bills b ,construction c
WHERE bd.bill_id=b.bill_id AND b.status=1
AND b.construction_id=c.construction_id AND c.status=1
AND bd.bill_detail_id=p_bill_detail_id AND bd.status=1;
# Lây tổng số công nhân thực hiện bơm
SELECT COUNT(*) INTO @staff_total FROM quantity_staff WHERE bill_detail_id=p_bill_detail_id;

#Nếu khối lượng bơm < hơn khối lượng công ty tính cho công nhân
IF(@quantity<v_quantity_limit) THEN
	SET v_quantity=@quantity_convert;
ELSE 
	SET v_quantity=@quantity;
END IF;
# Nếu là công trình đặc biệt
IF (@is_far = 1) THEN
	SET v_quantity=v_quantity + v_quantity*v_percent;
END IF;
#Công thức tính khối lượng: (Khối lượng * số lượng công nhân tối đa)/ số lượng công nhân thực hiện
SET v_quantity=v_quantity*@max_staff/ @staff_total;
#SET SQL_SAFE_UPDATES=0;
UPDATE  quantity_staff SET quantity=v_quantity, create_date = now() WHERE bill_detail_id=p_bill_detail_id;
#SET SQL_SAFE_UPDATES=1;
select v_quantity, @is_far,@max_staff, @staff_total;
END$$
DELIMITER ;



INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',1,'Vietcombank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',2,'Viettinbank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',3,'Agribank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',4,'BIDV');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',5,'ACB');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',6,'MB bank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',7,'VPBank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',8,'Sacombank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',9,'Oceanbank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('BANK',10,'Bắc Á Bank');
INSERT INTO params (param_key, param_value, param_name) VALUES ('PUMP_TYPE',1,'Bơm tĩnh');
INSERT INTO params (param_key, param_value, param_name) VALUES ('PUMP_TYPE',2,'Bơm cần');
INSERT INTO params (param_key, param_value, param_name) VALUES ('PUMP_TYPE',3,'Bơm cần phân phối');
INSERT INTO params (param_key, param_value, param_name) VALUES ('CONVERT_TYPE',1,'M3');
INSERT INTO params (param_key, param_value, param_name) VALUES ('CONVERT_TYPE',2,'Ca');
INSERT INTO params (param_key, param_value, param_name) VALUES ('LOCATION_TYPE',1,'Tầng');
INSERT INTO params (param_key, param_value, param_name) VALUES ('LOCATION_TYPE',2,'Cột vách');
INSERT INTO params (param_key, param_value, param_name) VALUES ('LOCATION_TYPE',3,'Cầu thang');


DROP TABLE IF EXISTS rent_equipment;
CREATE TABLE IF NOT EXISTS rent_equipment(
    rent_id BIGINT NOT NULL AUTO_INCREMENT,
    rent_type integer,
    customers_id integer,
    construction_id integer,    
    contact_id integer,
    start_date datetime,
    end_date datetime,
    average_price double default 0,
    status integer default 1,
    create_date timestamp default current_timestamp,
    PRIMARY KEY(rent_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng quan ly cho thue can phan phoi';


DELIMITER $$
CREATE PROCEDURE `proc_report_quantity`(IN p_pump_id VARCHAR(10), IN p_staff_id VARCHAR(10), IN p_from_date VARCHAR(10), IN p_to_date VARCHAR(10))
BEGIN

SELECT 
    c.construction_name as contruction,
    ppt.param_value as pump_type_value,
    ppt.param_name as pump_type,
    p.pump_name as pump_name,
    plt.param_value as location_type,
    concat(plt.param_name,' ', lower(l.location_name)) as location_name,
    DATE_FORMAT(b.prd_id, "%d/%c/%Y") as prd_id,
    s.staff_name as staff_name,
    ps.param_name as position,
    qs.quantity as quantity
FROM
    construction c,
    bills b,
    bill_detail bd,
    quantity_staff qs,
    staff s,
    params ppt,
    pumps p,
    location l,
    params ps,
    params plt
WHERE
    c.construction_id = b.construction_id
        AND c.status = 1
        AND b.status = 1
        AND b.bill_id = bd.bill_id
        AND bd.status in (1,2)
        AND bd.bill_detail_id = qs.bill_detail_id
        AND qs.staff_id = s.staff_id
        AND s.status = 1
        AND bd.pump_type = ppt.param_value
        AND ppt.status = 1
        AND ppt.param_key = 'PUMP_TYPE'
        AND s.position = ps.param_value
        AND ps.status = 1
        AND ps.param_key = 'POSITION'
        AND bd.pump_id = p.pump_id
        AND p.status = 1
        AND bd.location_id = l.location_id
        AND l.status = 1
        AND bd.location_type = plt.param_value
        AND plt.status = 1
        AND plt.param_key = 'LOCATION_TYPE'
        AND b.prd_id >= p_from_date
        AND b.prd_id <= p_to_date
        AND (s.staff_id = p_staff_id or p_staff_id is null)
        and (p.pump_id = p_pump_id or p_pump_id is null);

END$$
DELIMITER ;
