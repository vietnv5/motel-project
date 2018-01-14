-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 14, 2018 lúc 06:30 PM
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
  `BILL_CODE` int(11) NOT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `PAYMENT_DATE` date NOT NULL,
  `TOTAL_PRICE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bill_service`
--

CREATE TABLE `bill_service` (
  `BILL_SERVICE_ID` int(11) NOT NULL,
  `SERVICE_ID` int(11) NOT NULL,
  `AMOUNT` double NOT NULL,
  `PRICE` int(11) NOT NULL,
  `TOTAL_PRICE` int(11) NOT NULL,
  `STATUS` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_dictionary`
--

CREATE TABLE `cat_dictionary` (
  `CAT_CODE` varchar(30) NOT NULL,
  `CAT_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `EDITABLE` int(2) NOT NULL,
  `REGEX_VALUE` varchar(255) NOT NULL,
  `REQUIRE_VALUE` int(2) NOT NULL,
  `MESSAGE_VALID` varchar(500) NOT NULL
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
  `CODE` varchar(250) NOT NULL,
  `NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CAT_CODE` varchar(30) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATE_TIME` datetime(6) NOT NULL,
  `UPDATE_TIME` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `NOTE` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_role`
--

CREATE TABLE `cat_role` (
  `ROLE_ID` int(11) NOT NULL,
  `ROLE_NAME` varchar(200) NOT NULL,
  `ROLE_CODE` varchar(250) NOT NULL,
  `STATUS` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `cat_role`
--

INSERT INTO `cat_role` (`ROLE_ID`, `ROLE_NAME`, `ROLE_CODE`, `STATUS`) VALUES
(1, 'admin', 'admin', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cat_user`
--

CREATE TABLE `cat_user` (
  `USER_ID` int(11) NOT NULL,
  `USER_NAME` varchar(100) CHARACTER SET latin1 NOT NULL,
  `PASSWORD` varchar(200) CHARACTER SET latin1 NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  `EMP_ID` int(11) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `full_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cat_user`
--

INSERT INTO `cat_user` (`USER_ID`, `USER_NAME`, `PASSWORD`, `ROLE_ID`, `EMP_ID`, `STATUS`, `full_name`) VALUES
(1, 'admin', 'admin', 1, 0, 1, 'Nguyễn Văn Việt');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `contract`
--

CREATE TABLE `contract` (
  `CONTRACT_ID` int(11) NOT NULL,
  `CONTRACT_CODE` int(11) NOT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) NOT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `START_TIME` date NOT NULL,
  `END_TIME` date NOT NULL,
  `DEPOSIT` int(11) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATE_TIME` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `contract_service`
--

CREATE TABLE `contract_service` (
  `CONTRACT_ID` int(11) NOT NULL,
  `SERVICE_ID` int(11) NOT NULL,
  `CONTRACT_SERVICE_ID` int(11) NOT NULL,
  `INSERT_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `CUSTOMER_ID` int(20) NOT NULL,
  `CUSTOMER_NAME` varchar(200) DEFAULT NULL,
  `SEX` int(10) NOT NULL,
  `BIRTH_DATE` date NOT NULL,
  `ADDRESS` varchar(500) NOT NULL,
  `PHONE` varchar(20) NOT NULL,
  `STATUS` int(10) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `CMT` int(20) NOT NULL,
  `NGAY_CAP` date NOT NULL,
  `NOI_CAP` varchar(100) NOT NULL,
  `UPDATE_TIME` date NOT NULL,
  `GRUOP_ID` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer_room`
--

CREATE TABLE `customer_room` (
  `CUSTOMER_ROOM_ID` int(11) NOT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `ROOM_ID` int(11) NOT NULL,
  `START_TIME` date NOT NULL,
  `END_TIME` date NOT NULL,
  `STATUS` int(10) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `TYPE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `electric_water`
--

CREATE TABLE `electric_water` (
  `ROOM_ID` int(11) NOT NULL,
  `ELECTRIC_OLD` double NOT NULL,
  `WATER_OLD` double NOT NULL,
  `ELECTRIC_NEW` double NOT NULL,
  `WATER_NEW` double NOT NULL,
  `CREATE_TIME` date NOT NULL,
  `TIME_LINE` date NOT NULL,
  `STATUS` int(11) NOT NULL,
  `MONTH` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `function_path`
--

CREATE TABLE `function_path` (
  `FUNCTION_PATH_ID` int(11) NOT NULL,
  `URL` varchar(250) NOT NULL,
  `NAME` varchar(250) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `PARENT_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `group_user`
--

CREATE TABLE `group_user` (
  `ID` int(11) NOT NULL,
  `CODE` int(11) NOT NULL,
  `NAME` varchar(250) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime NOT NULL,
  `CREATE_TIME` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `home`
--

CREATE TABLE `home` (
  `HOME_ID` int(11) NOT NULL,
  `HOME_NAME` varchar(200) NOT NULL,
  `ADDRESS` varchar(500) NOT NULL,
  `STATUS` int(20) NOT NULL,
  `GRUOP_ID` int(10) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `HOME_CODE` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `home`
--

INSERT INTO `home` (`HOME_ID`, `HOME_NAME`, `ADDRESS`, `STATUS`, `GRUOP_ID`, `DESCRIPTION`, `HOME_CODE`) VALUES
(0, '', '', 0, 0, '', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `log_action`
--

CREATE TABLE `log_action` (
  `LOG_ACTION_ID` int(11) NOT NULL,
  `CLIENT_IP` varchar(50) NOT NULL,
  `USER_NAME` varchar(250) NOT NULL,
  `EVENT_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ACTION_TYPE` int(11) NOT NULL,
  `FUNCTION` varchar(250) NOT NULL,
  `CLASS_NAME` varchar(250) NOT NULL,
  `OBJECT_CODE` varchar(500) NOT NULL,
  `OLD_VALUE` longtext NOT NULL,
  `NEW_VALUE` longtext NOT NULL,
  `NOTENOTE` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role_has_function_path`
--

CREATE TABLE `role_has_function_path` (
  `ID` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  `FUNCTION_PATH_ID` int(11) NOT NULL,
  `STATUS` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room`
--

CREATE TABLE `room` (
  `ROOM_ID` int(11) NOT NULL,
  `HOME_ID` int(11) NOT NULL,
  `ROOM_NAME` varchar(100) NOT NULL,
  `PRICE` int(20) NOT NULL,
  `STATUS` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `service`
--

CREATE TABLE `service` (
  `SERVICE_ID` int(11) NOT NULL,
  `SERVICE_NAME` int(11) NOT NULL,
  `PRICE` int(11) NOT NULL,
  `UNIT` int(11) NOT NULL,
  `GROUP_ID` int(10) NOT NULL,
  `DEFAULT_STATUS` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  ADD PRIMARY KEY (`BILL_ID`);

--
-- Chỉ mục cho bảng `bill_service`
--
ALTER TABLE `bill_service`
  ADD PRIMARY KEY (`BILL_SERVICE_ID`);

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
  ADD PRIMARY KEY (`CONTRACT_ID`);

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`CUSTOMER_ID`);

--
-- Chỉ mục cho bảng `customer_room`
--
ALTER TABLE `customer_room`
  ADD PRIMARY KEY (`CUSTOMER_ROOM_ID`);

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
  ADD PRIMARY KEY (`ROOM_ID`);

--
-- Chỉ mục cho bảng `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`SERVICE_ID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `cat_item`
--
ALTER TABLE `cat_item`
  MODIFY `ITEM_ID` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
