package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.listener;

import java.util.List;
import java.util.Objects;

import fr.paris.lutece.plugins.filestoragetransfer.business.FileStorageTransferRequest;
import fr.paris.lutece.plugins.filestoragetransfer.service.listener.IFileStorageTransferListener;

import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Updates the key of the generic attributes response whose file was transferred
 */
@ApplicationScoped
public class FileStorageTransferListenerService implements IFileStorageTransferListener
{
    @Inject
    @ConfigProperty( name = "workflow-filestoragetransfer.filetransfercontext" )
    private String _strContextValue;

    @Override
    public void changeFileService( FileStorageTransferRequest fileTransferRequest )
    {
        if ( fileTransferRequest == null )
        {
            return;
        }
        else if( !verifyFileStorageTransferContext( fileTransferRequest.getRequestContext( ) ) )
        {
            return;
        }
        else {
            ResponseFilter responseFilter = new ResponseFilter();
            responseFilter.setFileKey(fileTransferRequest.getSourceFileKey());

            List<Response> listResponse = ResponseHome.getResponseList( responseFilter );
            Response response = null;
            if ( listResponse != null && !listResponse.isEmpty( ) )
            {
                response = listResponse.get( 0 );
                ResponseHome.updateFileKey( response.getIdResponse(), fileTransferRequest.getTargetFileKey(), fileTransferRequest.getTargetFileserviceproviderName() );
            }
            else {
                throw new IllegalStateException( "No response found for file key " + fileTransferRequest.getSourceFileKey() );
            }
        }
    }

    @Override
    public boolean verifyFileStorageTransferContext( String fileTransferRequestContext ) {
        return Objects.equals( _strContextValue, fileTransferRequestContext );
    }

    @Override
    public String getName()
    {
        return "FileTransferListenerService";
    }
}