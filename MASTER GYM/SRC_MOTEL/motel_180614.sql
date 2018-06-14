-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 14, 2018 lúc 05:33 PM
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
(14, 'SP001-00014', 1, 1, '2018-02-06 22:46:48', '', '2018-02-06', 1500000, NULL, NULL),
(15, 'SP001-00015', 1, 2, '2018-02-25 01:50:52', '', '2018-01-29', 372610, NULL, 2),
(16, 'SP006-00001', 5, 6, '2018-06-07 23:50:13', 'Tạo hóa đơn', '2018-06-07', 2000000, NULL, 7);

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
(40, 5, 1, 1500000, 1500000, NULL, 14, NULL, NULL),
(41, 5, 1, 200000, 200000, NULL, 15, NULL, NULL),
(42, 1, 15, 3500, 52500, NULL, 15, 55, 70),
(43, 4, 1, 100, 100, NULL, 15, NULL, NULL),
(44, 2, 4, 30000, 120000, NULL, 15, 16, 20),
(45, 3, 1, 10, 10, NULL, 15, NULL, NULL),
(46, 5, 1, 2000000, 2000000, NULL, 16, NULL, NULL);

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
(2, 'Khách hàng', 'CUSTOMER', 1),
(3, 'super admin', 'supper_admin', 1);

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
  `GROUP_USER_ID` int(11) DEFAULT '1' COMMENT 'Nhóm người dùng',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Email',
  `PHONE_NUMBER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SEX` int(10) DEFAULT NULL,
  `BIRTH_DATE` date DEFAULT NULL,
  `ADDRESS` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `img_path` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `job` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cat_user`
--

INSERT INTO `cat_user` (`USER_ID`, `USER_NAME`, `PASSWORD`, `ROLE_ID`, `EMP_ID`, `STATUS`, `full_name`, `GROUP_USER_ID`, `email`, `PHONE_NUMBER`, `SEX`, `BIRTH_DATE`, `ADDRESS`, `DESCRIPTION`, `img_path`, `job`) VALUES
(1, 'admin', 'admin', 1, 0, 1, 'Nguyễn Văn Việt', 0, '', '', 1, '1992-08-02', 'Thanh hóa', 'my application', NULL, 'IT'),
(2, 'vietnv', 'vietnv', 3, NULL, 1, 'Việt Nguyễn', 1, 'vietnv123@gmail.com', '', NULL, NULL, '', '', NULL, ''),
(3, 'vietnv123', '123', 3, NULL, 1, 'Nguyễn văn Việt', 0, '', '123123123', NULL, NULL, '', '', NULL, ''),
(4, 'user1', 'user1', 1, NULL, 1, 'user1', 0, 'vietnv0@gmail.com', '01663734698', 1, '2018-05-05', '', '', NULL, ''),
(5, 'ok', 'ok', 1, NULL, 1, 'ok', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, '123', '123', 2, NULL, -1, 'viet nguyen', 1, 'vietnv.uet@gmail.com', '', NULL, NULL, '', '', NULL, ''),
(7, '24234', '123', 2, NULL, -1, 'viet nguyen', 1, 'vietnv.uet@gmail.com', '', NULL, NULL, '', '', NULL, ''),
(9, '', '123', 2, NULL, 1, 'viet nguyen', 0, '', '', NULL, NULL, '', '', NULL, ''),
(10, NULL, '123', 2, NULL, 1, 'viet nguyen', 6, 'vietnv1@gmail.com', '123', NULL, NULL, NULL, NULL, NULL, NULL),
(11, NULL, '123', 2, NULL, 1, 'viet nguyen', 7, 'vietnv2@gmail.com', '123123', NULL, NULL, NULL, NULL, NULL, NULL),
(18, NULL, '123', 2, NULL, 1, 'viet nguyen', 8, 'vietnv6@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(19, NULL, '123', 2, NULL, 1, 'Hải', 9, 'haide@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(20, NULL, '123', 2, NULL, 1, 'Hải Xồm', 10, 'haidexom@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(21, NULL, '123', 2, NULL, 1, 'Thế Tư', 11, 'tude@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(22, NULL, '123', 2, NULL, 1, 'Tư', 12, 'tult@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(23, NULL, '123', 2, NULL, 1, 'Hải nv', 13, 'hainv@gmai.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(24, NULL, '123', 2, NULL, 1, 'hails', 14, 'hals@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL),
(25, NULL, '123', 2, NULL, 1, 'Tư4', 15, 'tu4@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `contract`
--

CREATE TABLE `contract` (
  `CONTRACT_ID` int(11) NOT NULL,
  `CONTRACT_CODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) DEFAULT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `START_TIME` date DEFAULT NULL,
  `END_TIME` date DEFAULT NULL,
  `DEPOSIT` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL COMMENT '1: hoạt động, 2: hết hạn',
  `CREATE_TIME` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `contract`
--

INSERT INTO `contract` (`CONTRACT_ID`, `CONTRACT_CODE`, `HOME_ID`, `ROOM_ID`, `CUSTOMER_ID`, `START_TIME`, `END_TIME`, `DEPOSIT`, `DESCRIPTION`, `STATUS`, `CREATE_TIME`) VALUES
(1, 'HD001-00002', 1, 1, 1, '2018-01-22', NULL, 540600000, 'ok', 2, NULL),
(2, 'HD001-00001', 1, 2, 2, '2018-01-22', '2018-02-04', 5000000, '', 2, NULL),
(3, 'HD001-00003', 1, 1, 3, NULL, NULL, 100000, '', 2, NULL),
(4, 'HD001-00004', 1, 1, 2, NULL, NULL, NULL, '', 2, NULL),
(5, 'HD001-00005', 1, 1, 2, NULL, NULL, NULL, '', 2, NULL),
(6, 'HD005-00001', 4, 5, 4, '2018-06-07', '2018-06-10', 1000000, '', 1, NULL),
(7, 'HD006-00001', 5, 6, 5, '2018-06-07', '2018-07-01', 5000000, 'chơi xơi nước', 1, NULL);

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
  `home_town` varchar(250) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `job` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `customer`
--

INSERT INTO `customer` (`CUSTOMER_ID`, `CUSTOMER_NAME`, `SEX`, `BIRTH_DATE`, `ADDRESS`, `PHONE`, `STATUS`, `DESCRIPTION`, `EMAIL`, `CMND`, `NGAY_CAP`, `NOI_CAP`, `UPDATE_TIME`, `GROUP_USER_ID`, `img_path`, `home_town`, `job`) VALUES
(1, 'Nguyễn Văn Việt', 1, '1992-01-01', 'Hà nội', '01663734698', 1, 'ok', 'vietnv.th@gmail.com', '123123', '2010-02-20', 'Thanh Hóa', NULL, 1, NULL, 'Hoằng Xuân, Hoằng Hóa, Thanh Hóa', NULL),
(2, 'Nguyễn A', 1, NULL, '', '0123456778', 1, '', 'vietnv', '', NULL, '', NULL, 1, NULL, '', NULL),
(3, 'Lê Thị Bé', 2, NULL, '', '0123456789', 1, '', 'v@gmail.com', '', NULL, '', NULL, 1, NULL, '', 'học sinh'),
(4, 'việt', 1, NULL, '', '0123123123', 1, '', 'vietnv.th@gmail.com', '', NULL, '', NULL, 5, NULL, '', ''),
(5, 'Nguyễn Hải', 1, NULL, '', '0981231231', 1, '', 'hails@123.com', '', NULL, '', NULL, 6, NULL, '', '');

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
(2, 1, 2, NULL, '2018-06-10', -1, '2018-01-22 01:40:22', 2, 0),
(3, 3, 2, '2018-02-07', '2018-06-10', -1, '2018-02-07 00:15:31', 2, 2),
(4, 2, 2, '2018-02-07', '2018-06-10', -1, '2018-02-07 00:19:16', 1, 2),
(5, 4, 5, NULL, NULL, 1, '2018-06-07 00:58:48', 1, 6),
(6, 5, 6, NULL, NULL, 1, '2018-06-07 23:46:21', 1, 7);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `document`
--

CREATE TABLE `document` (
  `DOCUMENT_ID` int(11) NOT NULL,
  `DOCUMENT_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ATTACH_FILE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILE_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GROUP_USER_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `document`
--

INSERT INTO `document` (`DOCUMENT_ID`, `DOCUMENT_NAME`, `ATTACH_FILE`, `DESCRIPTION`, `FILE_NAME`, `GROUP_USER_ID`) VALUES
(1, 'dfg', NULL, 'sdfs', 'mau hoa don.pdf', NULL),
(2, 'ggggggg', NULL, 'hg', 'CV Nguyễn Văn Hải (1).pdf', NULL);

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
(1, 1, 1, 2, 7, 8, '2018-01-25', '2018-01-25', 1, '2018-02-01'),
(2, 2, 5, 2, 10, 7, '2018-01-25', '2018-01-24', 1, '2018-03-01'),
(5, 2, 55, 16, 70, 20, '2018-01-25', '2018-01-25', 1, '2018-02-01'),
(6, 6, 1, 3, 11, 21, '2018-06-07', '2018-06-07', 1, '2018-06-01'),
(7, 7, 6, 7, 66, 65, '2018-06-07', '2018-06-07', 1, '2018-05-01'),
(8, 7, 66, 65, 67, 555, '2018-06-07', '2018-06-07', 1, '2018-06-01');

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
(13, '/dashboard', 'Trang chủ', 1, 2, NULL),
(14, '/catItem', 'Danh mục tham số', 1, 2, 2),
(15, '/catHome', 'Khu trọ', 1, 2, 2),
(16, '/catUser.action_modify_role', 'Quyền thay đổi role', 1, 3, 9),
(17, '/groupUser', 'Quản lý nhóm khách hàng', 1, 2, 3),
(18, '/functionPath', 'Danh mục chức năng', 1, 2, 3),
(19, '/catUser.action_modify_group', 'Quyền thay đổi nhóm người dùng', 1, 3, 9),
(20, '/catService', 'Dịch vụ', 1, 2, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `group_user`
--

CREATE TABLE `group_user` (
  `ID` int(11) NOT NULL,
  `CODE` varchar(250) DEFAULT NULL,
  `NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `START_TIME` datetime DEFAULT NULL COMMENT 'Thời gian bắt đầu sử dụng',
  `END_TIME` datetime DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP,
  `DESCRIPTION` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `MAX_NUMBER_ROOM` int(11) DEFAULT '10' COMMENT 'Số phòng tối đa'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `group_user`
--

INSERT INTO `group_user` (`ID`, `CODE`, `NAME`, `STATUS`, `START_TIME`, `END_TIME`, `CREATE_TIME`, `DESCRIPTION`, `MAX_NUMBER_ROOM`) VALUES
(0, 'super_admin', 'Quản trị phần mềm', 1, NULL, NULL, '2018-05-20 23:48:03', 'Dùng cho quản trị hệ thống phần mềm', 10),
(1, 'admin', 'quản lý', 1, '2018-01-16 00:00:00', '2018-01-31 00:00:00', NULL, NULL, 10),
(2, '20180522012931', 'Test nhóm', 1, '2018-05-22 00:00:00', '2018-05-22 00:00:00', '2018-05-22 01:29:31', 'test mô tả', 400),
(3, '20180606235650', 'helo', NULL, '2018-06-06 23:56:50', NULL, '2018-06-06 23:56:50', 'Registration Account', 10),
(4, '20180607002345', 'nhóm helo', NULL, '2018-06-07 00:23:45', NULL, '2018-06-07 00:23:45', 'Registration Account', 10),
(5, '20180607004447', 'nhóm helo', 1, '2018-06-07 00:44:57', NULL, '2018-06-07 00:44:46', 'Registration Account', 10),
(6, '20180607012808', 'Nhà nghỉ thân yêu', 1, '2018-06-07 01:28:08', NULL, '2018-06-07 01:28:08', 'Registration Account', 10),
(7, '20180607013143', 'Nhà nghỉ thân yêu', 1, '2018-06-07 01:31:43', NULL, '2018-06-07 01:31:43', 'Registration Account', 10),
(8, '20180607022327', 'Nhà nghỉ thân yêu', 1, '2018-06-07 02:23:29', NULL, '2018-06-07 02:23:26', 'Registration Account', 10),
(9, '20180610022354', 'Hải xồm', 1, '2018-06-10 02:23:54', NULL, '2018-06-10 02:23:54', 'Registration Account', 10),
(10, '20180610023400', 'Hải Xồm', 1, '2018-06-10 02:34:00', NULL, '2018-06-10 02:34:00', 'Registration Account', 10),
(11, '20180610024750', 'Tư dê', 1, '2018-06-10 02:47:50', NULL, '2018-06-10 02:47:50', 'Registration Account', 10),
(12, '20180610025936', 'Thế Tư', 1, '2018-06-10 02:59:36', NULL, '2018-06-10 02:59:36', 'Registration Account', 10),
(13, '20180610030613', 'Hải Nguyễn', 1, '2018-06-10 03:06:13', NULL, '2018-06-10 03:06:13', 'Registration Account', 10),
(14, '20180610031129', 'hails', 1, '2018-06-10 03:11:29', NULL, '2018-06-10 03:11:29', 'Registration Account', 10),
(15, '20180610031527', 'Tư 4', 1, '2018-06-10 03:15:27', NULL, '2018-06-10 03:15:27', 'Registration Account', 10);

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
(1, 'Hà Nội', '', 1, 1, '', ''),
(2, 'Phú Mỹ', 'Thanh Xuân - Hà nội', 1, 1, 'Thử thôi', NULL),
(3, 'Cấm trẻ em', 'Phú đô - Việt Nam', 2, 2, 'Dành cho sinh viên', NULL),
(4, 'karaok', '', 1, 5, '', NULL),
(5, 'Nhà nghỉ thân yêu', NULL, 1, 6, NULL, NULL),
(6, 'Nhà nghỉ thân yêu', NULL, 1, 7, NULL, NULL),
(7, 'Nhà nghỉ thân yêu', NULL, 1, 8, NULL, NULL),
(8, 'Hải xồm', NULL, 1, 9, NULL, NULL),
(9, 'Hải Xồm', NULL, 1, 10, NULL, NULL),
(10, 'Tư dê', NULL, 1, 11, NULL, NULL),
(11, 'Thế Tư', NULL, 1, 12, NULL, NULL),
(12, 'Hải Nguyễn', NULL, 1, 13, NULL, NULL),
(13, 'hails', NULL, 1, 14, NULL, NULL),
(14, 'Tư 4', NULL, 1, 15, NULL, NULL);

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
(39, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 2, 'onCheckoutCustomer', 'RoomController', NULL, 'model.CustomerRoom[ customerRoomId=3 ]', 'model.CustomerRoom[ customerRoomId=3 ]', NULL),
(40, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 1, 'onSaveOrUpdate', 'RoleController', NULL, NULL, 'com.slook.model.CatRole@6b927204[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=<null>\r\n  status=1\r\n  statusName=<null>\r\n]', NULL),
(41, '0:0:0:0:0:0:0:1', 'admin', '2018-02-06 17:00:00', 1, 'saveOrUpdate', 'catUserController', 'vietnv', NULL, 'com.slook.model.CatUser@3d3bab0[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=<null>\r\n  confirmPassword=vietnv\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupId=<null>\r\n]', NULL),
(42, '0:0:0:0:0:0:0:1', 'admin', '2018-02-23 17:00:00', 2, 'onChangePassword', 'catUserController', 'admin', 'com.slook.model.CatUser@4143321f[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@53259ab2[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupId=1\r\n]', 'com.slook.model.CatUser@4143321f[\r\n  userId=1\r\n  userName=admin\r\n  password=admin123\r\n  roleId=1\r\n  role=com.slook.model.CatRole@53259ab2[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=admin123\r\n  oldPassword=admin\r\n  newPassword=admin123\r\n  groupId=1\r\n]', NULL),
(43, '0:0:0:0:0:0:0:1', 'admin', '2018-02-23 17:00:00', 2, 'onChangePassword', 'catUserController', 'admin', 'com.slook.model.CatUser@49023063[\r\n  userId=1\r\n  userName=admin\r\n  password=admin123\r\n  roleId=1\r\n  role=com.slook.model.CatRole@5bbf35b3[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupId=1\r\n]', 'com.slook.model.CatUser@49023063[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@5bbf35b3[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=admin\r\n  oldPassword=admin123\r\n  newPassword=admin\r\n  groupId=1\r\n]', NULL),
(44, '0:0:0:0:0:0:0:1', 'admin', '2018-02-24 17:00:00', 1, 'onSaveOrUpdate', 'BillController', NULL, NULL, 'model.Bill[ billId=15 ]', NULL),
(45, '0:0:0:0:0:0:0:1', 'admin', '2018-02-27 17:00:00', 2, 'onSaveOrUpdate', 'CustomerController', NULL, 'model.Customer[ customerId=3 ]', 'model.Customer[ customerId=3 ]', NULL),
(46, '0:0:0:0:0:0:0:1', 'admin', '2018-05-05 17:00:00', 2, 'onSaveOrUpdate', 'RoomController', NULL, 'model.Room[ roomId=1 ]', 'model.Room[ roomId=1 ]', NULL),
(47, '0:0:0:0:0:0:0:1', 'admin', '2018-05-05 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=6 ]', NULL),
(48, '0:0:0:0:0:0:0:1', 'admin', '2018-05-05 17:00:00', 2, 'onSaveOrUpdate', 'ElectricWaterController', NULL, 'model.ElectricWater[ electricWaterId=6 ]', 'model.ElectricWater[ electricWaterId=6 ]', NULL),
(49, '0:0:0:0:0:0:0:1', 'admin', '2018-05-05 17:00:00', 2, 'onSaveOrUpdate', 'ElectricWaterController', NULL, 'model.ElectricWater[ electricWaterId=1 ]', 'model.ElectricWater[ electricWaterId=1 ]', NULL),
(50, '0:0:0:0:0:0:0:1', 'admin', '2018-05-05 17:00:00', 1, 'onSaveOrUpdate', 'RoomController', NULL, NULL, 'model.Room[ roomId=3 ]', NULL),
(51, '0:0:0:0:0:0:0:1', 'admin', '2018-05-12 17:00:00', 1, 'saveOrUpdate', 'catUserController', 'vietnv123', NULL, 'com.slook.model.CatUser@52b8d822[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n]', NULL),
(52, '0:0:0:0:0:0:0:1', 'admin', '2018-05-13 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv', 'com.slook.model.CatUser@40092225[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@1320f7a1[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=<null>\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n]', 'com.slook.model.CatUser@40092225[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@1320f7a1[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n]', NULL),
(53, '0:0:0:0:0:0:0:1', 'admin', '2018-05-20 17:00:00', 1, 'onSaveOrUpdate', 'HomeController', NULL, NULL, 'com.motel.model.Home@3afbc09[\r\n  homeId=2\r\n  homeName=Phú Mỹ\r\n  address=Thanh Xuân - Hà nội\r\n  status=1\r\n  groupUserId=1\r\n  description=Thử thôi\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(54, '0:0:0:0:0:0:0:1', 'admin', '2018-05-20 17:00:00', 2, 'onSaveOrUpdate', 'HomeController', NULL, 'com.motel.model.Home@550cd3e7[\r\n  homeId=1\r\n  homeName=Hà Nội\r\n  address=\r\n  status=0\r\n  groupUserId=1\r\n  description=\r\n  homeCode=\r\n  billList=[model.Bill[ billId=10 ], model.Bill[ billId=11 ], model.Bill[ billId=12 ], model.Bill[ billId=13 ], model.Bill[ billId=14 ], model.Bill[ billId=15 ]]\r\n  roomList=[com.motel.model.Room@49ac563a[\r\n  roomId=1\r\n  roomName=Phòng 101\r\n  price=1500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=1 ]]\r\n  home=com.motel.model.Home@3a9e4507\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=1 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@e3616ab[\r\n  roomId=2\r\n  roomName=Phòng 102\r\n  price=200000\r\n  status=2\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=2 ], model.CustomerRoom[ customerRoomId=4 ], model.CustomerRoom[ customerRoomId=3 ]]\r\n  home=com.motel.model.Home@3a9e4507\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=2 ], model.ElectricWater[ electricWaterId=5 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@6b142852[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@3a9e4507\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]]\r\n  statusName=<null>\r\n]', 'com.motel.model.Home@550cd3e7[\r\n  homeId=1\r\n  homeName=Hà Nội\r\n  address=\r\n  status=1\r\n  groupUserId=1\r\n  description=\r\n  homeCode=\r\n  billList=[model.Bill[ billId=10 ], model.Bill[ billId=11 ], model.Bill[ billId=12 ], model.Bill[ billId=13 ], model.Bill[ billId=14 ], model.Bill[ billId=15 ]]\r\n  roomList=[com.motel.model.Room@49ac563a[\r\n  roomId=1\r\n  roomName=Phòng 101\r\n  price=1500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=1 ]]\r\n  home=com.motel.model.Home@3a9e4507\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=1 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@e3616ab[\r\n  roomId=2\r\n  roomName=Phòng 102\r\n  price=200000\r\n  status=2\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=2 ], model.CustomerRoom[ customerRoomId=4 ], model.CustomerRoom[ customerRoomId=3 ]]\r\n  home=com.motel.model.Home@3a9e4507\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=2 ], model.ElectricWater[ electricWaterId=5 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@6b142852[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@3a9e4507\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]]\r\n  statusName=<null>\r\n]', NULL),
(55, '0:0:0:0:0:0:0:1', 'admin', '2018-05-20 17:00:00', 1, 'onSaveOrUpdate', 'RoomController', NULL, NULL, 'com.motel.model.Room@7cea1cf1[\r\n  roomId=4\r\n  roomName=103\r\n  price=40000\r\n  status=1\r\n  homeId=2\r\n  customerRoomList=<null>\r\n  home=<null>\r\n  electricWaterList=<null>\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(56, '0:0:0:0:0:0:0:1', 'vietnv', '2018-05-20 17:00:00', 1, 'onSaveOrUpdate', 'HomeController', NULL, NULL, 'com.motel.model.Home@7df8bc48[\r\n  homeId=3\r\n  homeName=Cấm trẻ em\r\n  address=Phú đô - Việt Nam\r\n  status=1\r\n  groupUserId=2\r\n  description=Dành cho sinh viên\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(57, '0:0:0:0:0:0:0:1', 'vietnv', '2018-05-20 17:00:00', 2, 'onSaveOrUpdate', 'HomeController', NULL, 'com.motel.model.Home@65d28dcf[\r\n  homeId=3\r\n  homeName=Cấm trẻ em\r\n  address=Phú đô - Việt Nam\r\n  status=1\r\n  groupUserId=2\r\n  description=Dành cho sinh viên\r\n  homeCode=<null>\r\n  billList=[]\r\n  roomList=[]\r\n  statusName=<null>\r\n]', 'com.motel.model.Home@65d28dcf[\r\n  homeId=3\r\n  homeName=Cấm trẻ em\r\n  address=Phú đô - Việt Nam\r\n  status=2\r\n  groupUserId=2\r\n  description=Dành cho sinh viên\r\n  homeCode=<null>\r\n  billList=[]\r\n  roomList=[]\r\n  statusName=<null>\r\n]', NULL),
(58, '0:0:0:0:0:0:0:1', 'vietnv', '2018-05-20 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv123', 'com.slook.model.CatUser@390678de[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@3faa35f[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n]', 'com.slook.model.CatUser@390678de[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@3faa35f[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=2\r\n  groupUser=<null>\r\n]', NULL),
(59, '0:0:0:0:0:0:0:1', 'vietnv', '2018-05-20 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv123', 'com.slook.model.CatUser@511ef9c[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@13ee4a0b[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n]', 'com.slook.model.CatUser@511ef9c[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@13ee4a0b[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=<null>\r\n]', NULL),
(60, '0:0:0:0:0:0:0:1', 'vietnv123', '2018-05-20 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv', 'com.slook.model.CatUser@14d79f92[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@27e95c7b[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n]', 'com.slook.model.CatUser@14d79f92[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@27e95c7b[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n]', NULL),
(61, '0:0:0:0:0:0:0:1', 'vietnv', '2018-05-20 17:00:00', 1, 'saveOrUpdate', 'catUserController', 'user1', NULL, 'com.slook.model.CatUser@122cbf43[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=user1\r\n  confirmPassword=user1\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=<null>\r\n]', NULL),
(62, '0:0:0:0:0:0:0:1', 'admin', '2018-05-20 17:00:00', 1, 'saveOrUpdate', 'catUserController', 'ok', NULL, 'com.slook.model.CatUser@5865bfad[\r\n  userId=5\r\n  userName=ok\r\n  password=ok\r\n  roleId=1\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=ok\r\n  confirmPassword=ok\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=<null>\r\n]', NULL),
(63, '0:0:0:0:0:0:0:1', 'admin', '2018-05-21 17:00:00', 1, 'onSaveOrUpdate', 'GroupUserController', NULL, NULL, 'com.motel.model.GroupUser@4c273d57[\r\n  id=2\r\n  code=20180522012931\r\n  name=Test nhóm\r\n  status=1\r\n  startTime=Tue May 22 00:00:00 ICT 2018\r\n  endTime=Tue May 22 00:00:00 ICT 2018\r\n  createTime=Tue May 22 01:29:31 ICT 2018\r\n  description=test mô tả\r\n  maxNumberRoom=400\r\n  statusName=<null>\r\n]', NULL),
(64, '0:0:0:0:0:0:0:1', 'admin', '2018-05-29 17:00:00', 2, 'onSaveOrUpdate', 'CustomerController', NULL, 'model.Customer[ customerId=3 ]', 'model.Customer[ customerId=3 ]', NULL),
(65, '0:0:0:0:0:0:0:1', 'admin', '2018-05-29 17:00:00', 2, 'onSaveOrUpdate', 'CustomerController', NULL, 'model.Customer[ customerId=3 ]', 'model.Customer[ customerId=3 ]', NULL),
(66, '0:0:0:0:0:0:0:1', 'admin', '2018-05-29 17:00:00', 2, 'onSaveOrUpdate', 'CustomerController', NULL, 'model.Customer[ customerId=3 ]', 'model.Customer[ customerId=3 ]', NULL),
(67, '0:0:0:0:0:0:0:1', 'admin', '2018-05-29 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'user1', 'com.slook.model.CatUser@2582e747[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=com.slook.model.CatRole@733a86ad[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=user1\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@2aa0651[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=<null>\r\n  description=<null>\r\n  email=<null>\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@2582e747[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=com.slook.model.CatRole@733a86ad[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=user1\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@2aa0651[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', NULL),
(68, '0:0:0:0:0:0:0:1', 'admin', '2018-05-29 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'user1', 'com.slook.model.CatUser@78740090[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=com.slook.model.CatRole@3db2bde8[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=user1\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@5081ba95[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', 'com.slook.model.CatUser@78740090[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=com.slook.model.CatRole@3db2bde8[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=user1\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@5081ba95[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sat May 05 00:00:00 ICT 2018\r\n  address=\r\n  phoneNumber=01663734698\r\n  description=\r\n  email=vietnv.th@gmail.com\r\n  job=\r\n]', NULL),
(69, '0:0:0:0:0:0:0:1', 'admin', '2018-05-30 17:00:00', 2, 'saveOrUpdateInfoCurrUser', 'catUserController', 'admin', 'com.slook.model.CatUser@cffec54[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@31bfc5b2[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@41f2e66[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=<null>\r\n  description=<null>\r\n  email=<null>\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@cffec54[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@31bfc5b2[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@41f2e66[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sun Aug 02 00:00:00 ICT 1992\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', NULL),
(70, '0:0:0:0:0:0:0:1', 'admin', '2018-05-30 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'user1', 'com.slook.model.CatUser@2779d277[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=com.slook.model.CatRole@7947b553[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=user1\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@63501e63[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=2018-05-05\r\n  address=\r\n  phoneNumber=01663734698\r\n  description=\r\n  email=vietnv.th@gmail.com\r\n  job=\r\n]', 'com.slook.model.CatUser@2779d277[\r\n  userId=4\r\n  userName=user1\r\n  password=user1\r\n  roleId=1\r\n  role=com.slook.model.CatRole@7947b553[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=user1\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@63501e63[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sat May 05 00:00:00 ICT 2018\r\n  address=\r\n  phoneNumber=01663734698\r\n  description=\r\n  email=vietnv0@gmail.com\r\n  job=\r\n]', NULL),
(71, '0:0:0:0:0:0:0:1', 'admin', '2018-05-30 17:00:00', 2, 'saveOrUpdateInfoCurrUser', 'catUserController', 'admin', 'com.slook.model.CatUser@4c610d3d[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@11e1dc6e[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@493e271c[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=1992-08-02\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', 'com.slook.model.CatUser@4c610d3d[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@11e1dc6e[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@493e271c[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sun Aug 02 00:00:00 ICT 1992\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', NULL);
INSERT INTO `log_action` (`LOG_ACTION_ID`, `CLIENT_IP`, `USER_NAME`, `EVENT_TIME`, `ACTION_TYPE`, `FUNCTION`, `CLASS_NAME`, `OBJECT_CODE`, `OLD_VALUE`, `NEW_VALUE`, `NOTE`) VALUES
(72, '0:0:0:0:0:0:0:1', 'admin', '2018-05-31 17:00:00', 2, 'saveOrUpdateInfoCurrUser', 'catUserController', 'admin', 'com.slook.model.CatUser@4f724902[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@3db928d9[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@1603da8f[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=1992-08-02\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', 'com.slook.model.CatUser@4f724902[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@3db928d9[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@1603da8f[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sun Aug 02 00:00:00 ICT 1992\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', NULL),
(73, '0:0:0:0:0:0:0:1', '', '2018-06-05 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.GroupUser@7d1ea26[\r\n  id=3\r\n  code=20180606235650\r\n  name=helo\r\n  status=<null>\r\n  startTime=Wed Jun 06 23:56:50 ICT 2018\r\n  endTime=<null>\r\n  createTime=Wed Jun 06 23:56:50 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(74, '0:0:0:0:0:0:0:1', '', '2018-06-05 17:00:00', 1, 'saveOrUpdateInfoCurrUser', 'catUserController', NULL, NULL, 'com.slook.model.CatUser@79b33c92[\r\n  userId=6\r\n  userName=<null>\r\n  password=123\r\n  roleId=<null>\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=viet nguyen\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=3\r\n  groupUser=com.motel.model.GroupUser@7d1ea26[\r\n  id=3\r\n  code=20180606235650\r\n  name=helo\r\n  status=<null>\r\n  startTime=Wed Jun 06 23:56:50 ICT 2018\r\n  endTime=<null>\r\n  createTime=Wed Jun 06 23:56:50 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=841663734698__\r\n  description=<null>\r\n  email=vietnv.uet@gmail.com\r\n  job=<null>\r\n]', NULL),
(75, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'onSaveOrUpdate', 'RoleController', NULL, 'com.slook.model.CatRole@5905259f[\r\n  roleId=2\r\n  roleName=Khách\r\n  roleCode=KHACH\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]', 'com.slook.model.CatRole@5905259f[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]', NULL),
(76, '0:0:0:0:0:0:0:1', 'user1', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'admin', 'com.slook.model.CatUser@3875db21[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@7b3c8ce0[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@cd3fb5a[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=1992-08-02\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', 'com.slook.model.CatUser@3875db21[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@7b3c8ce0[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@cd3fb5a[\r\n  id=1\r\n  code=admin\r\n  name=quản lý\r\n  status=1\r\n  startTime=2018-01-16 00:00:00.0\r\n  endTime=2018-01-31 00:00:00.0\r\n  createTime=<null>\r\n  description=<null>\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sun Aug 02 00:00:00 ICT 1992\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', NULL),
(77, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', '123', 'com.slook.model.CatUser@55686547[\r\n  userId=6\r\n  userName=<null>\r\n  password=123\r\n  roleId=<null>\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=3\r\n  groupUser=com.motel.model.GroupUser@7df3af7f[\r\n  id=3\r\n  code=20180606235650\r\n  name=helo\r\n  status=<null>\r\n  startTime=2018-06-06 23:56:50.0\r\n  endTime=<null>\r\n  createTime=2018-06-06 23:56:50.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=841663734698__\r\n  description=<null>\r\n  email=vietnv.uet@gmail.com\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@55686547[\r\n  userId=6\r\n  userName=123\r\n  password=123\r\n  roleId=2\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=2\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@7df3af7f[\r\n  id=3\r\n  code=20180606235650\r\n  name=helo\r\n  status=<null>\r\n  startTime=2018-06-06 23:56:50.0\r\n  endTime=<null>\r\n  createTime=2018-06-06 23:56:50.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=vietnv.uet@gmail.com\r\n  job=\r\n]', NULL),
(78, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.GroupUser@312bcf66[\r\n  id=4\r\n  code=20180607002345\r\n  name=nhóm helo\r\n  status=<null>\r\n  startTime=Thu Jun 07 00:23:45 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 00:23:45 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(79, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'saveOrUpdateInfoCurrUser', 'catUserController', NULL, NULL, 'com.slook.model.CatUser@6feb60f[\r\n  userId=7\r\n  userName=<null>\r\n  password=123\r\n  roleId=<null>\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=viet nguyen\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=4\r\n  groupUser=com.motel.model.GroupUser@312bcf66[\r\n  id=4\r\n  code=20180607002345\r\n  name=nhóm helo\r\n  status=<null>\r\n  startTime=Thu Jun 07 00:23:45 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 00:23:45 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=841663734698\r\n  description=<null>\r\n  email=vietnv.uet@gmail.com\r\n  job=<null>\r\n]', NULL),
(80, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', '24234', 'com.slook.model.CatUser@3bfa6ee6[\r\n  userId=7\r\n  userName=<null>\r\n  password=123\r\n  roleId=<null>\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=4\r\n  groupUser=com.motel.model.GroupUser@3dbcbb0e[\r\n  id=4\r\n  code=20180607002345\r\n  name=nhóm helo\r\n  status=<null>\r\n  startTime=2018-06-07 00:23:45.0\r\n  endTime=<null>\r\n  createTime=2018-06-07 00:23:45.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=841663734698\r\n  description=<null>\r\n  email=vietnv.uet@gmail.com\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@3bfa6ee6[\r\n  userId=7\r\n  userName=24234\r\n  password=123\r\n  roleId=2\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=2\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@3dbcbb0e[\r\n  id=4\r\n  code=20180607002345\r\n  name=nhóm helo\r\n  status=<null>\r\n  startTime=2018-06-07 00:23:45.0\r\n  endTime=<null>\r\n  createTime=2018-06-07 00:23:45.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=vietnv.uet@gmail.com\r\n  job=\r\n]', NULL),
(81, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv123', 'com.slook.model.CatUser@5463da9b[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@5ec924fd[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@38c71ccc[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=<null>\r\n  description=<null>\r\n  email=<null>\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@5463da9b[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@5ec924fd[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@38c71ccc[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', NULL),
(82, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv123', 'com.slook.model.CatUser@3e862f5b[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@57af488f[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@772f8b17[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', 'com.slook.model.CatUser@3e862f5b[\r\n  userId=3\r\n  userName=vietnv123\r\n  password=123\r\n  roleId=3\r\n  role=com.slook.model.CatRole@57af488f[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn văn Việt\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@772f8b17[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=123123123\r\n  description=\r\n  email=\r\n  job=\r\n]', NULL),
(83, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.GroupUser@12abeac4[\r\n  id=5\r\n  code=20180607004447\r\n  name=nhóm helo\r\n  status=1\r\n  startTime=Thu Jun 07 00:44:57 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 00:44:46 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(84, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'saveOrUpdateInfoCurrUser', 'catUserController', NULL, NULL, 'com.slook.model.CatUser@140b09b3[\r\n  userId=9\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=viet nguyen\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=5\r\n  groupUser=com.motel.model.GroupUser@12abeac4[\r\n  id=5\r\n  code=20180607004447\r\n  name=nhóm helo\r\n  status=1\r\n  startTime=Thu Jun 07 00:44:57 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 00:44:46 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=841663734698\r\n  description=<null>\r\n  email=vietnv.uet@gmail.com\r\n  job=<null>\r\n]', NULL),
(85, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'HomeController', NULL, NULL, 'com.motel.model.Home@2197d83a[\r\n  homeId=4\r\n  homeName=karaok\r\n  address=\r\n  status=1\r\n  groupUserId=5\r\n  description=\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(86, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'RoomController', NULL, NULL, 'com.motel.model.Room@11f13bca[\r\n  roomId=5\r\n  roomName=103\r\n  price=111111\r\n  status=1\r\n  homeId=4\r\n  customerRoomList=<null>\r\n  home=<null>\r\n  electricWaterList=<null>\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(87, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'CustomerController', NULL, NULL, 'model.Customer[ customerId=4 ]', NULL),
(88, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'ContractController', NULL, NULL, 'model.Contract[ contractId=6 ]', NULL),
(89, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.GroupUser@122fda65[\r\n  id=6\r\n  code=20180607012808\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=Thu Jun 07 01:28:08 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 01:28:08 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(90, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.Home@18a22de4[\r\n  homeId=5\r\n  homeName=Nhà nghỉ thân yêu\r\n  address=<null>\r\n  status=1\r\n  groupUserId=6\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(91, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'onCreateAccount', 'catUserController', NULL, NULL, 'com.slook.model.CatUser@7735c60[\r\n  userId=10\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=viet nguyen\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=6\r\n  groupUser=com.motel.model.GroupUser@122fda65[\r\n  id=6\r\n  code=20180607012808\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=Thu Jun 07 01:28:08 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 01:28:08 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=123\r\n  description=<null>\r\n  email=vietnv1@gmail.com\r\n  job=<null>\r\n]', NULL),
(92, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.GroupUser@2b08c90f[\r\n  id=7\r\n  code=20180607013143\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=Thu Jun 07 01:31:43 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 01:31:43 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(93, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'catUserController', NULL, NULL, 'com.motel.model.Home@4714b992[\r\n  homeId=6\r\n  homeName=Nhà nghỉ thân yêu\r\n  address=<null>\r\n  status=1\r\n  groupUserId=7\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(94, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'onCreateAccount', 'catUserController', NULL, NULL, 'com.slook.model.CatUser@4f22481c[\r\n  userId=11\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=<null>\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=viet nguyen\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=7\r\n  groupUser=com.motel.model.GroupUser@2b08c90f[\r\n  id=7\r\n  code=20180607013143\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=Thu Jun 07 01:31:43 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 01:31:43 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=123123\r\n  description=<null>\r\n  email=vietnv2@gmail.com\r\n  job=<null>\r\n]', NULL),
(95, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@1f2ec6d7[\r\n  id=8\r\n  code=20180607022327\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=Thu Jun 07 02:23:29 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 02:23:26 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(96, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@45745d9e[\r\n  homeId=7\r\n  homeName=Nhà nghỉ thân yêu\r\n  address=<null>\r\n  status=1\r\n  groupUserId=8\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(97, '0:0:0:0:0:0:0:1', '', '2018-06-06 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@12dc7216[\r\n  userId=18\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@6c4f9501[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=viet nguyen\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=8\r\n  groupUser=com.motel.model.GroupUser@1f2ec6d7[\r\n  id=8\r\n  code=20180607022327\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=Thu Jun 07 02:23:29 ICT 2018\r\n  endTime=<null>\r\n  createTime=Thu Jun 07 02:23:26 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=vietnv6@gmail.com\r\n  job=<null>\r\n]', NULL),
(98, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'admin', 'com.slook.model.CatUser@249557b1[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@2c827c91[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81, com.slook.model.RoleHasFunctionPath@de28]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@63cd322e[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=1992-08-02\r\n  address=Thanh hóa\r\n  phoneNumber=84166373469\r\n  description=my application\r\n  email=vietnv.th@gmail.com\r\n  job=IT\r\n]', 'com.slook.model.CatUser@249557b1[\r\n  userId=1\r\n  userName=admin\r\n  password=admin\r\n  roleId=1\r\n  role=com.slook.model.CatRole@2c827c91[\r\n  roleId=1\r\n  roleName=admin\r\n  roleCode=admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@db60, com.slook.model.RoleHasFunctionPath@d8f1, com.slook.model.RoleHasFunctionPath@db07, com.slook.model.RoleHasFunctionPath@d9fc, com.slook.model.RoleHasFunctionPath@daae, com.slook.model.RoleHasFunctionPath@da55, com.slook.model.RoleHasFunctionPath@d9a3, com.slook.model.RoleHasFunctionPath@dc12, com.slook.model.RoleHasFunctionPath@dbb9, com.slook.model.RoleHasFunctionPath@dcc4, com.slook.model.RoleHasFunctionPath@dc6b, com.slook.model.RoleHasFunctionPath@dd1d, com.slook.model.RoleHasFunctionPath@dd76, com.slook.model.RoleHasFunctionPath@ddcf, com.slook.model.RoleHasFunctionPath@d94a, com.slook.model.RoleHasFunctionPath@deda, com.slook.model.RoleHasFunctionPath@de81, com.slook.model.RoleHasFunctionPath@de28]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=0\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Nguyễn Văn Việt\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@63cd322e[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=1\r\n  birthDate=Sun Aug 02 00:00:00 ICT 1992\r\n  address=Thanh hóa\r\n  phoneNumber=\r\n  description=my application\r\n  email=\r\n  job=IT\r\n]', NULL),
(99, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', '', 'com.slook.model.CatUser@73d5554e[\r\n  userId=9\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@75d5747[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=5\r\n  groupUser=com.motel.model.GroupUser@c404e3a[\r\n  id=5\r\n  code=20180607004447\r\n  name=nhóm helo\r\n  status=1\r\n  startTime=2018-06-07 00:44:57.0\r\n  endTime=<null>\r\n  createTime=2018-06-07 00:44:46.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=841663734698\r\n  description=<null>\r\n  email=vietnv.uet@gmail.com\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@73d5554e[\r\n  userId=9\r\n  userName=\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@75d5747[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@c404e3a[\r\n  id=5\r\n  code=20180607004447\r\n  name=nhóm helo\r\n  status=1\r\n  startTime=2018-06-07 00:44:57.0\r\n  endTime=<null>\r\n  createTime=2018-06-07 00:44:46.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', NULL),
(100, '0:0:0:0:0:0:0:1', 'admin', '2018-06-06 17:00:00', 2, 'saveOrUpdate', 'catUserController', '', 'com.slook.model.CatUser@6465b1dd[\r\n  userId=9\r\n  userName=\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@4dc37202[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@40ef83c4[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', 'com.slook.model.CatUser@6465b1dd[\r\n  userId=9\r\n  userName=\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@4dc37202[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=viet nguyen\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=0\r\n  groupUser=com.motel.model.GroupUser@40ef83c4[\r\n  id=0\r\n  code=super_admin\r\n  name=Quản trị phần mềm\r\n  status=1\r\n  startTime=<null>\r\n  endTime=<null>\r\n  createTime=2018-05-20 23:48:03.0\r\n  description=Dùng cho quản trị hệ thống phần mềm\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=\r\n  job=\r\n]', NULL),
(101, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'CustomerController', NULL, NULL, 'model.Customer[ customerId=5 ]', NULL),
(102, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'RoomController', NULL, NULL, 'com.motel.model.Room@654f1658[\r\n  roomId=6\r\n  roomName=101\r\n  price=2000000\r\n  status=1\r\n  homeId=5\r\n  customerRoomList=<null>\r\n  home=<null>\r\n  electricWaterList=<null>\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(103, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'RoomController', NULL, NULL, 'com.motel.model.Room@6ca74b9c[\r\n  roomId=7\r\n  roomName=103\r\n  price=3300000\r\n  status=2\r\n  homeId=5\r\n  customerRoomList=<null>\r\n  home=<null>\r\n  electricWaterList=<null>\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(104, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'ContractController', NULL, NULL, 'model.Contract[ contractId=7 ]', NULL),
(105, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=6 ]', NULL),
(106, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=7 ]', NULL),
(107, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'ElectricWaterController', NULL, NULL, 'model.ElectricWater[ electricWaterId=8 ]', NULL),
(108, '0:0:0:0:0:0:0:1', NULL, '2018-06-06 17:00:00', 1, 'onSaveOrUpdate', 'BillController', NULL, NULL, 'model.Bill[ billId=16 ]', NULL),
(109, '0:0:0:0:0:0:0:1', 'vietnv', '2018-06-08 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv', 'com.slook.model.CatUser@6255747c[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@681fdc48[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83, com.slook.model.RoleHasFunctionPath@df35]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=<null>\r\n  groupUser=<null>\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=<null>\r\n  description=<null>\r\n  email=<null>\r\n  job=<null>\r\n]', 'com.slook.model.CatUser@6255747c[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@681fdc48[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83, com.slook.model.RoleHasFunctionPath@df35]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=8\r\n  groupUser=<null>\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=vietnv123@gmail.com\r\n  job=\r\n]', NULL),
(110, '0:0:0:0:0:0:0:1', 'admin', '2018-06-09 17:00:00', 2, 'onSaveOrUpdate', 'CatServiceController', NULL, 'model.Service[ serviceId=1 ]', 'model.Service[ serviceId=10 ]', NULL),
(111, '0:0:0:0:0:0:0:1', 'admin', '2018-06-09 17:00:00', 2, 'onSaveOrUpdate', 'CatServiceController', NULL, 'model.Service[ serviceId=1 ]', 'model.Service[ serviceId=1 ]', NULL),
(112, '0:0:0:0:0:0:0:1', 'admin', '2018-06-09 17:00:00', 2, 'saveOrUpdate', 'catUserController', 'vietnv', 'com.slook.model.CatUser@683fff7b[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@1d994dfd[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83, com.slook.model.RoleHasFunctionPath@df35, com.slook.model.RoleHasFunctionPath@df8e]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=<null>\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=8\r\n  groupUser=com.motel.model.GroupUser@5a63b607[\r\n  id=8\r\n  code=20180607022327\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=2018-06-07 02:23:29.0\r\n  endTime=<null>\r\n  createTime=2018-06-07 02:23:26.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=vietnv123@gmail.com\r\n  job=\r\n]', 'com.slook.model.CatUser@683fff7b[\r\n  userId=2\r\n  userName=vietnv\r\n  password=vietnv\r\n  roleId=3\r\n  role=com.slook.model.CatRole@1d994dfd[\r\n  roleId=3\r\n  roleName=super admin\r\n  roleCode=supper_admin\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@d94c, com.slook.model.RoleHasFunctionPath@dd78, com.slook.model.RoleHasFunctionPath@db62, com.slook.model.RoleHasFunctionPath@d8f3, com.slook.model.RoleHasFunctionPath@db09, com.slook.model.RoleHasFunctionPath@d9fe, com.slook.model.RoleHasFunctionPath@dab0, com.slook.model.RoleHasFunctionPath@da57, com.slook.model.RoleHasFunctionPath@d9a5, com.slook.model.RoleHasFunctionPath@dbbb, com.slook.model.RoleHasFunctionPath@dcc6, com.slook.model.RoleHasFunctionPath@dc6d, com.slook.model.RoleHasFunctionPath@dc14, com.slook.model.RoleHasFunctionPath@dd1f, com.slook.model.RoleHasFunctionPath@ddd1, com.slook.model.RoleHasFunctionPath@de2a, com.slook.model.RoleHasFunctionPath@dedc, com.slook.model.RoleHasFunctionPath@de83, com.slook.model.RoleHasFunctionPath@df35, com.slook.model.RoleHasFunctionPath@df8e]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=Hoạt động\r\n  fullName=Việt Nguyễn\r\n  confirmPassword=\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=1\r\n  groupUser=com.motel.model.GroupUser@5a63b607[\r\n  id=8\r\n  code=20180607022327\r\n  name=Nhà nghỉ thân yêu\r\n  status=1\r\n  startTime=2018-06-07 02:23:29.0\r\n  endTime=<null>\r\n  createTime=2018-06-07 02:23:26.0\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=\r\n  phoneNumber=\r\n  description=\r\n  email=vietnv123@gmail.com\r\n  job=\r\n]', NULL),
(113, '0:0:0:0:0:0:0:1', 'vietnv', '2018-06-09 17:00:00', 1, 'onSaveOrUpdate', 'CatServiceController', NULL, NULL, 'model.Service[ serviceId=11 ]', NULL);
INSERT INTO `log_action` (`LOG_ACTION_ID`, `CLIENT_IP`, `USER_NAME`, `EVENT_TIME`, `ACTION_TYPE`, `FUNCTION`, `CLASS_NAME`, `OBJECT_CODE`, `OLD_VALUE`, `NEW_VALUE`, `NOTE`) VALUES
(114, '0:0:0:0:0:0:0:1', 'vietnv', '2018-06-09 17:00:00', 2, 'onSaveOrUpdate', 'RoomController', NULL, 'com.motel.model.Room@3784bb4a[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@564d8847[\r\n  homeId=1\r\n  homeName=Hà Nội\r\n  address=\r\n  status=1\r\n  groupUserId=1\r\n  description=\r\n  homeCode=\r\n  billList=[model.Bill[ billId=10 ], model.Bill[ billId=11 ], model.Bill[ billId=12 ], model.Bill[ billId=13 ], model.Bill[ billId=14 ], model.Bill[ billId=15 ]]\r\n  roomList=[com.motel.model.Room@6f088ac[\r\n  roomId=1\r\n  roomName=Phòng 101\r\n  price=1500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=1 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=1 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2d46592d[\r\n  roomId=2\r\n  roomName=Phòng 102\r\n  price=200000\r\n  status=2\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=2 ], model.CustomerRoom[ customerRoomId=3 ], model.CustomerRoom[ customerRoomId=4 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=2 ], model.ElectricWater[ electricWaterId=5 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2cb5eeff[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]]\r\n  statusName=<null>\r\n]\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', 'com.motel.model.Room@3784bb4a[\r\n  roomId=3\r\n  roomName=Phòng 103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@564d8847[\r\n  homeId=1\r\n  homeName=Hà Nội\r\n  address=\r\n  status=1\r\n  groupUserId=1\r\n  description=\r\n  homeCode=\r\n  billList=[model.Bill[ billId=10 ], model.Bill[ billId=11 ], model.Bill[ billId=12 ], model.Bill[ billId=13 ], model.Bill[ billId=14 ], model.Bill[ billId=15 ]]\r\n  roomList=[com.motel.model.Room@6f088ac[\r\n  roomId=1\r\n  roomName=Phòng 101\r\n  price=1500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=1 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=1 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2d46592d[\r\n  roomId=2\r\n  roomName=Phòng 102\r\n  price=200000\r\n  status=2\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=2 ], model.CustomerRoom[ customerRoomId=3 ], model.CustomerRoom[ customerRoomId=4 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=2 ], model.ElectricWater[ electricWaterId=5 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2cb5eeff[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]]\r\n  statusName=<null>\r\n]\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(115, '0:0:0:0:0:0:0:1', 'vietnv', '2018-06-09 17:00:00', 2, 'onSaveOrUpdate', 'RoomController', NULL, 'com.motel.model.Room@3784bb4a[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@564d8847[\r\n  homeId=1\r\n  homeName=Hà Nội\r\n  address=\r\n  status=1\r\n  groupUserId=1\r\n  description=\r\n  homeCode=\r\n  billList=[model.Bill[ billId=10 ], model.Bill[ billId=11 ], model.Bill[ billId=12 ], model.Bill[ billId=13 ], model.Bill[ billId=14 ], model.Bill[ billId=15 ]]\r\n  roomList=[com.motel.model.Room@6f088ac[\r\n  roomId=1\r\n  roomName=Phòng 101\r\n  price=1500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=1 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=1 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2d46592d[\r\n  roomId=2\r\n  roomName=Phòng 102\r\n  price=200000\r\n  status=2\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=2 ], model.CustomerRoom[ customerRoomId=3 ], model.CustomerRoom[ customerRoomId=4 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=2 ], model.ElectricWater[ electricWaterId=5 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2cb5eeff[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]]\r\n  statusName=<null>\r\n]\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', 'com.motel.model.Room@6a75e706[\r\n  roomId=8\r\n  roomName=Phòng 104\r\n  price=1000000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=<null>\r\n  home=<null>\r\n  electricWaterList=<null>\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(116, '0:0:0:0:0:0:0:1', 'vietnv', '2018-06-09 17:00:00', 2, 'onSaveOrUpdate', 'RoomController', NULL, 'com.motel.model.Room@3784bb4a[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@564d8847[\r\n  homeId=1\r\n  homeName=Hà Nội\r\n  address=\r\n  status=1\r\n  groupUserId=1\r\n  description=\r\n  homeCode=\r\n  billList=[model.Bill[ billId=10 ], model.Bill[ billId=11 ], model.Bill[ billId=12 ], model.Bill[ billId=13 ], model.Bill[ billId=14 ], model.Bill[ billId=15 ]]\r\n  roomList=[com.motel.model.Room@6f088ac[\r\n  roomId=1\r\n  roomName=Phòng 101\r\n  price=1500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=1 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=1 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2d46592d[\r\n  roomId=2\r\n  roomName=Phòng 102\r\n  price=200000\r\n  status=2\r\n  homeId=1\r\n  customerRoomList=[model.CustomerRoom[ customerRoomId=2 ], model.CustomerRoom[ customerRoomId=3 ], model.CustomerRoom[ customerRoomId=4 ]]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[model.ElectricWater[ electricWaterId=2 ], model.ElectricWater[ electricWaterId=5 ]]\r\n  statusName=<null>\r\n  currContract=<null>\r\n], com.motel.model.Room@2cb5eeff[\r\n  roomId=3\r\n  roomName=103\r\n  price=500000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=[]\r\n  home=com.motel.model.Home@7cbffafa\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]]\r\n  statusName=<null>\r\n]\r\n  electricWaterList=[]\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', 'com.motel.model.Room@1d1b9aa0[\r\n  roomId=9\r\n  roomName=Phòng 105\r\n  price=1000000\r\n  status=1\r\n  homeId=1\r\n  customerRoomList=<null>\r\n  home=<null>\r\n  electricWaterList=<null>\r\n  statusName=<null>\r\n  currContract=<null>\r\n]', NULL),
(117, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@3e8dff79[\r\n  id=9\r\n  code=20180610022354\r\n  name=Hải xồm\r\n  status=1\r\n  startTime=Sun Jun 10 02:23:54 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:23:54 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(118, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@789ec360[\r\n  homeId=8\r\n  homeName=Hải xồm\r\n  address=<null>\r\n  status=1\r\n  groupUserId=9\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(119, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@2d7b255d[\r\n  userId=19\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@2841670b[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Hải\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=9\r\n  groupUser=com.motel.model.GroupUser@3e8dff79[\r\n  id=9\r\n  code=20180610022354\r\n  name=Hải xồm\r\n  status=1\r\n  startTime=Sun Jun 10 02:23:54 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:23:54 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=haide@gmail.com\r\n  job=<null>\r\n]', NULL),
(120, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@1581122c[\r\n  id=10\r\n  code=20180610023400\r\n  name=Hải Xồm\r\n  status=1\r\n  startTime=Sun Jun 10 02:34:00 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:34:00 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(121, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@7c087fb1[\r\n  homeId=9\r\n  homeName=Hải Xồm\r\n  address=<null>\r\n  status=1\r\n  groupUserId=10\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(122, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@297e20b9[\r\n  userId=20\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@6cb8ecb9[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Hải Xồm\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=10\r\n  groupUser=com.motel.model.GroupUser@1581122c[\r\n  id=10\r\n  code=20180610023400\r\n  name=Hải Xồm\r\n  status=1\r\n  startTime=Sun Jun 10 02:34:00 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:34:00 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=haidexom@gmail.com\r\n  job=<null>\r\n]', NULL),
(123, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@23b2060e[\r\n  id=11\r\n  code=20180610024750\r\n  name=Tư dê\r\n  status=1\r\n  startTime=Sun Jun 10 02:47:50 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:47:50 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(124, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@7432d5ad[\r\n  homeId=10\r\n  homeName=Tư dê\r\n  address=<null>\r\n  status=1\r\n  groupUserId=11\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(125, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@13153421[\r\n  userId=21\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@534a37ca[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Thế Tư\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=11\r\n  groupUser=com.motel.model.GroupUser@23b2060e[\r\n  id=11\r\n  code=20180610024750\r\n  name=Tư dê\r\n  status=1\r\n  startTime=Sun Jun 10 02:47:50 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:47:50 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=tude@gmail.com\r\n  job=<null>\r\n]', NULL),
(126, '0:0:0:0:0:0:0:1', 'admin', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@111b10b5[\r\n  id=12\r\n  code=20180610025936\r\n  name=Thế Tư\r\n  status=1\r\n  startTime=Sun Jun 10 02:59:36 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:59:36 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(127, '0:0:0:0:0:0:0:1', 'admin', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@367a8d5e[\r\n  homeId=11\r\n  homeName=Thế Tư\r\n  address=<null>\r\n  status=1\r\n  groupUserId=12\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(128, '0:0:0:0:0:0:0:1', 'admin', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@72a98dec[\r\n  userId=22\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@14248580[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Tư\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=12\r\n  groupUser=com.motel.model.GroupUser@111b10b5[\r\n  id=12\r\n  code=20180610025936\r\n  name=Thế Tư\r\n  status=1\r\n  startTime=Sun Jun 10 02:59:36 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 02:59:36 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=tult@gmail.com\r\n  job=<null>\r\n]', NULL),
(129, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@1e3fd53e[\r\n  id=13\r\n  code=20180610030613\r\n  name=Hải Nguyễn\r\n  status=1\r\n  startTime=Sun Jun 10 03:06:13 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 03:06:13 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(130, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@330af79f[\r\n  homeId=12\r\n  homeName=Hải Nguyễn\r\n  address=<null>\r\n  status=1\r\n  groupUserId=13\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(131, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@3842643[\r\n  userId=23\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@124ee527[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Hải nv\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=13\r\n  groupUser=com.motel.model.GroupUser@1e3fd53e[\r\n  id=13\r\n  code=20180610030613\r\n  name=Hải Nguyễn\r\n  status=1\r\n  startTime=Sun Jun 10 03:06:13 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 03:06:13 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=hainv@gmai.com\r\n  job=<null>\r\n]', NULL),
(132, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@2e66b842[\r\n  id=14\r\n  code=20180610031129\r\n  name=hails\r\n  status=1\r\n  startTime=Sun Jun 10 03:11:29 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 03:11:29 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(133, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@62bbf5ca[\r\n  homeId=13\r\n  homeName=hails\r\n  address=<null>\r\n  status=1\r\n  groupUserId=14\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(134, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@4fd11a2[\r\n  userId=24\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@36d91c16[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=hails\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=14\r\n  groupUser=com.motel.model.GroupUser@2e66b842[\r\n  id=14\r\n  code=20180610031129\r\n  name=hails\r\n  status=1\r\n  startTime=Sun Jun 10 03:11:29 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 03:11:29 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=hals@gmail.com\r\n  job=<null>\r\n]', NULL),
(135, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.GroupUser@51e6ab07[\r\n  id=15\r\n  code=20180610031527\r\n  name=Tư 4\r\n  status=1\r\n  startTime=Sun Jun 10 03:15:27 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 03:15:27 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]', NULL),
(136, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'processRegistrationAccount', 'AccountRegistrationController', NULL, NULL, 'com.motel.model.Home@2dd284b[\r\n  homeId=14\r\n  homeName=Tư 4\r\n  address=<null>\r\n  status=1\r\n  groupUserId=15\r\n  description=<null>\r\n  homeCode=<null>\r\n  billList=<null>\r\n  roomList=<null>\r\n  statusName=<null>\r\n]', NULL),
(137, '0:0:0:0:0:0:0:1', '', '2018-06-09 17:00:00', 1, 'onCreateAccount', 'AccountRegistrationController', NULL, NULL, 'com.slook.model.CatUser@6a7b3e61[\r\n  userId=25\r\n  userName=<null>\r\n  password=123\r\n  roleId=2\r\n  role=com.slook.model.CatRole@7cf78345[\r\n  roleId=2\r\n  roleName=Khách hàng\r\n  roleCode=CUSTOMER\r\n  functionPaths=[]\r\n  roleHasFunctionPaths=[com.slook.model.RoleHasFunctionPath@dd1e, com.slook.model.RoleHasFunctionPath@d8f2, com.slook.model.RoleHasFunctionPath@db08, com.slook.model.RoleHasFunctionPath@d9fd, com.slook.model.RoleHasFunctionPath@daaf, com.slook.model.RoleHasFunctionPath@da56, com.slook.model.RoleHasFunctionPath@ddd0, com.slook.model.RoleHasFunctionPath@db61, com.slook.model.RoleHasFunctionPath@df8d]\r\n  status=1\r\n  statusName=<null>\r\n]\r\n  empId=<null>\r\n  employee=<null>\r\n  status=1\r\n  statusName=<null>\r\n  fullName=Tư4\r\n  confirmPassword=123\r\n  oldPassword=<null>\r\n  newPassword=<null>\r\n  groupUserId=15\r\n  groupUser=com.motel.model.GroupUser@51e6ab07[\r\n  id=15\r\n  code=20180610031527\r\n  name=Tư 4\r\n  status=1\r\n  startTime=Sun Jun 10 03:15:27 ICT 2018\r\n  endTime=<null>\r\n  createTime=Sun Jun 10 03:15:27 ICT 2018\r\n  description=Registration Account\r\n  maxNumberRoom=10\r\n  statusName=<null>\r\n]\r\n  sex=<null>\r\n  birthDate=<null>\r\n  address=<null>\r\n  phoneNumber=\r\n  description=<null>\r\n  email=tu4@gmail.com\r\n  job=<null>\r\n]', NULL);

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
(19, 2, 5, 1),
(20, 3, 2, 1),
(21, 3, 14, 1),
(22, 3, 8, 1),
(23, 3, 1, 1),
(24, 3, 7, 1),
(25, 3, 4, 1),
(26, 3, 6, 1),
(27, 3, 5, 1),
(28, 3, 3, 1),
(29, 3, 9, 1),
(30, 3, 12, 1),
(31, 3, 11, 1),
(32, 3, 10, 1),
(33, 3, 13, 1),
(34, 2, 15, 1),
(35, 2, 8, 1),
(36, 3, 15, 1),
(37, 1, 14, 1),
(38, 1, 15, 1),
(39, 1, 2, 1),
(40, 3, 16, 1),
(41, 3, 18, 1),
(42, 3, 17, 1),
(43, 1, 18, 1),
(44, 1, 17, 1),
(45, 1, 16, 1),
(46, 3, 19, 1),
(47, 3, 20, 1),
(48, 2, 20, 1),
(49, 1, 20, 1);

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
(2, 1, 'Phòng 102', 200000, 1),
(3, 1, 'Phòng 103', 500000, 1),
(5, 4, '103', 111111, 2),
(6, 5, '101', 2000000, 2),
(7, 5, '103', 3300000, 2),
(8, 1, 'Phòng 104', 1000000, 1),
(9, 1, 'Phòng 105', 1000000, 1),
(10, 14, 'Phòng 101', 1500000, 1),
(11, 14, 'Phòng 102', 200000, 1),
(12, 14, 'Phòng 103', 500000, 1),
(13, 14, 'Phòng 104', 1000000, 1),
(14, 14, 'Phòng 105', 1000000, 1);

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
(1, 'ELECTRIC', 'Điện', 53500, 3, 1, 1, 1),
(2, 'WATER', 'Nước', 30000, 4, 1, 1, 1),
(3, 'VESINH', 'Vệ sinh', 10, 2, 1, 1, 2),
(4, 'Internet', 'Internet(mạng)', 100, 2, 1, 0, 2),
(5, 'PRICE_ROOM', 'Giá thuê phòng', NULL, 2, NULL, 0, 1),
(12, 'ELECTRIC', 'Điện', 53500, 3, 13, 1, 1),
(13, 'WATER', 'Nước', 30000, 4, 13, 1, 1),
(14, 'VESINH', 'Vệ sinh', 10, 2, 13, 1, 2),
(15, 'Internet', 'Internet(mạng)', 100, 2, 13, 0, 2),
(16, 'ELECTRIC', 'Điện', 53500, 3, 14, 1, 1),
(17, 'WATER', 'Nước', 30000, 4, 14, 1, 1),
(18, 'VESINH', 'Vệ sinh', 10, 2, 14, 1, 2),
(19, 'Internet', 'Internet(mạng)', 100, 2, 14, 0, 2),
(20, 'ELECTRIC', 'Điện', 53500, 3, 15, 1, 1),
(21, 'WATER', 'Nước', 30000, 4, 15, 1, 1),
(22, 'VESINH', 'Vệ sinh', 10, 2, 15, 1, 2),
(23, 'Internet', 'Internet(mạng)', 100, 2, 15, 0, 2);

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
-- Chỉ mục cho bảng `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`DOCUMENT_ID`);

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
  MODIFY `BILL_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `bill_service`
--
ALTER TABLE `bill_service`
  MODIFY `BILL_SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT cho bảng `cat_item`
--
ALTER TABLE `cat_item`
  MODIFY `ITEM_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `cat_role`
--
ALTER TABLE `cat_role`
  MODIFY `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `cat_user`
--
ALTER TABLE `cat_user`
  MODIFY `USER_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT cho bảng `contract`
--
ALTER TABLE `contract`
  MODIFY `CONTRACT_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `contract_service`
--
ALTER TABLE `contract_service`
  MODIFY `CONTRACT_SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT cho bảng `customer`
--
ALTER TABLE `customer`
  MODIFY `CUSTOMER_ID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `customer_room`
--
ALTER TABLE `customer_room`
  MODIFY `CUSTOMER_ROOM_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `document`
--
ALTER TABLE `document`
  MODIFY `DOCUMENT_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `electric_water`
--
ALTER TABLE `electric_water`
  MODIFY `electric_water_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `function_path`
--
ALTER TABLE `function_path`
  MODIFY `FUNCTION_PATH_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT cho bảng `group_user`
--
ALTER TABLE `group_user`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `home`
--
ALTER TABLE `home`
  MODIFY `HOME_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `log_action`
--
ALTER TABLE `log_action`
  MODIFY `LOG_ACTION_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=138;

--
-- AUTO_INCREMENT cho bảng `role_has_function_path`
--
ALTER TABLE `role_has_function_path`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT cho bảng `room`
--
ALTER TABLE `room`
  MODIFY `ROOM_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `service`
--
ALTER TABLE `service`
  MODIFY `SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

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
