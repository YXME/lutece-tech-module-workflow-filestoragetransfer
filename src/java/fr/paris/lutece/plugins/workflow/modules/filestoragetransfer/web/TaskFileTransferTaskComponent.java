package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.TaskFileTransferConfig;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.TaskFileTransferConfigProducer;
import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.plugins.filestoragetransfer.service.FileStoreProviderLabelService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Back office component of the file transfer task
 */
@ApplicationScoped
@Named( "workflow-filestoragetransfer.taskFileTransferTaskComponent" )
public class TaskFileTransferTaskComponent extends AbstractTaskComponent {


    private static final String MARK_CONFIG = "config";
    private static final String MARK_FILESERVICES_LIST = "file_services_list";
    private static final String MARK_TARGET_LABEL = "target_label";

    /*
    private static final String MARK_LIST_ENTRIES = "list_entries";
    private static final String MARK_LIST_RESOURCE_TYPE = "list_resource_type";
    private static final String MARK_LIST_RESOURCE_CODE = "list_resource_code";


    // Parameters
    private static final String PARAMETER_ACTION = "apply";
    private static final String PARAMETER_RESOURCE_TYPE = "resource_type";
    private static final String PARAMETER_RESOURCE_CODE = "resource_code";
    private static final String PARAMETER_ENTRY_CODE = "entry_code";


    // Action
    private static final String ACTION_SELECT_RESOURCE_TYPE = "select_resource_type";
    private static final String ACTION_SELECT_RESOURCE_CODE = "select_resource_code";
    private static final String ACTION_SELECT_ENTRY_CODE = "select_entry_code";
    */
    
    private static final String FILTER_RESOURCE_TYPE = "FORMS_FORM";

    // Parameters of the configuration form
    private static final String PARAMETER_ENTRY_CODE = "entryCode";
    private static final String PARAMETER_TARGET_FILESERVICEPROVIDER_NAME = "targetFileserviceproviderName";

    // Size of workflow_task_file_transfer_config.entry_code
    private static final int ENTRY_CODE_MAX_LENGTH = 50;

    // Messages
    private static final String MESSAGE_ENTRY_CODE_TOO_LONG = "module.workflow.filestoragetransfer.message.error.entryCodeTooLong";
    private static final String MESSAGE_UNKNOWN_FILE_STORE_PROVIDER = "module.workflow.filestoragetransfer.message.error.unknownFileStoreProvider";
    //private static final int FILTER_ENTRY_TYPE = 8;

    private ITaskConfigService _taskConfigService;

    @Inject
    @ConfigProperty( name = "workflow-filestoragetransfer.filetransfercontext" )
    private String _strFileTransferContext;

    @Inject
    @Any
    private Instance<IFileStoreServiceProvider> _fileStoreServiceProviders;

    @Inject
    private FileStoreProviderLabelService _providerLabelService;

    private static final String TEMPLATE_TASK_FILE_STORAGE_TRANSFER_CONFIG = "admin/plugins/workflow/modules/filestoragetransfer/task_file_storage_transfer_config_form.ftl";
    private static final String TEMPLATE_TASK_FILE_STORAGE_TRANSFER = "admin/plugins/workflow/modules/filestoragetransfer/task_file_storage_transfer_form.ftl";

    /**
     * Constructor for the CDI proxy
     */
    public TaskFileTransferTaskComponent( )
    {
    }

    /**
     * Constructor
     *
     * @param taskType
     *            the type of the task
     * @param taskConfigService
     *            the configuration service of the task
     */
    @Inject
    public TaskFileTransferTaskComponent( @Named( "workflow-filestoragetransfer.taskTypeFileTransfer" ) ITaskType taskType,
            @Named( TaskFileTransferConfigProducer.BEAN_CONFIG_SERVICE ) ITaskConfigService taskConfigService )
    {
        _taskConfigService = taskConfigService;
        setTaskType( taskType );
        setTaskConfigService( taskConfigService );
    }

    @Override
    public String getDisplayTaskForm(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task) 
    {
        Map<String, Object> model = new HashMap<>( );
        TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) );
        model.put( MARK_CONFIG, config );
        model.put( MARK_TARGET_LABEL, _providerLabelService.getLabel( config != null ? config.getTargetFileserviceproviderName( ) : null, locale ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FILE_STORAGE_TRANSFER, locale, model );

        return template.getHtml( );
    }

    @Override
    public String getDisplayConfigForm(HttpServletRequest request, Locale locale, ITask task) 
    {
        // TODO :
        // Modify this code to filter on different ResourceType linked to GenericAttributes
        // Modify this code to provide a list of resources (forms, appointments) to select the appropriate entry

            TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) ) == null 
            ? new TaskFileTransferConfig( ) 
            : _taskConfigService.findByPrimaryKey( task.getId( ) );

            Map<String, Object> model = new HashMap<>( );
            model.put ( MARK_FILESERVICES_LIST, _providerLabelService.getProviders( locale ) );
            model.put( MARK_CONFIG, config );

            final HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FILE_STORAGE_TRANSFER_CONFIG, locale, model );
            return template.getHtml( );
    }

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strEntryCode = StringUtils.trim( request.getParameter( PARAMETER_ENTRY_CODE ) );
        String strTargetFileserviceproviderName = request.getParameter( PARAMETER_TARGET_FILESERVICEPROVIDER_NAME );

        // The configuration is saved only when it can work: a code and a provider of this site, within the columns
        if ( StringUtils.isBlank( strEntryCode ) || StringUtils.isBlank( strTargetFileserviceproviderName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }
        if ( strEntryCode.length( ) > ENTRY_CODE_MAX_LENGTH )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ENTRY_CODE_TOO_LONG, new Object [ ] {
                    ENTRY_CODE_MAX_LENGTH
            }, AdminMessage.TYPE_STOP );
        }
        if ( !getFileStoreServiceProviderNames( ).contains( strTargetFileserviceproviderName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNKNOWN_FILE_STORE_PROVIDER, new Object [ ] {
                    strTargetFileserviceproviderName
            }, AdminMessage.TYPE_STOP );
        }

        TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) );

        boolean create = config == null;
        if ( create )
        {
            config = new TaskFileTransferConfig( );
            config.setIdTask( task.getId( ) );
            config.setTargetFileserviceproviderName( strTargetFileserviceproviderName );
            config.setContext( _strFileTransferContext );
            config.setEntryCode( strEntryCode );
            config.setResourceCode( "" );
            config.setResourceType( FILTER_RESOURCE_TYPE );

            _taskConfigService.create( config );
        }
        else
        {
            config.setEntryCode( strEntryCode );
            config.setTargetFileserviceproviderName( strTargetFileserviceproviderName );
            _taskConfigService.update( config );
        }
        return null;

    }

    /**
     * Returns the names of the file store service providers of the site
     *
     * @return the names of the providers
     */
    private List<String> getFileStoreServiceProviderNames( )
    {
        return _fileStoreServiceProviders.stream( ).map( IFileStoreServiceProvider::getName ).collect( Collectors.toList( ) );
    }

    @Override
    public String getDisplayTaskInformation(int nIdHistory, HttpServletRequest request, Locale locale, ITask task) {
        TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) ) == null 
        ? new TaskFileTransferConfig( ) 
        : _taskConfigService.findByPrimaryKey( task.getId( ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );
        model.put( MARK_TARGET_LABEL, _providerLabelService.getLabel( config.getTargetFileserviceproviderName( ), locale ) );

        return AppTemplateService.getTemplate( TEMPLATE_TASK_FILE_STORAGE_TRANSFER, locale, model ).getHtml( );
    }

    @Override
    public String doValidateTask(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task) {
        return null;
    }
}
