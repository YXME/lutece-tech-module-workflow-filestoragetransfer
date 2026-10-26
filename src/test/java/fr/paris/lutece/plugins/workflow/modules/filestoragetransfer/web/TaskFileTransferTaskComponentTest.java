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

package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.TaskFileTransfer;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Test of the back office component of the file transfer task
 */
public class TaskFileTransferTaskComponentTest extends LuteceTestCase
{
    private static final int ID_TASK = 9998;

    @Inject
    @Named( "workflow-filestoragetransfer.taskFileTransferTaskComponent" )
    private TaskFileTransferTaskComponent _component;

    @Inject
    @Named( "workflow-filestoragetransfer.taskFileTransfer" )
    private TaskFileTransfer _task;

    /**
     * The configuration form and the task information render their templates
     */
    @Test
    public void testDisplayForms( )
    {
        _task.setId( ID_TASK );

        String strConfigForm = _component.getDisplayConfigForm( new MockHttpServletRequest( ), Locale.FRENCH, _task );
        assertTrue( strConfigForm.contains( "targetFileserviceproviderName" ), "configuration form rendered" );
        assertTrue( strConfigForm.contains( "value=\"defaultDatabaseFileStoreProvider\"" ), "technical name posted" );
        assertTrue( strConfigForm.contains( "Libellé de test du module" ), "provider label shown" );

        String strInformation = _component.getDisplayTaskInformation( 0, new MockHttpServletRequest( ), Locale.FRENCH, _task );
        assertTrue( strInformation.contains( "entryCode" ), "task information rendered" );
    }
}
