-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 08, 2018 lúc 09:14 AM
-- Phiên bản máy phục vụ: 10.1.29-MariaDB
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

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`SERVICE_ID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `service`
--
ALTER TABLE `service`
  MODIFY `SERVICE_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
