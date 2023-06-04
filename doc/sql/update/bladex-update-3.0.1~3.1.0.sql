-- -----------------------------------
-- 由于blade-user合并至blade-system，api也发生变化，需要对blade_menu的对应记录修改地址
-- -----------------------------------
UPDATE blade_menu SET path = REPLACE ( path, 'blade-user', 'blade-system/user' );
