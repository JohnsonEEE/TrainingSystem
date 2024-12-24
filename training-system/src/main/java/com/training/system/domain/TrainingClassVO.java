/*
 *
 *
 * Copyright ( c ) 2024 TH Supcom Corporation. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TH Supcom
 * Corporation ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with TH Supcom Corporation or a TH Supcom
 * authorized reseller (the "License Agreement"). TH Supcom may make changes to the
 * Confidential Information from time to time. Such Confidential Information may
 * contain errors.
 *
 * EXCEPT AS EXPLICITLY SET FORTH IN THE LICENSE AGREEMENT, TH Supcom DISCLAIMS ALL
 * WARRANTIES, COVENANTS, REPRESENTATIONS, INDEMNITIES, AND GUARANTEES WITH
 * RESPECT TO SOFTWARE AND DOCUMENTATION, WHETHER EXPRESS OR IMPLIED, WRITTEN OR
 * ORAL, STATUTORY OR OTHERWISE INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, TITLE, NON-INFRINGEMENT AND FITNESS FOR A
 * PARTICULAR PURPOSE. TH Supcom DOES NOT WARRANT THAT END USER'S USE OF THE
 * SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR SECURE.
 *
 * TH Supcom SHALL NOT BE LIABLE TO END USER, OR ANY OTHER PERSON, CORPORATION OR
 * ENTITY FOR INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY OR CONSEQUENTIAL
 * DAMAGES, OR DAMAGES FOR LOSS OF PROFITS, REVENUE, DATA OR USE, WHETHER IN AN
 * ACTION IN CONTRACT, TORT OR OTHERWISE, EVEN IF TH Supcom HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES. TH Supcom' TOTAL LIABILITY TO END USER SHALL NOT
 * EXCEED THE AMOUNTS PAID FOR THE TH Supcom SOFTWARE BY END USER DURING THE PRIOR
 * TWELVE (12) MONTHS FROM THE DATE IN WHICH THE CLAIM AROSE.  BECAUSE SOME
 * STATES OR JURISDICTIONS DO NOT ALLOW LIMITATION OR EXCLUSION OF CONSEQUENTIAL
 * OR INCIDENTAL DAMAGES, THE ABOVE LIMITATION MAY NOT APPLY TO END USER.
 *
 * Copyright version 2.0
 */
package com.training.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.common.constant.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author training
 * @date 2024.12.23
 */
@Getter
@Setter
@Accessors(chain = true)
public class TrainingClassVO implements Serializable {

    private static final long serialVersionUID = 7182475451240734022L;

    /**
     * 课程id，主键
     */
    private Long classId;

    /**
     * 课程名称
     */
    private String className;

    /**
     * 课程名称拼音
     */
    private String classNamePY;

    /**
     * 课程开始时间str
     */
    private String classBeginTimeStr;

    /**
     * 课程开始时间
     */
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime classBeginTime;

    /**
     * 上课地点
     */
    private String location;

    /**
     * 老师姓名
     */
    private String teacherName;

    /**
     * 课程内容
     */
    private String content;

    /**
     * 课程状态
     * 状态可以查看枚举 com.training.common.enums.TrainingClassStatusEnum
     */
    private String status;

    /**
     * 最大报名人数
     */
    private Integer maxParticipantCount;

    /**
     * 报名id，主键
     */
    private Integer signUpId;

    /**
     * 人员id
     */
    private Integer peopleId;

    /**
     * 人员姓名
     */
    private String peopleName;

    /**
     * 报名状态
     */
    private String signUpStatus;

    /**
     * 报名时间
     */
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime signUpTime;

    /**
     * 取消报名时间
     */
    private LocalDateTime cancelTime;

    /**
     * 已报名人数
     */
    private Integer signUpCount;

    /**
     * 查询条件开始时间
     */
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime queryBeginTime;

    /**
     * 查询条件结束时间
     */
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime queryEndTime;
}
