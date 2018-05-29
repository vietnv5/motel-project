-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 28, 2018 lúc 05:48 PM
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
(3, 'Lê Thị Bé', 2, NULL, '', '0123456789', 1, '', 'gai', '', NULL, '', NULL, 1, NULL, '', NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`CUSTOMER_ID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `customer`
--
ALTER TABLE `customer`
  MODIFY `CUSTOMER_ID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
