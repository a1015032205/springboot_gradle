CREATE TABLE `boss_java_job`
(
    `id`           int(11)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '公司名称',
    `job_name`     varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '职位名称',
    `money`        varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '薪资',
    `city`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
    `are`          varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地区',
    `street`       varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '街道',
    `year`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '年限',
    `education`    varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '学历',
    `welfare`      varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '待遇',
    `technology`   varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '技术栈',
    `num`          varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '人数',
    `listed`       varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '上市情况',
    `type`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '公司类型',
    `name`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '招聘人',
    `c_user`       varchar(255) COLLATE utf8_bin DEFAULT '秒度' COMMENT '创建人',
    `c_time`       timestamp NULL                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `u_user`       varchar(255) COLLATE utf8_bin DEFAULT '秒度' COMMENT '修改人',
    `u_time`       timestamp NULL                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `del_tag`      varchar(255) COLLATE utf8_bin DEFAULT '0' COMMENT '是否有效 0否1是',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT 'BOSS招聘';

CREATE TABLE `java_job_51`
(
    `id`           int(11)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '公司名称',
    `job_name`     varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '职位名称',
    `money`        varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '薪资',
    `city`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
    `are`          varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地区',
    `per_num`      varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '招聘人数',
    `year`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '年限',
    `education`    varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '学历',
    `welfare`      varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '待遇',
    `time`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '发布时间',
    `num`          varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '人数',
    `listed`       varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '上市情况',
    `type`         varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '公司类型',
    `c_user`       varchar(255) COLLATE utf8_bin DEFAULT '秒度' COMMENT '创建人',
    `c_time`       timestamp NULL                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `u_user`       varchar(255) COLLATE utf8_bin DEFAULT '秒度' COMMENT '修改人',
    `u_time`       timestamp NULL                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `del_tag`      varchar(255) COLLATE utf8_bin DEFAULT '0' COMMENT '是否有效 0否1是',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT '前程无忧招聘';