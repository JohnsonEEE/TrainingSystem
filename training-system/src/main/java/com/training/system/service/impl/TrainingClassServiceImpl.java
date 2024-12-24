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
package com.training.system.service.impl;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.training.common.core.domain.entity.TrainingClass;
import com.training.common.core.domain.entity.TrainingSignUp;
import com.training.common.enums.TrainingClassStatusEnum;
import com.training.common.enums.TrainingSignUpStatusEnum;
import com.training.common.utils.StringUtils;
import com.training.common.utils.bean.BeanUtils;
import com.training.system.domain.TrainingClassVO;
import com.training.system.mapper.TrainingClassMapper;
import com.training.system.mapper.TrainingSignUpMapper;
import com.training.system.service.ITrainingClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author training
 * @date 2024.12.23
 */
@Service
public class TrainingClassServiceImpl implements ITrainingClassService {

    @Resource
    private TrainingClassMapper trainingClassMapper;

    @Resource
    private TrainingSignUpMapper trainingSignUpMapper;

    @Override
    public List<TrainingClassVO> selectClassList(TrainingClassVO trainingClassVO) {
        if (StringUtils.isNotBlank(trainingClassVO.getClassName())) {
            trainingClassVO.setClassName("%" + trainingClassVO.getClassName() + "%");
        }
        if (StringUtils.isNotBlank(trainingClassVO.getQueryBeginTimeStr())) {
            trainingClassVO.setQueryBeginTime(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(trainingClassVO.getQueryBeginTimeStr() + " 00:00:00")));
        }
        if (StringUtils.isNotBlank(trainingClassVO.getQueryEndTimeStr())) {
            trainingClassVO.setQueryEndTime(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(trainingClassVO.getQueryEndTimeStr() + " 23:59:59")));
        }
        return trainingClassMapper.selectClassList(trainingClassVO);
    }

    @Override
    public void addClass(TrainingClassVO trainingClassVO) {
        TrainingClass trainingClass = new TrainingClass();
        BeanUtils.copyBeanProp(trainingClass, trainingClassVO);
        trainingClass.setStatus(TrainingClassStatusEnum.NOT_BEGIN.getCode());
        trainingClass.setClassNamePY(PinyinUtil.getFirstLetter(trainingClassVO.getClassName(), ""));
        trainingClass.setClassBeginTime(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(trainingClassVO.getClassBeginTimeStr())));
        trainingClassMapper.addClass(trainingClass);
    }

    @Override
    public void delClass(Integer classId) {
        trainingClassMapper.delClass(classId);
    }

    @Override
    public TrainingClassVO getTrainingClass(Integer classId) {
        return trainingClassMapper.getTrainingClass(classId);
    }

    @Override
    public void updateClass(TrainingClassVO trainingClassVO) {
        TrainingClass trainingClass = new TrainingClass();
        BeanUtils.copyBeanProp(trainingClass, trainingClassVO);
        if (StringUtils.isNotBlank(trainingClassVO.getClassName())) {
            trainingClass.setClassNamePY(PinyinUtil.getFirstLetter(trainingClassVO.getClassName(), ""));
        }
        if (StringUtils.isNotBlank(trainingClassVO.getClassBeginTimeStr())) {
            trainingClass.setClassBeginTime(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(trainingClassVO.getClassBeginTimeStr())));
        }
        trainingClassMapper.updateClass(trainingClass);
    }

    @Override
    public void signUp(TrainingSignUp trainingSignUp) {
        TrainingClassVO trainingClass = trainingClassMapper.getTrainingClass(trainingSignUp.getClassId());
        if (trainingClass.getSignUpCount() >= trainingClass.getMaxParticipantCount()) {
            throw new RuntimeException("该课程已经满员，请选择其他课程报名");
        }

        trainingSignUp.setSignUpTime(LocalDateTime.now())
                .setStatus(TrainingSignUpStatusEnum.SIGN_UP.getCode())
                .setCompleteStatus("0");
        trainingSignUpMapper.addSignUp(trainingSignUp);
    }

    @Override
    public void check(TrainingSignUp trainingSignUp) {
        trainingSignUp.setStatus(TrainingSignUpStatusEnum.CHECK.getCode())
                .setCheckTime(LocalDateTime.now());
        trainingSignUpMapper.updateSignUp(trainingSignUp);
    }

    @Override
    public void cancelSignUp(TrainingSignUp trainingSignUp) {
        trainingSignUp.setCancelTime(LocalDateTime.now())
                .setStatus(TrainingSignUpStatusEnum.NOT_SIGN_UP.getCode());
        trainingSignUpMapper.updateSignUp(trainingSignUp);
    }

    @Override
    public List<TrainingClassVO> selectSignUpList(TrainingClassVO trainingClassVO) {
        if (StringUtils.isNotBlank(trainingClassVO.getClassName())) {
            trainingClassVO.setClassName("%" + trainingClassVO.getClassName() + "%");
        }
        if (StringUtils.isNotBlank(trainingClassVO.getQueryBeginTimeStr())) {
            trainingClassVO.setQueryBeginTime(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(trainingClassVO.getQueryBeginTimeStr() + " 00:00:00")));
        }
        if (StringUtils.isNotBlank(trainingClassVO.getQueryEndTimeStr())) {
            trainingClassVO.setQueryEndTime(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(trainingClassVO.getQueryEndTimeStr() + " 23:59:59")));
        }
        List<TrainingClassVO> list = trainingSignUpMapper.selectSignUpList(trainingClassVO);
        for (TrainingClassVO classVO : list) {
            // 默认未报名
            classVO.setSignUpStatus(StringUtils.isNotBlank(classVO.getSignUpStatus()) ? classVO.getSignUpStatus() : TrainingSignUpStatusEnum.NOT_SIGN_UP.getCode());
        }
        return list;
    }

    @Override
    public void complete(TrainingSignUp trainingSignUp) {
        TrainingSignUp update = new TrainingSignUp()
                .setSignUpId(trainingSignUp.getSignUpId())
                .setCompleteStatus("1".equals(trainingSignUp.getCompleteStatus()) ? "1" : "0");
        trainingSignUpMapper.updateSignUp(update);
    }
}
