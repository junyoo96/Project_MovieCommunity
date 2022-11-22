<?php
 	$sFileInfo = '';
	$headers = array();
	 
	foreach($_SERVER as $k => $v) {
		if(substr($k, 0, 9) == "HTTP_FILE") {
			$k = substr(strtolower($k), 5);
			$headers[$k] = $v;
		} 
	}

	$filename = rawurldecode($headers['file_name']);
	//내 수정 코드 시작
	$filename_ext_temp = explode('.',$filename);
	$filename_ext = strtolower(array_pop($filename_ext_temp));
	//내 수정 코드 끝
	//$filename_ext = strtolower(array_pop(explode('.',$filename)));
	$allow_file = array("jpg", "png", "bmp", "gif"); 

	if(!in_array($filename_ext, $allow_file)) {
		echo "NOTALLOW_".$filename;
	} else {
		$file = new stdClass;
		$file->name = date("YmdHis").mt_rand().".".$filename_ext;
		$file->content = file_get_contents("php://input");

        //내가 코드 수정
		$uploadDir = '../../upload/';

		if(!is_dir($uploadDir)){
			mkdir($uploadDir, 0777);
		}
		
		$newPath = $uploadDir.$file->name;
		
		if(file_put_contents($newPath, $file->content)) {
			$sFileInfo .= "&bNewLine=true";
			$sFileInfo .= "&sFileName=".$filename;
			$sFileInfo .= "&sFileURL=upload/".$file->name;
		}
		
		echo $sFileInfo;
	}
?>