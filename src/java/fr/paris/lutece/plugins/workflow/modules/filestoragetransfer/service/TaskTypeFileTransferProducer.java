/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service;

import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflowcore.business.task.TaskType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Produces the task type of the file transfer task
 */
@ApplicationScoped
public class TaskTypeFileTransferProducer
{
    /**
     * Produces the task type of the file transfer task from the properties of the module
     *
     * @param strKey
     *            the key of the task type
     * @param strTitleI18nKey
     *            the i18n key of its title
     * @param strBeanName
     *            the name of the task bean
     * @param strConfigBeanName
     *            the name of the task configuration bean
     * @param bConfigRequired
     *            whether the task needs a configuration
     * @param bFormTaskRequired
     *            whether the task displays a form when the action runs
     * @param bTaskForAutomaticAction
     *            whether the task may be used in an automatic action
     * @return the task type
     */
    @Produces
    @ApplicationScoped
    @Named( "workflow-filestoragetransfer.taskTypeFileTransfer" )
    public ITaskType produceTaskTypeFileTransfer( @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.key" ) String strKey,
            @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.titleI18nKey" ) String strTitleI18nKey,
            @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.beanName" ) String strBeanName,
            @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.configBeanName" ) String strConfigBeanName,
            @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.configRequired", defaultValue = "true" ) boolean bConfigRequired,
            @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.formTaskRequired", defaultValue = "false" ) boolean bFormTaskRequired,
            @ConfigProperty( name = "workflow-filestoragetransfer.taskTypeFileTransfer.taskForAutomaticAction", defaultValue = "true" ) boolean bTaskForAutomaticAction )
    {
        TaskType taskType = new TaskType( );
        taskType.setKey( strKey );
        taskType.setTitleI18nKey( strTitleI18nKey );
        taskType.setBeanName( strBeanName );
        taskType.setConfigBeanName( strConfigBeanName );
        taskType.setConfigRequired( bConfigRequired );
        taskType.setFormTaskRequired( bFormTaskRequired );
        taskType.setTaskForAutomaticAction( bTaskForAutomaticAction );

        return taskType;
    }
}
