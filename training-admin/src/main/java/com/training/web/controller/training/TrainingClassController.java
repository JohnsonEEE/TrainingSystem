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
package com.training.web.controller.training;

import com.training.common.core.controller.BaseController;
import com.training.common.core.domain.AjaxResult;
import com.training.system.domain.TrainingClassVO;
import com.training.system.service.ITrainingClassService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author training
 * @date 2024.12.23
 */
@RestController
@RequestMapping("/training")
public class TrainingClassController extends BaseController {
    @Resource
    private ITrainingClassService trainingClassService;

    @RequestMapping("/classList")
    public AjaxResult classList(TrainingClassVO trainingClassVO)
    {
        return AjaxResult.success(trainingClassService.selectClassList(trainingClassVO));
    }

    @RequestMapping("/addClass")
    public AjaxResult addClass(TrainingClassVO trainingClassVO)
    {
        trainingClassService.addClass(trainingClassVO);
        return AjaxResult.success();
    }

    @RequestMapping("/updateClass")
    public AjaxResult updateClass(TrainingClassVO trainingClassVO)
    {
        trainingClassService.updateClass(trainingClassVO);
        return AjaxResult.success();
    }

    @RequestMapping("/delClass")
    public AjaxResult delClass(String classId)
    {
        trainingClassService.delClass(classId);
        return AjaxResult.success();
    }

    @RequestMapping("/getClass")
    public AjaxResult getTrainingClass(String classId)
    {
        TrainingClassVO trainingClassVO = trainingClassService.getTrainingClass(classId);
        return AjaxResult.success(trainingClassVO);
    }
}
