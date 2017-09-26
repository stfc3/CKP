#### Kinh doanh ######

DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS customers
(
    customer_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_code VARCHAR(50),
    customer_name VARCHAR(200),
    customer_address VARCHAR(200),
    customer_phone VARCHAR(20),
    tax_code VARCHAR(20) comment 'Mã số thuế',
    account_number VARCHAR(50) comment 'Tài khoản ngân hàng',
    bank_name VARCHAR(200) comment 'Tên ngân hàng',
    status INT DEFAULT 1 comment '1: Hoạt động; 0: Không hoạt động',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(customer_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng khách hàng';

DROP TABLE IF EXISTS prices;
CREATE TABLE IF NOT EXISTS prices
(
    price_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_id BIGINT,
    pump_id BIGINT,
    pump_type INT comment 'Loại máy bơm: bơm tĩnh, bơm qua cần phân phối,...',
    price_m3 DOUBLE comment 'Đơn giá theo m3',
    price_shift DOUBLE comment 'Đơn giá theo ca',
    price_wait DOUBLE comment 'Đơn giá theo ca chờ',
    convert_type INT comment 'Loại chuyển đổi. 1: m3 sang m3; 2: m3 sang ca',
    convert_value DOUBLE comment 'Giá trị tính chuyển đổi',
	status INT DEFAULT 1 comment '1: Hoạt động; 0: Không hoạt động',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(price_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng giá';

DROP TABLE IF EXISTS price_location;
CREATE TABLE IF NOT EXISTS price_location
(
    price_location_id BIGINT NOT NULL AUTO_INCREMENT,
    price_id BIGINT,
    location_type INT comment 'Loại vị trí: 1 tầng; 2 các vị trí còn lại',
    location_min BIGINT comment 'Từ tầng',
    location_max BIGINT comment 'Đến tầng',
    price_location DOUBLE comment 'Đơn giá theo vị trí',
	status INT DEFAULT 1 comment '1: Hoạt động; 0: Không hoạt động',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(price_location_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng giá theo vị trí';

DROP TABLE IF EXISTS contracts;
CREATE TABLE IF NOT EXISTS contracts
(
    contract_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_code VARCHAR(50),
    contract_name VARCHAR(200),
    file_path VARCHAR(100),
    VAT DOUBLE comment 'Thuế VAT, có giá trị thì là loại hợp đồng đã bao gồm thuế VAT',
    effective_date DATE comment 'Ngày hợp đồng có hiệu lực',
    expiration_date DATE comment 'Ngày hợp đồng hết hiệu lực',
    customer_id BIGINT ,
    discount DOUBLE comment '% chiết khâu khách hàng',
    bill_money DOUBLE comment '% làm hóa đơn',
    status INT DEFAULT 1 comment '1: Hoạt động; 0: Không hoạt động',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(contract_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng hợp đồng';

DROP TABLE IF EXISTS construction;
CREATE TABLE IF NOT EXISTS construction
(
    construction_id BIGINT NOT NULL AUTO_INCREMENT,
    contract_id BIGINT NOT NULL,
    construction_code VARCHAR(50),
    construction_name VARCHAR(100),
    status INT default 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(construction_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng công trình';

DROP TABLE IF EXISTS pumps;
CREATE TABLE IF NOT EXISTS pumps
(
    pump_id BIGINT NOT NULL AUTO_INCREMENT,
    pump_code VARCHAR(50),
    pump_name VARCHAR(200),
    pump_capacity VARCHAR(50) comment 'Hiệu suất bơm',
    pump_high VARCHAR(50) comment 'Khả năng bơm cao',
    pump_far VARCHAR(50) comment 'Khả năng bơm xa',
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(pump_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng máy bơm';

DROP TABLE IF EXISTS location;
CREATE TABLE IF NOT EXISTS location
(
    location_id BIGINT NOT NULL AUTO_INCREMENT,
    location_code VARCHAR(50),
    location_name VARCHAR(200),
    location_value int,
    location_type INT comment 'Loại vị trí 1: tầng, n các loại khác',
    status INT default 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(location_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng vị trí';

DROP TABLE IF EXISTS bills;
CREATE TABLE IF NOT EXISTS bills
(
    bill_id BIGINT NOT NULL AUTO_INCREMENT,
    bill_code VARCHAR(50),
    customer_id BIGINT,
    prd_id INT comment 'Ngày nhập phiếu bơm',
    from_time DATETIME comment 'Thời gian đến công trình',
    to_time DATETIME comment 'Thời gian rời công trình',
    start_time DATETIME comment 'Thời gian bắt đầu bơm',
    end_time DATETIME comment 'Thời gian bơm xong',
    construction_id BIGINT,
    status INT default 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(bill_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng phiếu bơm';

DROP TABLE IF EXISTS bill_detail;
CREATE TABLE IF NOT EXISTS bill_detail
(
    bill_detail_id BIGINT NOT NULL AUTO_INCREMENT,
    bill_id BIGINT,
    pump_id BIGINT,
    pump_type INT comment 'Loại máy bơm: bơm tĩnh, bơm qua cần phân phối,...',
    location_id BIGINT,
    location_type INT comment 'Loại vị trí sàn, cột vách,...',
    quantity DOUBLE comment 'Khối lượng bơm',
    quantity_approve DOUBLE comment 'Khối lượng bơm đã duyệt',
    shift INT comment 'Ca chờ',
    total DOUBLE comment 'Tổng tiền',
    total_approve DOUBLE comment 'Tổng tiền đã duyệt',
    max_staff INT comment 'Số công nhân tối đa. Mặc định là 3 đối với bơm CPP, 5 đối với bơm tĩnh, Có thể hơn nếu được GĐ duyệt',
	is_far INT DEFAULT 0 comment 'Công trình xa hay không: 1 Xa; 0: không xa',
    quantity_convert DOUBLE comment 'Giới hạn quy đổi cho công nhân',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(bill_detail_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng phiếu bơm chi tiết';


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
    status INT default 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(staff_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng nhân viên';

DROP TABLE IF EXISTS quantity_staff;
CREATE TABLE IF NOT EXISTS quantity_staff
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    staff_id BIGINT,
    bill_detail_id BIGINT,
    quantity DOUBLE comment 'Khối lượng của từng công nhân',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng sản lượng của nhân viên theo phiếu bơm';



### USERS ####

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(100),
    full_name VARCHAR(200),
    password VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20) comment 'Số điện thoai',
    address VARCHAR(200) comment 'Địa chỉ',
    card VARCHAR(20) comment 'Số CMT',
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng người dùng';

DROP TABLE IF EXISTS roles;
CREATE TABLE IF NOT EXISTS roles
(
    role_id BIGINT NOT NULL AUTO_INCREMENT,
    role_code VARCHAR(50),
    role_name VARCHAR(100),
    description VARCHAR(400),
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(role_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng vai trò';

DROP TABLE IF EXISTS objects;
CREATE TABLE IF NOT EXISTS objects
(
    object_id BIGINT NOT NULL AUTO_INCREMENT,
    object_code VARCHAR(50),
    object_name VARCHAR(200),
    object_type INT comment 'loại đối tượng 1: chức năng danh mục; 2: chức năng báo cáo',
    status INT DEFAULT 1,
    path VARCHAR(100) comment 'đường dẫn chức năng',
    parent_id BIGINT comment 'mã chức năng cha',
    icon VARCHAR(100) comment 'icon của chức năng',
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(object_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng danh mục chức năng';

DROP TABLE IF EXISTS role_object;
CREATE TABLE IF NOT EXISTS role_object
(
    role_object_id BIGINT NOT NULL AUTO_INCREMENT,
    role_id BIGINT,
    object_id BIGINT,
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(role_object_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng map vai trò với chức năng';

DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_role
(
    user_role_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    role_id BIGINT,
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_role_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Bảng map user với vai trò';


DROP TABLE IF EXISTS params;
CREATE TABLE IF NOT EXISTS params
(
    param_id BIGINT NOT NULL AUTO_INCREMENT,
    param_key VARCHAR(50),
    param_value BIGINT,
    param_name VARCHAR(50),
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(param_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment 'Danh mục các tham số cấu hình';
