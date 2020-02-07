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
	
	$('[id^="newFilesDiv"]:visible').slideUp( "slow", function() {
		$(this).remove();
	});
	
	$('[id^="add_file_btn"]:visible').removeAttr("disabled");
	$('[id^="add_file_btn"]:visible').children('input').removeAttr("disabled");
	$('[id^="add_file_btn"]:visible').attr("title", "");
}

function removeFile(id)
{	
	var elementId = "filerow_"+id;
	var curElement = $("[id='"+elementId+"']");
	curElement.parent().slideUp( "slow", function() {});
	$('[id^="filesAdded"]:visible').siblings('fieldset').children('input').val('');
	curElement.remove();
	$('[id^="add_file_btn"]:visible').children('input').removeAttr("disabled");
	$('[id^="add_file_btn"]:visible').removeAttr("disabled");
	$('[id^="add_file_btn"]:visible').attr("title", "");
	    
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
    	jqList.append("<h4>Redan tillagd fil:<h4><div class='divider'></div>");
    	list.id = "existingFilesDiv";
    }
    else if ( !showOnly )  {
    	jqList.append("<h4>Fil som ska läggas till:<h4><div class='divider'></div>");
    	list.id = "newFilesDiv";
    } 
    for(var i = 0; i < files.length; i++) {
    	var columns = "";
    	if(removable){
    		columns = newColumn("<a href='#' onclick='downloadFile(); return false'>" + files[i].name + "</a>"  ,6,'')+
    		newColumn(humanFileSize(files[i].size,true),4,'text-center');
    	}
    	else{
    		columns = newColumn(files[i].name,6,'')+
    		newColumn(humanFileSize(files[i].size,true),4,'text-center');
    	}
    	
    	if ( removable )
    	{
    		columns += newColumn('<a href="javascript:void(0)"><div onclick="removeFile('+files[i].fileid+')" class="glyphicon glyphicon-trash"/></a>',2,'text-center');
    	}	
    	else{
    		columns += newColumn('<a href="javascript:void(0)"><div onclick="cleanFiles()" class="glyphicon glyphicon-trash"/></a>',2,'text-center');
    	}
    	var row = newRow( columns , "filerow_"+files[i].fileid );
    	jqList.append(row);  
    }
//    if ( !removable && !showOnly ) jqList.append(cleanButton);
    return list;
}


$(document).on('change', ':file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label,input.get(0).files]);
});

function onFileSelect(event, numFiles, label,files) {
	var targetId = event.target.id;
	var questionId = targetId.substring(targetId.indexOf("-"))
	console.log(questionId);
    filesToBeUploaded = '';
    if(files.length > 0){
	    for(var k=0, len = files.length; k < len; k++){
	        filesToBeUploaded += files[k].name;
	    }
	}
    document.getElementById('filesAdded'+ questionId).appendChild( makeUL( files , false , false ) );
    $("#fileUploaderInput" + questionId).val(filesToBeUploaded);
	$('#add_file_btn' + questionId).attr("disabled", true);
	$('#add_file_btn' + questionId).children('input').attr("disabled", true);
	$('#add_file_btn' + questionId).attr("title", "Du måste ta bort den tillagda filen för att kunna lägga till en ny.");
}

$(document).ready(function() {
	$(':file').on('fileselect', onFileSelect); 
	
//	$('#add_file_btn').on("click", function() {
//		if(!$(this).attr('disabled')){
//			$('#fileselect').trigger('click');
//			console.log('clicked');
//		}
//	});
});
//$('#add_file_btn').click(function(){
//	$(this).children('input').click();
//});
