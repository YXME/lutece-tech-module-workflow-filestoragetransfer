<@formGroup labelFor='entryCode' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnCode}' mandatory=true>
    <@input type='text' id='entryCode' name='entryCode' maxlength=50 value='${config.entryCode!\'\'}' tabIndex='1' />
</@formGroup>
<@formGroup labelFor='targetFileserviceproviderName' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnTargetFileserviceproviderName}' mandatory=true>
    <@select id='targetFileserviceproviderName' name='targetFileserviceproviderName'>
            <#list file_services_list as provider>
                <@option value=provider.name label=provider.label selected=( provider.name == config.targetFileserviceproviderName!'' ) />
            </#list>
    </@select>
</@formGroup>
