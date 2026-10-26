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

package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.filestoragetransfer.service.resource.FileStorageTransferResourceLink;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.inject.Inject;

/**
 * Test of the link from a transferred file to its form response
 */
public class FormsResponseResourceLinkProviderTest extends LuteceTestCase
{
    private static final String FORM_TITLE = "Resource link test form";
    private static final String FILE_KEY = "resource-link-test-key";

    @Inject
    private FormsResponseResourceLinkProvider _provider;

    private final List<String> _listCleanUp = new ArrayList<>( );
    private int _nIdFormResponse;

    /**
     * Runs an insert and returns the generated id
     *
     * @param strSql
     *            the insert
     * @param params
     *            the values
     * @return the generated id
     */
    private static int insert( String strSql, Object... params )
    {
        try ( DAOUtil daoUtil = new DAOUtil( strSql, Statement.RETURN_GENERATED_KEYS ) )
        {
            for ( int i = 0; i < params.length; i++ )
            {
                if ( params [i] instanceof Integer )
                {
                    daoUtil.setInt( i + 1, (Integer) params [i] );
                }
                else
                {
                    daoUtil.setString( i + 1, (String) params [i] );
                }
            }
            daoUtil.executeUpdate( );
            daoUtil.nextGeneratedKey( );

            return daoUtil.getGeneratedKeyInt( 1 );
        }
    }

    /**
     * Creates a form, a form response and a file entry response that carries {@link #FILE_KEY}
     *
     * @throws Exception
     *             if the parent setup fails
     */
    @BeforeEach
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        int nIdForm = insert( "INSERT INTO forms_form ( title ) VALUES ( ? )", FORM_TITLE );
        _nIdFormResponse = insert( "INSERT INTO forms_response ( id_form ) VALUES ( ? )", nIdForm );
        int nIdQuestionResponse = insert( "INSERT INTO forms_question_response ( id_form_response ) VALUES ( ? )", _nIdFormResponse );
        int nIdResponse = insert( "INSERT INTO genatt_response ( file_key, file_store ) VALUES ( ?, ? )", FILE_KEY, "defaultDatabaseFileStoreProvider" );
        int nIdQuestionEntryResponse = insert( "INSERT INTO forms_question_entry_response ( id_question_response, id_entry_response ) VALUES ( ?, ? )",
                nIdQuestionResponse, nIdResponse );

        _listCleanUp.add( "DELETE FROM forms_question_entry_response WHERE id_question_entry_response = " + nIdQuestionEntryResponse );
        _listCleanUp.add( "DELETE FROM genatt_response WHERE id_response = " + nIdResponse );
        _listCleanUp.add( "DELETE FROM forms_question_response WHERE id_question_response = " + nIdQuestionResponse );
        _listCleanUp.add( "DELETE FROM forms_response WHERE id_response = " + _nIdFormResponse );
        _listCleanUp.add( "DELETE FROM forms_form WHERE id_form = " + nIdForm );
    }

    /**
     * Removes the rows of the test
     *
     * @throws Exception
     *             if the parent tear down fails
     */
    @AfterEach
    @Override
    protected void tearDown( ) throws Exception
    {
        for ( String strSql : _listCleanUp )
        {
            try ( DAOUtil daoUtil = new DAOUtil( strSql ) )
            {
                daoUtil.executeUpdate( );
            }
        }

        super.tearDown( );
    }

    /**
     * The provider answers the context of the task only
     */
    @Test
    public void testIsInvoked( )
    {
        assertTrue( _provider.isInvoked( "genericattributes" ), "context of the task" );
        assertFalse( _provider.isInvoked( "other" ), "other context" );
        assertFalse( _provider.isInvoked( null ), "no context" );
    }

    /**
     * A known key links to the detail of its form response, an unknown key gives nothing
     */
    @Test
    public void testResourceLinks( )
    {
        Map<String, FileStorageTransferResourceLink> mapLinks = _provider.getResourceLinks( List.of( FILE_KEY, "unknown-key" ), Locale.FRENCH );

        assertEquals( 1, mapLinks.size( ), "one link" );
        FileStorageTransferResourceLink link = mapLinks.get( FILE_KEY );
        assertEquals( "jsp/admin/plugins/forms/ManageDirectoryFormResponseDetails.jsp?view=view_form_response_details&id_form_response=" + _nIdFormResponse,
                link.getUrl( ), "url of the form response detail" );
        assertTrue( link.getLabel( ).contains( FORM_TITLE ) && link.getLabel( ).contains( String.valueOf( _nIdFormResponse ) ), "label: " + link.getLabel( ) );
        assertTrue( _provider.getResourceLinks( List.of( ), Locale.FRENCH ).isEmpty( ), "no key, no link" );
    }
}
