-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 07, 2018 lúc 01:54 AM
-- Phiên bản máy phục vụ: 5.7.18-log
-- Phiên bản PHP: 7.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `motel`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bill`
--

CREATE TABLE `bill` (
  `BILL_ID` int(11) NOT NULL,
  `BILL_CODE` varchar(100) NOT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `PAYMENT_DATE` date DEFAULT NULL,
  `TOTAL_PRICE` int(11) DEFAULT NULL,
  `MONTH` date DEFAULT NULL COMMENT 'Hóa đơn tháng',
  `CONTRACT_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `bill`
--

INSERT INTO `bill` (`BILL_ID`, `BILL_CODE`, `HOME_ID`, `ROOM_ID`, `CREATE_TIME`, `DESCRIPTION`, `PAYMENT_DATE`, `TOTAL_PRICE`, `MONTH`, `CONTRACT_ID`) VALUES
(10, 'SP001-00010', 1, 1, '2018-02-03 23:49:16', '', '2018-02-03', 1920000, NULL, NULL),
(11, 'SP001-00011', 1, 2, '2018-02-04 00:18:24', '', '2018-02-04', 2672610, NULL, NULL),
(12, 'SP001-00012', 1, 2, '2018-02-04 17:44:55', 'TEST PRINT', '2018-02-04', 372610, NULL, 2),
(13, 'SP001-00013', 1, 1, '2018-02-06 22:20:48', '', '2018-02-06', 1500000, NULL, NULL),
(14, 'SP001-00014', 1, 1, '2018-02-06 22:46:48', '', '2018-02-06', 1500000, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bill_service`
--

CREATE TABLE `bill_service` (
  `BILL_SERVICE_ID` int(11) NOT NULL,
  `SERVICE_ID` int(11) DEFAULT NULL,
  `AMOUNT` double DEFAULT NULL,
  `PRICE` int(11) DEFAULT NULL,
  `TOTAL_PRICE` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `BILL_ID` int(11) NOT NULL,
  `index_old` double DEFAULT NULL COMMENT 'chỉ số cũ',
  `index_new` double DEFAULT NULL COMMENT 'chỉ số mới'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `bill_service`
--

INSERT INTO `bill_service` (`BILL_SERVICE_ID`, `SERVICE_ID`, `AMOUNT`, `PRICE`, `TOTAL_PRICE`, `STATUS`, `BILL_ID`, `index_old`, `index_new`) VALUES
(26, 5, 1, 1500000, 1500000, NULL, 10, NULL, NULL),
(27, 1, 120, 3500, 420000, NULL, 10, 1, NULL),
(28, 2, 0, 30000, 0, NULL, 10, 2, NULL),
(29, 5, 1, 2500000, 2500000, NULL, 11, NULL, NULL),
(30, 1, 15, 3500, 52500, NULL, 11, 55, 70),
(31, 4, 1, 100, 100, NULL, 11, NULL, NULL),
(32, 2, 4, 30000, 120000, NULL, 11, 16, 20),
(33, 3, 1, 10, 10, NULL, 11, NULL, NULL),
(34, 5, 1, 200000, 200000, NULL, 12, NULL, NULL),
(35, 1, 15, 3500, 52500, NULL, 12, 55, 70),
(36, 4, 1, 100, 100, NULL, 12, NULL, NULL),
(37, 2, 4, 30000, 120000, NULL, 12, 16, 20),
(38, 3, 1, 10, 10, NULL, 12, NULL, NULL),
(39, 5, 1, 1500000, 1500000, NULL, 13, NULL, NULL),
(40, 5, 1, 1500000, 1500000, NULL, 14, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_dictionary`
--

CREATE TABLE `cat_dictionary` (
  `CAT_CODE` varchar(30) NOT NULL,
  `CAT_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `EDITABLE` int(2) DEFAULT NULL,
  `REGEX_VALUE` varchar(255) DEFAULT NULL,
  `REQUIRE_VALUE` int(2) DEFAULT NULL,
  `MESSAGE_VALID` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `cat_dictionary`
--

INSERT INTO `cat_dictionary` (`CAT_CODE`, `CAT_NAME`, `EDITABLE`, `REGEX_VALUE`, `REQUIRE_VALUE`, `MESSAGE_VALID`) VALUES
('UNIT', 'Đơn vị', 1, '', 0, '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_item`
--

CREATE TABLE `cat_item` (
  `ITEM_ID` int(11) NOT NULL,
  `CODE` varchar(250) DEFAULT NULL,
  `NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE` varchar(25) DEFAULT NULL,
  `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CAT_CODE` varchar(30) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `CREATE_TIME` datetime(6) DEFAULT NULL,
  `UPDATE_TIME` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `NOTE` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `cat_item`
--

INSERT INTO `cat_item` (`ITEM_ID`, `CODE`, `NAME`, `VALUE`, `DESCRIPTION`, `CAT_CODE`, `STATUS`, `CREATE_TIME`, `UPDATE_TIME`, `NOTE`) VALUES
(1, 'OTHER', 'Khác', '', '', 'UNIT', NULL, NULL, '2018-01-18 19:01:04.000000', NULL),
(2, 'ROOM_PER_MONTH', 'phòng/tháng', '', '', 'UNIT', NULL, NULL, '2018-01-20 19:23:17.244638', NULL),
(3, 'kWh', 'kWh', '', '', 'UNIT', NULL, NULL, '2018-01-20 19:23:53.438539', NULL),
(4, 'm3', 'khối', '', '', 'UNIT', NULL, NULL, '2018-01-20 19:24:58.736097', NULL),
(5, 'person_month', 'người/tháng', '', '', 'UNIT', NULL, NULL, '2018-01-20 19:27:21.783321', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_role`
--

CREATE TABLE `cat_role` (
  `ROLE_ID` int(11) NOT NULL,
  `ROLE_NAME` varchar(200) DEFAULT NULL,
  `ROLE_CODE` varchar(250) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `cat_role`
--

INSERT INTO `cat_role` (`ROLE_ID`, `ROLE_NAME`, `ROLE_CODE`, `STATUS`) VALUES
(1, 'admin', 'admin', 1),
(2, 'Khách', 'KHACH', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_user`
--

CREATE TABLE `cat_user` (
  `USER_ID` int(11) NOT NULL,
  `USER_NAME` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `PASSWORD` varchar(200) CHARACTER SET latin1 DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  `EMP_ID` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `full_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `GROUP_ID` int(11) DEFAULT '1' COMMENT 'Nhóm người dùng'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cat_user`
--

INSERT INTO `cat_user` (`USER_ID`, `USER_NAME`, `PASSWORD`, `ROLE_ID`, `EMP_ID`, `STATUS`, `full_name`, `GROUP_ID`) VALUES
(1, 'admin', 'admin', 1, 0, 1, 'Nguyễn Văn Việt', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `contract`
--

CREATE TABLE `contract` (
  `CONTRACT_ID` int(11) NOT NULL,
  `CONTRACT_CODE` varchar(100) DEFAULT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) DEFAULT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `START_TIME` date DEFAULT NULL,
  `END_TIME` date DEFAULT NULL,
  `DEPOSIT` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL COMMENT '1: hoạt động, 2: hết hạn',
  `CREATE_TIME` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `contract`
--

INSERT INTO `contract` (`CONTRACT_ID`, `CONTRACT_CODE`, `HOME_ID`, `ROOM_ID`, `CUSTOMER_ID`, `START_TIME`, `END_TIME`, `DEPOSIT`, `DESCRIPTION`, `STATUS`, `CREATE_TIME`) VALUES
(1, 'HD001-00002', 1, 1, 1, '2018-01-22', NULL, 540600000, 'ok', 2, NULL),
(2, 'HD001-00001', 1, 2, 2, '2018-01-22', '2018-02-04', 5000000, '', 1, NULL),
(3, 'HD001-00003', 1, 1, 3, NULL, NULL, 100000, '', 2, NULL),
(4, 'HD001-00004', 1, 1, 2, NULL, NULL, NULL, '', 2, NULL),
(5, 'HD001-00005', 1, 1, 2, NULL, NULL, NULL, '', 2, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `contract_service`
--

CREATE TABLE `contract_service` (
  `CONTRACT_ID` int(11) NOT NULL,
  `SERVICE_ID` int(11) NOT NULL,
  `CONTRACT_SERVICE_ID` int(11) NOT NULL,
  `INSERT_TIME` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `contract_service`
--

INSERT INTO `contract_service` (`CONTRACT_ID`, `SERVICE_ID`, `CONTRACT_SERVICE_ID`, `INSERT_TIME`) VALUES
(1, 1, 1, '2018-01-24 22:58:54'),
(1, 2, 2, '2018-01-24 22:58:54'),
(2, 1, 3, '2018-01-24 22:58:04'),
(2, 4, 4, '2018-01-24 22:58:04'),
(2, 2, 5, '2018-01-24 22:58:04'),
(2, 3, 6, '2018-01-24 22:58:04'),
(3, 1, 7, '2018-02-07 00:29:07'),
(3, 2, 8, '2018-02-07 00:29:07'),
(3, 3, 9, '2018-02-07 00:29:07'),
(4, 1, 10, '2018-02-07 00:32:19'),
(4, 2, 11, '2018-02-07 00:32:19'),
(4, 3, 12, '2018-02-07 00:32:19'),
(4, 1, 13, '2018-02-07 00:32:47'),
(4, 2, 14, '2018-02-07 00:32:47'),
(4, 3, 15, '2018-02-07 00:32:47'),
(5, 1, 16, '2018-02-07 00:38:03'),
(5, 2, 17, '2018-02-07 00:38:05'),
(5, 3, 18, '2018-02-07 00:38:07');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `CUSTOMER_ID` int(20) NOT NULL,
  `CUSTOMER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `SEX` int(10) DEFAULT NULL,
  `BIRTH_DATE` date DEFAULT NULL,
  `ADDRESS` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `STATUS` int(10) DEFAULT '1',
  `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `CMND` varchar(20) DEFAULT NULL,
  `NGAY_CAP` date DEFAULT NULL,
  `NOI_CAP` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` date DEFAULT NULL,
  `GROUP_USER_ID` int(10) DEFAULT '1',
  `img_path` varchar(100) DEFAULT NULL,
  `home_town` varchar(250) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `customer`
--

INSERT INTO `customer` (`CUSTOMER_ID`, `CUSTOMER_NAME`, `SEX`, `BIRTH_DATE`, `ADDRESS`, `PHONE`, `STATUS`, `DESCRIPTION`, `EMAIL`, `CMND`, `NGAY_CAP`, `NOI_CAP`, `UPDATE_TIME`, `GROUP_USER_ID`, `img_path`, `home_town`) VALUES
(1, 'Nguyễn Văn Việt', 1, '1992-01-01', 'Hà nội', '01663734698', 1, 'ok', 'vietnv.th@gmail.com', '123123', '2010-02-20', 'Thanh Hóa', NULL, 1, NULL, 'Hoằng Xuân, Hoằng Hóa, Thanh Hóa'),
(2, 'Nguyễn A', 1, NULL, '', '0123456778', 1, '', 'vietnv', '', NULL, '', NULL, 1, NULL, ''),
(3, 'Lê Thị Bé', 2, NULL, '', '0123456789', 1, '', 'gai', '', NULL, '', NULL, 1, NULL, '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer_room`
--

CREATE TABLE `customer_room` (
  `CUSTOMER_ROOM_ID` int(11) NOT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) NOT NULL,
  `START_TIME` date DEFAULT NULL,
  `END_TIME` date DEFAULT NULL,
  `STATUS` int(10) DEFAULT NULL COMMENT '-1 xoa, 1: hoat dong, 2 da tung su dung',
  `CREATE_TIME` datetime DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL COMMENT '1:chính, 2: ở ghép',
  `CONTRACT_ID` int(11) DEFAULT NULL COMMENT 'khách hàng theo hợp đồng'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `customer_room`
--

INSERT INTO `customer_room` (`CUSTOMER_ROOM_ID`, `CUSTOMER_ID`, `ROOM_ID`, `START_TIME`, `END_TIME`, `STATUS`, `CREATE_TIME`, `TYPE`, `CONTRACT_ID`) VALUES
(1, 1, 1, NULL, '2018-02-06', -1, '2018-01-22 01:26:01', 1, 0),
(2, 1, 2, NULL, NULL, 1, '2018-01-22 01:40:22', 2, 0),
(3, 3, 2, '2018-02-07', NULL, 2, '2018-02-07 00:15:31', 2, 2),
(4, 2, 2, '2018-02-07', NULL, 1, '2018-02-07 00:19:16', 1, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `electric_water`
--

CREATE TABLE `electric_water` (
  `electric_water_id` int(11) NOT NULL,
  `ROOM_ID` int(11) NOT NULL,
  `ELECTRIC_OLD` double DEFAULT NULL,
  `WATER_OLD` double DEFAULT NULL,
  `ELECTRIC_NEW` double DEFAULT NULL,
  `WATER_NEW` double DEFAULT NULL,
  `CREATE_TIME` date DEFAULT NULL COMMENT 'Thời gian tạo',
  `TIME_LINE` date DEFAULT NULL COMMENT 'Ngày chốt số(số ngày chốt sau không được nhỏ hơn số chốt lần trước)',
  `STATUS` int(11) NOT NULL DEFAULT '1',
  `MONTH` date DEFAULT NULL COMMENT 'Tháng lập hóa đơn'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `electric_water`
--

INSERT INTO `electric_water` (`electric_water_id`, `ROOM_ID`, `ELECTRIC_OLD`, `WATER_OLD`, `ELECTRIC_NEW`, `WATER_NEW`, `CREATE_TIME`, `TIME_LINE`, `STATUS`, `MONTH`) VALUES
(1, 1, 1, 2, NULL, NULL, '2018-01-25', '2018-01-25', 1, '2018-02-01'),
(2, 2, 5, 2, 10, 7, '2018-01-25', '2018-01-24', 1, '2018-03-01'),
(5, 2, 55, 16, 70, 20, '2018-01-25', '2018-01-25', 1, '2018-02-01');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `function_path`
--

CREATE TABLE `function_path` (
  `FUNCTION_PATH_ID` int(11) NOT NULL,
  `URL` varchar(250) DEFAULT NULL,
  `NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `function_path`
--

INSERT INTO `function_path` (`FUNCTION_PATH_ID`, `URL`, `NAME`, `STATUS`, `TYPE`, `PARENT_ID`) VALUES
(1, '/', 'Quản lý chung', 1, 1, NULL),
(2, '/', 'Danh mục', 1, 1, NULL),
(3, '/', 'Quản trị', 1, 1, NULL),
(4, '/bill', 'Hóa đơn', 1, 2, 1),
(5, '/customerMotel', 'Khách hàng', 1, 2, 1),
(6, '/contractMotel', 'Hợp đồng', 1, 2, 1),
(7, '/electricWater', 'Ghi điện, nước', 1, 2, 1),
(8, '/roomMotel', 'Phòng trọ', 1, 2, 2),
(9, '/catUser', 'Acount', 1, 2, 3),
(10, '/catRole', 'Phân quyền', 1, 2, 3),
(11, '/logAction', 'Lịch sử tác động', 1, 2, 3),
(12, '/document', 'Hướng dẫn', 1, 2, 3),
(13, '/dashboard', 'Trang chủ', 1, 2, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `group_user`
--

CREATE TABLE `group_user` (
  `ID` int(11) NOT NULL,
  `CODE` varchar(250) DEFAULT NULL,
  `NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `group_user`
--

INSERT INTO `group_user` (`ID`, `CODE`, `NAME`, `STATUS`, `START_TIME`, `END_TIME`, `CREATE_TIME`) VALUES
(1, 'admin', 'quản lý', 1, '2018-01-16 00:00:00', '2018-01-31 00:00:00', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `home`
--

CREATE TABLE `home` (
  `HOME_ID` int(11) NOT NULL,
  `HOME_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ADDRESS` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` int(20) DEFAULT NULL,
  `GROUP_USER_ID` int(10) DEFAULT '1',
  `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `HOME_CODE` varchar(20) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `home`
--

INSERT INTO `home` (`HOME_ID`, `HOME_NAME`, `ADDRESS`, `STATUS`, `GROUP_USER_ID`, `DESCRIPTION`, `HOME_CODE`) VALUES
(1, 'Hà Nội', '', 0, 1, '', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `log_action`
--

CREATE TABLE `log_action` (
  `LOG_ACTION_ID` int(11) NOT NULL,
  `CLIENT_IP` varchar(50) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `USER_NAME` varchar(250) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `EVENT_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ACTION_TYPE` int(11) DEFAULT NULL,
  `FUNCTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `OBJECT_CODE` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `OLD_VALUE` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `NEW_VALUE` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `NOTE` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `log_action`
--

INSERT INTO `log_action` (`LOG_ACTION_ID`, `CLIENT_IP`, `USER_NAME`, `EVENT_TIME`, `ACTION_TYPE`, `FUNCTION`, `CLASS_NAME`, `OBJECT_CODE`, `OLD_VALUE`, `NEW_VALUE`, `NOTE`) VALUES
(1, '0:0:0:0:0:0:0:1', 'admin', '2018-01-21 17:00:00', 2, 'onSaveOrUpdate', 'ContractController', NULL, 'model.Contract[ contractId=1 ]', 'model.Contract[ contractId=1 ]', NULL),
(2, '0:0:0:0:0:0:0:1', 'admin', '2018-01-21 17:00:00', 1, 'onSaveOrUpdate', 'ContractController', NULL, NULL, 'model.Contract[ contractId=2 ]', NULL),
(3, '0:0:0:0:0:0:0:1', 'admin', '2018-01-21 17:00:00', 2, 'onSaveOrUpdate', 'ContractController', NULL, 'model.Contract[ contractId=1 ]', 'model.Contract[ contractId=1 ]', NULL),
(4, '0:0:0:0:0:0:0:1', 'admin', '2018-01-23 17:00:00', 2, 'onSaveOrUpdate', 'ContractController', NULL, 'model.Contract[ contractId=2 ]', 'model.Contract[ contractId=2 ]', NULL),
(5, '0:0:0:0:0:0:0:1', 'admin', '2018-01-23 17:00:00', 2, 'onSaveOrUpdate', 'ContractController', NULL, 'model.Contract[ contractId=1 ]', 'model.Contract[ contractId=1 ]', NULL),
(6, '0:0:0:0:0:0:0:1', 'admin', '2018-01-24 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=1 ]', NULL),
(7, '0:0:0:0:0:0:0:1', 'admin', '2018-01-24 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=2 ]', NULL),
(8, '0:0:0:0:0:0:0:1', 'admin', '2018-01-24 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=3 ]', NULL),
(9, '0:0:0:0:0:0:0:1', 'admin', '2018-01-24 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=4 ]', NULL),
(10, '0:0:0:0:0:0:0:1', 'admin', '2018-01-24 17:00:00', 2, 'onSaveOrUpdate', 'ElectricWaterController', NULL, 'model.ElectricWater[ electricWaterId=4 ]', 'model.ElectricWater[ electricWaterId=5 ]', NULL),
(11, '0:0:0:0:0:0:0:1', 'admin', '2018-02-02 17:00:00', 2, 'onSaveOrUpdate', 'ElectricWaterController', NULL, 'model.ElectricWater[ electricWaterId=5 ]', 'model.ElectricWater[ electricWaterId=5 ]', NULL),
(12, '0:0:0:0:0:0:0:1', 'admin', '2018-02-02 17:00:00', 1, 'onSaveOrUpdate', 'BillController', NULL, NULL, 'model.Bill[ billId=10 ]', NULL),
(13, '0:0:0:0:0:0:0:1', 'admin', '2018-02-02 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=3 ]', 'model.Bill[ billId=3 ]', NULL),
(14, '0:0:0:0:0:0:0:1', 'admin', '2018-02-02 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=6 ]', 'model.Bill[ billId=3 ]', NULL),
(15, '0:0:0:0:0:0:0:1', 'admin', '2018-02-02 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=2 ]', 'model.Bill[ billId=3 ]', NULL),
(16, '0:0:0:0:0:0:0:1', 'admin', '2018-02-02 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=5 ]', 'model.Bill[ billId=3 ]', NULL),
(17, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 2, 'onSaveOrUpdate', 'BillController', NULL, 'model.Bill[ billId=10 ]', 'model.Bill[ billId=10 ]', NULL),
(18, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=8 ]', 'model.Bill[ billId=8 ]', NULL),
(19, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=9 ]', 'model.Bill[ billId=8 ]', NULL),
(20, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=4 ]', 'model.Bill[ billId=8 ]', NULL),
(21, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=7 ]', 'model.Bill[ billId=null ]', NULL),
(22, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 2, 'onSaveOrUpdate', 'BillController', NULL, 'model.Bill[ billId=1 ]', 'model.Bill[ billId=1 ]', NULL),
(23, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 3, 'onDelete', 'BillController', NULL, 'model.Bill[ billId=1 ]', 'model.Bill[ billId=1 ]', NULL),
(24, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 2, 'onSaveOrUpdate', 'BillController', NULL, 'model.Bill[ billId=10 ]', 'model.Bill[ billId=11 ]', NULL),
(25, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 2, 'onSaveOrUpdate', 'BillController', NULL, 'model.Bill[ billId=11 ]', 'model.Bill[ billId=11 ]', NULL),
(26, '0:0:0:0:0:0:0:1', 'admin', '2018-02-03 17:00:00', 1, 'onSaveOrUpdate', 'BillController', NULL, NULL, 'model.Bill[ billId=12 ]', NULL),
(27, '0:0:0:0:0:0:0:1', 'admin', '2018-02-04 17:00:00', 1, 'onSaveOrUpdate', 'RoleController', NULL, NULL, 'com.slook.model.CatRole@590042b6[\r\n  roleId=2\r\n  roleName=Khách\r\n  roleCode=KHACH\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=<null>\r\n  status=1\r\n  statusName=<null>\r\n]', NULL),
(28, '0:0:0:0:0:0:0:1', 'admin', '2018-02-05 17:00:00', 1, 'onSaveOrUpdate', 'BillController', NULL, NULL, 'model.Bill[ billId=13 ]', NULL),
(29, '0:0:0:0:0:0:0:1', 'admin', '2018-02-05 17:00:00', 1, 'onSaveOrUpdate', 'BillController', NULL, NULL, 'model.Bill[ billId=14 ]', NULL),
(30, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 1, 'onSaveOrUpdate', 'CustomerController', NULL, NULL, 'model.Customer[ customerId=2 ]', NULL),
(31, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 1, 'onSaveOrUpdate', 'CustomerController', NULL, NULL, 'model.Customer[ customerId=3 ]', NULL),
(32, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onSaveCustomerRoom', 'RoomController', NULL, 'model.Room[ roomId=2 ]', 'model.CustomerRoom[ customerRoomId=null ]', NULL),
(33, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onSaveCustomerRoom', 'RoomController', NULL, 'model.Room[ roomId=2 ]', 'model.CustomerRoom[ customerRoomId=null ]', NULL),
(34, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onSaveCustomerRoom', 'RoomController', NULL, 'model.Room[ roomId=2 ]', 'model.CustomerRoom[ customerRoomId=3 ]', NULL),
(35, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onSaveCustomerRoom', 'RoomController', NULL, 'model.Room[ roomId=2 ]', 'model.CustomerRoom[ customerRoomId=3 ]', NULL),
(36, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onSaveCustomerRoom', 'RoomController', NULL, 'model.Room[ roomId=2 ]', 'model.CustomerRoom[ customerRoomId=4 ]', NULL),
(37, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onChangeTypeToPrimary', 'RoomController', NULL, 'model.Contract[ contractId=2 ]', 'model.Contract[ contractId=2 ]', NULL),
(38, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onChangeTypeToPrimary', 'RoomController', NULL, NULL, 'model.CustomerRoom[ customerRoomId=null ]', NULL),
(39, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onCheckoutCustomer', 'RoomController', NULL, 'model.CustomerRoom[ customerRoomId=3 ]', 'model.CustomerRoom[ customerRoomId=3 ]', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role_has_function_path`
--

CREATE TABLE `role_has_function_path` (
  `ID` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  `FUNCTION_PATH_ID` int(11) NOT NULL,
  `STATUS` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `role_has_function_path`
--

INSERT INTO `role_has_function_path` (`ID`, `ROLE_ID`, `FUNCTION_PATH_ID`, `STATUS`) VALUES
(1, 1, 2, 1),
(2, 1, 8, 1),
(3, 1, 1, 1),
(4, 1, 7, 1),
(5, 1, 4, 1),
(6, 1, 6, 1),
(7, 1, 5, 1),
(8, 1, 3, 1),
(9, 1, 10, 1),
(10, 1, 9, 1),
(11, 1, 12, 1),
(12, 1, 11, 1),
(13, 1, 13, 1),
(14, 2, 13, 1),
(15, 2, 1, 1),
(16, 2, 7, 1),
(17, 2, 4, 1),
(18, 2, 6, 1),
(19, 2, 5, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room`
--

CREATE TABLE `room` (
  `ROOM_ID` int(11) NOT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `PRICE` int(20) DEFAULT NULL,
  `STATUS` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `room`
--

INSERT INTO `room` (`ROOM_ID`, `HOME_ID`, `ROOM_NAME`, `PRICE`, `STATUS`) VALUES
(1, 1, 'Phòng 101', 1500000, 1),
(2, 1, 'Phòng 102', 200000, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `service`
--

CREATE TABLE `service` (
  `SERVICE_ID` int(11) NOT NULL,
  `SERVICE_CODE` varchar(20) DEFAULT NULL COMMENT 'mã ngầm định cho dịch vụ điện/nước',
  `SERVICE_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `PRICE` int(11) DEFAULT NULL,
  `UNIT` int(11) DEFAULT NULL,
  `GROUP_USER_ID` int(10) DEFAULT NULL,
  `DEFAULT_STATUS` int(11) DEFAULT '0' COMMENT 'Mặc định có khi tạo hợp đồng hay không',
  `STATUS` int(11) DEFAULT NULL COMMENT '1: giá trị mặc định khi khởi tạo nhóm không được xóa;(mặc định sẽ phải có  2dich vụ điện nước) ,'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `service`
--

INSERT INTO `service` (`SERVICE_ID`, `SERVICE_CODE`, `SERVICE_NAME`, `PRICE`, `UNIT`, `GROUP_USER_ID`, `DEFAULT_STATUS`, `STATUS`) VALUES
(1, 'ELECTRIC', 'Điện', 3500, 3, 1, 1, 1),
(2, 'WATER', 'Nước', 30000, 4, 1, 1, 1),
(3, 'VESINH', 'Vệ sinh', 10, 2, 1, 1, 2),
(4, 'Internet', 'Internet(mạng)', 100, 2, 1, 0, 2),
(5, 'PRICE_ROOM', 'Giá thuê phòng', NULL, 2, NULL, 0, 1),
(6, NULL, 'Phòng 101', NULL, NULL, NULL, NULL, NULL),
(7, NULL, 'Phòng 102', NULL, NULL, NULL, NULL, NULL),
(8, NULL, 'Phòng 102', NULL, NULL, NULL, NULL, NULL),
(9, NULL, 'Phòng 101', NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc đóng vai cho view `v_cat_item`
-- (See below for the actual view)
--
CREATE TABLE `v_cat_item` (
`ITEM_ID` int(11)
,`ITEM_CODE` varchar(250)
,`ITEM_NAME` varchar(250)
,`DESCRIPTION` varchar(500)
,`VALUE` varchar(25)
,`CAT_CODE` varchar(30)
,`EDITABLE` int(2)
,`CAT_NAME` varchar(30)
);

-- --------------------------------------------------------

--
-- Cấu trúc cho view `v_cat_item`
--
DROP TABLE IF EXISTS `v_cat_item`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_cat_item`  AS  select `a`.`ITEM_ID` AS `ITEM_ID`,`a`.`CODE` AS `ITEM_CODE`,`a`.`NAME` AS `ITEM_NAME`,`a`.`DESCRIPTION` AS `DESCRIPTION`,`a`.`VALUE` AS `VALUE`,`a`.`CAT_CODE` AS `CAT_CODE`,`b`.`EDITABLE` AS `EDITABLE`,`b`.`CAT_CODE` AS `CAT_NAME` from (`cat_item` `a` join `cat_dictionary` `b` on((`a`.`CAT_CODE` = `b`.`CAT_CODE`))) ;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`BILL_ID`),
  ADD KEY `HOME_ID` (`HOME_ID`),
  ADD KEY `ROOM_ID` (`ROOM_ID`);

--
-- Chỉ mục cho bảng `bill_service`
--
ALTER TABLE `bill_service`
  ADD PRIMARY KEY (`BILL_SERVICE_ID`),
  ADD KEY `bill_service_ibfk_1` (`BILL_ID`);

--
-- Chỉ mục cho bảng `cat_dictionary`
--
ALTER TABLE `cat_dictionary`
  ADD PRIMARY KEY (`CAT_CODE`);

--
-- Chỉ mục cho bảng `cat_item`
--
ALTER TABLE `cat_item`
  ADD PRIMARY KEY (`ITEM_ID`);

--
-- Chỉ mục cho bảng `cat_role`
--
ALTER TABLE `cat_role`
  ADD PRIMARY KEY (`ROLE_ID`);

--
-- Chỉ mục cho bảng `cat_user`
--
ALTER TABLE `cat_user`
  ADD PRIMARY KEY (`USER_ID`);

--
-- Chỉ mục cho bảng `contract`
--
ALTER TABLE `contract`
  ADD PRIMARY KEY (`CONTRACT_ID`),
  ADD KEY `CUSTOMER_ID` (`CUSTOMER_ID`);

--
-- Chỉ mục cho bảng `contract_service`
--
ALTER TABLE `contract_service`
  ADD PRIMARY KEY (`CONTRACT_SERVICE_ID`),
  ADD KEY `CONTRACT_ID` (`CONTRACT_ID`),
  ADD KEY `SERVICE_ID` (`SERVICE_ID`);

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`CUSTOMER_ID`);

--
-- Chỉ mục cho bảng `customer_room`
--
ALTER TABLE `customer_room`
  ADD PRIMARY KEY (`CUSTOMER_ROOM_ID`),
  ADD KEY `CUSTOMER_ID` (`CUSTOMER_ID`),
  ADD KEY `ROOM_ID` (`ROOM_ID`);

--
-- Chỉ mục cho bảng `electric_water`
--
ALTER TABLE `electric_water`
  ADD PRIMARY KEY (`electric_water_id`),
  ADD KEY `ROOM_ID` (`ROOM_ID`);

--
-- Chỉ mục cho bảng `function_path`
--
ALTER TABLE `function_path`
  ADD PRIMARY KEY (`FUNCTION_PATH_ID`);

--
-- Chỉ mục cho bảng `group_user`
--
ALTER TABLE `group_user`
  ADD PRIMARY KEY (`ID`);

--
-- Chỉ mục cho bảng `home`
--
ALTER TABLE `home`
  ADD PRIMARY KEY (`HOME_ID`);

--
-- Chỉ mục cho bảng `log_action`
--
ALTER TABLE `log_action`
  ADD PRIMARY KEY (`LOG_ACTION_ID`);

--
-- Chỉ mục cho bảng `role_has_function_path`
--
ALTER TABLE `role_has_function_path`
  ADD PRIMARY KEY (`ID`);

--
-- Chỉ mục cho bảng `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`ROOM_ID`),
  ADD KEY `HOME_ID` (`HOME_ID`);

--
-- Chỉ mục cho bảng `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`SERVICE_ID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bill`
--
ALTER TABLE `bill`
  MODIFY `BILL_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `bill_service`
--
ALTER TABLE `bill_service`
  MODIFY `BILL_SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT cho bảng `cat_item`
--
ALTER TABLE `cat_item`
  MODIFY `ITEM_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `cat_role`
--
ALTER TABLE `cat_role`
  MODIFY `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `cat_user`
--
ALTER TABLE `cat_user`
  MODIFY `USER_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `contract`
--
ALTER TABLE `contract`
  MODIFY `CONTRACT_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `contract_service`
--
ALTER TABLE `contract_service`
  MODIFY `CONTRACT_SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT cho bảng `customer`
--
ALTER TABLE `customer`
  MODIFY `CUSTOMER_ID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `customer_room`
--
ALTER TABLE `customer_room`
  MODIFY `CUSTOMER_ROOM_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `electric_water`
--
ALTER TABLE `electric_water`
  MODIFY `electric_water_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `function_path`
--
ALTER TABLE `function_path`
  MODIFY `FUNCTION_PATH_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT cho bảng `group_user`
--
ALTER TABLE `group_user`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `home`
--
ALTER TABLE `home`
  MODIFY `HOME_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `log_action`
--
ALTER TABLE `log_action`
  MODIFY `LOG_ACTION_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT cho bảng `role_has_function_path`
--
ALTER TABLE `role_has_function_path`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT cho bảng `room`
--
ALTER TABLE `room`
  MODIFY `ROOM_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `service`
--
ALTER TABLE `service`
  MODIFY `SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bill`
--
ALTER TABLE `bill`
  ADD CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`HOME_ID`) REFERENCES `home` (`HOME_ID`),
  ADD CONSTRAINT `bill_ibfk_2` FOREIGN KEY (`ROOM_ID`) REFERENCES `room` (`ROOM_ID`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `bill_service`
--
ALTER TABLE `bill_service`
  ADD CONSTRAINT `bill_service_ibfk_1` FOREIGN KEY (`BILL_ID`) REFERENCES `bill` (`BILL_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `contract`
--
ALTER TABLE `contract`
  ADD CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`CUSTOMER_ID`);

--
-- Các ràng buộc cho bảng `contract_service`
--
ALTER TABLE `contract_service`
  ADD CONSTRAINT `contract_service_ibfk_1` FOREIGN KEY (`CONTRACT_ID`) REFERENCES `contract` (`CONTRACT_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `contract_service_ibfk_2` FOREIGN KEY (`SERVICE_ID`) REFERENCES `service` (`SERVICE_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `customer_room`
--
ALTER TABLE `customer_room`
  ADD CONSTRAINT `customer_room_ibfk_1` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`CUSTOMER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `customer_room_ibfk_2` FOREIGN KEY (`ROOM_ID`) REFERENCES `room` (`ROOM_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `electric_water`
--
ALTER TABLE `electric_water`
  ADD CONSTRAINT `electric_water_ibfk_1` FOREIGN KEY (`ROOM_ID`) REFERENCES `room` (`ROOM_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`HOME_ID`) REFERENCES `home` (`HOME_ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
