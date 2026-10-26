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

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.TaskFileTransferConfigProducer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.sql.Statement;

/**
 * This class provides Data Access methods for TaskFileTransferConfig objects
 */
@ApplicationScoped
@Named( TaskFileTransferConfigProducer.BEAN_CONFIG_DAO )
public class TaskFileTransferConfigDAO implements ITaskConfigDAO<TaskFileTransferConfig>
{
    
    // Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_file_transfer_config ( id_task, resource_type, resource_code, entry_code, target_fileserviceprovider_name, context ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_file_transfer_config WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_file_transfer_config SET resource_type = ?, resource_code = ?, entry_code = ?, target_fileserviceprovider_name = ?, context = ? WHERE id_task = ?";
   
	private static final String SQL_QUERY_SELECTALL = "SELECT id_task, resource_type, resource_code, entry_code, target_fileserviceprovider_name, context FROM workflow_task_file_transfer_config";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_task FROM workflow_task_file_transfer_config";

    private static final String SQL_QUERY_SELECTALL_BY_IDS = SQL_QUERY_SELECTALL + " WHERE id_task IN (  ";
	private static final String SQL_QUERY_SELECT_BY_ID = SQL_QUERY_SELECTALL + " WHERE id_task = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( TaskFileTransferConfig taskFileTransferConfig )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++ , taskFileTransferConfig.getIdTask( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getResourceType( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getResourceCode( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getEntryCode( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getTargetFileserviceproviderName( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getContext( ) );
            
            daoUtil.executeUpdate( );
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public TaskFileTransferConfig load( int nIdTask )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID ) )
        {
	        daoUtil.setInt( 1 , nIdTask );
	        daoUtil.executeQuery( );
	        TaskFileTransferConfig taskFileTransferConfig = null;
	
	        if ( daoUtil.next( ) )
	        {
	            taskFileTransferConfig = loadFromDaoUtil( daoUtil );
	        }
	
	        return taskFileTransferConfig ;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nIdTask )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
	        daoUtil.setInt( 1 , nIdTask );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( TaskFileTransferConfig taskFileTransferConfig )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
	        int nIndex = 1;
	        
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getResourceType( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getResourceCode( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getEntryCode( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getTargetFileserviceproviderName( ) );
            daoUtil.setString( nIndex++ , taskFileTransferConfig.getContext( ) );
            daoUtil.setInt( nIndex , taskFileTransferConfig.getIdTask( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

	private TaskFileTransferConfig loadFromDaoUtil (DAOUtil daoUtil) {
		
		TaskFileTransferConfig taskFileTransferConfig = new TaskFileTransferConfig(  );
		int nIndex = 1;
		
		taskFileTransferConfig.setIdTask( daoUtil.getInt( nIndex++ ) );
        taskFileTransferConfig.setResourceType( daoUtil.getString( nIndex++ ) );
        taskFileTransferConfig.setResourceCode( daoUtil.getString( nIndex++ ) );
        taskFileTransferConfig.setEntryCode( daoUtil.getString( nIndex++ ) );
		taskFileTransferConfig.setTargetFileserviceproviderName( daoUtil.getString( nIndex++ ) );
		taskFileTransferConfig.setContext( daoUtil.getString( nIndex ) );
		
		return taskFileTransferConfig;
	}
}
