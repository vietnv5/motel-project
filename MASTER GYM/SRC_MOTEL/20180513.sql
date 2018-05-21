ALTER TABLE `cat_user` CHANGE `GROUP_ID` `GROUP_USER_ID` INT(11) NULL DEFAULT '1' COMMENT 'Nhóm người dùng';

//ALTER TABLE `group_user` ADD `JOIN_DATE` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian bắt đầu sử dụng' AFTER `CREATE_TIME`;
ALTER TABLE `group_user` ADD `DESCRIPTION` VARCHAR(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER `JOIN_DATE`;
INSERT INTO `function_path` (`FUNCTION_PATH_ID`, `URL`, `NAME`, `STATUS`, `TYPE`, `PARENT_ID`) VALUES ('15', '/catHome', 'Khu trọ', '1', '2', '2');
INSERT INTO `group_user` (`ID`, `CODE`, `NAME`, `STATUS`, `START_TIME`, `END_TIME`, `CREATE_TIME`, `JOIN_DATE`, `DESCRIPTION`)
 VALUES ('0', 'super_admin', 'Quản trị phần mềm', '1', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Dùng cho quản trị hệ thống phần mềm');

 INSERT INTO `function_path` (`FUNCTION_PATH_ID`, `URL`, `NAME`, `STATUS`, `TYPE`, `PARENT_ID`) VALUES (NULL, '/catUser.action_modify_role', 'Quyền thay đổi role', '1', '3', '9');

 
INSERT INTO `function_path` (`FUNCTION_PATH_ID`, `URL`, `NAME`, `STATUS`, `TYPE`, `PARENT_ID`) 
VALUES (null, '/groupUser', 'Quản lý nhóm khách hàng', '1', '2', '3');
INSERT INTO `function_path` (`FUNCTION_PATH_ID`, `URL`, `NAME`, `STATUS`, `TYPE`, `PARENT_ID`) 
VALUES (NULL, '/functionPath', 'Danh mục chức năng', '1', '2', '3');

ALTER TABLE `group_user` ADD `MAX_NUMBER_ROOM` INT(11)   NULL AFTER `DESCRIPTION`;



 9
/catUser