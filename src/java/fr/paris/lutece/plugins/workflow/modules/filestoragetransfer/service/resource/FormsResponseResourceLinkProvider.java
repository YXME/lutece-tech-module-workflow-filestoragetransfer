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

package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.plugins.filestoragetransfer.service.resource.FileStorageTransferResourceLink;
import fr.paris.lutece.plugins.filestoragetransfer.service.resource.IFileStorageTransferResourceLinkProvider;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.FormsResponseFile;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.IFormsResponseFileDAO;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Links the files that the file transfer task sent to transfer to the back office detail of their form response
 */
@ApplicationScoped
public class FormsResponseResourceLinkProvider implements IFileStorageTransferResourceLinkProvider
{
    private static final String URL_FORM_RESPONSE_DETAILS = "jsp/admin/plugins/forms/ManageDirectoryFormResponseDetails.jsp";
    private static final String PARAMETER_VIEW = "view";
    private static final String VIEW_FORM_RESPONSE_DETAILS = "view_form_response_details";
    private static final String PARAMETER_ID_FORM_RESPONSE = "id_form_response";
    private static final String MESSAGE_LABEL = "module.workflow.filestoragetransfer.resource_link.formResponse";

    @Inject
    @ConfigProperty( name = "workflow-filestoragetransfer.filetransfercontext" )
    private String _strFileTransferContext;

    @Inject
    private IFormsResponseFileDAO _formsResponseFileDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strRequestContext )
    {
        return Objects.equals( _strFileTransferContext, strRequestContext );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FileStorageTransferResourceLink> getResourceLinks( Collection<String> listFileKeys, Locale locale )
    {
        Map<String, FileStorageTransferResourceLink> mapLinks = new HashMap<>( );

        for ( FormsResponseFile file : _formsResponseFileDAO.selectByFileKeys( listFileKeys ) )
        {
            UrlItem url = new UrlItem( URL_FORM_RESPONSE_DETAILS );
            url.addParameter( PARAMETER_VIEW, VIEW_FORM_RESPONSE_DETAILS );
            url.addParameter( PARAMETER_ID_FORM_RESPONSE, file.getIdFormResponse( ) );

            String strLabel = I18nService.getLocalizedString( MESSAGE_LABEL, new Object [ ] {
                    file.getFormTitle( ), String.valueOf( file.getIdFormResponse( ) )
            }, locale );

            mapLinks.putIfAbsent( file.getFileKey( ), new FileStorageTransferResourceLink( url.getUrl( ), strLabel ) );
        }

        return mapLinks;
    }
}
