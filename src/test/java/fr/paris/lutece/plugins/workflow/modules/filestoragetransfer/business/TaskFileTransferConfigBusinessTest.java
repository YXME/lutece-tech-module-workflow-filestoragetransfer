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
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.TaskFileTransferConfigProducer;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * This is the business class test for the object TaskFileTransferConfig
 */
public class TaskFileTransferConfigBusinessTest extends LuteceTestCase
{
    private static final int ID_TASK = 9999;
    private static final String ENTRYCODE1 = "EntryCode1";
    private static final String ENTRYCODE2 = "EntryCode2";
    private static final String TARGETFILESERVICEPROVIDERNAME1 = "TargetFileserviceproviderName1";
    private static final String TARGETFILESERVICEPROVIDERNAME2 = "TargetFileserviceproviderName2";
    private static final String CONTEXT1 = "Context1";

    @Inject
    @Named( TaskFileTransferConfigProducer.BEAN_CONFIG_SERVICE )
    private ITaskConfigService _taskConfigService;

    /**
     * test TaskFileTransferConfig
     */
    @Test
    public void testBusiness( )
    {
        TaskFileTransferConfig config = new TaskFileTransferConfig( );
        config.setIdTask( ID_TASK );
        config.setResourceType( "FORMS_FORM" );
        config.setResourceCode( "" );
        config.setEntryCode( ENTRYCODE1 );
        config.setTargetFileserviceproviderName( TARGETFILESERVICEPROVIDERNAME1 );
        config.setContext( CONTEXT1 );

        _taskConfigService.create( config );
        TaskFileTransferConfig stored = _taskConfigService.findByPrimaryKey( ID_TASK );
        assertNotNull( stored, "config created" );
        assertEquals( ENTRYCODE1, stored.getEntryCode( ), "entry code" );
        assertEquals( TARGETFILESERVICEPROVIDERNAME1, stored.getTargetFileserviceproviderName( ), "target" );
        assertEquals( CONTEXT1, stored.getContext( ), "context" );

        config.setEntryCode( ENTRYCODE2 );
        config.setTargetFileserviceproviderName( TARGETFILESERVICEPROVIDERNAME2 );
        _taskConfigService.update( config );
        stored = _taskConfigService.findByPrimaryKey( ID_TASK );
        assertEquals( ENTRYCODE2, stored.getEntryCode( ), "entry code updated" );
        assertEquals( TARGETFILESERVICEPROVIDERNAME2, stored.getTargetFileserviceproviderName( ), "target updated" );

        _taskConfigService.remove( ID_TASK );
        assertNull( _taskConfigService.findByPrimaryKey( ID_TASK ), "config removed" );
    }
}
