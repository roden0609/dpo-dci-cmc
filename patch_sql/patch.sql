-- togcmrce1.cmc_message_param definition

-- CREATE TABLE `cmc_message_param` (
--   `para_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
--   `para_source` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
--   `message_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
--   `mars_user_attr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
--   `static_para_name_en` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
--   `static_para_name_tc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
--   `static_para_name_sc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
--   PRIMARY KEY (`para_name`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- INSERT INTO togcmrce1.cmc_message_param (para_name,para_source,message_type,mars_user_attr,static_para_name_en,static_para_name_tc,static_para_name_sc) VALUES
-- 	 ('ALIAS','M','AL','alias','DEFAULT_ALIAS_EN','DEFAULT_ALIAS_TC','DEFAULT_ALIAS_SC');


UPDATE togcmrce1.adm_rpt_info SET impl_class='hk.gov.cmc.processor.report.impl.IAS04DReport' WHERE rpt_id='RPT-IAS-04-D';

COMMIT;