function humanFileSize(bytes, si) {
    var thresh = si ? 1000 : 1024;
    if(Math.abs(bytes) < thresh) {
        return bytes + ' B';
    }
    var units = si
        ? ['kB','MB','GB','TB','PB','EB','ZB','YB']
        : ['KiB','MiB','GiB','TiB','PiB','EiB','ZiB','YiB'];
    var u = -1;
    do {
        bytes /= thresh;
        ++u;
    } while(Math.abs(bytes) >= thresh && u < units.length - 1);
    return bytes.toFixed(1)+' '+units[u];
}

function newColumn(content,sz,align)
{
	return '<div class="col-xs-'+sz+' col-sm-'+sz+' col-md-'+sz+' col-lg-'+sz+' '+align+'">'+content+'</div>';
}
function newRow(content,id)
{
	return '<div id="'+id+'" class="row vdivide"><div>'+ content + '</div>';
}

function cleanFiles()
{
	$("[id='fileselect']").val('');
	var e = $("[id='newFilesDiv']");
	if ( e != undefined ) e.remove();				
}

function removeFile(id)
{
	var elementId = "filerow_"+id;
	var submitData = "<input type='hidden' value='"+id+"' name='remove_file'/>";
	var curElement = $("[id='"+elementId+"']");
	curElement.slideUp( "slow", function() {});
	curElement.remove();
	$("[id='filesAdded']").append(submitData);
}

function makeUL(files,removable,showOnly) 
{
	var updating = (typeof isUpdating !== 'undefined');
	var adding = (typeof isAdding !== 'undefined');
	 
	var cleanButton = "<button class='btn btn-default btn-file pull-right' onclick='cleanFiles()'>Rensa alla<span class='glyphicon glyphicon-trash'/></button>";
    var list = document.createElement('div');
    //list.className = "container panel";
    list.className = "";
    jqList = $(list);
    if ( removable ) {
    	jqList.append("<h4>Redan tillagda filer:<h4><div class='divider'></div>");
    	list.id = "existingFilesDiv";
    }
    else if ( !showOnly )  {
    	jqList.append("<h4>Filer som ska läggas till:<h4><div class='divider'></div>");
    	list.id = "newFilesDiv";
    } 
    for(var i = 0; i < files.length; i++) {
    	var columns = newColumn(files[i].name,6,'')+
    		newColumn(humanFileSize(files[i].size,true),4,'text-center');
    	if ( removable )
    	{
    		columns += newColumn('<a href="javascript:void(0)"><div onclick="removeFile('+files[i].fileid+')" class="glyphicon glyphicon-trash"/></a>',2,'text-center');
    	}			    		
    	var row = newRow( columns , "filerow_"+files[i].fileid );
    	jqList.append(row);  
    }
    if ( !removable && !showOnly ) jqList.append(cleanButton);
    return list;
}

$(document).on('change', ':file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label,input.get(0).files]);
});

function onFileSelect(event, numFiles, label,files) {
        
    filesToBeUploaded = ''
    if(files.length > 0){
	    for(var k=0, len = files.length; k < len; k++){
	        filesToBeUploaded += files[k].name + '|';
	    }
	}
    document.getElementById('filesAdded').appendChild( makeUL( files , false , false ) );
    $("[id='fileUploaderInput']").val(filesToBeUploaded); 
}