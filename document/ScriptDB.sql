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
    bank_name VARCHAR(200) COMMENT 'Tên ngân hàng',
    status INT DEFAULT 1 COMMENT '1: Hoạt động; 0: Không hoạt động',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(customer_id)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT 'Bảng khách hàng';

DROP TABLE IF EXISTS prices;
CREATE TABLE IF NOT EXISTS prices
(
    price_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_id BIGINT,
    pump_id BIGINT,
    pump_type INT COMMENT 'Loại máy bơm: bơm tĩnh, bơm qua cần phân phối,...',
    price_m3 DOUBLE COMMENT 'Đơn giá theo m3',
    price_shift DOUBLE COMMENT 'Đơn giá theo ca',
    price_wait DOUBLE COMMENT 'Đơn giá theo ca chờ',
    convert_type INT COMMENT 'Loại chuyển đổi. 1: m3 sang m3; 2: m3 sang ca',
    convert_value DOUBLE COMMENT 'Giá trị tính chuyển đổi',
	status INT DEFAULT 1 COMMENT '1: Hoạt động; 0: Không hoạt động',
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
    price_location DOUBLE COMMENT 'Đơn giá theo vị trí',
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
    total DOUBLE COMMENT 'Tổng tiền',
    total_approve DOUBLE COMMENT 'Tổng tiền đã duyệt',
    max_staff INT COMMENT 'Số công nhân tối đa. Mặc định là 3 đối với bơm CPP, 5 đối với bơm tĩnh, Có thể hơn nếu được GĐ duyệt',
	is_far INT DEFAULT 0 COMMENT 'Công trình xa hay không: 1 Xa; 0: không xa',
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
    object_type INT COMMENT 'loại đối tượng 1: chức năng danh mục; 2: chức năng báo cáo',
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
CREATE PROCEDURE `calculator_revenue`(IN p_construction_id BIGINT, IN p_pump_id BIGINT, IN p_pump_type INT, 
IN p_location_type INT, IN p_location_id BIGINT, IN p_quantity DOUBLE, IN p_num_shift INT)
BEGIN
#biến dừng lặp cursor
DECLARE done INT DEFAULT FALSE;
#Biến tính tổng tiền
DECLARE total_revenue DOUBLE DEFAULT 0;
DECLARE v_m3_revenue DOUBLE DEFAULT 0;
DECLARE v_shift_revenue DOUBLE DEFAULT 0;
DECLARE v_wait_revenue DOUBLE DEFAULT 0;
DECLARE v_location_revenue DOUBLE DEFAULT 0;
#Biến select trong price
DECLARE v_price_m3, v_price_shift, v_price_wait, v_price_location, v_convert_value DOUBLE;
DECLARE v_convert_type INT;
DECLARE v_price_id, v_location_min, v_location_max BIGINT;

DECLARE c_data CURSOR FOR 
SELECT p.price_id, p.price_m3, p.price_shift, p.price_wait,  p.convert_type, p.convert_value 
FROM construction ct, contracts c, prices p 
WHERE  ct.contract_id=c.contract_id AND c.contract_id=p.contract_id AND ct.status=1 AND c.status=1 AND p.status=1
AND ct.construction_id=p_construction_id AND p.pump_id=p_pump_id AND p.pump_type=p_pump_type;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
OPEN c_data;		
        START_LOOP: LOOP
			FETCH c_data INTO v_price_id, v_price_m3, v_price_shift, v_price_wait, v_convert_type, v_convert_value;
			IF done THEN
				LEAVE START_LOOP;
			END IF;
            BEGIN
				#Nếu có ca chờ --> Tính giá ca chờ
				IF(p_num_shift IS NOT NULL) THEN
                    SET v_wait_revenue=p_num_shift*v_price_wait;
                    #select v_price_wait,v_wait_revenue;
				END IF;
				#Tính giá bơm
                #Nếu loại quy đổi là m3
                IF(v_convert_type=1) THEN
					#Nếu khối lượng < khối lượng giới hạn quy đổi
                    IF(p_quantity< v_convert_value) THEN
						SET p_quantity = v_convert_value;
                    END IF;
                    SET v_m3_revenue =p_quantity*v_price_m3;
				#Nếu tính loại quy đổi là ca
				ELSE
					#Nếu khối lượng < khối lượng giới hạn quy đổi
                    IF(p_quantity< v_convert_value) THEN
                        SET v_shift_revenue =v_price_shift;
					ELSE
						SET v_m3_revenue =p_quantity*v_price_m3;
                    END IF;
				END IF;
                #Tính giá vị trí
                SELECT location_value INTO @lo_value FROM location l WHERE l.location_id=p_location_id AND l.location_type=1;
                SELECT lmin.location_value, lmax.location_value, pl.price_location INTO @lo_min, @lo_max, @price_location FROM location lmin, location lmax, price_location pl  
				WHERE lmin.location_id=pl.location_min AND lmax.location_id=pl.location_max AND pl.location_type=1 AND pl.price_id=3;
                #Nêu vị trí nhỏ hơn vị trí min hoặc lớn hơn vị trí max thì tính lũy tiến
                IF(@lo_value<@lo_min) THEN
					SET v_location_revenue= (@lo_min-@lo_value)*@price_location;
                ELSE
					IF(@lo_value>@lo_max) THEN
						SET v_location_revenue= (@lo_value-@lo_max)*@price_location;
					END IF;
				END IF;
                #Nếu loại vị trí không phải là sàn
                IF(p_location_type !=1) THEN
                    SELECT price_location INTO @price_location_other  FROM price_location p WHERE p.price_id=v_price_id AND p.location_type=p_location_type;
                    SET v_location_revenue=v_location_revenue+@price_location_other;
                END IF;
			END;
		END LOOP START_LOOP; 
        SET total_revenue=v_location_revenue+v_m3_revenue+v_wait_revenue;
        SELECT total_revenue, 'Thành công' description;
END$$
DELIMITER ;