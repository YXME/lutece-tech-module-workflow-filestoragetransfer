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

import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.TaskFileTransferConfig;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.config.TaskConfigService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

/**
 * Produces the configuration service of the file transfer task
 */
@ApplicationScoped
public class TaskFileTransferConfigProducer
{
    public static final String BEAN_CONFIG_SERVICE = "workflow-filestoragetransfer.taskFileTransferService";
    public static final String BEAN_CONFIG_DAO = "workflow-filestoragetransfer.taskFileTransferConfigDAO";

    /**
     * Produces the configuration service of the file transfer task
     *
     * @param taskConfigDAO
     *            the DAO of the configuration
     * @return the configuration service
     */
    @SuppressWarnings( {
            "rawtypes", "unchecked"
    } )
    @Produces
    @ApplicationScoped
    @Named( BEAN_CONFIG_SERVICE )
    public ITaskConfigService produceTaskFileTransferConfigService( @Named( BEAN_CONFIG_DAO ) ITaskConfigDAO<TaskFileTransferConfig> taskConfigDAO )
    {
        TaskConfigService taskConfigService = new TaskConfigService( );
        taskConfigService.setTaskConfigDAO( (ITaskConfigDAO) taskConfigDAO );

        return taskConfigService;
    }
}
