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

package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Finds the form responses that own some files, through the genericattributes responses and the forms question responses
 */
@ApplicationScoped
public class FormsResponseFileDAO implements IFormsResponseFileDAO
{
    private static final String SQL_QUERY_SELECT_BY_FILE_KEYS = "SELECT r.file_key, fr.id_response, f.title FROM genatt_response r"
            + " INNER JOIN forms_question_entry_response qer ON qer.id_entry_response = r.id_response"
            + " INNER JOIN forms_question_response qr ON qr.id_question_response = qer.id_question_response"
            + " INNER JOIN forms_response fr ON fr.id_response = qr.id_form_response" + " INNER JOIN forms_form f ON f.id_form = fr.id_form"
            + " WHERE r.file_key IN ( ";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormsResponseFile> selectByFileKeys( Collection<String> listFileKeys )
    {
        List<FormsResponseFile> listFiles = new ArrayList<>( );

        if ( listFileKeys.isEmpty( ) )
        {
            return listFiles;
        }

        String strQuery = SQL_QUERY_SELECT_BY_FILE_KEYS + String.join( ", ", listFileKeys.stream( ).map( strKey -> "?" ).toList( ) ) + " )";

        try ( DAOUtil daoUtil = new DAOUtil( strQuery ) )
        {
            int nIndex = 1;

            for ( String strKey : listFileKeys )
            {
                daoUtil.setString( nIndex++, strKey );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listFiles.add( new FormsResponseFile( daoUtil.getString( 1 ), daoUtil.getInt( 2 ), daoUtil.getString( 3 ) ) );
            }
        }

        return listFiles;
    }
}
