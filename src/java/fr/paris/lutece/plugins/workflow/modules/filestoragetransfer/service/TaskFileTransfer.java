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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.filestoragetransfer.business.FileStorageTransferRequest;
import fr.paris.lutece.plugins.filestoragetransfer.business.FileStorageTransferRequestHome;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.file.GenericAttributeFileService;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.TaskFileTransferConfig;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Workflow task creating a file storage transfer request for each file of a form response entry
 */
@Dependent
@Named( "workflow-filestoragetransfer.taskFileTransfer" )
public class TaskFileTransfer extends SimpleTask
{
    // Variables
    public static final String TASK_TYPE = "FileTransferTask";
    public static final String TASK_TYPE_LABEL = "File Transfer Task";

    // SERVICES
    @Inject
    @Named( TaskFileTransferConfigProducer.BEAN_CONFIG_SERVICE )
    private ITaskConfigService _taskConfigService;

    @Inject
    private GenericAttributeFileService _genericAttributeFileService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( )
    {
        // Implement the initialization of the task here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processTaskWithResult( int nIdResource, String strResourceType, int nIdResourceHistory, HttpServletRequest request, Locale locale,
            User user )
    {
        // TODO: This task should be re-written to not have any dependencies on the forms plugin

        // Retrieve the configuration
        TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( this.getId( ) );

        // Retrieve the file key
        List<FormQuestionResponse> listFormQuestionResponse = FormQuestionResponseHome.getFormQuestionResponseListByFormResponse( nIdResource );

        // Filter the list to get the entry response with the entry code
        List<Response> listResponse = new ArrayList<Response>( );

        listFormQuestionResponse.stream( ).filter( fqr -> fqr.getQuestion( ).getCode( ).equals( config.getEntryCode( ) ) )
                .forEach( fqr -> listResponse.addAll( fqr.getEntryResponse( ) ) );

        if ( listResponse.size( ) == 0 )
        {
            return false;
        }
        else
        {
            for ( Response response : listResponse )
            {
                if ( response.getFile( ) == null || response.getFile( ).getFileKey( ) == null )
                {
                    continue;
                }
                else
                {
                    String currentFileStoreServiceProvider = _genericAttributeFileService.getFileStoreProviderName( response.getFile( ).getOrigin( ) );

                    FileStorageTransferRequest fileRequestTransfer = new FileStorageTransferRequest( response.getFile( ).getFileKey( ),
                            currentFileStoreServiceProvider, config.getTargetFileserviceproviderName( ), config.getContext( ), "" );

                    FileStorageTransferRequestHome.create( fileRequestTransfer );
                }

            }
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        _taskConfigService.remove( this.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        // The title of the task type, in the language of the administrator (the workflow back office shows it)
        if ( getTaskType( ) != null && StringUtils.isNotBlank( getTaskType( ).getTitleI18nKey( ) ) )
        {
            return I18nService.getLocalizedString( getTaskType( ).getTitleI18nKey( ), locale );
        }

        return TASK_TYPE_LABEL;
    }
}
